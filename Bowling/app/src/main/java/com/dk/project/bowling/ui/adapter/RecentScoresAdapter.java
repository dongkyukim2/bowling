package com.dk.project.bowling.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.bowling.R;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.viewHolder.InfoRecentScoresViewHolder;
import com.dk.project.bowling.ui.viewHolder.RecentScoresViewHolder;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;
import java.util.ArrayList;

public class RecentScoresAdapter extends BaseRecyclerViewAdapter<BindViewHolder> {

  private final int INFO_SCORE = 0;
  private final int DAY_SCORE = 1;


  private ArrayList<ScoreModel> recentScoresList = new ArrayList<>();


  @Override
  public int getItemViewType(int position) {
    if (position < 4) {
      return INFO_SCORE;
    } else {
      return DAY_SCORE;
    }
  }

  @Override
  public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (viewType == INFO_SCORE) {
      return new InfoRecentScoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_info_recent_scores, parent, false));
    } else {
      return new RecentScoresViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_recent_scores, parent, false));
    }

  }

  @Override
  public void onBindViewHolder(BindViewHolder holder, int position) {
    holder.onBindView(recentScoresList.get(position));
  }

  @Override
  public int getItemCount() {
    return recentScoresList.size();
  }


  public void setRecentScoreList(ArrayList<ScoreModel> recentScoresList, boolean clear) {
    recentScoresList.add(0,new ScoreModel());
    recentScoresList.add(0,new ScoreModel());
    recentScoresList.add(0,new ScoreModel());
    recentScoresList.add(0,new ScoreModel());
    if (this.recentScoresList.isEmpty()) {
      this.recentScoresList = recentScoresList;
      notifyItemRangeInserted(0, recentScoresList.size());
    } else {
      ArrayList<ScoreModel> list = new ArrayList<>();
      if (!clear) {
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
}
