package com.dk.project.bowling.ui.viewHolder;

import android.text.TextUtils;
import android.view.View;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderCreateGameBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;

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
            binding.gameNumScore7.setText(String.valueOf(item.getScore(6)));
            binding.gameNumScore8.setText(String.valueOf(item.getScore(7)));


            binding.dragIcon.setVisibility(View.VISIBLE);
            binding.userInviteIcon.setVisibility(View.INVISIBLE);
            binding.userNameText.setVisibility(View.VISIBLE);
            binding.scoreLayout.setVisibility(View.VISIBLE);
            binding.createGameTeamName.setVisibility(View.INVISIBLE);
            binding.userProfileImageView.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(item.getUserPhoto())) {
                GlideApp.with(binding.userProfileImageView)
                        .load(R.drawable.user_profile)
                        .centerCrop()
                        .into(binding.userProfileImageView);
            } else {
                GlideApp.with(binding.userProfileImageView)
                        .applyDefaultRequestOptions(ImageUtil.getGlideRequestOption())
                        .load(Define.IMAGE_URL + item.getUserPhoto())
                        .centerCrop()
                        .into(binding.userProfileImageView);
            }
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