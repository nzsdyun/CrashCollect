package com.emporia.common.util.crash.reporter.mailreporter;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * use javaMail send email, href = "http://www.jondev.net/articles/Sending_Emails_without_User_Intervention_(no_Intents)_in_Android",
 * you can use like:
 * <pre>
 *      <code>
 *               Mail m = new Mail("gmailusername@gmail.com", "password");
 *               String[] toArr = {"bla@bla.com", "lala@lala.com"};
 *               m.setTo(toArr);
 *               m.setFrom("wooo@wooo.com");
 *               m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
 *               m.setBody("Email body.");
 *
 *               try {
 *                    m.addAttachment("/sdcard/filelocation");
 *
 *                   if(m.send()) {
 *                       Toast.makeText(MailApp.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
 *                   } else {
 *                       Toast.makeText(MailApp.this, "Email was not sent.", Toast.LENGTH_LONG).show();
 *                   }
 *               } catch(Exception e) {
 *                   //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
 *                   Log.e("MailApp", "Could not send email", e);
 *               }
 *      </code>
 * </pre>
 */
public class Mail extends javax.mail.Authenticator {
    private String user;
    private String pass;

    private String[] to;
    private String from;

    private String port;
    private String sport;

    private String host;

    private String subject;
    private String body;

    private boolean auth;

    private boolean debuggable;

    private Multipart multipart;

    public String getUser() {
        return user;
    }

    public Mail setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPass() {
        return pass;
    }

    public Mail setPass(String pass) {
        this.pass = pass;
        return this;
    }

    public String[] getTo() {
        return to;
    }

    public Mail setTo(String[] to) {
        this.to = to;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public Mail setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getPort() {
        return port;
    }

    public Mail setPort(String port) {
        this.port = port;
        return this;
    }

    public String getSport() {
        return sport;
    }

    public Mail setSport(String sport) {
        this.sport = sport;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Mail setHost(String host) {
        this.host = host;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isAuth() {
        return auth;
    }

    public Mail setAuth(boolean auth) {
        this.auth = auth;
        return this;
    }


    public boolean isDebuggable() {
        return debuggable;
    }

    public Mail setDebuggable(boolean debuggable) {
        this.debuggable = debuggable;
        return this;
    }

    public Mail() {
        host = "smtp.gmail.com"; // default smtp server
        port = "465"; // default smtp port
        sport = "465"; // default socketfactory port

        user = ""; // username
        pass = ""; // password
        from = ""; // email sent from
        subject = ""; // email subject
        body = ""; // email body

        debuggable = false; // debug mode on or off - default off
        auth = true; // smtp authentication - default on

        multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);
    }

    public Mail(String user, String pass) {
        this();

        this.user = user;
        this.pass = pass;
    }
    /** send email */
    public boolean send() throws Exception {
        Properties props = _setProperties();
        if(!user.equals("") && !pass.equals("") && to.length > 0 && !from.equals("") && !subject.equals("") && !body.equals("")) {
            Session session = Session.getInstance(props, this);

            MimeMessage msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(from));

            InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);

            // Put parts in message
            msg.setContent(multipart);

            // send email
            Transport.send(msg);

            return true;
        } else {
            return false;
        }
    }

    /**
     * add file attachments
     * @param filePath file path
     * @param fileName file name
     * @throws Exception @see Exception
     */
    public void addAttachment(String filePath, String fileName) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);

        multipart.addBodyPart(messageBodyPart);
    }

    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, pass);
    }

    private Properties _setProperties() {
        Properties props = new Properties();

        props.put("mail.smtp.host", host);

        if(debuggable) {
            props.put("mail.debug", "true");
        }

        if(auth) {
            props.put("mail.smtp.auth", "true");
        }

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        return props;
    }
}
