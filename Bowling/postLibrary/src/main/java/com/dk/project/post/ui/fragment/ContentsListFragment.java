package com.dk.project.post.ui.fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dk.project.post.R;
import com.dk.project.post.base.BindFragment;
import com.dk.project.post.databinding.FragmentContentsListBinding;
import com.dk.project.post.ui.adapter.ContentsListAdapter;
import com.dk.project.post.viewModel.ContentListViewModel;

public class ContentsListFragment extends BindFragment<FragmentContentsListBinding, ContentListViewModel> {

    public static ContentsListFragment newInstance(String cLubId) {
        Bundle bundle = new Bundle();
        bundle.putString("POST_CLUB_ID", cLubId);
        ContentsListFragment contentsListFragment = new ContentsListFragment();
        contentsListFragment.setArguments(bundle);
        return contentsListFragment;
    }

    public static ContentsListFragment newInstance() {
        return new ContentsListFragment();
    }

    private ContentsListAdapter contentsListAdapter;
    private OnFloatingActionButtonListener onFloatingActionButtonListener;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contents_list;
    }

    @Override
    protected ContentListViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ContentListViewModel.class);
    }

    @Override
    protected void registerLiveData() {
        viewModel.getPostItemList().observe(this, postModels -> {
            contentsListAdapter.setClearPostList(postModels);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.contentsListView.setLayoutManager(linearLayoutManager);
        contentsListAdapter = new ContentsListAdapter(mContext, viewModel);
        binding.contentsListView.setItemAnimator(new DefaultItemAnimator());
        binding.contentsListView.setAdapter(contentsListAdapter);
        viewModel.setRecyclerViewAdapterNmanager(contentsListAdapter, linearLayoutManager);

        binding.contentsListViewRefresh.setOnRefreshListener(() -> {

            viewModel.getPostList(0, viewModel.getClubId(), null, receivedData -> {
                contentsListAdapter.setClearPostList(receivedData.getData());
                binding.contentsListViewRefresh.setRefreshing(false);
            }, errorData -> {

            });

//      Observable.just("").delay(1, TimeUnit.SECONDS).
//          observeOn(AndroidSchedulers.mainThread())
//          .subscribe(v -> {
//            binding.contentsListViewRefresh.setRefreshing(false);
//            contentsListAdapter.notifyDataSetChanged();
//          });

        });
        binding.contentsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int moveY;
            boolean showEvent = false;
            boolean hideEvent = false;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                ContentsContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
//        if (onFloatingActionButtonListener != null) {
//          if (dy < 0) {
//            if (moveY > 0) {
//              moveY = 0;
//            }
//            hideEvent = false;
//            moveY += dy;
//          } else {
//            if (moveY < 0) {
//              moveY = 0;
//            }
//            showEvent = false;
//            moveY += dy;
//          }
//          if (moveY > 300 && !hideEvent) {
//            hideEvent = true;
//            onFloatingActionButtonListener.showFloatingActionButton(false);
//          } else if (moveY < -300 && !showEvent) {
//            showEvent = true;
//            onFloatingActionButtonListener.showFloatingActionButton(true);
//          }
//        }

                if (dy > 0 && !isLoading && linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                    isLoading = true;

                    viewModel.getPostList(linearLayoutManager.getItemCount(), viewModel.getClubId(), null, receivedData -> {
                        contentsListAdapter.setMorePostList(receivedData.getData());
                    }, errorData -> {
                    });
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        contentsListAdapter.refreshWriteDate();
    }

    public void changeListViewHolderType() {
        viewModel.changeListViewHolderType();
    }

    public void setOnFloatingActionButtonListener(OnFloatingActionButtonListener onFloatingActionButtonListener) {
        this.onFloatingActionButtonListener = onFloatingActionButtonListener;
    }

    public interface OnFloatingActionButtonListener {

        void showFloatingActionButton(boolean visible);
    }
}