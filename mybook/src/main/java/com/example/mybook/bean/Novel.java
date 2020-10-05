package com.example.mybook.bean;

import java.io.Serializable;

public class Novel implements Serializable {
    String name;
    String wirter;
    String link;

    public Novel(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public Novel(String name, String wirter, String link) {
        this.name = name;
        this.wirter = wirter;
        this.link = link;
    }

    public void setWirter(String wirter) {
        this.wirter = wirter;
    }

    public String getWirter() {
        return wirter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
