package com.example.shenhaichen.educationalgameapp.model.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shenhaichen.educationalgameapp.model.data.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhaichen on 12/05/2017.
 */

public class SQLiteForUser {
    private SQLiteDatabase db;
    private static SQLiteForUser instance = null;
    private static final String TABLE_NAME = "t_name";

    private SQLiteForUser(Context context) {
        if (db == null) {
            db = new MySQLiteOpenHelper(context).getWritableDatabase();
        }
    }


    public static SQLiteForUser getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteForUser(context);
        }
        return instance;
    }

    /**
     * insert the data of user
     * @param user
     */
    public void insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("nickname", user.nickName);
        values.put("nameID", user.nameId);
        values.put("time", user.time);
        db.insert(TABLE_NAME, null, values);
    }

    /**
     * select all users
     *
     * @return all users as an ArrayList
     */
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            users.add(getUserFromCursor(cursor));
        }
        return users;
    }

    /**
     * select an User from t_name by nickname
     * @param nickname nickname
     * @return user
     */
    public User selectByNickname(String nickname) {
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where nickname=?", new String[]{nickname});
        User user = null;
        if (cursor.moveToNext()) {
            user = getUserFromCursor(cursor);
        }
        return user;
    }

    /**
     * select an User from t_name by nameID
     *
     * @param nameID nameID
     * @return user
     */
    public User selectByNameID(String nameID) {
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where nameID=?", new String[]{nameID});
        User user = null;
        if (cursor.moveToNext()) {
            user = getUserFromCursor(cursor);
        }
        return user;
    }

    /**
     * update user login time
     *
     * @param nameID userID
     * @param time   login time
     */
    public void update(String nameID, String time) {
        ContentValues values = new ContentValues();
        values.put("time", time);
        int res = db.update(TABLE_NAME, values, "nameID=?", new String[]{nameID});
        if (res > 0) {
            System.out.println( "update user success!");
        } else {
            System.out.println( "update user fail!");
        }
    }

    /**
     * delete all user from the table
     */
    public void delete() {
        db.delete(TABLE_NAME, null, null);
    }

    /**
     * get the current information of user from cursor
     *
     * @param cursor
     * @return user
     */
    private User getUserFromCursor(Cursor cursor) {
        User user = new User();
        user.nameId = cursor.getString(cursor.getColumnIndexOrThrow("nameID"));
        user.nickName = cursor.getString(cursor.getColumnIndexOrThrow("nickname"));
        user.time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        return user;
    }

}
