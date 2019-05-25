package com.dk.project.bowling.ui.viewHolder;

import android.text.TextUtils;
import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderDayScoresBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;

public class DayScoreViewHolder extends BindViewHolder<ViewHolderDayScoresBinding, ScoreModel> {


    public DayScoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreModel item, int position) {
        binding.scoreDate.setText(item.getPlayDateTime().split(" ")[1].substring(0, 5));
        binding.score.setText(String.valueOf(item.getScore()));
        if (TextUtils.isEmpty(item.getComment())) {
            binding.comment.setText("내용 없음");
        } else {
            binding.comment.setText(item.getComment());
        }
    }

}