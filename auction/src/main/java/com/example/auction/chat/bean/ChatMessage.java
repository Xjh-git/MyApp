package com.example.auction.chat.bean;

import java.util.Date;

public class ChatMessage {
    private String name;
    private String msg;
    private Type type;
    private Date date;

    public ChatMessage() {
    }

    public ChatMessage(String msg, Type type, Date date) {
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public enum Type{
        INCOMING,OUTCOMING
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }

    public Type getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }
}
