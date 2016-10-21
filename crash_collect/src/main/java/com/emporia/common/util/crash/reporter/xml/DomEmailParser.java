package com.emporia.common.util.crash.reporter.xml;

import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

import org.xml.sax.Attributes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * use dom parser email information
 * @author sky
 */
public class DomEmailParser implements EmailParser {
    /** send email list */
    private List<EmailInfo.AccountInfo> fromEmailList = new ArrayList<EmailInfo.AccountInfo>();
    /** receiver email list */
    private List<String> toEmailList = new ArrayList<String>();
    /** email account information */
    private EmailInfo.AccountInfo mailInfo = null;
    /** email information */
    private EmailInfo emailInfos = null;

    @Override
    public EmailInfo parser(InputStream inputStream, String encode) throws Exception {
        RootElement document = new RootElement("email");
        document.setElementListener(new ElementListener() {
            @Override
            public void end() {

                emailInfos.setFromEmails(fromEmailList);
                emailInfos.setToEmails(toEmailList);
            }

            @Override
            public void start(Attributes attributes) {
                emailInfos = new EmailInfo();

            }
        });
        Element fromList = document.getChild("from-list");
        Element itemHost = fromList.getChild("host");
        itemHost.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                emailInfos.setHost(body);
            }
        });
        Element itemPort = fromList.getChild("port");
        itemPort.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                emailInfos.setPort(body);
            }
        });
        Element itemFrom = fromList.getChild("item");
        itemFrom.setElementListener(new ElementListener() {

            @Override
            public void end() {
                fromEmailList.add(mailInfo);
                mailInfo = null;
            }

            @Override
            public void start(Attributes attributes) {
                mailInfo = new EmailInfo.AccountInfo();
            }
        });
        itemFrom.getChild("account").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mailInfo.setUser(body);
            }
        });
        itemFrom.getChild("password").setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                mailInfo.setPass(body);
            }
        });
        Element toList = document.getChild("to-list");
        Element itemTo = toList.getChild("item");
        itemTo.setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String body) {
                toEmailList.add(body);
            }
        });
        Xml.parse(new InputStreamReader(inputStream, encode), document.getContentHandler());
        return emailInfos;
    }
}
