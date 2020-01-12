package com.dk.project.post.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityMediaSelectBinding;
import com.dk.project.post.model.MediaSelectModel;
import com.dk.project.post.ui.adapter.MediaSelectAdapter;
import com.dk.project.post.utils.RecyclerViewClickListener;
import com.dk.project.post.viewModel.MediaSelectViewModel;

public class MediaSelectActivity extends BindActivity<ActivityMediaSelectBinding, MediaSelectViewModel> {

    private MediaSelectAdapter mediaSelectAdapter;
    private boolean multiSelect;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media_select;
    }

    @Override
    protected MediaSelectViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(MediaSelectViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.setViewModel(viewModel);

        multiSelect = getIntent().getBooleanExtra(IMAGE_MULTI_SELECT, true);

        binding.mediaSelectRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.mediaSelectRecycler.setItemAnimator(new DefaultItemAnimator());
        mediaSelectAdapter = new MediaSelectAdapter(this, "image");
        binding.mediaSelectRecycler.setAdapter(mediaSelectAdapter);

        viewModel.getMediaSelectlistLiveData().observe(this, mediaSelectModels -> {
            mediaSelectAdapter.addList(mediaSelectModels);
            mediaSelectAdapter.notifyDataSetChanged();
        });

        RecyclerViewClickListener.setItemClickListener(binding.mediaSelectRecycler, (view, position) -> {
            MediaSelectModel mediaSelectModel = mediaSelectAdapter.getItem(position);
            Bundle bundle = new Bundle();
            bundle.putString("BucketId", mediaSelectModel.getBucketId());
            bundle.putInt("type", mediaSelectModel.getType());
            bundle.putString("viewerType", "image");
            bundle.putBoolean(IMAGE_MULTI_SELECT, multiSelect);
            Intent intent = new Intent(this, MediaSelectListActivity.class);
            intent.putExtras(bundle);
            this.startActivityForResult(intent, MEDIA_ATTACH_LIST);

        });

        viewModel.getMediaList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case MEDIA_ATTACH_LIST:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
