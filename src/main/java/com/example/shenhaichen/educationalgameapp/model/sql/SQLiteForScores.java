package com.example.shenhaichen.educationalgameapp.model.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shenhaichen.educationalgameapp.model.data.Scores;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shenhaichen on 11/05/2017.
 */

public class SQLiteForScores {
    private SQLiteDatabase db;
    private static SQLiteForScores instance = null;
    private static final String TABLE_NAME = "t_scores";

    public SQLiteForScores(Context context) {
       if (db == null){
           db = new MySQLiteOpenHelper(context).getWritableDatabase();
       }
    }


    /**
     *  Using Singleton mode to avoid overlapping
     * @param context
     * @return
     */
    public static SQLiteForScores getInstance(Context context){
        if (instance == null){
            instance = new SQLiteForScores(context);
        }
        return instance;
    }

    /**
     *  insert score into the scores table
     * @param score
     */
    public void insertScore(Scores score){
        ContentValues values = new ContentValues();
        values.put("id", score.id);
        values.put("nickname", score.nickname);
        values.put("scores", score.socres);
        values.put("time", score.time);
        values.put("level",score.level);
        db.insert(TABLE_NAME, null, values);
    }

    /**
     *  select all scores
     * @return
     */
    public List<Scores> selectAllScores() {
        List<Scores> scores = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            scores.add(getScoreFromCursor(cursor));
        }
        return scores;
    }

    /**
     * select  Score list from t_name by nameID
     *
     * @param nameID nameID
     * @return scores
     */
    public List<Scores> selectByNameID(String nameID) {
        List<Scores> scores = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where nameID=?", new String[]{nameID});
        while (cursor.moveToNext()) {
            scores.add(getScoreFromCursor(cursor));
        }
        return scores;
    }

    /**
     * select Score from t_name by nickname
     *
     * @param nameID nickname
     * @return score
     */
    public Scores selectByNickname(String nameID) {
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where nameID=?", new String[]{nameID});
        Scores score = null;
        if (cursor.moveToNext()) {
            score = getScoreFromCursor(cursor);
        }
        return score;
    }

    /**
     * update user login time
     */
    public void update(Scores scores) {
        ContentValues values = new ContentValues();
        values.put("time", scores.time);
        values.put("scores", scores.socres);
        values.put("nickname",scores.nickname);
        values.put("level",scores.level);
        int res = db.update(TABLE_NAME, values, "id=?", new String[]{scores.id});
        if (res > 0) {
            System.out.println("update user success!");
        } else {
            System.out.println("update user fail!");
        }
    }

    /**
     * delete all user from the table
     */
    public void delete() {
        db.delete(TABLE_NAME, null, null);
    }

    /**
     * get current information of user from the cursor
     *
     * @param cursor
     * @return
     */
    private Scores getScoreFromCursor(Cursor cursor) {
        Scores score = new Scores();
        score.id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
        score.socres = cursor.getString(cursor.getColumnIndexOrThrow("scores"));
        score.nickname = cursor.getString(cursor.getColumnIndexOrThrow("nickname"));
        score.time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        score.level = cursor.getString(cursor.getColumnIndexOrThrow("level"));
        return score;
    }
}
