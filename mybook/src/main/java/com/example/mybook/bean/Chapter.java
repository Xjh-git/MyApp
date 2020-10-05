package com.example.mybook.bean;

public class Chapter {
    String title;
    String content;


    public Chapter(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String toString() {
        return (title + "\n" + content).replaceAll("\n","\r\n");
    }
}
