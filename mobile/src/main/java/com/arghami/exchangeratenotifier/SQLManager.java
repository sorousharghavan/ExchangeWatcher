package com.arghami.exchangeratenotifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soroush Arghavan on 11/26/2015.
 */

public class SQLManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExWatch";
    private static final String TABLE_NAME = "Temp";
    private static final String KEY_TEMP = "Temp";
    private static final String KEY_ID = "ID";
    private static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    KEY_ID + " TEXT PRIMARY KEY, " + "" +
                    KEY_TEMP + " TEXT);";

    SQLManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(TABLE_CREATE);
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "=?", new String[]{"1"});
        if (c.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, "1");
            db.insert(TABLE_NAME, null, values);
        }
        c.close();
        db.close();
    }

    public void updateSymb(String symbol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEMP, symbol);
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{"1"});
        db.close();
    }

    public String readSym() {
        SQLiteDatabase db = this.getReadableDatabase();
        String symbol="Error";

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + "=?", new String[]{"1"});
        try {
            if (c.moveToFirst()) {
                symbol = c.getString(c.getColumnIndex(KEY_TEMP));
                c.close();
                db.close();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return symbol;
    }

}