package com.example.foodorderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long insertOrder(String username, String items, double total) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_ORDER_USER, username);
        values.put(DatabaseHelper.COL_ORDER_ITEMS, items);
        values.put(DatabaseHelper.COL_ORDER_TOTAL, total);
        values.put(DatabaseHelper.COL_ORDER_DATE, System.currentTimeMillis());
        values.put(DatabaseHelper.COL_ORDER_STATUS, "Đang xử lý");
        return db.insert(DatabaseHelper.TABLE_ORDER, null, values);
    }

    public List<Order> getOrdersByUser(String username) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ORDER, null,
                DatabaseHelper.COL_ORDER_USER + "=?",
                new String[]{username}, null, null,
                DatabaseHelper.COL_ORDER_DATE + " DESC");
        while (cursor.moveToNext()) {
            Order o = new Order();
            o.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ID)));
            o.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_USER)));
            o.setItems(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_ITEMS)));
            o.setTotal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_TOTAL)));
            o.setDate(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_DATE)));
            o.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ORDER_STATUS)));
            list.add(o);
        }
        cursor.close();
        return list;
    }

    public int getTotalOrders(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_ORDER
                + " WHERE " + DatabaseHelper.COL_ORDER_USER + "=?", new String[]{username});
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public double getTotalSpent(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + DatabaseHelper.COL_ORDER_TOTAL + ") FROM "
                        + DatabaseHelper.TABLE_ORDER + " WHERE " + DatabaseHelper.COL_ORDER_USER + "=?",
                new String[]{username});
        double total = 0;
        if (cursor.moveToFirst()) total = cursor.getDouble(0);
        cursor.close();
        return total;
    }
}