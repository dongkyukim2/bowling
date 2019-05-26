package com.dk.project.bowling.ui.viewHolder;

import android.view.View;
import com.dk.project.bowling.databinding.ViewHolderClubBinding;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.post.base.BindViewHolder;

public class ClubViewHolder extends BindViewHolder<ViewHolderClubBinding, ClubModel> {


    public ClubViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ClubModel item, int position) {
        binding.clubTitleTextView.setText(item.getClubTitle());
        binding.clubSubTitleTextView.setText(item.getClubInfo());
        binding.clubCountText.setText(String.valueOf(item.getCount()));
    }
}