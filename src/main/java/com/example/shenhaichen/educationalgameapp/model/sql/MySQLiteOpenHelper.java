package com.example.shenhaichen.educationalgameapp.model.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shenhaichen on 11/05/2017.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_stem_system";
    private static final int VERSION = 1;


    private static final String T_SCORES = "CREATE TABLE t_scores (" +
            "id TEXT," +
            "nickname TEXT," +
            "scores integer," +
            "level TEXT,"+
            "time TEXT" + ");";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(T_NAME);
        db.execSQL(T_SCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
