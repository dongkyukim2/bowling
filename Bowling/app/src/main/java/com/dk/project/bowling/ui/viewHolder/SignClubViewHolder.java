package com.dk.project.bowling.ui.viewHolder;

import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dk.project.bowling.databinding.ViewHolderSignClubBinding;
import com.dk.project.bowling.utils;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.utils.GlideApp;

public class SignClubViewHolder extends BindViewHolder<ViewHolderSignClubBinding, ClubModel> {

    public SignClubViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(ClubModel item, int position) {


        if (item.getClubImage().length() == 1) {
            GlideApp.with(binding.clubImageView.getContext()).asBitmap().load(utils.getDefaultImage(Integer.valueOf(item.getClubImage()))).centerCrop().addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    Palette.from(resource).generate(palette -> {
                        for (Palette.Swatch swatch : palette.getSwatches()) {
                            if (swatch != null) {
                                binding.clubTitleTextView.setBackgroundColor(swatch.getRgb());
                                break;
                            }
                        }
                    });
                    return false;
                }
            }).into(binding.clubImageView);
        } else {
            GlideApp.with(binding.clubImageView.getContext()).asBitmap().load(utils.getDefaultImage(Integer.valueOf(item.getClubImage()))).centerCrop().addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    Palette.from(resource).generate(palette -> {
                        for (Palette.Swatch swatch : palette.getSwatches()) {
                            if (swatch != null) {
                                binding.clubTitleTextView.setBackgroundColor(swatch.getRgb());
                                break;
                            }
                        }
                    });
                    return false;
                }
            }).into(binding.clubImageView);
        }
        if (item.getType() == Define.USER_TYPE_JOIN_WAIT) {
            binding.joinWaitBg.setVisibility(View.VISIBLE);
            binding.joinWaitText.setVisibility(View.VISIBLE);
        } else {
            binding.joinWaitBg.setVisibility(View.GONE);
            binding.joinWaitText.setVisibility(View.GONE);
        }

        binding.clubTitleTextView.setText(item.getClubTitle());
    }
}