package com.example.foodorderapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "foodorder.db";
    static final int DATABASE_VERSION = 2;

    // MENU
    static final String TABLE_MENU = "menu_items";
    static final String COL_ID = "id";
    static final String COL_NAME = "name";
    static final String COL_PRICE = "price";
    static final String COL_DESC = "description";

    // USER
    static final String TABLE_USER = "users";
    static final String COL_USER_ID = "id";
    static final String COL_USERNAME = "username";
    static final String COL_PASSWORD = "password";
    static final String COL_FULLNAME = "fullname";
    static final String COL_PHONE = "phone";

    // ORDER
    static final String TABLE_ORDER = "orders";
    static final String COL_ORDER_ID = "id";
    static final String COL_ORDER_USER = "username";
    static final String COL_ORDER_ITEMS = "items";
    static final String COL_ORDER_TOTAL = "total";
    static final String COL_ORDER_DATE = "date";
    static final String COL_ORDER_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MENU + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_PRICE + " REAL NOT NULL, "
                + COL_DESC + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_USER + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT UNIQUE NOT NULL, "
                + COL_PASSWORD + " TEXT NOT NULL, "
                + COL_FULLNAME + " TEXT, "
                + COL_PHONE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ORDER + " ("
                + COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ORDER_USER + " TEXT, "
                + COL_ORDER_ITEMS + " TEXT, "
                + COL_ORDER_TOTAL + " REAL, "
                + COL_ORDER_DATE + " INTEGER, "
                + COL_ORDER_STATUS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        onCreate(db);
    }
}