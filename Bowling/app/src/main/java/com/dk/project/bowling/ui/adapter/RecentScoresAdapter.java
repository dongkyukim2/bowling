package com.dk.project.bowling.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.ScoreAvgModel;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.bowling.ui.fragment.GraphFragment;
import com.dk.project.bowling.ui.viewHolder.InfoRecentScoresViewHolder;
import com.dk.project.bowling.ui.viewHolder.MainInfoGraphViewHolder;
import com.dk.project.bowling.ui.viewHolder.RecentScoresViewHolder;
import com.dk.project.bowling.viewModel.GraphViewModel;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;

import java.util.ArrayList;

public class RecentScoresAdapter extends BaseRecyclerViewAdapter<BindViewHolder> {

    private final int GRAPH_SCORE = 0;
    private final int INFO_SCORE = 1;
    private final int DAY_SCORE = 2;


    private Context context;
    private ScoreModel monthAvg;
    private ArrayList<ScoreModel> recentScoresList = new ArrayList<>();


    public RecentScoresAdapter(Context context) {
        this.context = context;
    }

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
            MainInfoGraphViewHolder mainInfoGraphViewHolder = new MainInfoGraphViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_main_info_graph, parent, false));
            mainInfoGraphViewHolder.getBinding().scoreGraphLeft.setOnClickListener(v ->
                    ((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getBinding().prevMonth.callOnClick());
            mainInfoGraphViewHolder.getBinding().scoreGraphRight.setOnClickListener(v ->
                    ((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getBinding().nextMonth.callOnClick());
            return mainInfoGraphViewHolder;
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

    public void setScoreAvgModel(ScoreAvgModel scoreAvgModel) {
        monthAvg = new ScoreModel();
        if (scoreAvgModel.getMonthAvg() == null) {
            monthAvg.setAvgScore(0);
            monthAvg.setMinScore(0);
            monthAvg.setMaxScore(0);
        } else {
            monthAvg.setAvgScore(scoreAvgModel.getMonthAvg().getAvgScore());
            monthAvg.setMinScore(scoreAvgModel.getMonthAvg().getMinScore());
            monthAvg.setMaxScore(scoreAvgModel.getMonthAvg().getMaxScore());
        }

        GraphViewModel graphViewModel = ((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel();
        if (graphViewModel == null) {
            new Handler().postDelayed(() -> {
                monthAvg.setPlayDateTime(((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel().getYearMonth());
                setRecentScoreList(new ArrayList<>(scoreAvgModel.getMonthAvgList()), true);
            }, 500);

        } else {
            monthAvg.setPlayDateTime(((GraphFragment) ((MainActivity) context).getMainViewPagerFragmentAdapter().createFragment(1)).getViewModel().getYearMonth());
            setRecentScoreList(new ArrayList<>(scoreAvgModel.getMonthAvgList()), true);
        }
    }
}
