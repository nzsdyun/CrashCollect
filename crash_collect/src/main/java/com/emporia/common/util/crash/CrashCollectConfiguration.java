package com.emporia.common.util.crash;

import android.content.Context;

import com.emporia.common.util.crash.reporter.FileCacheSizeSendStrategy;
import com.emporia.common.util.crash.reporter.SendStrategy;
import com.emporia.common.util.crash.reporter.mailreporter.CrashEmailReporter;

/**
 * Crash collect configuration
 * @see com.emporia.common.util.crash.reporter.mailreporter.CrashEmailReporter
 * @see com.emporia.common.util.crash.reporter.FileCacheSizeSendStrategy
 * @see com.emporia.common.util.crash.reporter.FileCountSendStrategy
 * @see com.emporia.common.util.crash.reporter.FileLimitAgeSendStrategy
 * @author sky
 */
public final class CrashCollectConfiguration {
    /** send crash way, you can use email or http, default use email send */
    final AbstractCrashReportHandler mSendAbstractCrashReportHandler;
    /** send strategy, you can use limit age, limit cache size, limit file size, default use cache size send strategy */
    final SendStrategy mSendStrategy;
    /** application context */
    final Context mContext;
    /** if only wifi send crash file, default is true */
    final boolean isOnlyWifiSend;
    /** check send crash file time interval, default interval time is 8 hour, unit of milliseconds */
    final long checkSendTimeInterval;

    private CrashCollectConfiguration(final Builder builder) {
        this.mSendAbstractCrashReportHandler = builder.sendAbstractCrashReportHandler;
        this.mSendStrategy = builder.sendStrategy;
        this.mContext = builder.context;
        this.isOnlyWifiSend = builder.isOnlyWifiSend;
        this.checkSendTimeInterval = builder.checkSendTimeInterval;
    }

    public static final class Builder {
        /** @see {@value} */
        private Context context;
        /** @see {@value} s*/
        private AbstractCrashReportHandler sendAbstractCrashReportHandler;
        /** @see {@value} */
        private SendStrategy sendStrategy;
        /** if only wifi send crash file, default is true */
        private boolean isOnlyWifiSend = true;
        /** check send crash file time interval, default interval time is 8 hour, unit of milliseconds */
        private long checkSendTimeInterval = 8 * 60 * 60 * 1000;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        /**
         * set send strategy
         * @param sendStrategy @see {@link com.emporia.common.util.crash.reporter.FileCacheSizeSendStrategy},
         * @see {@link com.emporia.common.util.crash.reporter.FileCountSendStrategy},
         * @see {@link com.emporia.common.util.crash.reporter.FileLimitAgeSendStrategy}
         */
        public Builder sendStrategy(SendStrategy sendStrategy) {
            this.sendStrategy = sendStrategy;
            return this;
        }

        /**
         * set send way
         * @param sendAbstractCrashReportHandler @see {@link com.emporia.common.util.crash.reporter.mailreporter.CrashEmailReporter}
         */
        public Builder sendCrashReportHandler(AbstractCrashReportHandler sendAbstractCrashReportHandler) {
            this.sendAbstractCrashReportHandler = sendAbstractCrashReportHandler;
            return this;
        }

        /**
         * set if only wifi send crash file, default is true
         * @param isOnlyWifiSend @see boolean
         */
        public Builder isOnlyWifiSend(boolean isOnlyWifiSend) {
            this.isOnlyWifiSend = isOnlyWifiSend;
            return this;
        }

        /**
         * set check send time interval, default is 8 hour
         * @param sendTimeInterval unit of milliseconds
         */
        public Builder checkSendTimeInterval(long sendTimeInterval) {
            this.checkSendTimeInterval = sendTimeInterval;
            return this;
        }

        /** Builds configured {@link CrashCollectConfiguration} object */
        public CrashCollectConfiguration build() {
            initEmptyValues();
            return new CrashCollectConfiguration(this);
        }

        /** when configuration values is empty init */
        private void initEmptyValues() {
            if (sendAbstractCrashReportHandler == null) {
                sendAbstractCrashReportHandler = new CrashEmailReporter(context);
            }
            if (sendStrategy == null) {
                sendStrategy = new FileCacheSizeSendStrategy(10 * 1024);
            }
            if (checkSendTimeInterval  <= 0) {
                checkSendTimeInterval = 8 * 60 * 60 * 1000;
            }
        }

    }
}
