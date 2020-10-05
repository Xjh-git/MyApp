package com.example.chat.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessageDAO {
    ChatDatabase helper;

    SQLiteDatabase db;


    private static final String TAG = "tag1";

    public ChatMessageDAO(Context context) {
        helper = new ChatDatabase(context);
    }

    public void insert(ChatMessage chatMessage) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        db = helper.getWritableDatabase();

        String sql = "insert into messages(sendusername,acceptusername,msg,msgdate) values (?,?,?,?);";
        db.execSQL(sql, new Object[]{chatMessage.getSendUserName(), chatMessage.getAcceptUserName(),
                chatMessage.getMsg(),df.format( chatMessage.getDate())});

        db.close();
    }

    public List<ChatMessage> select(String user, String contacts) {
        db = helper.getWritableDatabase();

        String sql = "select * from messages where acceptusername = ? and sendusername = ? or acceptusername = ? and sendusername = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{user, contacts, contacts, user});
        ChatMessage chatMessage = null;
        List<ChatMessage> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ChatMessage.Type type = ChatMessage.Type.INCOMING;
            if (cursor.getString(cursor.getColumnIndex("sendusername")).equals(user)) {
                type = ChatMessage.Type.OUTCOMING;
            } else {
                type = ChatMessage.Type.INCOMING;
            }

            try {
                chatMessage = new ChatMessage(cursor.getString(cursor.getColumnIndex("msg")),
                        df.parse(df.format(cursor.getString(cursor.getColumnIndex("msgdate")))),
                        cursor.getString(cursor.getColumnIndex("sendusername")),
                        cursor.getString(cursor.getColumnIndex("acceptusername")),
                        cursor.getInt(cursor.getColumnIndex("id")),
                        type);
            } catch (ParseException e) {
                chatMessage = new ChatMessage(cursor.getString(cursor.getColumnIndex("msg")),
                        new Date(),
                        cursor.getString(cursor.getColumnIndex("sendusername")),
                        cursor.getString(cursor.getColumnIndex("acceptusername")),
                        cursor.getInt(cursor.getColumnIndex("id")),
                        type);
                e.printStackTrace();
            }
            list.add(chatMessage);
        }

        return list;

    }

    public List<ChatMessage> getUserMsg(String user) {
        db = helper.getWritableDatabase();
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage chatMessage = null;

        String sql = "select * from messages where acceptusername = ? or sendusername = ? order by id desc ;";
        Cursor cursor = db.rawQuery(sql, new String[]{user, user});
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            try {
                chatMessage = new ChatMessage(cursor.getString(cursor.getColumnIndex("msg")),
                        df.parse(df.format(cursor.getString(cursor.getColumnIndex("msgdate")))),
                        cursor.getString(cursor.getColumnIndex("sendusername")),
                        cursor.getString(cursor.getColumnIndex("acceptusername")),
                        cursor.getInt(cursor.getColumnIndex("id")));
                list.add(chatMessage);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return list;
    }


}
