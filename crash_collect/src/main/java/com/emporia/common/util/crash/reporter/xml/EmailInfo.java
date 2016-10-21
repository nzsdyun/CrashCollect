package com.emporia.common.util.crash.reporter.xml;

import java.util.List;

/***
 * email configuration information
 * @author sky
 */
public class EmailInfo {
    /** the sender email list */
    private List<AccountInfo> fromEmails;
    /** the receiver email list */
    private List<String> toEmails;
    /** email server host */
    private String host;
    /** email server port */
    private String port;


    public EmailInfo() {
    }

    public EmailInfo(List<AccountInfo> fromEmails, List<String> toEmails) {
        this.fromEmails = fromEmails;
        this.toEmails = toEmails;

    }

    public List<String> getToEmails() {
        return toEmails;
    }

    public void setToEmails(List<String> toEmails) {
        this.toEmails = toEmails;
    }

    public List<AccountInfo> getFromEmails() {
        return fromEmails;
    }

    public void setFromEmails(List<AccountInfo> fromEmails) {
        this.fromEmails = fromEmails;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Email info[host=" + host + ", port=" + port + ", fromEmails:"
                + toListString(fromEmails) + ", toEmails=" + toListString(toEmails) + "]";
    }
    /** list convert to string */
    private <T> String toListString(List<T> list) {
        if (list == null || list.size() <= 0)
            return "";
        StringBuffer sb = new StringBuffer("{");
        for (T t : list) {
            sb.append(t.toString()).append(",");
        }
        sb.deleteCharAt(list.size() - 1);
        sb.append("}");
        return sb.toString();
    }

    /**
     * account information
     */
    public static class AccountInfo {
        /**
         * user
         */
        private String user;
        /**
         * password
         */
        private String pass;

        public AccountInfo() {
        }

        public AccountInfo(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }


        public String getPass() {
            return pass;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "[user=" + user + ", password=" + pass + "]";
        }
    }
}
