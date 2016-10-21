package com.emporia.common.util.crash;

import java.io.File;
/**
 * Interface to save the crash file
 * @author sky
 *
 */
public interface CrashSendListener {
	/** Save the file after you can via email, http log */
	void sendCrashFile(File file);
}
