package com.dk.project.bowling.ui.viewHolder;

import android.view.View;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderClubUserBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.ScoreClubUserModel;

public class ClubUserViewHolder extends BindViewHolder<ViewHolderClubUserBinding, ScoreClubUserModel> {

    public ClubUserViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ScoreClubUserModel item, int position) {
        binding.userNameText.setText(item.getUserName());
        binding.userCheckBox.setClickable(false);

        if (item.isCheck()) {
            binding.userCheckBox.setImageResource(R.drawable.ic_action_check);
        } else {
            binding.userCheckBox.setImageResource(R.drawable.ic_action_uncheck);
        }
    }
}