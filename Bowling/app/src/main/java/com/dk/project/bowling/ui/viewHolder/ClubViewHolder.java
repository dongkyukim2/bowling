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
        if (item.getClubId().equals("0")) {
            binding.clubInfoParent.setVisibility(View.GONE);
            binding.recommendTextView.setVisibility(View.VISIBLE);
        } else {
            binding.clubInfoParent.setVisibility(View.VISIBLE);
            binding.recommendTextView.setVisibility(View.GONE);
            binding.clubTitleTextView.setText(item.getClubTitle());
            binding.clubSubTitleTextView.setText(item.getClubInfo());
            binding.clubCountText.setText(String.valueOf(item.getCount()));
        }

    }
}