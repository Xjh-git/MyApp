package com.example.chat.bean;

public class Contacts {
    User contacts;
    String nickName;

    public Contacts(User contacts, String nickName) {
        this.contacts = contacts;
        this.nickName = nickName;
    }

    public void setContacts(User contacts) {
        this.contacts = contacts;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public User getContacts() {
        return contacts;
    }

    public String getNickName() {
        return nickName;
    }
}
