package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderCreateGameBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.ScoreClubUserModel;

public class CreateGameViewHolder extends BindViewHolder<ViewHolderCreateGameBinding, ScoreClubUserModel> {

    private ScoreClubUserModel item;

    public CreateGameViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreClubUserModel item, int position) {
        this.item = item;
        if (!item.isUserType()) { // 헤더
            binding.createGameTeamName.setText(item.getTeamName());
            binding.dragIcon.setVisibility(View.INVISIBLE);
            binding.userInviteIcon.setVisibility(View.VISIBLE);
            binding.userNameText.setVisibility(View.INVISIBLE);
            binding.scoreLayout.setVisibility(View.GONE);
            binding.createGameTeamName.setVisibility(View.VISIBLE);
            binding.userProfileImageView.setVisibility(View.GONE);

        } else { // 유저
            binding.userNameText.setText(item.getUserName());

            binding.gameNumScore1.setText(String.valueOf(item.getScore(0)));
            binding.gameNumScore2.setText(String.valueOf(item.getScore(1)));
            binding.gameNumScore3.setText(String.valueOf(item.getScore(2)));
            binding.gameNumScore4.setText(String.valueOf(item.getScore(3)));
            binding.gameNumScore5.setText(String.valueOf(item.getScore(4)));
            binding.gameNumScore6.setText(String.valueOf(item.getScore(5)));


            binding.dragIcon.setVisibility(View.VISIBLE);
            binding.userInviteIcon.setVisibility(View.INVISIBLE);
            binding.userNameText.setVisibility(View.VISIBLE);
            binding.scoreLayout.setVisibility(View.VISIBLE);
            binding.createGameTeamName.setVisibility(View.INVISIBLE);
            binding.userProfileImageView.setVisibility(View.VISIBLE);
        }
        if (item.isCheck()) {
            binding.userCheckBox.setBackgroundResource(R.drawable.check_enable);
        } else {
            binding.userCheckBox.setBackgroundResource(R.drawable.check_disable);
        }
    }

    public ScoreClubUserModel getItem() {
        return item;
    }
}