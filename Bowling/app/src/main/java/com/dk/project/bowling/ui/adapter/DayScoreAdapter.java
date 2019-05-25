package com.dk.project.bowling.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.viewHolder.DayScoreViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;

public class DayScoreAdapter extends BaseRecyclerViewAdapter<DayScoreViewHolder> {

    private ArrayList<ScoreModel> dayScoreList = new ArrayList<>();

    @Override
    public DayScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DayScoreViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_day_scores, parent, false));
    }

    @Override
    public void onBindViewHolder(DayScoreViewHolder holder, int position) {

        holder.onBindView(dayScoreList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dayScoreList.size();
    }


    public void setDayScoreList(ArrayList<ScoreModel> dayScoreList) {
        this.dayScoreList.addAll(dayScoreList);
        notifyDataSetChanged();
    }
}
