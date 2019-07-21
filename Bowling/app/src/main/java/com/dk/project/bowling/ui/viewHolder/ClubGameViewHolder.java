package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderClubGameBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.ReadGameModel;

public class ClubGameViewHolder extends BindViewHolder<ViewHolderClubGameBinding, ReadGameModel> {


    public ClubGameViewHolder(View itemView) {
        super(itemView);
    }


    @Override
    public void onBindView(ReadGameModel item, int position) {
        super.onBindView(item, position);
        binding.gameName.setText(item.getGameName());
        binding.gameDate.setText(item.getPlayDateTime());
    }
}