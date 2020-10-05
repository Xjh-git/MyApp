package com.example.auction.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.auction.bean.UserInfo;

public class UserDAO {
    private Database helper;
    private SQLiteDatabase db;

    public UserDAO(Context context) {
        helper = new Database(context);
    }

    public int insert(UserInfo user) {
        db = helper.getWritableDatabase();

        String sql = "insert into user(password,name,icon,address,money,email) values(?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{user.getPassword(), user.getName(), user.getIcon(),
                user.getAddress(), user.getMoney(), user.getEmail()});

        Cursor cursor = db.rawQuery("select last_insert_rowid() from user", null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            return id;
        }
        db.close();
        return -1;
    }

    public  void  update(UserInfo user){
        db = helper.getWritableDatabase();

        String sql = "update user set password = ?,name = ?,icon = ?,address = ?,money = ?,email =?";
        db.execSQL(sql, new Object[]{user.getPassword(), user.getName(), user.getIcon(),
                user.getAddress(), user.getMoney(), user.getEmail()});

        db.close();

    }

    public void delete(int id) {
        db = helper.getWritableDatabase();

        String sql = "delete from user where id = ?";
        db.execSQL(sql, new Object[]{id});

        db.close();
    }

    public UserInfo select(int id) {

        db = helper.getWritableDatabase();

        String sql = "select * from user where id = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            UserInfo user = new UserInfo(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("icon")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getDouble(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("email")));
            return user;
        }

        db.close();


        return null;
    }

    public UserInfo select(String email) {

        db = helper.getWritableDatabase();

        String sql = "select * from user where email = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(email)});
        while (cursor.moveToNext()) {
            UserInfo user = new UserInfo(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("password")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("icon")),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getDouble(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("email")));
            return user;
        }

        db.close();


        return null;
    }
}
