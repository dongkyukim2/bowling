package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderCreateGameBinding;
import com.dk.project.bowling.model.UserModel;
import com.dk.project.post.base.BindViewHolder;

public class CreateGameViewHolder extends BindViewHolder<ViewHolderCreateGameBinding, UserModel> {


    public CreateGameViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(UserModel item, int position) {
        if (item.getViewType() == 0) { // 헤더
            binding.createGameTeamName.setText(item.getTeamName());

            binding.dragIcon.setVisibility(View.INVISIBLE);
            binding.userInviteIcon.setVisibility(View.VISIBLE);
            binding.userNameText.setVisibility(View.INVISIBLE);
            binding.scoreLayout.setVisibility(View.GONE);
            binding.createGameTeamName.setVisibility(View.VISIBLE);

        } else { // 유저
            binding.userNameText.setText(item.getUserName());
            binding.dragIcon.setVisibility(View.VISIBLE);
            binding.userInviteIcon.setVisibility(View.INVISIBLE);
            binding.userNameText.setVisibility(View.VISIBLE);
            binding.scoreLayout.setVisibility(View.VISIBLE);
            binding.createGameTeamName.setVisibility(View.INVISIBLE);
        }
        binding.userCheckBox.setChecked(item.isCheck());
    }
}