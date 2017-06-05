package com.example.shenhaichen.educationalgameapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.ui.ChooseLevelActivity;
import com.example.shenhaichen.educationalgameapp.utils.SPUtil;
import com.example.shenhaichen.educationalgameapp.utils.Utils;

/**
 * main screen to start the game
 * Created by shenhaichen on 05/05/2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    private EditText nickname_et;
    private Button start_game_btn;
    private TextView welcome_tv;
    private Button use_btn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        nickname_et = (EditText) view.findViewById(R.id.nickname_et);
        start_game_btn = (Button) view.findViewById(R.id.start_game_btn);
        start_game_btn.setOnClickListener(this);
        welcome_tv = (TextView) view.findViewById(R.id.welcome_tv);
        use_btn = (Button) view.findViewById(R.id.use_btn);
        use_btn.setOnClickListener(this);

        String nickname = (String) SPUtil.get(getActivity(), "nickname", "");
        if (!TextUtils.isEmpty(nickname)) {
            nickname_et.setText(nickname);
        }
    }

    @Override
    public void onClick(View v) {
         int id = 1;
        switch (v.getId()) {
            case R.id.use_btn:
                // TODO start game
                String nickname = nickname_et.getText().toString().trim();
                if (TextUtils.isEmpty(nickname)) {
                    Toast.makeText(getActivity(), "Please input your nickname!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Utils.nickname = nickname;
                    SPUtil.put(getActivity(), "nickname", nickname);
                    SPUtil.put(getActivity(), "id", ""+id);
                    welcome_tv.setText("Hello " + nickname + "!Welcome!");
                    nickname_et.setVisibility(View.GONE);
                    use_btn.setVisibility(View.GONE);
                    welcome_tv.setVisibility(View.VISIBLE);
                    start_game_btn.setVisibility(View.VISIBLE);
                    id++;
                }
                break;
            case R.id.start_game_btn:
                startActivity(new Intent(getActivity(), ChooseLevelActivity.class));
                break;
        }

    }
}
