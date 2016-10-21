package com.emporia.common.util.crash.reporter;

import java.io.File;

/**
 * send strategy
 * @see com.emporia.common.util.crash.reporter.FileCacheSizeSendStrategy
 * @see com.emporia.common.util.crash.reporter.FileCountSendStrategy
 * @see com.emporia.common.util.crash.reporter.FileLimitAgeSendStrategy
 * @author sky
 */
public interface SendStrategy {
    /** whether can send crash file */
    boolean isSend(File fileDir);
}
