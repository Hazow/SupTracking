package com.supinfo.suptracking.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.supinfo.suptracking.model.User;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAO{
    public static final String TAG = "DAO";
    private MyOpenHelper dbhelper;;
    private SQLiteDatabase db;

    private static final String TABLE_NAME="quote";

    public DAO(Context context) {
        dbhelper = new MyOpenHelper(context);
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void open() throws SQLException{
        db = dbhelper.getWritableDatabase();
    }

    public void insertUser(User user){
        ContentValues values = new ContentValues();
        values.put("username",user.getUsername());
        values.put("password", user.getPassword());
        db.insert("user",null,values);
    }

    public boolean checkIfExist(String name,String password) {
        Cursor cursor = db.rawQuery("select username from user where username = '" + name + "' and password ='" + password + "' ", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean checkTableIsEmpty() {
        String count = "SELECT count(*) FROM user";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount>0) {
            return true;
        }else{
            return false;
        }
    }

    public User getLastUser(){
        String query = "SELECT * FROM user";
        Cursor result = db.rawQuery(query, null);
        result.moveToLast();
        User user = new User();
        user.setId(result.getInt(0));
        user.setUsername(result.getString(1));
        user.setPassword(result.getString(2));
        result.close();
        return user;
    }
}
