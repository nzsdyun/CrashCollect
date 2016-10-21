package com.emporia.common.util.crash.reporter;

import java.io.File;

/**
 * according to the modified time intervals of the file
 * @author sky
 */
public class FileLimitAgeSendStrategy implements SendStrategy {
    /** default from the last time interval of files */
    private static final int DEFAULT_LIMIT_AGE = 24 * 60 * 60 * 1000;
    /** last time interval of files */
    private int mLimitAge = DEFAULT_LIMIT_AGE;

    public FileLimitAgeSendStrategy() {
    }

    public FileLimitAgeSendStrategy(int limitAge) {
        this.mLimitAge = limitAge;
    }

    /**
     * set time interval, in milliseconds
     * @param limitAge time interval
     */
    public void limitAge(int limitAge) {
        this.mLimitAge = limitAge;
    }

    @Override
    public boolean isSend(File fileDir) {
        if (fileDir == null || !fileDir.exists()) {
            return false;
        }
        if (System.currentTimeMillis() - fileDir.lastModified() > mLimitAge) {
            return true;
        }
        return false;
    }
}
