package com.dk.project.post.ui.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dk.project.post.R;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.impl.ReplyListener;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.ui.activity.ReplyModifyActivity;
import com.dk.project.post.ui.viewHolder.ReplyViewHolder;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ToastUtil;
import com.dk.project.post.viewModel.ReadViewModel;

import java.util.ArrayList;

/**
 * Created by dkkim on 2017-04-23.
 */

public class ReplyAdapter extends BaseRecyclerViewAdapter<ReplyViewHolder> implements ReplyListener {

    private final int REPLY_TYPE = 0;
    private final int MORE_TYPE = 1;

    private BindActivity mContext;
    private ArrayList<ReplyModel> replyItemArrayList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private ReadViewModel readViewModel;
    private boolean requestModifyReply = false;

    public ReplyAdapter(BindActivity context, boolean isReplyMoreList, ReadViewModel readViewModel) {
        mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.readViewModel = readViewModel;
    }

    @Override
    public ReplyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReplyViewHolder replyViewHolder = new ReplyViewHolder(layoutInflater.inflate(R.layout.reply_item, parent, false), this);
        return replyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReplyViewHolder holder, int position) {
        holder.onBindView(replyItemArrayList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return replyItemArrayList == null ? 0 : replyItemArrayList.size();
    }


    public void setDeleteReply(ReplyModel deleteReplyModel) {
        ArrayList<ReplyModel> replyModelArrayList = new ArrayList<>(replyItemArrayList);
        for (ReplyModel replyModel : replyModelArrayList) {
            if (deleteReplyModel.getReplyId().equals(replyModel.getReplyId())) {
                replyModelArrayList.remove(replyModel);
                setReplyList(replyModelArrayList, true);
                if (readViewModel != null) {
                    int replyCount = Integer.valueOf(readViewModel.getReplyCountLiveData().getValue());
                    replyCount--;
                    readViewModel.getReplyCountLiveData().setValue(String.valueOf(replyCount));
                }
                break;
            }
        }
    }

    public void setReplyList(ArrayList<ReplyModel> replyList, boolean clean) {
        if (clean) {
            replyItemArrayList.clear();

        }
        replyItemArrayList.addAll(replyList);
        notifyDataSetChanged();
    }

    public DiffUtil.DiffResult getDiffUtil(ArrayList<ReplyModel> oldList, ArrayList<ReplyModel> newList) {
        return DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                ReplyModel old = oldList.get(oldItemPosition);
                ReplyModel comment = newList.get(newItemPosition);
                return TextUtils.equals(old.getReplyId(), comment.getReplyId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ReplyModel old = oldList.get(oldItemPosition);
                ReplyModel comment = newList.get(newItemPosition);
                return TextUtils.equals(old.getReplyContents(), comment.getReplyContents());
            }
        });
    }


    @Override
    public boolean onReplyLongClick(ReplyModel replyModel) {
        switch (LoginManager.getInstance().isPermissionUser(replyModel.getUserId())) {
            case Define.OK:
                AlertDialogUtil.showListAlertDialog(mContext, null, new String[]{"댓글 수정", "삭제하기"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(mContext, ReplyModifyActivity.class);
                            intent.putExtra(REPLY_MODEL, replyModel);
                            mContext.startActivityForResult(intent, MODIFY_REPLY);
                            mContext.overridePendingTransition(0, 0);
                            break;
                        case 1:
                            if (requestModifyReply) {
                                ToastUtil.showWaitToastCenter(mContext);
                                return;
                            }
                            requestModifyReply = true;
                            AlertDialogUtil.showAlertDialog(mContext, null, "댓글을 삭제하시겠습니까?", (dialog1, which1) ->
                                    executeRx(PostApi.getInstance().deleteReply(replyModel.getReplyId(),
                                            receivedData -> {
                                                setDeleteReply(receivedData.getData());
                                                requestModifyReply = false;
                                            },
                                            errorData -> {
                                                requestModifyReply = false;
                                            })), (dialog1, which1) -> {
                                requestModifyReply = false;
                            });
                            break;
                    }
                });
                break;
            case Define.LOGOUT:
                AlertDialogUtil.showLoginAlertDialog(mContext);
                break;
            default:
                break;
        }
        return true;
    }
}
