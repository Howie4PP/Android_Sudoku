package com.example.shenhaichen.educationalgameapp.utils;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by shenhaichen on 19/05/2017.
 */

public class Music {

    private static MediaPlayer mediaPlayer = null;

    private Music() {
    }

    public static void play(Context context, int resource) {
        stop();
        if (Utils.isMusicOpen) {
            mediaPlayer = MediaPlayer.create(context, resource);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
