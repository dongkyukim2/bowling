package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderCreateGameBinding;
import com.dk.project.bowling.model.UserModel;
import com.dk.project.post.base.BindViewHolder;

public class ClubUserViewHolder extends BindViewHolder<ViewHolderCreateGameBinding, UserModel> {

    public ClubUserViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(UserModel item, int position) {
        binding.userNameText.setText(item.getUserName());
        binding.dragIcon.setVisibility(View.INVISIBLE);
        binding.userCheckBox.setClickable(false);

        if(item.isCheck()){
            binding.userCheckBox.setImageResource(R.drawable.ic_action_check);
        } else {
            binding.userCheckBox.setImageResource(R.drawable.ic_action_uncheck);
        }
    }
}