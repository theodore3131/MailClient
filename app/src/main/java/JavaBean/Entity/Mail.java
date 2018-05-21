package JavaBean.Entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Mail implements Serializable {
    private static final long serialVersionUID = 1L;
    private int mail_id;
    private String from;
    private String to;
    private String[] toList;
    private String subject;
    private String content;
    private Timestamp time;
    private int readStat;
    private int sendStat;
    public int getMail_id() {
        return mail_id;
    }

    public void setMail_id(int mail_id) {
        this.mail_id = mail_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getToList() {
        return toList;
    }

    public void setToList(String[] toList) {
        this.toList = toList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public  void setSubject(String subject) {
        this.subject = subject;
    }

    public int getReadStat() {
        return readStat;
    }

    public void setReadStat(int readStat) {
        this.readStat = readStat;
    }

    public int getSendStat() {
        return sendStat;
    }

    public void setSendStat(int sendStat) {
        this.sendStat = sendStat;
    }

    public Mail() {
    }

    public Mail(String from, String[] toList, String subject, String content) {
        this.from = from;
        this.toList = toList;
        this.subject = subject;
        this.content = content;
    }
}
