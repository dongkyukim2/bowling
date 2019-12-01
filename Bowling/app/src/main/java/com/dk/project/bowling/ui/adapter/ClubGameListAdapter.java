package com.dk.project.bowling.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.viewHolder.ClubGameViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.bowling.model.ReadGameModel;

import java.util.ArrayList;

public class ClubGameListAdapter extends BaseRecyclerViewAdapter<ClubGameViewHolder> {

    private ArrayList<ReadGameModel> clubGameList = new ArrayList<>();

    @Override
    public ClubGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClubGameViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_club_game, parent, false));
    }

    @Override
    public void onBindViewHolder(ClubGameViewHolder holder, int position) {
        holder.onBindView(clubGameList.get(position), position);
        if (clubGameList.size() - 1 == position) {
            holder.getBinding().divider.setVisibility(View.GONE);
        } else {
            holder.getBinding().divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return clubGameList.size();
    }

    public void setClubList(Pair<ArrayList<ReadGameModel>, Boolean> clubGameList) {
        if (clubGameList.second) {
            this.clubGameList = clubGameList.first;
            notifyDataSetChanged();
        } else {
            clubGameList.first.addAll(0, this.clubGameList);
            DiffUtil.DiffResult result = getDiffUtil(this.clubGameList, clubGameList.first);
            this.clubGameList = clubGameList.first;
            result.dispatchUpdatesTo(this);
        }
    }


    public DiffUtil.DiffResult getDiffUtil(ArrayList<ReadGameModel> oldList, ArrayList<ReadGameModel> newList) {
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
                ReadGameModel oldModel = oldList.get(oldItemPosition);
                ReadGameModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getGameId(), newModel.getGameId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ReadGameModel oldModel = oldList.get(oldItemPosition);
                ReadGameModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getGameId(), newModel.getGameId());
            }
        });
        return diffResult;
    }

    public ReadGameModel getClubGame(int position) {
        return clubGameList.get(position);
    }
}
