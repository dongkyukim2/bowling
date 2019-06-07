package com.dk.project.bowling.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.viewHolder.InfoRecentScoresViewHolder;
import com.dk.project.bowling.ui.viewHolder.MainInfoGraphViewHolder;
import com.dk.project.bowling.ui.viewHolder.RecentScoresViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;

import java.util.ArrayList;

public class RecentScoresAdapter extends BaseRecyclerViewAdapter<BindViewHolder> {

    private final int GRAPH_SCORE = 0;
    private final int INFO_SCORE = 1;
    private final int DAY_SCORE = 2;


    private ScoreModel monthAvg;
    private ArrayList<ScoreModel> recentScoresList = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return GRAPH_SCORE;
        } else if (position < 3) {
            return INFO_SCORE;
        } else {
            return DAY_SCORE;
        }
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == GRAPH_SCORE) {
            return new MainInfoGraphViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_main_info_graph, parent, false));
        } else if (viewType == INFO_SCORE) {
            return new InfoRecentScoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_info_recent_scores, parent, false));
        } else {
            return new RecentScoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recent_scores, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(BindViewHolder holder, int position) {
        holder.onBindView(recentScoresList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return recentScoresList.size();
    }

    public ScoreModel getItem(int position) {
        return recentScoresList.get(position);
    }


    public void setRecentScoreList(ArrayList<ScoreModel> recentScoresList, boolean clear) {

        if (this.recentScoresList.isEmpty()) {

            recentScoresList.add(0, monthAvg);
            recentScoresList.add(0, monthAvg);
            recentScoresList.add(0, monthAvg);

            this.recentScoresList = recentScoresList;
            notifyItemRangeInserted(0, recentScoresList.size());
        } else {
            ArrayList<ScoreModel> list = new ArrayList<>();
            if (clear) {
                list.add(monthAvg);
                list.add(monthAvg);
                list.add(monthAvg);
            } else {
                list.addAll(this.recentScoresList);
            }
            list.addAll(recentScoresList);
            DiffUtil.DiffResult result = getDiffUtil(this.recentScoresList, list);
            this.recentScoresList = list;
            result.dispatchUpdatesTo(this);
        }
    }

    public DiffUtil.DiffResult getDiffUtil(ArrayList<ScoreModel> oldList,
                                           ArrayList<ScoreModel> newList) {
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
                ScoreModel oldModel = oldList.get(oldItemPosition);
                ScoreModel newModel = newList.get(newItemPosition);
                return TextUtils.equals(oldModel.getPlayDateTime(), newModel.getPlayDateTime());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                ScoreModel oldModel = oldList.get(oldItemPosition);
                ScoreModel newModel = newList.get(newItemPosition);
                return oldModel.getAvgScore() == newModel.getAvgScore() &&
                        oldModel.getMaxScore() == newModel.getMaxScore() &&
                        oldModel.getMinScore() == newModel.getMinScore();
            }
        });
        return diffResult;
    }

    public void setMonthAvg(ScoreModel monthAvg) {
        this.monthAvg = monthAvg;
    }
}
