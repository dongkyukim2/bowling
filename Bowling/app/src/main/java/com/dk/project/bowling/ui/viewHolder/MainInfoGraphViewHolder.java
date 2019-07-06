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
    public void onBindView(ScoreModel item, int position) {
        binding.scoreGraph.setBottomText(item.getPlayDateTime());
        binding.scoreGraph.setBottomTextSize(70);
        binding.scoreGraph.setMax(300);
        if (item == null) {
            binding.scoreGraph.setProgress(0);
        } else {
            binding.scoreGraph.setProgress(item.getAvgScore());
        }

        binding.scoreGraphLeft.setSpeed(0.9f);
        binding.scoreGraphRight.setSpeed(0.9f);
    }
}
