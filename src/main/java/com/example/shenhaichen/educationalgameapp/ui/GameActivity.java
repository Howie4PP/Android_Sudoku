package com.example.shenhaichen.educationalgameapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.model.data.Scores;
import com.example.shenhaichen.educationalgameapp.model.sql.SQLiteForScores;
import com.example.shenhaichen.educationalgameapp.utils.GameLevel;
import com.example.shenhaichen.educationalgameapp.utils.Music;
import com.example.shenhaichen.educationalgameapp.utils.SPUtil;
import com.example.shenhaichen.educationalgameapp.utils.SudokuPuzzleGenerator;
import com.example.shenhaichen.educationalgameapp.utils.Utils;

import java.util.UUID;

public class GameActivity extends Activity {

    private Context context;
    private CustomSquareView squareView;
    private static int[][] WHOLE_SUDOKU = new int[9][9];
    private static int[][] SPACE_SUDOKU = new int[9][9];
    // keep the default array, in order to clean all blank square
    private int[][] sudokuArray;
    private boolean isEnd = false;
    // default level is base
    private GameLevel gameLevel = GameLevel.BASE;
    private TextView time_tv;
    private int time;
    private String hourStr = "00";
    private String secondStr = "00";
    private String minuteStr = "00";
    private boolean isGameStart = false;
    // get sensor
    private SensorManager sensorManager;
    private static final int SHAKE = 11;
    //any value
    private static final int SHAKE_VALUE = 19;
    private boolean isThreadStarted = false;
    private boolean isShakeEnable = true;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = this;
        initView();
        initData();
        initSensor();
    }

    /**
     * get the sensor service
     */
    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    private void initView() {
        this.findViewById(R.id.btn_one).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_two).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_three).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_four).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_five).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_six).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_seven).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_eight).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_nine).setOnClickListener(NumOnClickListener);
        this.findViewById(R.id.btn_del).setOnClickListener(NumOnClickListener);

        this.findViewById(R.id.btn_clear).setOnClickListener(normalOnClickListener);
        this.findViewById(R.id.btn_exit).setOnClickListener(normalOnClickListener);
        this.findViewById(R.id.btn_check).setOnClickListener(normalOnClickListener);
        squareView = (CustomSquareView) this.findViewById(R.id.custom_view);
        time_tv = (TextView) this.findViewById(R.id.time_tv);
    }

    private void initData() {
        gameLevel = (GameLevel) getIntent().getSerializableExtra("level");
        Utils.isMusicOpen = (boolean) SPUtil.get(context, "music_open", true);
        initThread();
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager == null) {
            initSensor();
        }
        // 第一个参数是Listener，第二个获取加速传感器，第三个参数值获取传感器信息的频率
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * the thread that create the detail number in the square
     * because have to control the order, which means must create array first, then
     * put the value of array into square, so use thread and handler.
     */
    private void initThread() {
        new Thread() {
            @Override
            public void run() {
                try {
                    WHOLE_SUDOKU = SudokuPuzzleGenerator.getInstance().generatePuzzleMatrix();
                    SPACE_SUDOKU = SudokuPuzzleGenerator.getInstance().createSpaceMatrix(WHOLE_SUDOKU, gameLevel);
                    // this array is for backup to store the array which show on the interface
                    sudokuArray = new int[SPACE_SUDOKU.length][SPACE_SUDOKU.length];
                    for (int i = 0; i < SPACE_SUDOKU.length; i++) {
                        System.arraycopy(SPACE_SUDOKU[i], 0, sudokuArray[i], 0, SPACE_SUDOKU[i].length);
                    }
                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    Log.e("GAME", e.getMessage(), e);
                }
            }
        }.start();
    }

    // the thread of time
    private Thread timeThread = new Thread() {
        @Override
        public void run() {
            while (true)
                while (isGameStart) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;
                    handler.sendEmptyMessage(1);
                }

        }
    };

    /**
     * handler for updating UI and control the order
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //when start then draw the square
                    squareView.setSudokuArray(SPACE_SUDOKU, true);
                    isGameStart = true;
                    // after draw the square, then start the thread of time
                    if (!isThreadStarted) {
                        timeThread.start();
                        isThreadStarted = true;
                    }
                    Music.play(context, R.raw.game);
                    isShakeEnable = true;
                    break;
                case 1:
                    int hour = time / 3600;
                    int minute = time / 60;
                    int second = time % 60;
                    hourStr = (hour < 10) ? ("0" + hour) : (hour + "");
                    minuteStr = (minute < 10) ? ("0" + minute) : (minute + "");
                    secondStr = (second < 10) ? ("0" + second) : (second + "");
                    // main thread to update the UI!!!
                    time_tv.setText(hourStr + ":" + minuteStr + ":" + secondStr);
                    break;
                case SHAKE:
                    // to handle shake operator:refresh the arrays
                    isGameStart = false;
                    new AlertDialog.Builder(context).setTitle("Hint")
                            .setMessage("Would you like to restart the game?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        time = 0;
                                        initThread();
                                    } catch (Exception e) {
                                        Log.d("GAME", e.getMessage(), e);
                                    }
                                }
                            })
                            .setNegativeButton("NO", null)
                            .create()
                            .show();
                    break;
            }
        }
    };

    /**
     *  the function of shake to restart the game
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // handle sensor event
            if (isShakeEnable) {
                float[] values = event.values;
                float valueX = values[0];
                float valueY = values[1];
                float valueZ = values[2];
//            Log.d("GAME", "x=" + valueX + " y=" + valueY + " z=" + valueZ);
                if (Math.abs(valueX) > SHAKE_VALUE || Math.abs(valueY) > SHAKE_VALUE || Math.abs(valueZ) > SHAKE_VALUE) {
                    count++;
                    if (count > 5) {
                        count = 0;
                        handler.sendEmptyMessage(SHAKE);
                        isShakeEnable = false;
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     *  filling up the number of user choose
     */
    private View.OnClickListener NumOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int selectI = squareView.getSelectI();
            int selectJ = squareView.getSelectJ();
            if (selectI == -1 || selectJ == -1) {
                Toast.makeText(context, "please select blank square", Toast.LENGTH_SHORT).show();
                return;
            }
            switch (v.getId()) {
                case R.id.btn_one:
                    sudokuArray[selectI][selectJ] = 1;
                    break;
                case R.id.btn_two:
                    sudokuArray[selectI][selectJ] = 2;
                    break;
                case R.id.btn_three:
                    sudokuArray[selectI][selectJ] = 3;
                    break;
                case R.id.btn_four:
                    sudokuArray[selectI][selectJ] = 4;
                    break;
                case R.id.btn_five:
                    sudokuArray[selectI][selectJ] = 5;
                    break;
                case R.id.btn_six:
                    sudokuArray[selectI][selectJ] = 6;
                    break;
                case R.id.btn_seven:
                    sudokuArray[selectI][selectJ] = 7;
                    break;
                case R.id.btn_eight:
                    sudokuArray[selectI][selectJ] = 8;
                    break;
                case R.id.btn_nine:
                    sudokuArray[selectI][selectJ] = 9;
                    break;
                case R.id.btn_del:
                    sudokuArray[selectI][selectJ] = 0;
                    break;
            }
            squareView.setSudokuArray(sudokuArray, false);
        }
    };

    private View.OnClickListener normalOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_exit:
                    exit();
                    break;
                case R.id.btn_clear:
                    squareView.setSudokuArray(SPACE_SUDOKU, true);
                    break;
                case R.id.btn_check:
                    check();
                    break;
            }
        }
    };

    /**
     * after user filling the space, the check is correct or not
     */
    private void check() {
        int[][] checkArray = new int[SPACE_SUDOKU.length][SPACE_SUDOKU.length];
        for (int i = 0; i < sudokuArray.length; i++) {
            int[] row = sudokuArray[i];
            for (int j = 0; j < row.length; j++) {
                // if there is value in the array, then set this cell is -1
                if (SPACE_SUDOKU[i][j] != 0) {
                    checkArray[i][j] = -1;
                } else if (sudokuArray[i][j] == 0) {
                    checkArray[i][j] = 2;
                } else if (sudokuArray[i][j] != 0 && sudokuArray[i][j] == WHOLE_SUDOKU[i][j]) {
                    checkArray[i][j] = 1;
                } else if (sudokuArray[i][j] != 0 && sudokuArray[i][j] != WHOLE_SUDOKU[i][j]) {
                    // to judge the value is correct or not
                    checkArray[i][j] = 0;
                }
            }
        }
        squareView.setCheckArray(checkArray);
        for (int[] checkRow : checkArray) {
            for (int checkUnit : checkRow) {
                if (checkUnit == 0 || checkUnit == 2) {
                    isEnd = false;
                    break;
                } else {
                    isEnd = true;
                }
            }
            if (!isEnd) {
                break;
            }
        }
        if (isEnd) {
            calculateScores();
        }
    }

    /**
     *  when quit game, it will call this function
     */
    private void exit() {
        if (isGameStart) {
            new AlertDialog.Builder(context).setTitle("Hint")
                    .setMessage("Quit Game?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .create().show();
        }else {
            finish();
        }
    }

    /**
     * if quit directly, there is not score and give up the game
     */
    private void calculateScores() {
        isGameStart = false;// times up
        final String nickname = (String) SPUtil.get(context, "nickname", "");
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle("Hello " + nickname)
                .setMessage("Congratulations,you win the game!")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Scores scores = new Scores();
                        // use UUID to avoid repeating
                        scores.id = UUID.randomUUID().toString();
                        scores.nickname = nickname;
                        //this game is check for time, more less time more better
                        scores.socres = time + "";
                        //this is current time, not how long user play
                        scores.time = System.currentTimeMillis() + "";
                        scores.level = gameLevel.getLevelStr();
                        SQLiteForScores.getInstance(context).insertScore(scores);
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isGameStart = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 0) {
            exit();
        }
        return true;
    }
}
