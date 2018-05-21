package JavaBean.Entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Mail implements Serializable {

    private static final long serialVersionUID = 1L;


    private int mail_id;
    private String sender;
    private String to;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String from;


    public String[] getToList() {
        return toList;
    }

    public void setToList(String[] toList) {
        this.toList = toList;
    }

    private String receiver;
    private String subject;
    private String[] toList;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

}
