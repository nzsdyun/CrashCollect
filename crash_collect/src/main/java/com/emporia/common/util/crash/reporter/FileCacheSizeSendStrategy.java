package com.emporia.common.util.crash.reporter;

import java.io.File;

/**
 * according to the file cache size to decide whether to send
 * @author sky
 */
public class FileCacheSizeSendStrategy implements SendStrategy {
    /** default file cache size */
    private static final int DEFAULT_CACHE_SIZE = 32 * 1024; // 32KB
    /** file cache size */
    private int mCacheSize = DEFAULT_CACHE_SIZE;

    public FileCacheSizeSendStrategy() {
    }

    public FileCacheSizeSendStrategy(int cacheSize) {
        this.mCacheSize = cacheSize;
    }

    /**
     * set file cache size
     * @param cacheSize file cache size
     */
    public void cacheSize(int cacheSize) {
        this.mCacheSize = cacheSize;
    }

    @Override
    public boolean isSend(File fileDir) {
        if (fileDir == null || !fileDir.exists()) {
            return false;
        }
        if (fileDir.getTotalSpace() > mCacheSize) {
            return true;
        }
        return false;
    }
}
