package com.example.chat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabase extends SQLiteOpenHelper {

    private final static String DB_NAME = "chat.db";

    private final static int DB_VERSION = 1;

    public ChatDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists messages(\n" +
                "id INTEGER  primary key," +
                "sendusername varchar(20)," +
                "acceptusername varchar(20)," +
                "msg varchar(200)," +
                "msgdate varchar(50)" +
                ");");
        db.execSQL("create table if not exists msg(\n" +
                "msg varchar(200)," +
                "msgdate varchar(50)," +
                "user varchar(20)," +
                "contacts varchar(20)," +
                "read varchar(1)," +
                "primary key(user,contacts)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
