package com.example.chat.bean;

import java.io.Serializable;
import java.util.List;

public class ResultMsg implements Serializable {
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
    public static final int DELETE_NEW_MSG = 11;  //删除新消息表的数据,返回值Boolean
    public static final int GET_READ_MSG = 12 ; //得到和莫人的聊天记录

    int control = 0;
    boolean result = false;
    List<ChatMessage> chatMessageList;
    int sendId;

    List<String> userList;

    ChatMessage chatMessage;
    int newMsgCount;

    List<NewMessage> newMessageList;

    public ResultMsg(int control) {
        this.control = control;
    }

    public ResultMsg(int control, ChatMessage chatMessage, int newMsgCount) {
        this.control = control;
        this.chatMessage = chatMessage;
        this.newMsgCount = newMsgCount;
    }

    //返回申请好友、好友的列表
    public ResultMsg(List<String> userList) {
        this.userList = userList;
    }

    //得到消息的返回结果
    public ResultMsg(int control, List<ChatMessage> chatMessageList) {
        this.control = control;
        this.chatMessageList = chatMessageList;
    }

    //注册、登录
    public ResultMsg(int control, boolean result) {
        this.control = control;
        this.result = result;
    }

    // 发送消息的返回结果
    //注册、登录
    public ResultMsg(int control, int result) {
        this.control = control;
        this.sendId = result;
    }

    public void setResult(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public void setControl(int control) {
        this.control = control;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getControl() {
        return control;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public boolean getResult() {
        return result;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    public int getSendId() {
        return sendId;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public int getNewMsgCount() {
        return newMsgCount;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public void setNewMsgCount(int newMsgCount) {
        this.newMsgCount = newMsgCount;
    }

    public List<NewMessage> getNewMessageList() {
        return newMessageList;
    }

    public void setNewMessageList(List<NewMessage> newMessageList) {
        this.newMessageList = newMessageList;
    }
}
