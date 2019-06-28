package com.dk.project.bowling.ui.viewHolder;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.dk.project.bowling.databinding.ViewHolderGraphScoresBinding;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.Utils;

public class GraphRecentScoreViewHolder extends BindViewHolder<ViewHolderGraphScoresBinding, ScoreModel> {


    private int graphType;

    private GradientDrawable gradientDrawable = ImageUtil.getGradientDrawable();

    public GraphRecentScoreViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreModel item, int position) {

        binding.graphDate.setText(item.getPlayDateTime().split(" ")[0].split("-")[2] + "일");
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.graphLine.getLayoutParams();

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


        binding.graphScore.setText(String.valueOf(score));
        layoutParams.verticalBias = Utils.getScorePercent(score);
        binding.graphLine.setLayoutParams(layoutParams);

        binding.graphLayout.setBackground(gradientDrawable);
    }

    public void setGraphType(int graphType) {
        this.graphType = graphType;
    }
}