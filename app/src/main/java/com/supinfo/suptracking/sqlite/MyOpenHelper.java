package com.supinfo.suptracking.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="my.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="user";
    private static final String TABLE_CREATE=
            "CREATE TABLE "+TABLE_NAME+"("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "username VARCHAR NOT NULL, "+
                    "password VARCHAR NOT NULL);";

    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
