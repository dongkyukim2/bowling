package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.ClubDetailActivity;
import com.dk.project.bowling.ui.viewHolder.SignClubViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.retrofit.ResponseModel;

import java.util.ArrayList;

public class SignClubViewPagerAdapter extends BaseRecyclerViewAdapter<SignClubViewHolder> {

    private Context context;
    private ArrayList<ClubModel> clubList = new ArrayList<>();

    public SignClubViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SignClubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SignClubViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_sign_club, parent, false));
    }

    @Override
    public void onBindViewHolder(SignClubViewHolder holder, int position) {
        holder.onBindView(clubList.get(position), position);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ClubDetailActivity.class);
            intent.putExtra(CLUB_MODEL, clubList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }


    public void setClubList(Pair<ResponseModel<ArrayList<ClubModel>>, ResponseModel<ArrayList<ClubModel>>> clubListModel) {
        DiffUtil.DiffResult result = getDiffUtil(this.clubList, clubListModel.first.getData());
        this.clubList = clubListModel.first.getData();
        result.dispatchUpdatesTo(this);

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

    public void removeClub(String clubId) {

        for (ClubModel clubModel : clubList) {
            if (clubId.equals(clubModel.getClubId())) {
                clubList.remove(clubModel);
                break;
            }
        }
        notifyDataSetChanged();
    }
}
