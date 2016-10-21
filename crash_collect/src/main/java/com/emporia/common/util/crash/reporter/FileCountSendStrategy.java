package com.emporia.common.util.crash.reporter;

import java.io.File;

/**
 * according to the number of files to determine whether to send
 * @author sky
 */
public class FileCountSendStrategy implements SendStrategy {
    /** default file file count */
    private static final int DEFAULT_FILE_COUNT = 32;
    /** file count */
    private int mFileCount = DEFAULT_FILE_COUNT;

    public FileCountSendStrategy() {
    }

    public FileCountSendStrategy(int fileCount) {
        this.mFileCount = fileCount;
    }

    /**
     * set file count
     * @param fileCount file count
     */
    public void fileCount(int fileCount) {
        this.mFileCount = fileCount;
    }

    @Override
    public boolean isSend(File fileDir) {
        if (fileDir == null || !fileDir.exists()) {
            return false;
        }
        if (fileDir.isDirectory()) {
            File[] files = fileDir.listFiles();
            if (files != null && files.length >= mFileCount) {
                return true;
            }
        }
        return false;
    }
}
