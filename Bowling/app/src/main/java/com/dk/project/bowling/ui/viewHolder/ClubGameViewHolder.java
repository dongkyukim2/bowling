package com.dk.project.bowling.ui.viewHolder;

import android.view.View;

import com.dk.project.bowling.databinding.ViewHolderClubGameBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.utils.Utils;

public class ClubGameViewHolder extends BindViewHolder<ViewHolderClubGameBinding, ReadGameModel> {

    public ClubGameViewHolder(View itemView) {
        super(itemView);
    }


    @Override
    public void onBindView(ReadGameModel item, int position) {
        super.onBindView(item, position);
        binding.gameName.setText(item.getGameName());

        String[] date = item.getPlayDateTime().split(" ");
        String week = Utils.getDAY_OF_WEEK(date[0]);

        String[] time = date[1].split(":");
        binding.gameDate.setText(date[0] + " (" + week + ") " + time[0] + ":" + time[1]);
    }
}