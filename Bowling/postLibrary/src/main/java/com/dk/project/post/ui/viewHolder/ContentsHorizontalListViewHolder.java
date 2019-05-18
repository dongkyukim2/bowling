package com.dk.project.post.ui.viewHolder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.databinding.FragmentContentsHorizontalListItemBinding;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.YoutubeUtil;


/**
 * Created by dkkim on 2017-03-26.
 */

public class ContentsHorizontalListViewHolder<T extends MediaSelectListModel> extends
    BindViewHolder<FragmentContentsHorizontalListItemBinding, T> {

    private static int maxWidth;
    private static int maxHeight;
    private static int widthSpace;

    public ContentsHorizontalListViewHolder(View itemView) {
        super(itemView);
        if (maxHeight == 0) {
            widthSpace = ScreenUtil.dpToPixel(16);
            int[] size = ScreenUtil.getDeviceScreenSize(itemView.getContext());
            maxWidth = size[0] - widthSpace;
            maxHeight = (int) (maxWidth * CONTENT_VIEW_HOLDER_RATIO);
        }
    }

    @Override
    public void onBindView(T imageModel, int position) {
        Context context = itemView.getContext();
        binding.attachLayout.removeAllViews();
        if (!TextUtils.isEmpty(imageModel.getYoutubeUrl())) {
            binding.attachLayout.addView(YoutubeUtil
                .getYoutubeThumbnail(context, imageModel.getYoutubeUrl(), maxWidth, maxHeight, null));
        } else {
            binding.attachLayout.addView(addImageView(imageModel, maxWidth, maxHeight));
        }
    }

    private LinearLayout addImageView(MediaSelectListModel imageModel, int width, int height) {
        if (imageModel.isGif() || imageModel.isWebp()) {
            return ImageUtil
                .getImageGifThumbnail(itemView.getContext(), width, Math.min(maxHeight, height), imageModel.getFilePath(), false, null);
        } else {
            return ImageUtil.getImageViewThumbnail(itemView.getContext(), width, Math.min(maxHeight, height), imageModel.getFilePath(), null);
        }
    }
}
