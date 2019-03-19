package com.dk.project.post.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.ui.viewHolder.ContentsHorizontalListViewHolder;
import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ContentsHorizontalListAdapter extends BaseRecyclerViewAdapter {

  private ArrayList<MediaSelectListModel> imageList;
  private Context context;
  private LayoutInflater layoutInflater;

  public ContentsHorizontalListAdapter(Context context) {
    imageList = new ArrayList<>();
    this.context = context;
    layoutInflater = LayoutInflater.from(context);
  }

  @Override
  public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ContentsHorizontalListViewHolder(layoutInflater.inflate(R.layout.fragment_contents_horizontal_list_item, parent, false));
  }

  @Override
  public void onBindViewHolder(BindViewHolder holder, int position) {
    holder.onBindView(imageList.get(position));
  }

  @Override
  public int getItemCount() {
    return imageList.size();
  }

  public void setImageList(ArrayList<MediaSelectListModel> imageList) {
    if (this.imageList.isEmpty()) {
      this.imageList = imageList;
      notifyItemRangeInserted(0, imageList.size());
    } else {
      DiffUtil.DiffResult result = getDiffUtil(this.imageList, imageList);
      this.imageList = imageList;
      result.dispatchUpdatesTo(this);
    }
  }

  public DiffUtil.DiffResult getDiffUtil(ArrayList<MediaSelectListModel> oldList, ArrayList<MediaSelectListModel> newList) {
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
        MediaSelectListModel oldModel = oldList.get(oldItemPosition);
        MediaSelectListModel newModel = newList.get(newItemPosition);
        return TextUtils.equals(oldModel.getFilePath(), newModel.getFilePath());
      }

      @Override
      public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MediaSelectListModel oldModel = oldList.get(oldItemPosition);
        MediaSelectListModel newModel = newList.get(newItemPosition);
        return TextUtils.equals(oldModel.getFilePath(), newModel.getFilePath());
      }
    });
    return diffResult;
  }
}