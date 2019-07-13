package com.dk.project.post.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.ui.viewHolder.BaseContentsViewHolder;
import com.dk.project.post.ui.viewHolder.ContentsListViewHolder;
import com.dk.project.post.ui.viewHolder.ContentsThumbViewHolder;
import com.dk.project.post.viewModel.ContentListViewModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ContentsListAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<PostModel> postList;
    private Context context;
    private LayoutInflater layoutInflater;
    private ContentListViewModel viewModel;
    private HashMap<BaseContentsViewHolder, Integer> viewHolderMap;
    private int holderType = THUMBNAIL_TYPE;

    public ContentsListAdapter(Context context, ContentListViewModel viewModel) {
        postList = new ArrayList<>();
        this.context = context;
        this.viewModel = viewModel;
        layoutInflater = LayoutInflater.from(context);
        viewHolderMap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        return holderType;
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case THUMBNAIL_TYPE:
                return new ContentsThumbViewHolder(layoutInflater.inflate(R.layout.fragment_contents_thumb_item, parent, false), viewModel);
            case LIST_TYPE:
                return new ContentsListViewHolder(layoutInflater.inflate(R.layout.fragment_contents_list_item, parent, false), viewModel);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BindViewHolder holder, int position) {
        if (holder instanceof ContentsThumbViewHolder) {
            viewHolderMap.put((ContentsThumbViewHolder) holder, position);
        } else {
            viewHolderMap.put((ContentsListViewHolder) holder, position);
        }
        holder.onBindView(postList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setMorePostList(ArrayList<PostModel> postList) {
        if (this.postList.isEmpty()) {
            this.postList = postList;
            notifyItemRangeInserted(0, postList.size());
        } else {
            postList.addAll(0, this.postList);
            DiffUtil.DiffResult result = getDiffUtil(this.postList, postList);
            this.postList = postList;
            result.dispatchUpdatesTo(this);
        }
    }

    public void setClearPostList(ArrayList<PostModel> postList) {
        if (this.postList.isEmpty()) {
            this.postList = postList;
            notifyItemRangeInserted(0, postList.size());
        } else {
            DiffUtil.DiffResult result = getDiffUtil(this.postList, postList);
            this.postList = postList;
            result.dispatchUpdatesTo(this);
        }
    }

    public DiffUtil.DiffResult getDiffUtil(ArrayList<PostModel> oldList, ArrayList<PostModel> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
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
                PostModel oldModel = oldList.get(oldItemPosition);
                PostModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getPostId(), newModel.getPostId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                PostModel oldModel = oldList.get(oldItemPosition);
                PostModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getInputText(), newModel.getInputText()) &&
                        TextUtils.equals(oldModel.getWriteDate(), newModel.getWriteDate()) &&
                        oldModel.getLikeCount() == newModel.getLikeCount() &&
                        oldModel.isLikeSelected() == newModel.isLikeSelected() &&
                        oldModel.getReplyCount() == newModel.getReplyCount() &&
                        compareImageList(oldModel.getImageList(), newModel.getImageList()) &&
                        compareReplyList(oldModel.getReplyList(), newModel.getReplyList());
            }
        });
        return diffResult;
    }

    public void changeListViewHolderType() {
        if (holderType == THUMBNAIL_TYPE) {
            holderType = LIST_TYPE;
        } else {
            holderType = THUMBNAIL_TYPE;
        }
        notifyDataSetChanged();
    }

    private boolean compareImageList(ArrayList<MediaSelectListModel> oldList, ArrayList<MediaSelectListModel> newList) {
        if (oldList.size() == newList.size()) {
            boolean value = true;
            for (int i = 0; i < oldList.size(); i++) {
                if (!TextUtils.equals(oldList.get(i).getFilePath(), newList.get(i).getFilePath())) {
                    value = false;
                    break;
                }
            }
            return value;
        } else {
            return false;
        }
    }

    private boolean compareReplyList(ArrayList<ReplyModel> oldList, ArrayList<ReplyModel> newList) {
        if (oldList.size() == newList.size()) {
            boolean value = true;
            for (int i = 0; i < oldList.size(); i++) {
                if (!TextUtils.equals(oldList.get(i).getReplyContents(), newList.get(i).getReplyContents())) {
                    value = false;
                    break;
                }
            }
            return value;
        } else {
            return false;
        }
    }

    public void refreshWriteDate() {
        for (BaseContentsViewHolder a : viewHolderMap.keySet()) {
            a.refreshWriteDate();
        }
    }
}