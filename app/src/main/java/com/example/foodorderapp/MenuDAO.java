package com.example.foodorderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private DatabaseHelper dbHelper;

    public MenuDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // THÊM món
    public long insert(MenuItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, item.getName());
        values.put(DatabaseHelper.COL_PRICE, item.getPrice());
        values.put(DatabaseHelper.COL_DESC, item.getDescription());
        return db.insert(DatabaseHelper.TABLE_MENU, null, values);
    }

    // LẤY TẤT CẢ món
    public List<MenuItem> getAll() {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MENU,
                null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            MenuItem item = new MenuItem();
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
            item.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)));
            item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESC)));
            list.add(item);
        }
        cursor.close();
        return list;
    }

    // TÌM KIẾM món theo tên
    public List<MenuItem> search(String keyword) {
        List<MenuItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_MENU,
                null,
                DatabaseHelper.COL_NAME + " LIKE ?",
                new String[]{"%" + keyword + "%"},
                null, null, null);

        while (cursor.moveToNext()) {
            MenuItem item = new MenuItem();
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID)));
            item.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)));
            item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)));
            item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESC)));
            list.add(item);
        }
        cursor.close();
        return list;
    }

    // SỬA món
    public int update(MenuItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, item.getName());
        values.put(DatabaseHelper.COL_PRICE, item.getPrice());
        values.put(DatabaseHelper.COL_DESC, item.getDescription());
        return db.update(DatabaseHelper.TABLE_MENU, values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    // XÓA món
    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_MENU,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(id)});
    }
}