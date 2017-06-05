package com.example.shenhaichen.educationalgameapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shenhaichen.educationalgameapp.fragment.HighScoresFragment;
import com.example.shenhaichen.educationalgameapp.fragment.MainFragment;
import com.example.shenhaichen.educationalgameapp.ui.SettingActivity;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private Button btn_main, btn_socres;
    private MainFragment mainFragment;
    private HighScoresFragment highScoresFragment;
    public static String TWITTERSOCRES = null;
    private boolean selectScoreFragment = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_main = (Button) this.findViewById(R.id.main_btn);
        btn_socres = (Button) this.findViewById(R.id.scores_btn);
        btn_socres.setOnClickListener(this);
        btn_main.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        btn_main.performClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share_twitter:
                if (selectScoreFragment) {
                    if (TWITTERSOCRES != null) {
                        String mes = "The time I spend is " + TWITTERSOCRES + ". Let's play together, Sudoku!";
                        String myUrl = "https://twitter.com/intent/tweet?text=" + mes;
                        Uri uri = Uri.parse(myUrl);
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }else {
                        Toast.makeText(this,"You do not have scores!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.settings:
                // settings
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (v.getId()) {
            case R.id.main_btn:
                setSelectedFalse();
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    fragmentTransaction.add(R.id.fragment_content, mainFragment);
                } else {
                    fragmentTransaction.show(mainFragment);
                }
                selectScoreFragment = false;
                btn_main.setSelected(true);
                break;
            case R.id.scores_btn:
                setSelectedFalse();
                if (highScoresFragment == null) {
                    highScoresFragment = new HighScoresFragment();
                    fragmentTransaction.add(R.id.fragment_content, highScoresFragment);
                } else {
                    fragmentTransaction.show(highScoresFragment);
                }
                selectScoreFragment = true;
                btn_socres.setSelected(true);
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 0) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Hint")
                    .setMessage("EXIT?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("NO", null)
                    .create().show();
        }
        return true;
    }

    private void setSelectedFalse() {
        btn_main.setSelected(false);
        btn_socres.setSelected(false);
    }

    //hide all fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (mainFragment != null) fragmentTransaction.hide(mainFragment);
        if (highScoresFragment != null) fragmentTransaction.hide(highScoresFragment);
    }
}
