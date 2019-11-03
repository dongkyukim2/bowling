package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.viewHolder.RecentScoresViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.bowling.model.ScoreModel;

import java.util.ArrayList;

public class RecentScoresAdapter extends BaseRecyclerViewAdapter<BindViewHolder> {

    private Context context;
    private ArrayList<ScoreModel> recentScoresList = new ArrayList<>();


    public RecentScoresAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentScoresViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_recent_scores, parent, false));
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

            this.recentScoresList = recentScoresList;
            notifyItemRangeInserted(0, recentScoresList.size());
        } else {
            ArrayList<ScoreModel> list = new ArrayList<>();
            if (clear) {

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

//    public void setScoreAvgModel(ScoreAvgModel scoreAvgModel) {
//        monthAvg = new ScoreModel();
//        if (scoreAvgModel.getMonthAvg() == null) {
//            monthAvg.setAvgScore(0);
//            monthAvg.setMinScore(0);
//            monthAvg.setMaxScore(0);
//        } else {
//            monthAvg.setAvgScore(scoreAvgModel.getMonthAvg().getAvgScore());
//            monthAvg.setMinScore(scoreAvgModel.getMonthAvg().getMinScore());
//            monthAvg.setMaxScore(scoreAvgModel.getMonthAvg().getMaxScore());
//        }
//
//        GraphViewModel graphViewModel = ((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel();
//        if (graphViewModel == null) {
//            new Handler().postDelayed(() -> {
//                monthAvg.setPlayDateTime(((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel().getYearMonth());
//                setRecentScoreList(new ArrayList<>(scoreAvgModel.getMonthAvgList()), true);
//            }, 500);
//
//        } else {
//            monthAvg.setPlayDateTime(((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel().getYearMonth());
//            setRecentScoreList(new ArrayList<>(scoreAvgModel.getMonthAvgList()), true);
//        }
//    }
}
