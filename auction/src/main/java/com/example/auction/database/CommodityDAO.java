package com.example.auction.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.auction.bean.Commodity;

import java.util.ArrayList;
import java.util.List;

public class CommodityDAO {
    private Database helper;

    private SQLiteDatabase db;

    public CommodityDAO(Context context) {
        helper = new Database(context);
    }

    public void insert(Commodity commodity) {
        db = helper.getWritableDatabase();

        String sql = "insert into commodity(name,details,currentPrice,maxPrice,icon,time,ownerID) values(?,?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{commodity.getName(), commodity.getDetails(),
                commodity.getCurrentPrice(), commodity.getMaxPrice(), commodity.getIcon(),
                commodity.getTime(), commodity.getOwnerID()});
        db.close();
    }

    public void delete(int id) {
        db = helper.getWritableDatabase();
        String sql = "delete from commodity where id = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }

    public void update(Commodity commodity) {
        db = helper.getWritableDatabase();
        String sql = "update commodity set name=?,details=?,currentPrice=?,maxPrice=?,icon=?,time=?,ownerID=? where id = ?";
        db.execSQL(sql, new Object[]{commodity.getName(), commodity.getDetails(), commodity.getCurrentPrice(),
                commodity.getMaxPrice(), commodity.getIcon(), commodity.getId(), commodity.getTime(), commodity.getOwnerID()});
        db.close();
    }

    public Commodity find(int id) {
        Commodity commodity = null;

        db = helper.getWritableDatabase();
        String sql = "select * from commodity where id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
        while (cursor.moveToNext()) {
            commodity = new Commodity(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("details")),
                    cursor.getString(cursor.getColumnIndex("icon")),
                    cursor.getDouble(cursor.getColumnIndex("maxPrice")),
                    cursor.getDouble(cursor.getColumnIndex("currentPrice")),
                    cursor.getInt(cursor.getColumnIndex("time")),
                    cursor.getInt(cursor.getColumnIndex("ownerID")));
        }
        db.close();

        return commodity;
    }

    public List<Commodity> get() {
        Commodity commodity = null;
        List<Commodity> list = new ArrayList<>();

        db = helper.getWritableDatabase();
        String sql = "select * from commodity";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        while (cursor.moveToNext()) {
            commodity = new Commodity(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("details")),
                    cursor.getString(cursor.getColumnIndex("icon")),
                    cursor.getDouble(cursor.getColumnIndex("maxPrice")),
                    cursor.getDouble(cursor.getColumnIndex("currentPrice")),
                    cursor.getInt(cursor.getColumnIndex("time")),
                    cursor.getInt(cursor.getColumnIndex("ownerID")));
            list.add(commodity);
        }
        db.close();
        return list;
    }
}
