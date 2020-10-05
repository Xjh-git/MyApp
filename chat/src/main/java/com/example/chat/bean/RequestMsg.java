package com.example.chat.bean;

import java.io.Serializable;

public class RequestMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int LOGIN = 1;  //登录，返回值 Boolean
    public static final int REGISTER = 2;  //注册，返回值 Boolean
    public static final int SEND_MSG = 3;  //发消息， 返回值Boolean
    public static final int GET_MSG = 4;   //得到所有消息，返回值List<ChatMessage> 消息列表
    public static final int GET_NEW_MSG = 5;  //得到最新消息，返回值消息列表
    public static final int GET_CONTACTS_NEW_MSG = 6;  //得到和莫一个联系人的新消息，返回值消息列表
    public static final int CONTACTS_APPLY = 7;  //申请好友，返回值Boolean
    public static final int CONTACTS_APPLY_AGREE = 8;  //同意申请好友，返回值Boolean
    public static final int GET_CONTACTS_APPLY = 9;  //申请好友列表，返回值List<User>用户列表
    public static final int GET_CONTACTS = 10;  //好友列表，返回值用户列表
    public  static  final  int DELETE_NEW_MSG = 11;  //删除新消息表的数据,返回值Boolean
    public static final int GET_READ_MSG = 12 ; //得到和莫人的聊天记录

    private int control = 0;

    User user;
    ChatMessage chatMessage;

    int sendID = 0;
    String username, contactsName;


    //请求用户的好友或申请好友列表
    public RequestMsg(int control, String username) {
        this.control = control;
        this.username = username;
    }

    //请求申请好友或同意申请好友,删除两人的新消息数据，将数据写入消息表,得到两人的聊天纪录
    public RequestMsg(int control, String username, String contactsName) {
        this.control = control;
        this.username = username;
        this.contactsName = contactsName;
    }


    //请求注册或登录,请求的到所有用户的新消息
    public RequestMsg(int control, User user) {
        this.control = control;
        this.user = user;
    }

    //请求发送消息
    public RequestMsg(int control, ChatMessage chatMessage) {
        this.control = control;
        this.chatMessage = chatMessage;
    }

    public RequestMsg() {
    }

    public void setControl(int control) {
        this.control = control;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public int getControl() {
        return control;
    }

    public User getUser() {
        return user;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setSendID(int sendID) {
        this.sendID = sendID;
    }

    public int getSendID() {
        return sendID;
    }

    public String getUsername() {
        return username;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }
}
