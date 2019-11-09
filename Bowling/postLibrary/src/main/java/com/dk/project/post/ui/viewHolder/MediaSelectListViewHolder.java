package com.dk.project.post.ui.viewHolder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.databinding.MediaSelectItemBinding;
import com.dk.project.post.model.MediaSelectModel;
import com.dk.project.post.ui.activity.MediaSelectListActivity;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;

/**
 * Created by dkkim on 2017-03-26.
 */

public class MediaSelectListViewHolder<T extends MediaSelectModel> extends BindViewHolder<MediaSelectItemBinding, T> {

    private boolean multiSelect;

    public MediaSelectListViewHolder(View itemView, boolean multiSelect) {
        super(itemView);
        this.multiSelect = multiSelect;
    }

    @Override
    public void onBindView(MediaSelectModel item, int position) {
        binding.folderName.setText(item.getName());
        binding.fileCount.setText(String.valueOf(item.getAlbumCount()));
        setThumbnail(item);
        binding.getRoot().setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("BucketId", item.getBucketId());
                    bundle.putInt("type", item.getType());
                    bundle.putString("viewerType", "image");
                    bundle.putBoolean(IMAGE_MULTI_SELECT, multiSelect);
                    Intent intent = new Intent(binding.folderName.getContext(), MediaSelectListActivity.class);
                    intent.putExtras(bundle);
                    ((BaseActivity) binding.folderName.getContext()).startActivityForResult(intent, MEDIA_ATTACH_LIST);
                }
        );
    }

    private void setThumbnail(MediaSelectModel item) {
        if (item.getPath().toLowerCase().endsWith(".gif")) {
            binding.folderThumbnailGif.setVisibility(View.VISIBLE);
            binding.folderThumbnail.setVisibility(View.GONE);
            binding.folderThumbnailGif.setController(ImageUtil.getDraweeController(item.getPath(), 500, 500));
        } else {
            binding.folderThumbnailGif.setVisibility(View.GONE);
            binding.folderThumbnail.setVisibility(View.VISIBLE);
            GlideApp.with(binding.folderName.getContext()).load(item.getPath()).apply(ImageUtil.getGlideRequestOption()).into(binding.folderThumbnail);
        }
    }
}
