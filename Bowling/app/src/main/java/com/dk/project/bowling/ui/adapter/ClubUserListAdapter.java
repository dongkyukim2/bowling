package com.dk.project.bowling.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.viewHolder.ClubUserViewHolder;
import com.dk.project.bowling.viewModel.ClubUserListViewModel;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.bowling.model.ScoreClubUserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import kotlin.Pair;

public class ClubUserListAdapter extends BaseRecyclerViewAdapter<ClubUserViewHolder> {

    private ClubUserListViewModel clubUserListViewModel;
    private ArrayList<ScoreClubUserModel> clubUserList = new ArrayList<>();
    private HashMap<String, Boolean> selectedUserMap = new HashMap<>();

    private boolean selectMode;

    @Override
    public ClubUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClubUserViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_club_user, parent, false));
    }

    @Override
    public void onBindViewHolder(ClubUserViewHolder holder, int position) {
        holder.onBindView(clubUserList.get(position), position);
        if (!clubUserListViewModel.isSelectMode()) {
            holder.getBinding().userCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return clubUserList.size();
    }


    public void setClubUserList(Pair<List<ScoreClubUserModel>, List<ScoreClubUserModel>> clubUserList) {

        if (selectMode) {
            this.clubUserList.addAll(clubUserList.getSecond());
        } else {
            this.clubUserList.addAll(clubUserList.getFirst());
            this.clubUserList.addAll(clubUserList.getSecond());
        }

        notifyDataSetChanged();
    }

    public void setCheck(int position) {
        ScoreClubUserModel userModel = clubUserList.get(position);
        if (selectedUserMap.containsKey(userModel.getUserId())) {
            return;
        }
        clubUserList.get(position).setCheck(!userModel.isCheck());
        notifyItemChanged(position);
    }

    public ArrayList<ScoreClubUserModel> getSelectList() {
        return Observable.fromIterable(clubUserList).filter(ScoreClubUserModel::isCheck).toList().map(userModels -> new ArrayList(userModels)).blockingGet();
    }

    public void setSelectedUserMap(HashMap<String, Boolean> userMap) {
        selectedUserMap.putAll(userMap);
    }

    public void setClubUserListViewModel(ClubUserListViewModel clubUserListViewModel) {
        this.clubUserListViewModel = clubUserListViewModel;
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }
}
