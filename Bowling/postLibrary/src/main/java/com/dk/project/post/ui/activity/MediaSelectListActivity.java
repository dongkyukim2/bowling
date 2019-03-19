package com.dk.project.post.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.databinding.ActivityMediaSelectListBinding;
import com.dk.project.post.ui.adapter.MediaSelectListAdapter;
import com.dk.project.post.viewModel.MediaSelectListViewModel;

public class MediaSelectListActivity extends BindActivity<ActivityMediaSelectListBinding, MediaSelectListViewModel> {

  private MediaSelectListAdapter mediaSelectListAdapter;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_media_select_list;
  }

  @Override
  protected MediaSelectListViewModel getViewModel() {
    return ViewModelProviders.of(this).get(MediaSelectListViewModel.class);
  }

  @Override
  protected void subscribeToModel() {
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewModel.getMediaMutableLiveData().observe(this, mediaSelectListModels -> {
      mediaSelectListAdapter.addList(mediaSelectListModels);
      mediaSelectListAdapter.notifyDataSetChanged();
    });

    ListController.getInstance().mediaSelectListClear();

    binding.setViewModel(viewModel);

    binding.mediaSelectListRecycler.setLayoutManager(new GridLayoutManager(this, 3));
    binding.mediaSelectListRecycler.setItemAnimator(new DefaultItemAnimator());
    mediaSelectListAdapter = new MediaSelectListAdapter(this, null, "image", true,
        (GridLayoutManager) binding.mediaSelectListRecycler.getLayoutManager());
    binding.mediaSelectListRecycler.setAdapter(mediaSelectListAdapter);

    viewModel.getMediaList(getIntent().getStringExtra("BucketId"), getIntent().getIntExtra("type", 0));

    toolbarRightButton.setVisibility(View.VISIBLE);

  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);

  }

  @Override
  public void onToolbarRightClick() {
    if (ListController.getInstance().getMediaSelectList().isEmpty()) {
      return;
    }
    setResult(RESULT_OK);
    finish();
  }
}
