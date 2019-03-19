package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dk.project.bowling.databinding.ViewHolderGraphScoresBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.utils.Utils;

public class GraphRecentScoreViewHolder extends
    BindViewHolder<ViewHolderGraphScoresBinding, ScoreModel> {

  public GraphRecentScoreViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  public void onBindView(ScoreModel item) {

    binding.graphScore.setText(String.valueOf(item.getAvgScore()));
    binding.graphDate.setText(item.getPlayDateTime().split(" ")[0].split("-")[2] + "일");

    ConstraintLayout.LayoutParams layoutParams =
        (ConstraintLayout.LayoutParams) binding.graphLine.getLayoutParams();
    layoutParams.verticalBias = Utils.getScorePercent(item.getAvgScore());
    binding.graphLine.setLayoutParams(layoutParams);
  }
}