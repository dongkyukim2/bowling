package com.dk.project.post.ui.viewHolder;

import android.content.Context;
import android.view.View;

import com.dk.project.post.R;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.base.Define;
import com.dk.project.post.databinding.ReplyItemBinding;
import com.dk.project.post.impl.ReplyListener;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.Utils;

/**
 * Created by dkkim on 2017-03-26.
 */

public class ReplyViewHolder<T extends ReplyModel> extends BindViewHolder<ReplyItemBinding, T> {

    private Context mContext;
    private ReplyListener replyListener;
    private ReplyModel replyModel;

    public ReplyViewHolder(View itemView, ReplyListener replyListener) {
        super(itemView);
        mContext = itemView.getContext();
        this.replyListener = replyListener;
        itemView.setOnLongClickListener(v -> replyListener.onReplyLongClick(replyModel));
    }

    @Override
    public void onBindView(ReplyModel item, int position) {
        replyModel = item;
        binding.replyDateText.setText(Utils.converterDate(item.getReplyDate()));
        binding.replyUserNameText.setText(item.getUserName());
        binding.replyContentsText.setText(item.getReplyContents());
        GlideApp.with(mContext).load(Define.IMAGE_URL + item.getUserProfile())
                .apply(ImageUtil.getGlideRequestOption().placeholder(R.drawable.user_profile))
                .into(binding.replyProfileImage);
    }
}
