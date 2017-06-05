package com.example.shenhaichen.educationalgameapp.utils;

import java.io.Serializable;

/**
 * Created by shenhaichen on 14/05/2017.
 */

public enum GameLevel implements Serializable {

    BASE(1), PRIMARY(2), INTERMEDIATE(3), ADVANCED(4), EVIL(5);

    private int level;
    private int digtialNum = 15;

    private GameLevel(int v) {
        this.level = v;
    }


    public int getLevel() {
        return level;
    }

    public String getLevelStr() {
        switch (level) {
            case 1:
                return "BASE";
            case 2:
                return "PRIMARY";
            case 3:
                return "INTERMEDIATE";
            case 4:
                return "ADVANCED";
            case 5:
                return "EVIL";
            default:
                return "BASE";

        }
    }

    public static GameLevel get(int v) {
        for (GameLevel gameLevel : GameLevel.values()) {
            if (gameLevel.getLevel() == v) {
                return gameLevel;
            }
        }
        throw new IllegalArgumentException(
                "GameLevel get parameter level is not exists.");
    }
}
