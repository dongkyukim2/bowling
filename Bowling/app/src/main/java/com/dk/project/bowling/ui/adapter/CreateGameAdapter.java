package com.dk.project.bowling.ui.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.UserModel;
import com.dk.project.bowling.ui.adapter.callback.ItemMoveCallback;
import com.dk.project.bowling.ui.viewHolder.CreateGameViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class CreateGameAdapter extends BaseRecyclerViewAdapter<CreateGameViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private ArrayList<UserModel> userList = new ArrayList<>();

    @Override
    public CreateGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreateGameViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_create_game, parent, false));
    }

    @Override
    public void onBindViewHolder(CreateGameViewHolder holder, int position) {
        holder.onBindView(userList.get(position), position);
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(userList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(userList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(CreateGameViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(CreateGameViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public void setUserList(ArrayList<UserModel> clubList) {
//        if (userList.isEmpty()) {
        userList.addAll(clubList);
        notifyDataSetChanged();
//        } else {
//            DiffUtil.DiffResult result = getDiffUtil(this.userList, clubList);
//            this.userList = clubList;
//            result.dispatchUpdatesTo(this);
//        }

    }

    public DiffUtil.DiffResult getDiffUtil(ArrayList<UserModel> oldList,
                                           ArrayList<UserModel> newList) {
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
                UserModel oldModel = oldList.get(oldItemPosition);
                UserModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getUserId(), newModel.getUserId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                UserModel oldModel = oldList.get(oldItemPosition);
                UserModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getUserId(), newModel.getUserId());
            }
        });
        return diffResult;
    }

}
