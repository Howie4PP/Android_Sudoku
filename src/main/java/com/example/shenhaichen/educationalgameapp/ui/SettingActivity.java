package com.example.shenhaichen.educationalgameapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.model.sql.SQLiteForScores;
import com.example.shenhaichen.educationalgameapp.utils.SPUtil;
import com.example.shenhaichen.educationalgameapp.utils.Utils;

public class SettingActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView music_status_tv;
    private ImageButton music_switch_btn;
    private boolean isMusicOn = true;

    private LinearLayout settings_wipe_cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        music_status_tv = (TextView) this.findViewById(R.id.music_status_tv);
        music_switch_btn = (ImageButton) this.findViewById(R.id.music_switch_btn);
        music_switch_btn.setOnClickListener(this);
        boolean isMusicSelect = (boolean) SPUtil.get(this, "music_open", true);
        isMusicOn = isMusicSelect;
        if (isMusicSelect) {
            music_status_tv.setText("ON");
            Utils.isMusicOpen = true;
            music_switch_btn.setSelected(true);
        } else {
            music_status_tv.setText("OFF");
            Utils.isMusicOpen = false;
            music_switch_btn.setSelected(false);
        }

        settings_wipe_cache = (LinearLayout) this.findViewById(R.id.settings_wipe_cache);
        settings_wipe_cache.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.music_switch_btn:
                isMusicOn = !isMusicOn;
                music_switch_btn.setSelected(isMusicOn);
                if (isMusicOn) {
                    music_status_tv.setText("ON");
                    Utils.isMusicOpen = true;
                } else {
                    music_status_tv.setText("OFF");
                    Utils.isMusicOpen = false;
                }
                SPUtil.put(this, "music_open", isMusicOn);
                break;
            case R.id.settings_wipe_cache:
                // this is clean cache
                new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setTitle("Prompt")
                        .setMessage("Are you sure to wipe cache?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SQLiteForScores.getInstance(SettingActivity.this).delete();
//                                SPUtil.clear(getApplicationContext());
//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("NO", null)
                        .create().show();
                break;
        }

    }
}
