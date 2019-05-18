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
  public void onBindView(ScoreModel item, int position) {

    if (position == 1) {
      binding.score.setText(String.valueOf(item.getMaxScore()));
      binding.scoreTitle.setText("이번달 최고점");
    } else {
      binding.score.setText(String.valueOf(item.getMinScore()));
      binding.scoreTitle.setText("이번달 최저점");
    }
  }
}