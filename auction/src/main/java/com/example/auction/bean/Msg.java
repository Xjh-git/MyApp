package com.example.auction.bean;

import com.example.auction.database.Database;

import java.util.Date;

public class Msg {
    int id, buyer, seller;
    String msgBuyer, msgSeller;
    Date msgDate;
    boolean isReadBuyer, isReadSeller;

    public Msg(int buyer, int seller, String magBuyer, String msgSeller, Date msgDate, boolean isReadBuyer, boolean isReadSeller) {
        this.buyer = buyer;
        this.seller = seller;
        this.msgBuyer = magBuyer;
        this.msgSeller = msgSeller;
        this.msgDate = msgDate;
        this.isReadBuyer = isReadBuyer;
        this.isReadSeller = isReadSeller;
    }

    public Msg(int id, int buyer, int seller, String magBuyer, String msgSeller, Date msgDate, boolean isReadBuyer, boolean isReadSeller) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.msgBuyer = magBuyer;
        this.msgSeller = msgSeller;
        this.msgDate = msgDate;
        this.isReadBuyer = isReadBuyer;
        this.isReadSeller = isReadSeller;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

    public void setSeller(int seller) {
        this.seller = seller;
    }

    public void setMsgBuyer(String magBuyer) {
        this.msgBuyer = magBuyer;
    }

    public void setMsgSeller(String msgSeller) {
        this.msgSeller = msgSeller;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public void setReadBuyer(boolean readBuyer) {
        isReadBuyer = readBuyer;
    }

    public void setReadSeller(boolean readSeller) {
        isReadSeller = readSeller;
    }

    public int getId() {
        return id;
    }

    public int getBuyer() {
        return buyer;
    }

    public int getSeller() {
        return seller;
    }

    public String getMsgBuyer() {
        return msgBuyer;
    }

    public String getMsgSeller() {
        return msgSeller;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public boolean isReadBuyer() {
        return isReadBuyer;
    }

    public boolean isReadSeller() {
        return isReadSeller;
    }
}
