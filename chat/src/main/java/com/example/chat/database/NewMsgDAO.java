package com.example.chat.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.NewMessage;
import com.example.chat.bean.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMsgDAO {
    SQLiteDatabase db;

    ChatDatabase helper;


    private static String TAG = "tag1";

    public NewMsgDAO(Context context) {
        helper = new ChatDatabase(context);
    }


    public void update(String user, NewMessage newMessage) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        db = helper.getWritableDatabase();
        String sql = "select * from msg where user = ? and contacts = ? ; ";
        Cursor cursor = db.rawQuery(sql, new String[]{user, newMessage.getContacts()});
        if (cursor.moveToNext()) {
            sql = "update msg set msg = ?,msgdate = ?,read = ? where user = ? and contacts = ? ;";
            //            Log.d(TAG, "update: " + newMessage.getDate().toString());
            if (newMessage.isRead())
                db.execSQL(sql, new Object[]{newMessage.getMsg(), df.format(newMessage.getDate()), '1', user, newMessage.getContacts()});
            else
                db.execSQL(sql, new Object[]{newMessage.getMsg(), df.format(newMessage.getDate()), '0', user, newMessage.getContacts()});
        } else {
            sql = "insert into msg values(?,?,?,?,?);";
            if (newMessage.isRead())
                db.execSQL(sql, new Object[]{newMessage.getMsg(), df.format(newMessage.getDate()), user, newMessage.getContacts(), '1'});
            else
                db.execSQL(sql, new Object[]{newMessage.getMsg(), df.format(newMessage.getDate()), user, newMessage.getContacts(), '0'});
        }

        cursor.close();
        db.close();

    }


    public void delete(String user, String contacts) {
        db = helper.getWritableDatabase();
        String sql = "delete from msg where user = ? and contacts = ? ;";
        db.execSQL(sql, new Object[]{user, contacts});
        db.close();
    }

    public List<NewMessage> select(String user) {
        db = helper.getWritableDatabase();
        List<NewMessage> list = new ArrayList<>();

        String sql = "select * from msg where user = ? order by read; ";
        Cursor cursor = db.rawQuery(sql, new String[]{user});
        NewMessage newMessage;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            try {
                Log.d(TAG, "select: " + cursor.getString(cursor.getColumnIndex("msgdate")));
                if (cursor.getString(cursor.getColumnIndex("read")).equals("1"))
                    newMessage = new NewMessage(cursor.getString(cursor.getColumnIndex("msg")),
                            df.parse(cursor.getString(cursor.getColumnIndex("msgdate"))),
//                            new Date(),
                            cursor.getString(cursor.getColumnIndex("contacts")),
                            0, true);
                else
                    newMessage = new NewMessage(cursor.getString(cursor.getColumnIndex("msg")),
                            df.parse(cursor.getString(cursor.getColumnIndex("msgdate"))),
//                            new Date(),
                            cursor.getString(cursor.getColumnIndex("contacts")),
                            0, false);
                list.add(newMessage);
            } catch (ParseException e) {
                newMessage = new NewMessage(cursor.getString(cursor.getColumnIndex("msg")),
                        new Date(),
                        cursor.getString(cursor.getColumnIndex("contacts")),
                        0, true);
                list.add(newMessage);
                e.printStackTrace();
            }
        }

        cursor.close();
        db.close();

        return list;
    }
}
