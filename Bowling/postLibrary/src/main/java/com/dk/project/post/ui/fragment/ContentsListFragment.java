package com.dk.project.post.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.project.post.R;
import com.dk.project.post.base.BindFragment;
import com.dk.project.post.base.Define;
import com.dk.project.post.databinding.FragmentContentsListBinding;
import com.dk.project.post.manager.AppDatabase;
import com.dk.project.post.manager.DataBaseManager;
import com.dk.project.post.ui.adapter.ContentsListAdapter;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.viewModel.ContentListViewModel;

import java.util.HashMap;
import java.util.List;

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
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(ContentListViewModel.class);
    }

    @Override
    protected void registerLiveData() {
        viewModel.getPostItemList().observe(this, postModels -> {
            setEmptyView(postModels);
            contentsListAdapter.setClearPostList(postModels);
            binding.contentsListViewRefresh.setRefreshing(false);
        });

        viewModel.executeRx(RxBus.getInstance().registerRxObserver(busModel -> {
            switch (busModel.first) {
                case Define.EVENT_ALREADY_DELETE_POST:
                    contentsListAdapter.deletePost(busModel.second.toString());
                    break;
            }
        }));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.contentsListView.setLayoutManager(linearLayoutManager);
        contentsListAdapter = new ContentsListAdapter(mContext, viewModel);
        binding.contentsListView.setItemAnimator(new DefaultItemAnimator());
        binding.contentsListView.setAdapter(contentsListAdapter);
        viewModel.setRecyclerViewAdapterNmanager(contentsListAdapter, linearLayoutManager);

        binding.contentsListViewRefresh.setOnRefreshListener(() ->
                RxBus.getInstance().eventPost(new Pair(EVENT_POST_REFRESH, viewModel.getClubId())));

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

    private void setEmptyView(List list) {
        if (list.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.contentsListView.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.contentsListView.setVisibility(View.VISIBLE);
        }
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