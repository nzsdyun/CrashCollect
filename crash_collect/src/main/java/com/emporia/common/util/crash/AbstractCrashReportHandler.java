package com.emporia.common.util.crash;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * abstract collection classes, subclasses can realize mail to send, send http, etc
 * @author sky
 */
public abstract class AbstractCrashReportHandler implements CrashSendListener {
    /** application context */
    private Context mContext;

    public AbstractCrashReportHandler(Context context) {
        this.mContext = context;
        CrashExceptionHandler.getIntance().init(mContext);
    }

    @Override
    public void sendCrashFile(final File file) {
//        sendReport(buildSubject(mContext), buildBody(mContext), file);
    }

    /** send crash file */
    public void sendCrashFile() {
        sendReport(buildSubject(mContext), buildBody(mContext), buildFile(mContext));
    }

    /** init send way */
    public void init() {
        CrashExceptionHandler.getIntance().setCrashSendWay(this);
        AlarmSenderService.startService(mContext);
    }

    /** subclasses implement */
    protected abstract boolean sendReport(String subject, String body, File file);

    /** build send title */
    private String buildSubject(Context context) {
        File fileDir = buildFileDir(context);
        if (fileDir == null || !fileDir.exists()) {
            return "title";
        }
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            if (files != null) {
                for (int i = files.length - 1; i >= 0; i--) {
                    File file = files[i];
                    if (file != null) {
                        return file.getName();
                    }
                }
            } else {
                return fileDir.getName();
            }
        }
        return fileDir.getName();
    }
    /** build send body */
    private String buildBody(Context context) {
        File fileDir = buildFileDir(context);
        if (fileDir == null || !fileDir.exists()) {
            return "body";
        }
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file != null && file.isFile()) {
                        return getBodyContent(file);
                    }
                }
            } else {
                return "body";
            }
        }
        return getBodyContent(fileDir);
    }
    /** get last file content as send body */
    private String getBodyContent(File file) {
        if (file == null)
            return "body";
        StringBuffer body = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String content;
            while ((content = bufferedReader.readLine()) != null) {
                body.append(content + "\n");
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body.toString();
    }

    /** build crash file, will crash down all files compressed into a zip file */
    private File buildFile(Context context) {
        return CrashExceptionHandler.getIntance().getCrashZipFile();
    }
    /** get crash file directory  */
    private File buildFileDir(Context context) {
        return CrashExceptionHandler.getIntance().getCrashFileDir();
    }

    /** delete crash file directory all files */
    public void clear() {
        File fileDir = buildFileDir(getContext());
        if (fileDir != null) {
            File[] files = fileDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    /** get context */
    public Context getContext() {
        return mContext;
    }

}
