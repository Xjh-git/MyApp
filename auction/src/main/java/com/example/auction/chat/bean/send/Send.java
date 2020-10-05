package com.example.auction.chat.bean.send;

public class Send {
//    private int reqType;
    private Perception perception;
    private UserInfo userInfo;

    public Send(int reqType, Perception perception) {
//        this.reqType = reqType;
        this.perception = perception;
        this.userInfo = new UserInfo();
    }

    public Send(Perception perception) {
//        this.reqType = 0;
        this.perception = perception;
        this.userInfo = new UserInfo();
    }
}
