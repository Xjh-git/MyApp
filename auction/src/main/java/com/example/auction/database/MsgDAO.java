package com.example.auction.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.auction.bean.Msg;
import com.example.auction.bean.UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgDAO {
    private Database helper;
    private SQLiteDatabase db;

    public MsgDAO(Context context) {
        helper = new Database(context);
    }

    /*
     * 买卖双发读取通知
     * */
    public void readMsg(Msg msg) {
        db = helper.getWritableDatabase();
    }

    /*
     * 商品被买下后，向买卖双发发送消息
     * */
    public void sendMsg(Msg msg) {
        db = helper.getWritableDatabase();

        String sql = "insert into msg values(null,?,?,?,?,?,?,?);";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int isReadBuyer = 0, isReadSeller = 0;
        if (msg.isReadSeller())
            isReadSeller = 1;
        if (msg.isReadBuyer())
            isReadBuyer = 1;
        db.execSQL(sql, new Object[]{msg.getMsgBuyer(), msg.getMsgSeller(), df.format(msg.getMsgDate()),
                msg.getBuyer(), msg.getSeller(), isReadBuyer, isReadSeller});
        db.close();
    }

    /*
     * 选择消息
     * */
    public List<Msg> selectMsg(int user) {
        List<Msg> list = new ArrayList<>();
        db = helper.getWritableDatabase();
        String sql = "select * from  msg where buyer = ? or seller = ?;";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(user), String.valueOf(user)});
        Msg msg;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        while (cursor.moveToNext()) {
            try {
                msg = new Msg(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("buyer")),
                        cursor.getInt(cursor.getColumnIndex("seller")),
                        cursor.getString(cursor.getColumnIndex("msg_buyer")),
                        cursor.getString(cursor.getColumnIndex("msg_seller")),
                        df.parse(cursor.getString(cursor.getColumnIndex("msgdate"))),
                        cursor.getString(cursor.getColumnIndex("read_buyer")).equals("1"),
                        cursor.getString(cursor.getColumnIndex("read_seller")).equals("1"));
            } catch (ParseException e) {
                msg = new Msg(cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getInt(cursor.getColumnIndex("buyer")),
                        cursor.getInt(cursor.getColumnIndex("seller")),
                        cursor.getString(cursor.getColumnIndex("msg_buyer")),
                        cursor.getString(cursor.getColumnIndex("msg_seller")),
                        new Date(),
                        cursor.getString(cursor.getColumnIndex("read_buyer")).equals("1"),
                        cursor.getString(cursor.getColumnIndex("read_seller")).equals("1"));
                e.printStackTrace();
            }
            list.add(msg);
        }

        db.close();
        return list;
    }

    /*
     * 清楚已读的消息
     * */
    public void deleteMsg(Msg msg) {
        db = helper.getWritableDatabase();

        String sql = "delete from msg where id = ?;";
        db.execSQL(sql, new Object[]{String.valueOf(msg.getId())});
        db.close();
    }


}
