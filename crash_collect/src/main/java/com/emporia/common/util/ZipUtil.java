package com.emporia.common.util;

import com.emporia.common.util.crash.CrashExceptionHandler;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

/**
 * use zip4j compress file
 * @author sky
 */
public final class ZipUtil {
    private static final String TAG = ZipUtil.class.getSimpleName();
    /** compress file password */
    private static final String compressPwd = "emporia123456";
    /** compress files, files will be stored in its original parent directory */
    public static File compress(File fileDirPath) {
        if (fileDirPath == null || !fileDirPath.exists()) {
            return null;
        }
        String destFilePath = CrashExceptionHandler.getIntance().getCrashFileDir().getAbsolutePath() + File.separator +  "crash.zip";
        return compress(destFilePath, fileDirPath);
    }
    /** compress files by file path */
    public static File compress(String destFilePath, String srcFolders) {
        File destFile = new File(destFilePath);
        File srcFile = new File(srcFolders);
        if (srcFile == null
                || !srcFile.exists()
                || destFile == null) {
            return null;
        }
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            ZipFile zipFile = new ZipFile(destFile);
            // Initiate Zip Parameters which define various properties such
            // as compression method, etc. More parameters are explained in other
            // examples
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

            // Set the compression level. This value has to be in between 0 to 9
            // Several predefined compression levels are available
            // DEFLATE_LEVEL_FASTEST - Lowest compression level but higher speed of compression
            // DEFLATE_LEVEL_FAST - Low compression level but higher speed of compression
            // DEFLATE_LEVEL_NORMAL - Optimal balance between compression level/speed
            // DEFLATE_LEVEL_MAXIMUM - High compression level with a compromise of speed
            // DEFLATE_LEVEL_ULTRA - Highest compression level but low speed
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            // Set the encryption flag to true
            // If this is set to false, then the rest of encryption properties are ignored
            parameters.setEncryptFiles(true);

            // Set the encryption method to AES Zip Encryption
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

            // Set AES Key strength. Key strengths available for AES encryption are:
            // AES_STRENGTH_128 - For both encryption and decryption
            // AES_STRENGTH_192 - For decryption only
            // AES_STRENGTH_256 - For both encryption and decryption
            // Key strength 192 cannot be used for encryption. But if a zip file already has a
            // file encrypted with key strength of 192, then Zip4j can decrypt this file
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

            // Set password
            parameters.setPassword(compressPwd);

            // Now add files to the zip file
            // Note: To add a single file, the method addFile can be used
            // Note: If the zip file already exists and if this zip file is a split file
            // then this method throws an exception as Zip Format Specification does not
            // allow updating split zip files
            zipFile.addFolder(srcFile, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return destFile;
    }
    /** compress files by file */
    public static File compress(String destFilePath, File srcFolders) {
        if (srcFolders == null) {
            return null;
        }
        return compress(destFilePath, srcFolders.getAbsolutePath());
    }
}
