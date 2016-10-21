package com.example.sky.crashcollect;

import android.app.Application;

import com.emporia.common.util.ActivityLifecycleMonitor;
import com.emporia.common.util.crash.CrashCollect;
import com.emporia.common.util.crash.CrashCollectConfiguration;
import com.emporia.common.util.crash.CrashExceptionHandler;
import com.emporia.common.util.crash.reporter.FileCountSendStrategy;
import com.emporia.common.util.crash.reporter.mailreporter.CrashEmailReporter;

/**
 * Created by chenkun on 10/14/2016.
 */
public class SApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleMonitor());
        CrashCollectConfiguration crashCollectConfiguration = new CrashCollectConfiguration.Builder(this)
                .sendCrashReportHandler(new CrashEmailReporter(this))
                .sendStrategy(new FileCountSendStrategy(2))
                .isOnlyWifiSend(true)
                .checkSendTimeInterval(8 * 60 * 60 * 1000)
                .build();
        CrashCollect.getInstance().init(crashCollectConfiguration);
//        CrashExceptionHandler.getIntance().init(this);
    }
}
