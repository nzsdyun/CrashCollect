package com.emporia.common.util.crash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import com.emporia.common.util.ActivityLifecycleMonitor;
import com.emporia.common.util.PackageUtil;
import com.emporia.common.util.ZipUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * collect crash information and save to internal Storage file.
 * @author sky
 * @version 1.0
 */
public class CrashExceptionHandler implements Thread.UncaughtExceptionHandler {
	private static final String TAG = CrashExceptionHandler.class
			.getSimpleName();
	/** singleton */
	private static CrashExceptionHandler sCrashExceptionHandler;
	private Context mContext;
	/** default uncaught exception handler */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	private Map<String, String> mBaseInfos = new HashMap<String, String>();
	/** one hour a crash file */
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
	private SimpleDateFormat mCrashHappenFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** single thread */
	private ExecutorService mSingleExecutor = Executors.newSingleThreadExecutor();
	/** crash directory file */
	private String mCrashFileDir = "crash";
	/** bug collection interface */
	private CrashSendListener mCrashSendListener;
	/** crash log subject */
	private String mSubject;
	/** crash log body */
	private String mBody;

	private CrashExceptionHandler() {
	}

	public static CrashExceptionHandler getIntance() {
		if (sCrashExceptionHandler == null) {
			synchronized (CrashExceptionHandler.class) {
				if (sCrashExceptionHandler == null) {
					sCrashExceptionHandler = new CrashExceptionHandler();
				}
			}
		}
		return sCrashExceptionHandler;
	}

	public void init(Context context) {
		this.mContext = context;
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// set {@code CrashExceptionHandler } is system default exception handler
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// if user not handle crash, system default handler will deal with
		if (mDefaultHandler != null && !handleException(ex)) {
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			boolean isRestart = restartCurrentActivity(mContext);
			if (!isRestart) {
				try {
					Thread.sleep(3 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// abnormal exit
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(-1);
			}
		}
	}

	/**
	 * self deal with crash exception
	 * @param ex @see Throwable
	 * @return if handler success return true, otherwise false
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null)
			return false;
		mSingleExecutor.execute(new Runnable() {
			@Override
			public void run() {
				// you maybe need show toast
				collectDeviceInfo();
				saveCrashInfo2File(ex);
				sendCrashFile();
			}
		});
		return true;
	}

	/**
	 * save crash information to file
	 * @param ex @see Throwable
	 */
	private void saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		sb.append(mCrashHappenFormat.format(System.currentTimeMillis()) + "\n");
		for (Map.Entry<String, String> entry : mBaseInfos.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		sb.append(writer.toString());
		sb.append("\n\n");
		mBody = sb.toString();
		String time = mDateFormat.format(new Date());
		String fileName = PackageUtil.getApplicationName(mContext) + "-crash-"
				+ time + ".txt";
		mSubject = fileName;
		String fileDir = mCrashFileDir;
		// save file to internal Storage
		File fileDirPath = new File(mContext.getFilesDir(), fileDir);
		Log.e(TAG, "crash info:\n" + sb.toString());
		if (fileDirPath.exists() || fileDirPath.mkdirs()) {
			try {
				FileWriter fileWriter = new FileWriter(
						fileDirPath.getAbsolutePath() + File.separator + fileName,
						true);
				fileWriter.write(sb.toString());
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				Log.e(TAG, "write file data failed:" + e.getMessage());
			}
		}
	}
	/** this implementation is incomplete */
	private void sendCrashFile() {
		if (mCrashSendListener != null) {
			mCrashSendListener.sendCrashFile(getCrashFile());
		}
	}
	/** collect device info */
	private void collectDeviceInfo() {
		PackageInfo pi = PackageUtil.getPackageInfo(mContext);
		if (pi == null) {
			mBaseInfos.put("versionCode",
					String.valueOf(PackageUtil.getVersionCode(mContext)));
			mBaseInfos.put("versionName", PackageUtil.getVersionName(mContext));
		} else {
			mBaseInfos.put("versionCode", String.valueOf(pi.versionCode));
			mBaseInfos.put("versionName", pi.versionName);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mBaseInfos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				Log.e(TAG, "get device info error:" + e.getMessage());
			}
		}
	}
	/** get crash file  */
	public File getCrashFile() {
		File fileFile = null;
		File crashFileDir = new File(mContext.getFilesDir(), mCrashFileDir);
		if (crashFileDir != null && crashFileDir.exists()) {
			fileFile = new File(crashFileDir.getAbsolutePath() + File.separator + mSubject);
		}
		return fileFile;
	}
	/** compression bug directory for a file  */
	public File getCrashZipFile() {
		File fileFile = null;
		File crashFileDir = new File(mContext.getFilesDir(), mCrashFileDir);
		if (crashFileDir != null && crashFileDir.exists()) {
			fileFile = ZipUtil.compress(crashFileDir);
		}
		return fileFile;
	}
	/** get crash file directory, Path for data/data/application_name/files/crash */
	public File getCrashFileDir() {
		return new File(mContext.getFilesDir(), mCrashFileDir);
	}
	/** get crash log subject */
	public String getSubject() {
		return mSubject;
	}
	/** get crash log body */
	public String getBody() {
		return mBody;
	}
	/** set crash send callback listener */
	public void setCrashSendWay(CrashSendListener crashSendListener) {
		this.mCrashSendListener = crashSendListener;
	}
	/** restart current activity */
	public boolean restartCurrentActivity(Context context){
		boolean success = false;
		Activity currentActivity = ActivityLifecycleMonitor.currentActivity;
		if (currentActivity != null) {
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setComponent(currentActivity.getComponentName());
			try {
				context.startActivity(intent);
				android.os.Process.killProcess(android.os.Process.myPid());
				success = true;
			} catch (Exception e) {
				success = false;
			}
		}
		return success;
	}
}
