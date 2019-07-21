package com.dk.project.bowling.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.viewHolder.GraphRecentScoreViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.bowling.model.ScoreModel;

import java.util.ArrayList;

public class GraphScoreAdapter extends BaseRecyclerViewAdapter<GraphRecentScoreViewHolder> {

    private ArrayList<ScoreModel> graphScoreList = new ArrayList<>();
    private int graphType;


    @Override
    public GraphRecentScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GraphRecentScoreViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_holder_graph_scores, parent, false));
    }

    @Override
    public void onBindViewHolder(GraphRecentScoreViewHolder holder, int position) {
        holder.setGraphType(graphType);
        holder.onBindView(graphScoreList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return graphScoreList.size();
    }


    public void setGraphScoreList(ArrayList<ScoreModel> recentScoresList) {
        DiffUtil.DiffResult result = getDiffUtil(this.graphScoreList, recentScoresList);
        this.graphScoreList = recentScoresList;
        result.dispatchUpdatesTo(this);
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

    public void setGraphType(int graphType) {
        this.graphType = graphType;
        notifyDataSetChanged();
    }
}
