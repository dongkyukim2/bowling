package com.dk.project.bowling.ui.viewHolder;

import android.view.View;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ViewHolderClubBinding;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;

public class ClubViewHolder extends BindViewHolder<ViewHolderClubBinding, ClubModel> {

    public ClubViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ClubModel item, int position) {
        binding.clubTitleTextView.setText(item.getClubTitle());
        binding.clubSubTitleTextView.setText(item.getClubInfo());
        binding.clubCountText.setText(String.valueOf(item.getCount()));

        GlideApp.with(binding.clubImageView.getContext())
                .load(item.getClubImage().length() == 1 ? R.drawable.team_default_1 : IMAGE_URL + item.getClubImage())
                .apply(ImageUtil.getGlideRequestOption())
                .thumbnail(0.4f)
                .centerCrop()
                .into(binding.clubImageView);

        binding.clubCountText.setVisibility(View.GONE);
        binding.clubCountImage.setVisibility(View.GONE);
    }
}