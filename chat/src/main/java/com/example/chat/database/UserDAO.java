package com.example.chat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.chat.bean.User;

public class UserDAO {
    ChatDatabase helper;

    SQLiteDatabase db;

    public UserDAO(Context context) {
        helper = new ChatDatabase(context);
    }

    public void registeerUser(User user) {

    }

    public void editUserInfo(User user) {

    }

    public boolean checkUserInfo(String username, String password) {

        return false;
    }

}
