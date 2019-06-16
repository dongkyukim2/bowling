package com.dk.project.bowling.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.GameModel;
import com.dk.project.bowling.ui.viewHolder.ClubGameViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;

public class ClubGameListAdapter extends BaseRecyclerViewAdapter<ClubGameViewHolder> {

    private ArrayList<GameModel> clubList = new ArrayList<>();

    public ClubGameListAdapter() {
        clubList.add(new GameModel());
        clubList.add(new GameModel());
        clubList.add(new GameModel());
        clubList.add(new GameModel());
    }

    @Override
    public ClubGameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClubGameViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_club_game, parent, false));
    }

    @Override
    public void onBindViewHolder(ClubGameViewHolder holder, int position) {

        holder.onBindView(clubList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }


//    public void setClubList(ArrayList<ClubModel> clubList) {
//        if (clubList.isEmpty()) {
//            clubList.addAll(clubList);
//            notifyDataSetChanged();
//        } else {
//            DiffUtil.DiffResult result = getDiffUtil(this.clubList, clubList);
//            this.clubList = clubList;
//            result.dispatchUpdatesTo(this);
//        }
//
//    }
//
//    public DiffUtil.DiffResult getDiffUtil(ArrayList<ClubModel> oldList,
//                                           ArrayList<ClubModel> newList) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//            @Override
//            public int getOldListSize() {
//                return oldList.size();
//            }
//
//            @Override
//            public int getNewListSize() {
//                return newList.size();
//            }
//
//            @Override
//            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                ClubModel oldModel = oldList.get(oldItemPosition);
//                ClubModel newModel = newList.get(newItemPosition);
//                return TextUtils.equals(oldModel.getClubId(), newModel.getClubId());
//            }
//
//            @Override
//            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                ClubModel oldModel = oldList.get(oldItemPosition);
//                ClubModel newModel = newList.get(newItemPosition);
//                return TextUtils.equals(oldModel.getClubId(), newModel.getClubId());
//            }
//        });
//        return diffResult;
//    }
//
//    public ClubModel getClubModel(int position) {
//        return clubList.get(position);
//    }
}
