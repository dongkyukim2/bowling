package com.dk.project.bowling.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.UserModel;
import com.dk.project.bowling.ui.viewHolder.ClubUserViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;

public class ClubUserListAdapter extends BaseRecyclerViewAdapter<ClubUserViewHolder> {

    private ArrayList<UserModel> clubUserList = new ArrayList<>();

    @Override
    public ClubUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClubUserViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_create_game, parent, false));
    }

    @Override
    public void onBindViewHolder(ClubUserViewHolder holder, int position) {
        holder.onBindView(clubUserList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return clubUserList.size();
    }


    public void setClubUserList(ArrayList<UserModel> clubUserList) {
        this.clubUserList.addAll(clubUserList);
        notifyDataSetChanged();
    }

    public void setCheck(int position) {
        clubUserList.get(position).setCheck(!clubUserList.get(position).isCheck());
        notifyItemChanged(position);
    }
}
