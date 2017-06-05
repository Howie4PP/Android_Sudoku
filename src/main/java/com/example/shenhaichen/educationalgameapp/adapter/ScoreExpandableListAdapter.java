package com.example.shenhaichen.educationalgameapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.example.shenhaichen.educationalgameapp.MainActivity;
import com.example.shenhaichen.educationalgameapp.R;
import com.example.shenhaichen.educationalgameapp.model.data.Scores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  depends on the level of the game, sort the scores
 * Created by shenhaichen on 19/05/2017.
 */

public class ScoreExpandableListAdapter implements ExpandableListAdapter {
    private Context context;
    private List<Scores> scores;
    private Map<String, List<Scores>> groupMap;
    private List<String> keyList;
    private SimpleDateFormat sdf;

    @SuppressLint("SimpleDateFormat")
    public ScoreExpandableListAdapter(Context context, List<Scores> scores) {
        this.context = context;
        this.scores = scores;
        groupMap = new HashMap<>();
        keyList = new ArrayList<>();
        sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:dd");
        group();
    }

    /**
     *  list the scores
     */
    public void group() {
        for (Scores score : scores) {
            if (groupMap.containsKey(score.level)) {
                groupMap.get(score.level).add(score);
            } else {
                List<Scores> scores = new ArrayList<>();
                scores.add(score);
                keyList.add(score.level);
                groupMap.put(score.level, scores);
            }
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return groupMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupMap.get(keyList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return keyList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupMap.get(keyList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Parent_ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_el_parent, null);
            holder = new Parent_ViewHolder();
            holder.key_name = (TextView) convertView.findViewById(R.id.key_name);
            convertView.setTag(holder);
        } else {
            holder = (Parent_ViewHolder) convertView.getTag();
        }
        holder.key_name.setText(keyList.get(groupPosition));
        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Child_ViewHolder holder;
        Scores scores = (Scores) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_scores, null);
            holder = new Child_ViewHolder();
            holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
            holder.item_time = (TextView) convertView.findViewById(R.id.item_time);
            holder.item_score = (TextView) convertView.findViewById(R.id.item_score);
            convertView.setTag(holder);
        } else {
            holder = (Child_ViewHolder) convertView.getTag();
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
        MainActivity.TWITTERSOCRES = scoreStr;
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }

    private class Parent_ViewHolder {
        TextView key_name;
    }

    private class Child_ViewHolder {
        TextView item_score;
        TextView item_name;
        TextView item_time;
    }

}
