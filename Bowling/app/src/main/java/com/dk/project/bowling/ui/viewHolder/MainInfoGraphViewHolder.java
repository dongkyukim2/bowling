package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderMainInfoGraphBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;


public class MainInfoGraphViewHolder extends BindViewHolder<ViewHolderMainInfoGraphBinding, ScoreModel> {

  public MainInfoGraphViewHolder(View itemView) {
    super(itemView);
  }

  @Override
  public void onBindView(ScoreModel item) {
    binding.scoreGraph.setBottomText("월평균");
    binding.scoreGraph.setMax(300);
    if (item == null) {
      binding.scoreGraph.setProgress(0);
    } else {
      binding.scoreGraph.setProgress(item.getAvgScore());
    }
  }
}