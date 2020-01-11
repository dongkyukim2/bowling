package com.dk.project.post.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityReplyMoreBinding;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.ui.adapter.ReplyAdapter;
import com.dk.project.post.viewModel.ReplyMoreViewModel;

public class ReplyMoreActivity extends BindActivity<ActivityReplyMoreBinding, ReplyMoreViewModel> {


    private ReplyAdapter replyAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PostModel postModel;
    private boolean isLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply_more;
    }

    @Override
    protected ReplyMoreViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(ReplyMoreViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postModel = getIntent().getParcelableExtra(POST_MODEL);

        replyAdapter = new ReplyAdapter(this, true, null);

        linearLayoutManager = new LinearLayoutManager(this);
        binding.replyMoreRecycler.setLayoutManager(linearLayoutManager);
        binding.replyMoreRecycler.setAdapter(replyAdapter);

        binding.replyMoreRecycler.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && !isLoading && linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                    isLoading = true;
                    PostApi.getInstance().getReplyList(linearLayoutManager.getItemCount(), postModel.getPostId(), receivedData -> {
                        replyAdapter.setReplyList(receivedData.getData(), false);
                        isLoading = false;
                    }, errorData -> {
                        isLoading = false;
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PostApi.getInstance()
                .getReplyList(0, postModel.getPostId(), receivedData -> replyAdapter.setReplyList(receivedData.getData(), true), errorData -> {
                });
    }
}
