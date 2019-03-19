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
    private boolean multiSelect;

    public MediaSelectAdapter(Context context, String type, boolean multi) {
        this.context = context;
        viewerType = type;
        multiSelect = multi;
        mediaList = new ArrayList<>();
    }

    @Override
    public BindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaSelectListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_select_item, parent, false));
    }

    @Override
    public void onBindViewHolder(BindViewHolder holder, int position) {
        holder.onBindView(mediaList.get(position));
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void addList(ArrayList<MediaSelectModel> list) {
        mediaList.addAll(list);
    }

    /*@Override
    public MediaAttachViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_attach_list_item, parent, false);
        MediaAttachViewHolder viewHolder = new MediaAttachViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MediaAttachViewHolder holder, final int position) {
        MediaAttachItem item = mediaList.get(position);
        setThumbnail(holder, item, position);
        holder.folderName.setText(item.getName());
        holder.folderCount.setText(String.valueOf(item.getAlbumCount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("BucketId", mediaList.get(position).getBucketId());
                bundle.putInt("type", mediaList.get(position).getType());
                bundle.putString("viewerType", viewerType);
                bundle.putBoolean("multiSelect", multiSelect);
                Intent intent = new Intent(holder.itemView.getContext(), MediaAttachListActivity.class);
                intent.putExtras(bundle);
                ((Activity) context).startActivityForResult(intent, MEDIA_ATTACH_LIST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public void setItemList(ArrayList<MediaAttachItem> list) {
        mediaList.addAll(list);
    }

    private void setThumbnail(final MediaAttachViewHolder holder, final MediaAttachItem item, final int position) {
        if (viewerType.equals("image")) {
            if (item.getPath().toLowerCase().endsWith(".gif")) {
                holder.folderThumbnailGif.setVisibility(View.VISIBLE);
                holder.folderThumbnail.setVisibility(View.GONE);
                int width = 500, height = 500;
                Uri imageUri = Uri.fromFile(new File(item.getPath()));
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();

                DraweeController animatedController = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true)
                        .setImageRequest(request)
                        .build();
                holder.folderThumbnailGif.setController(animatedController);
            } else {
                holder.folderThumbnailGif.setVisibility(View.GONE);
                holder.folderThumbnail.setVisibility(View.VISIBLE);
                Glide.with(context).load(item.getPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.folderThumbnail);
            }
//            Uri playableUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(item.getCoverId()));

        } else if (viewerType.equals("video")) {

            new AsyncTask<MediaAttachItem, Long, Bitmap>() {
                @Override
                protected Bitmap doInBackground(MediaAttachItem... params) {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(params[0].getPath());
                    Bitmap bitmap = retriever.getFrameAtTime((params[0].getDuration() / 2) * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    holder.folderThumbnail.setImageBitmap(bitmap);
                    super.onPostExecute(bitmap);
                }
            }.execute(item);
            //retriever.release();

        }
    }*/
}