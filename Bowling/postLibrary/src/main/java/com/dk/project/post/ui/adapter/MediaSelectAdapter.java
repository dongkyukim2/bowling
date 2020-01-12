package com.dk.project.post.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dk.project.post.R;
import com.dk.project.post.base.BaseRecyclerViewAdapter;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.model.MediaSelectModel;
import com.dk.project.post.ui.viewHolder.MediaSelectListViewHolder;

import java.util.ArrayList;

public class MediaSelectAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<MediaSelectModel> mediaList;
    private Context context;
    private String viewerType;

    public MediaSelectAdapter(Context context, String type) {
        this.context = context;
        viewerType = type;
        mediaList = new ArrayList<>();
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaSelectListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BindViewHolder holder, int position) {
        holder.onBindView(mediaList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void addList(ArrayList<MediaSelectModel> list) {
        mediaList.addAll(list);
    }

    public MediaSelectModel getItem(int position) {
        return mediaList.get(position);
    }
}