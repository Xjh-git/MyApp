package com.example.chat.bean;

import java.io.Serializable;
import java.util.Date;

public class NewMessage implements Serializable {
    String msg;
    Date date;
    String contacts;
    int count;
    boolean isRead;

    public NewMessage(String msg, Date date, String contacts, int count, boolean isRead) {
        this.msg = msg;
        this.date = date;
        this.contacts = contacts;
        this.count = count;
        this.isRead = isRead;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getMsg() {
        return msg;
    }

    public Date getDate() {
        return date;
    }

    public String getContacts() {
        return contacts;
    }

    public int getCount() {
        return count;
    }

    public boolean isRead() {
        return isRead;
    }
}
