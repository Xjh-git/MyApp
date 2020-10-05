package com.example.chat.bean;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    private String msg;
    private Date date;
    String sendUserName, acceptUserName;
    int sendID;

    private Type type;

    public enum Type {
        INCOMING, OUTCOMING;
    }

    public ChatMessage(String msg, Date date, String sendUserName, String acceptUserName, int sendID, Type type) {
        this.msg = msg;
        this.date = date;
        this.sendUserName = sendUserName;
        this.acceptUserName = acceptUserName;
        this.sendID = sendID;
        this.type = type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public ChatMessage(String msg, Date date, String sendUserName, String acceptUserName, Type type) {
        this.msg = msg;
        this.date = date;
        this.sendUserName = sendUserName;
        this.acceptUserName = acceptUserName;
        this.type = type;
    }

    public ChatMessage(String msg, Date date, String sendUserName, String acceptUserName) {
        this.msg = msg;
        this.date = date;
        this.sendUserName = sendUserName;
        this.acceptUserName = acceptUserName;
        this.sendID = 0;
    }

    public ChatMessage(String msg, Date date, String sendUserName, String acceptUserName, int sendID) {
        this.msg = msg;
        this.date = date;
        this.sendUserName = sendUserName;
        this.acceptUserName = acceptUserName;
        this.sendID = sendID;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public void setAcceptUserName(String acceptUserName) {
        this.acceptUserName = acceptUserName;
    }

    public String getMsg() {
        return msg;
    }

    public Date getDate() {
        return date;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public String getAcceptUserName() {
        return acceptUserName;
    }

    public void setSendID(int sendID) {
        this.sendID = sendID;
    }

    public int getSendID() {
        return sendID;
    }
}
