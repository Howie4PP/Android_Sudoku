package com.example.shenhaichen.educationalgameapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.utils.GameLevel;

public class ChooseLevelActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
//                getSupportActionBar().setTitle("GAME");
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        context = this;
        findViewById(R.id.btn_base).setOnClickListener(onClickListener);
        findViewById(R.id.btn_primary).setOnClickListener(onClickListener);
        findViewById(R.id.btn_intermediate).setOnClickListener(onClickListener);
        findViewById(R.id.btn_advance).setOnClickListener(onClickListener);
        findViewById(R.id.btn_evil).setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, GameActivity.class);
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.btn_base:
                    bundle.putSerializable("level", GameLevel.BASE);
                    break;
                case R.id.btn_primary:
                    bundle.putSerializable("level", GameLevel.PRIMARY);
                    break;
                case R.id.btn_intermediate:
                    bundle.putSerializable("level", GameLevel.INTERMEDIATE);
                    break;
                case R.id.btn_advance:
                    bundle.putSerializable("level", GameLevel.ADVANCED);
                    break;
                case R.id.btn_evil:
                    bundle.putSerializable("level", GameLevel.EVIL);
                    break;
                default:
                    bundle.putSerializable("level", GameLevel.BASE);
                    break;
            }
            intent.putExtras(bundle);
            startActivity(intent);
        }

    };

    }

