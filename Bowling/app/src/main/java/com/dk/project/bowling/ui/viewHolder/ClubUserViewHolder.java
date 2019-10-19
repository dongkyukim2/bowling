package com.dk.project.bowling.ui.viewHolder;

import android.view.View;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderClubUserBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.UserModel;

public class ClubUserViewHolder extends BindViewHolder<ViewHolderClubUserBinding, UserModel> {

    public ClubUserViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(UserModel item, int position) {
        binding.userNameText.setText(item.getUserName());
        binding.userCheckBox.setClickable(false);

        if (item.isCheck()) {
            binding.userCheckBox.setImageResource(R.drawable.ic_action_check);
        } else {
            binding.userCheckBox.setImageResource(R.drawable.ic_action_uncheck);
        }
    }
}