package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderRecentScoresBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.utils.Utils;

public class RecentScoresViewHolder extends BindViewHolder<ViewHolderRecentScoresBinding, ScoreModel> {

    public RecentScoresViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreModel item, int position) {
        binding.recentDateText.setText(item.getPlayDate() + " (" + Utils.getDAY_OF_WEEK(item.getPlayDate()) + ")");
        binding.recentAvgText.setText(String.valueOf(item.getAvgScore()));
        binding.recentMaxText.setText(String.valueOf(item.getMaxScore()));
        binding.recentMinText.setText(String.valueOf(item.getMinScore()));
    }
}