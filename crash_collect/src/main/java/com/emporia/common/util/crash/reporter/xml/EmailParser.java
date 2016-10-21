package com.emporia.common.util.crash.reporter.xml;

import java.io.InputStream;

/**
 * email xml message parser, email files need to be in the assets folder,
 * the following email message format
 * <pre>
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
public interface EmailParser {
    /** parser email information */
    EmailInfo parser(InputStream inputStream, String encode) throws Exception;
}
