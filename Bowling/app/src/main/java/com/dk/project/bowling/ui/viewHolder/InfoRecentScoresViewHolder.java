package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderInfoRecentScoresBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;

public class InfoRecentScoresViewHolder extends BindViewHolder<ViewHolderInfoRecentScoresBinding, ScoreModel> {

  public InfoRecentScoresViewHolder(View itemView) {
    super(itemView);
  }


  @Override
  public void onBindView(ScoreModel item) {
//    binding.recentDateText.setText(item.getPlayDate());
//    binding.recentAvgText.setText(String.valueOf(item.getAvgScore()));
//    binding.recentMaxText.setText(String.valueOf(item.getMaxScore()));
//    binding.recentMinText.setText(String.valueOf(item.getMinScore()));
  }
}