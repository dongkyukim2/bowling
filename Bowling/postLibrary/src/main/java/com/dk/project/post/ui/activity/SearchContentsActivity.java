package com.dk.project.post.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivitySearchContentsBinding;
import com.dk.project.post.ui.fragment.ContentsListFragment;
import com.dk.project.post.utils.FragmentUtil;
import com.dk.project.post.utils.TextViewUtil;
import com.dk.project.post.viewModel.ContentListViewModel;
import com.dk.project.post.viewModel.SearchViewModel;

public class SearchContentsActivity extends BindActivity<ActivitySearchContentsBinding, SearchViewModel> {

  private ContentsListFragment contentsListFragment;
  private ContentListViewModel contentListViewModel;

  @Override
  protected int getLayoutId() {
    return R.layout.activity_search_contents;
  }

  @Override
  protected SearchViewModel getViewModel() {
    return ViewModelProviders.of(this).get(SearchViewModel.class);
  }

  @Override
  protected void subscribeToModel() {

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    toolbarTitle.setVisibility(View.GONE);
    toolbarRightButton.setVisibility(View.GONE);
    toolbarLeftButton.setVisibility(View.VISIBLE);
    toolbarLeftButton.setImageResource(R.drawable.ic_arrow_back_black_24dp);
    toolbarRightSearch.setVisibility(View.VISIBLE);

    contentsListFragment = ContentsListFragment.newInstance();
    FragmentUtil.replaceFragment(this, binding.searchFragmentContainer.getId(), contentsListFragment);

    toolbarRightSearch.setOnEditorActionListener((v, actionId, event) -> requestSearch(v, actionId));
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    contentListViewModel = contentsListFragment.getViewModel();
  }

  @Override
  public void onToolbarLeftClick() {
    super.onToolbarLeftClick();
    finish();
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(0, 0);
  }

  private boolean requestSearch(TextView editText, int actionId) {
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      String search = editText.getText().toString().trim();
      if (search.length() < 3) {
        Toast.makeText(this, "세 글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
        return false;
      }
      TextViewUtil.hideKeyBoard(this, toolbarRightSearch);
      contentListViewModel.getPostList(0, search, null, null);
    }
    return false;
  }
}
