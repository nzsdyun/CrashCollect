package com.emporia.common.util.crash;

import java.io.File;

/**
 * Crash framework uses the class, you need to add the following statement to the application:
 * <pre>
 *     first:
 *     //add code in application onCreate()
 *     <code>
 *           public void onCreate() {
 *				super.onCreate();
 *				CrashCollectConfiguration crashCollectConfiguration = new CrashCollectConfiguration.Builder(this)
 *							.sendCrashReportHandler(new CrashEmailReporter(this))
 *							.sendStrategy(new FileCountSendStrategy(5))
 *							.isOnlyWifiSend(true)
 *							.checkSendTimeInterval(8 * 60 * 60 * 1000)
 *							.build();
 *				CrashCollect.getInstance().init(crashCollectConfiguration);
 *			}
 *     </code>
 *     send:
 *     //add permissions and register receiver and service in AndroidManifest.xml
 *     <code>
 *			//add permissions
 *			<uses-permission android:name="android.permission.INTERNET"/>
 *			<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>
 *			<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *		    //add receiver and service
 *		    <receiver android:name="com.emporia.common.util.crash.AlarmSenderReceiver" />
 *		    <service android:name="com.emporia.common.util.crash.AlarmSenderService" />
 *     </code>
 *     //if you choose email send, you need third steps, otherwise ignore
 *     third:
 *     //in assets folders add xml file {@code emails.xml }, configuration information follow:
 *     <code>
 *          <?xml version="1.0" encoding="utf-8"?>
 *           <email>
 *               <from-list>
 *                   <host>smtp.163.com</host>
 *                   <port>25</port>
 *                   <item>
 *                       <account>your email account@163.com</account>
 *                       <password>your email password</password>
 *                   </item>
 *               </from-list>
 *               <to-list>
 *                   <item>receive E-mail account</item>
 *               </to-list>
 *           </email>
 *     </code>
 * </pre>
 */
public class CrashCollect {
	private static CrashCollect sCrashCollect = new CrashCollect();
	private CrashCollectConfiguration mCrashCollectConfiguration;
	private CrashCollect() {}
	public static CrashCollect getInstance() {
		return sCrashCollect;
	}
	/** init */
	public void init(CrashCollectConfiguration crashCollectConfiguration) {
		this.mCrashCollectConfiguration = crashCollectConfiguration;
		if (mCrashCollectConfiguration != null) {
			mCrashCollectConfiguration.mSendAbstractCrashReportHandler.init();
		}
	}
	/** send crash file */
	public void send() {
		if (mCrashCollectConfiguration != null) {
			mCrashCollectConfiguration.mSendAbstractCrashReportHandler.sendCrashFile();
		}
	}
	/** judge whether meet the send conditions */
	public boolean isSend(File fileDir) {
		if (mCrashCollectConfiguration != null) {
			return mCrashCollectConfiguration.mSendStrategy.isSend(fileDir);
		}
		return false;
	}
	/** check send crash time interval, default is 8 hour, unit of milliseconds */
	public long checkSendTimeInterval() {
		if (mCrashCollectConfiguration != null) {
			return mCrashCollectConfiguration.checkSendTimeInterval;
		}
		return 8 * 60 * 60 * 1000;
	}
	/** if only wifi send crash file, default is true  */
	public boolean isOnlyWifiSend() {
		if (mCrashCollectConfiguration != null) {
			return mCrashCollectConfiguration.isOnlyWifiSend;
		}
		return true;
	}

}
