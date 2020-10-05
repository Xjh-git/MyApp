package com.example.auction.bean;

import java.io.Serializable;

public class Commodity implements Serializable {
    int id;
    String name, details, icon;
    double maxPrice, currentPrice;
    int time;
    int ownerID;

    public Commodity(int id, String name, String details, String icon, double maxPrice, double currentPrice, int time, int ownerID) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.icon = icon;
        this.maxPrice = maxPrice;
        this.currentPrice = currentPrice;
        this.time = time;
        this.ownerID = ownerID;
    }

    public Commodity(String name, String details, String icon, double maxPrice, double currentPrice, int time, int ownerID) {
        this.name = name;
        this.details = details;
        this.icon = icon;
        this.maxPrice = maxPrice;
        this.currentPrice = currentPrice;
        this.time = time;
        this.ownerID = ownerID;
    }

    public Commodity() {
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getOwnerID() {
        return ownerID;
    }
}
