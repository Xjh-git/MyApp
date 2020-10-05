package com.example.chat.bean;

import java.io.Serializable;

public class User implements Serializable {
    String username, password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public User(String username, String password, String icon) {
        this.username = username;
        this.password = password;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
