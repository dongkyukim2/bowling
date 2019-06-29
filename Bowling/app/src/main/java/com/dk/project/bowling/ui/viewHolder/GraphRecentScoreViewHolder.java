package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dk.project.bowling.databinding.ViewHolderGraphScoresBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.utils.Utils;

public class GraphRecentScoreViewHolder extends BindViewHolder<ViewHolderGraphScoresBinding, ScoreModel> {

    private int graphType;

    public GraphRecentScoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreModel item, int position) {

        binding.graphDate.setText(item.getPlayDateTime().split(" ")[0].split("-")[2] + "일");
        binding.graphScore.setText(String.valueOf(getScore(item)));

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.graphLine.getLayoutParams();
        layoutParams.verticalBias = Utils.getScorePercent(getScore(item));
        binding.graphLine.setLayoutParams(layoutParams);

    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }

    private int getScore(ScoreModel item) {
        int score = 0;
        switch (graphType) {
            case 0:
                score = item.getAvgScore();
                break;
            case 1:
                score = item.getMaxScore();
                break;
            case 2:
                score = item.getMinScore();
                break;
        }
        return score;
    }
}