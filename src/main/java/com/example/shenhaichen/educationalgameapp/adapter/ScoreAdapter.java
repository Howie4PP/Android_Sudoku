package com.example.shenhaichen.educationalgameapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.model.data.Scores;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by shenhaichen on 12/05/2017.
 */

public class ScoreAdapter extends BaseAdapter {

    private Context context;
    private List<Scores> score_list;
    private Scores scores;
    private SimpleDateFormat sdf;

    public ScoreAdapter(Context context, List<Scores> score_list){
        this.context = context;
        this.score_list = score_list;
        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:dd");
    }

    @Override
    public int getCount() {
        return score_list.size();
    }

    @Override
    public Object getItem(int position) {
        return score_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        scores = score_list.get(position);
        if (convertView == null){
            convertView = View.inflate(context, R.layout.item_scores,parent);
            holder = new ViewHolder();
            holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.item_time = (TextView) convertView.findViewById(R.id.item_time);
            holder.item_score = (TextView) convertView.findViewById(R.id.item_score);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        int score = Integer.parseInt(scores.socres);
        int hour = score / 3600;
        int minute = score / 60;
        int second = score % 60;
        String scoreStr = ((hour < 10) ? ("0" + hour) : (hour + "")) + ":"
                + ((minute < 10) ? ("0" + minute) : (minute + "")) + ":"
                + ((second < 10) ? ("0" + second) : (second + ""));
        holder.item_score.setText(scoreStr);
        holder.item_name.setText(scores.nickname);
        String time = sdf.format(Long.parseLong(scores.time));
        holder.item_time.setText(time);

        return convertView;
    }

    class ViewHolder{
        TextView item_score;
        TextView item_name;
        TextView item_time;
    }

 }
