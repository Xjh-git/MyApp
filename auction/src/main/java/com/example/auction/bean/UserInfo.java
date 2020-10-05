package com.example.auction.bean;

public class UserInfo {
    int id;
    String password, name, icon;
    String address;
    double money;
    String email;

    public UserInfo(int id, String password, String name, String icon, String address, double money, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.icon = icon;
        this.address = address;
        this.money = money;
        this.email = email;
    }

    public UserInfo(String password, String name, String icon, String email) {
        this.password = password;
        this.name = name;
        this.icon = icon;
        this.email = email;
        this.money = 0.0;
        this.address = "湖北省武汉市洪山区青菱街道武汉科技大学黄家湖校区";
    }

    public UserInfo() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getAddress() {
        return address;
    }

    public double getMoney() {
        return money;
    }

    public String getEmail() {
        return email;
    }
}
