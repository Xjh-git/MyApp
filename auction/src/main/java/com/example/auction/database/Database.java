package com.example.auction.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public final static String DB_NAME = "auction.db";

    public final static int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists commodity(" +
                "id INTEGER primary key AUTOINCREMENT," +
                "name VARCHAR(20)," +
                "details VARCHAR(400)," +
                "currentPrice DOUBLE," +
                "maxPrice DOUBLE," +
                "icon VARCHAR(100)," +
                "time INTEGER," +
                "ownerID INTEGER)");

        db.execSQL("create table if not exists user(" +
                "id INTEGER primary key AUTOINCREMENT," +
                "password VARCHAR(20)," +
                "name VARCHAR(20)," +
                "icon VARCHAR(100)," +
                "address VARCHAR(400)," +
                "money DOUBLE," +
                "email VARCHAR(20))");

        db.execSQL("create table if not exists msg(\n" +
                "id INTEGER primary key AUTOINCREMENT," +
                "msg_buyer varchar(200)," +
                "msg_seller varchar(200)," +
                "msgdate varchar(50)," +
                "buyer INTEGER," +
                "seller INTEGER," +
                "read_buyer varchar(1)," +
                "read_seller varchar(1));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
