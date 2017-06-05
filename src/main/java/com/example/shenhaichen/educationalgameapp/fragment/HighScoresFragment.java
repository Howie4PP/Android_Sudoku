package com.example.shenhaichen.educationalgameapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.shenhaichen.educationalgameapp.MainActivity;
import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.adapter.ScoreExpandableListAdapter;
import com.example.shenhaichen.educationalgameapp.model.data.Scores;
import com.example.shenhaichen.educationalgameapp.model.sql.SQLiteForScores;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shenhaichen on 13/05/2017.
 */

public class HighScoresFragment extends Fragment {
    private ExpandableListView scores_el;
    private TextView no_scores_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        scores_el = (ExpandableListView) view.findViewById(R.id.scores_el);
        no_scores_tv = (TextView) view.findViewById(R.id.no_scores_tv);
    }

    /**
     *  get the data from the database then show to the view
     */
    private void getData(){
        List<Scores> scores = SQLiteForScores.getInstance(getActivity()).selectAllScores();
        sequence(scores);
        if (scores.size() > 0) {
            ScoreExpandableListAdapter adapter = new ScoreExpandableListAdapter(getContext(), scores);
            scores_el.setAdapter(adapter);
            no_scores_tv.setVisibility(View.GONE);

        } else {
            no_scores_tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    /**
     * depends on the order of time to sequence
     */
    private void sequence(List<Scores> list) {
        Collections.sort(list, new Comparator<Scores>() {
            @Override
            public int compare(Scores s1, Scores s2) {
                long time1 = Long.parseLong(s1.time);
                long time2 = Long.parseLong(s2.time);
                if (time1 > time2) {
                    return 1;
                } else if (time1 < time2) {
                    return -1;
                } else
                    return 0;
            }
        });
    }
}
