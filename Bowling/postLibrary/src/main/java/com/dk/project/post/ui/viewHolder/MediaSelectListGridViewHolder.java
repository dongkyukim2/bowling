package com.dk.project.post.ui.viewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dk.project.post.base.BindViewHolder;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.databinding.MediaSelectListGridItemBinding;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.ui.adapter.MediaSelectListAdapter;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.ScreenUtil;

public class MediaSelectListGridViewHolder<T extends MediaSelectListModel> extends
        BindViewHolder<MediaSelectListGridItemBinding, T> {

    private MediaSelectListAdapter adapter;
    private static int width;
    private boolean multiSelect;

    public MediaSelectListGridViewHolder(View itemView, MediaSelectListAdapter adapter, boolean multiSelect) {
        super(itemView);
        this.adapter = adapter;
        if (width == 0) {
            width = ScreenUtil.getDeviceScreenSize(itemView.getContext())[0];
        }
        this.multiSelect = multiSelect;
    }

    @Override
    public void onBindView(T item, int position) {

        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        params.height = width / 3;
        itemView.setLayoutParams(params);

        setThumbnail(item);
        if (ListController.getInstance().isExistModel(item.getFilePath())) {
            binding.itemCheck.setVisibility(View.VISIBLE);
            item.setSort(ListController.getInstance().getIndex(item.getFilePath()));
            binding.itemCheck.setText(String.valueOf(item.getSort()));
        } else {
            binding.itemCheck.setVisibility(View.GONE);
        }
        binding.getRoot().setOnClickListener(view -> {
            if (multiSelect) {
                boolean checked = !ListController.getInstance().isExistModel(item.getFilePath());
                if (checked && ListController.getInstance().getSelectListCount() >= IMAGE_LIMIT_COUNT) {
                    Toast.makeText(binding.itemCheck.getContext(), (IMAGE_LIMIT_COUNT + "개 이상 첨부 불가"),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                binding.itemCheck.setVisibility(checked ? View.VISIBLE : View.GONE);
                if (checked) {
                    item.setSort(ListController.getInstance().getSelectListCount() + 1);
                    binding.itemCheck.setText(String.valueOf(item.getSort()));
                    ListController.getInstance().addMediaSelectModel(item);
                } else {
                    ListController.getInstance().removeMediaSelectModel(item);
                    int removeIndex = item.getSort();
                    for (MediaSelectListModel model : ListController.getInstance().getMediaSelectList()) {
                        if (model.getSort() > removeIndex) {
                            model.setMinusOrder();
                        }
                    }
                    adapter.unCheckRefresh();
                }
            } else {
                boolean checked = !ListController.getInstance().isExistModel(item.getFilePath());
                adapter.allUnCheckRefresh();
                ListController.getInstance().mediaSelectListClear();

                if (checked) {
                    ListController.getInstance().addMediaSelectModel(item);
                    binding.itemCheck.setText("1");
                }
                binding.itemCheck.setVisibility(checked ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setThumbnail(MediaSelectListModel item) {
        itemView.setTag(item.getFilePath());
        if (item.getFilePath().toLowerCase().endsWith(".gif")) {
            binding.itemThumbnailGif.setVisibility(View.VISIBLE);
            binding.itemThumbnail.setVisibility(View.GONE);
            binding.itemThumbnailGif.setController(
                    ImageUtil.getDraweeController(item.getFilePath(), width / 3, width / 3));
        } else {
            binding.itemThumbnailGif.setVisibility(View.GONE);
            binding.itemThumbnail.setVisibility(View.VISIBLE);
            GlideApp.with(binding.itemThumbnail.getContext())
                    .load(item.getFilePath())
                    .thumbnail(0.4f)
                    .apply(ImageUtil.getGlideRequestOption(DiskCacheStrategy.RESOURCE).centerCrop())
                    .into(binding.itemThumbnail);
        }
    }
}