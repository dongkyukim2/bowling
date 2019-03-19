package com.dk.project.post.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.dk.project.post.R;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.ui.viewHolder.MediaSelectListGridViewHolder;
import java.util.ArrayList;

/**
 * Created by Eznix on 2015-10-05.
 */
public class MediaSelectListAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<MediaSelectListModel> itemList = new ArrayList<>();
    private Context context;
    private TextView countView;
    private String viewerType;
    private boolean multiSelect;
    private GridLayoutManager layoutManager;

    public MediaSelectListAdapter(Context context, TextView countView, String type, boolean multi, GridLayoutManager layoutManager) {
        super();
        this.context = context;
        this.countView = countView;
        this.layoutManager = layoutManager;
        viewerType = type;
        multiSelect = multi;
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MediaSelectListGridViewHolder holder = new MediaSelectListGridViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_select_list_grid_item, parent, false), this);
        return holder;
    }

    @Override
    public void onBindViewHolder(BindViewHolder holder, int position) {
        holder.onBindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void unCheckRefresh() {
        int index = 0;
        for (MediaSelectListModel model : ListController.getInstance().getMediaSelectList()) {
            int position = itemList.indexOf(model);
            if (position >= 0) {
                View view = layoutManager.findViewByPosition(position);
                index++;
                if (view != null) {
                    TextView textView = view.findViewById(R.id.item_check);
                    textView.setText(String.valueOf(index));
                }
            }
        }
    }

    public void addList(ArrayList<MediaSelectListModel> itemList) {
        this.itemList.addAll(itemList);
    }
}