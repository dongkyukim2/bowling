package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderCreateGameBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.model.LoginInfoModel;

public class CreateGameViewHolder extends BindViewHolder<ViewHolderCreateGameBinding, LoginInfoModel> {


    public CreateGameViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(LoginInfoModel item, int position) {
        binding.createGameUserName.setText(item.getUserName());

    }
}