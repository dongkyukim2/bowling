package com.dk.project.bowling.ui.adapter.callback;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.viewHolder.ClubViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.bowling.model.ClubModel;

import java.util.ArrayList;

public class ClubSearchAdapter extends BaseRecyclerViewAdapter<ClubViewHolder> {

    private ArrayList<ClubModel> clubList = new ArrayList<>();

    @Override
    public ClubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClubViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_club, parent, false));
    }

    @Override
    public void onBindViewHolder(ClubViewHolder holder, int position) {
        holder.onBindView(clubList.get(position), position);

        View divider = holder.getBinding().divider;
        if (clubList.size() - 1 == position) {
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }


    public void setClubList(ArrayList<ClubModel> list, boolean clear) {
        if (clear) {
            clubList.clear();
        }
        clubList.addAll(list);
        notifyDataSetChanged();

//        ArrayList<ClubModel> receiveClubList = new ArrayList<>();
//
//
//        if (clubListModel.first.isSuccess() && clubListModel.second.isSuccess()) {
//            ArrayList<ClubModel> filterList = new ArrayList<>();
//            for (ClubModel signClub : clubListModel.first.getData()) {
//                for (ClubModel recommendClub : clubListModel.second.getData()) {
//                    if (signClub.getClubId().equals(recommendClub.getClubId())) {
//                        filterList.add(recommendClub);
//                    }
//                }
//            }
//            clubListModel.second.getData().removeAll(filterList);
//        }
//
//        receiveClubList.addAll(clubListModel.second.getData());
//
//        DiffUtil.DiffResult result = getDiffUtil(this.clubList, receiveClubList);
//        this.clubList = receiveClubList;
//        result.dispatchUpdatesTo(this);

    }

    public DiffUtil.DiffResult getDiffUtil(ArrayList<ClubModel> oldList,
                                           ArrayList<ClubModel> newList) {
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
                ClubModel oldModel = oldList.get(oldItemPosition);
                ClubModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getClubId(), newModel.getClubId());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ClubModel oldModel = oldList.get(oldItemPosition);
                ClubModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getClubId(), newModel.getClubId());
            }
        });
        return diffResult;
    }

    public ClubModel getClubModel(int position) {
        return clubList.get(position);
    }
}
