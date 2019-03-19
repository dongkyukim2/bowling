package com.dk.project.post.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityMediaSelectBinding;
import com.dk.project.post.ui.adapter.MediaSelectAdapter;
import com.dk.project.post.viewModel.MediaSelectViewModel;

public class MediaSelectActivity extends BindActivity<ActivityMediaSelectBinding, MediaSelectViewModel> {

  private MediaSelectAdapter mediaSelectAdapter;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_media_select;
  }

  @Override
  protected MediaSelectViewModel getViewModel() {
    return ViewModelProviders.of(this).get(MediaSelectViewModel.class);
  }

  @Override
  protected void subscribeToModel() {

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding.setViewModel(viewModel);

    binding.mediaSelectRecycler.setLayoutManager(new LinearLayoutManager(this));
    binding.mediaSelectRecycler.setItemAnimator(new DefaultItemAnimator());
    mediaSelectAdapter = new MediaSelectAdapter(this, "image", true);
    binding.mediaSelectRecycler.setAdapter(mediaSelectAdapter);

    viewModel.getMediaSelectlistLiveData().observe(this, mediaSelectModels -> {
      mediaSelectAdapter.addList(mediaSelectModels);
      mediaSelectAdapter.notifyDataSetChanged();
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
