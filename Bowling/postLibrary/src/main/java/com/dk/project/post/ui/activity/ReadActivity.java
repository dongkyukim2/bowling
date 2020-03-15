package com.dk.project.post.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.databinding.ActivityReadBinding;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.ui.adapter.ReplyAdapter;
import com.dk.project.post.ui.fragment.YoutubeFragment;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.AnimationUtil;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.ToastUtil;
import com.dk.project.post.utils.Utils;
import com.dk.project.post.viewModel.ReadViewModel;

public class ReadActivity extends BindActivity<ActivityReadBinding, ReadViewModel> implements NestedScrollView.OnScrollChangeListener {

    private ReplyAdapter replyAdapter;
    private int scrollPosition = -1;
    private int scrollY;
    private int oldScrollY;

    private float originalY = -1;
    private int scrollOffset;

    private boolean findYoutubeLayout = true;
    private LinearLayout youtubeView;
    private YoutubeFragment youtubeFragment;

    private int layoutMargin;
    private int toolbarHeight;
    private int userInfoHeight;

    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading;
    private boolean isReplyLast;

    private boolean requestDeletePost = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    protected ReadViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(ReadViewModel.class);
    }

    @Override
    protected void subscribeToModel() {
        viewModel.getReplyItemList().observe(this, replyModels -> {
            replyAdapter.setReplyList(replyModels, true);

            viewModel.getPostModel().getReplyList().clear();
            switch (replyModels.size()) {
                case 0:
                    break;
                case 1:
                case 2:
                    viewModel.getPostModel().getReplyList().addAll(replyModels);
                    break;
                default:
                    viewModel.getPostModel().getReplyList().addAll(replyModels.subList(0, 2));
                    break;
            }
        });


        viewModel.getReplyCountLiveData().observe(this, binding.replyCountText::setText);

        viewModel.getReplyItemDelete().observe(this, replyModel -> {
            replyAdapter.setDeleteReply(replyModel);
        });

        viewModel.getLikeCountLiveData().observe(this, postModel -> {
            binding.likeCountText.setText(String.valueOf(postModel.getLikeCount()));
            AnimationUtil.getLikeAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (postModel.isLikeSelected()) {
                        binding.likeImageView.setImageResource(R.drawable.ic_heart_red);
                    } else {
                        binding.likeImageView.setImageResource(R.drawable.ic_heart_outline_grey);
                    }
                }
            }, binding.likeImageView);
        });

        viewModel.executeRx(RxBus.getInstance().registerRxObserver(integerObjectPair -> {
            switch (integerObjectPair.first) {
                case Define.EVENT_ALREADY_DELETE_POST:
                    if (viewModel.getPostModel().getPostId().equalsIgnoreCase(integerObjectPair.second.toString())) {
                        ToastUtil.showToastCenter(this, "이미 삭제된 내용입니다.");
                        finish();
                    }
                    break;
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutMargin = ScreenUtil.dpToPixel(6);
        layoutMargin = 0;

        PostModel postModel = getIntent().getParcelableExtra(POST_MODEL);
        viewModel.setPostModel(postModel);

        viewModel.setPostLayout(binding.readContentLayout);

        binding.setBindingReadViewModel(viewModel);

        replyAdapter = new ReplyAdapter(this, false, viewModel);
//        bottomSheetReplyAdapter = new ReplyAdapter(this, true);

        linearLayoutManager = new LinearLayoutManager(this);

        binding.replyList.setLayoutManager(linearLayoutManager);
        binding.replyList.setAdapter(replyAdapter);

        binding.rootScrollView.setOnScrollChangeListener(this);
        binding.rootScrollView.post(() -> {
            binding.rootScrollView.scrollTo(0, scrollPosition);
            scrollPosition = -1;
        });

        binding.userName.setSelected(true);

        binding.writeDate.setText(Utils.converterDate(viewModel.getPostModel().getWriteDate()));

        if (!TextUtils.isEmpty(viewModel.getPostModel().getUserProfile())) {
            GlideApp.with(this).load(Define.IMAGE_URL + viewModel.getPostModel().getUserProfile())
                    .apply(ImageUtil.getGlideRequestOption().placeholder(R.drawable.user_profile))
                    .into(binding.userProfile);
        }


        binding.likeImageView.setImageResource(viewModel.getPostModel().isLikeSelected() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);

        binding.replyCountText.setText(String.valueOf(viewModel.getPostModel().getReplyCount()));

        binding.likeCountText.setText(String.valueOf(viewModel.getPostModel().getLikeCount()));

        if (LoginManager.getInstance().isPermissionUser(viewModel.getPostModel().getUserId()) == Define.OK) {
            binding.moreImageView.setVisibility(View.VISIBLE);
        }
        binding.moreImageView.setOnClickListener(view -> AlertDialogUtil.showBottomSheetDialog(this, v -> {
            if (v.getId() == R.id.btnModify) {
                Intent intent = new Intent(this, WriteActivity.class);
                intent.putExtra(POST_MODEL, viewModel.getPostModel());
                startActivityForResult(intent, MODIFY_POST);
            } else if (v.getId() == R.id.btnDelete) {
                if (requestDeletePost) {
                    ToastUtil.showWaitToastCenter(this);
                    return;
                }
                requestDeletePost = true;
                AlertDialogUtil.showAlertDialog(this, null, "삭제 하시겠습니까?", (dialog, which) ->
                                PostApi.getInstance().deletePost(viewModel.getPostModel().getPostId(), receivedData -> {
                                    if (receivedData.isSuccess()) {
                                        RxBus.getInstance().eventPost(new Pair(EVENT_DELETE_POST, receivedData.getData()));
                                        finish();
                                        requestDeletePost = false;
                                    } else {
                                        Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                        requestDeletePost = false;
                                    }
                                }, errorData -> {
                                    Toast.makeText(this, "삭제 실패", Toast.LENGTH_SHORT).show();
                                    requestDeletePost = false;
                                }),
                        (dialog, which) -> {
                            requestDeletePost = false;
                        });
            }
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case MODIFY_REPLY:
//        ReplyModel replyModel = data.getParcelableExtra(REPLY_MODEL);
//        ArrayList<ReplyModel> newReplyList = new ArrayList<>(viewModel.getReplyItemList().getValue());
//        for (int i = 0; i < newReplyList.size(); i++) {
//          ReplyModel model = newReplyList.get(i);
//          if (TextUtils.equals(replyModel.getReplyId(), model.getReplyId())) {
//            newReplyList.set(i, replyModel);
//            viewModel.getReplyItemList().setValue(newReplyList);
//            break;
//          }
//        }
                break;
            case MODIFY_POST:
//        finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        replyAdapter.unSubscribeFromObservable();

        String replyCount = viewModel.getReplyCountLiveData().getValue();
        viewModel.getPostModel().setReplyCount(Integer.parseInt(replyCount == null ? "0" : replyCount));
        RxBus.getInstance().eventPost(new Pair<>(EVENT_POST_REFRESH_REPLY_LIKE, viewModel.getPostModel()));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && scrollPosition < 0) {
            scrollPosition = (oldScrollY + scrollY) / 2;
        } else {
            if (scrollPosition > 0) {
                binding.rootScrollView.scrollTo(0, scrollPosition);
                scrollPosition = -1;
            }
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        /*
        ReadActivity.this.scrollY = scrollY;
        ReadActivity.this.oldScrollY = oldScrollY;

        if (findYoutubeLayout) {
            findYoutubeLayout = false;
            youtubeView = binding.readContentLayout.findViewWithTag(FIX_POSITION);
        }

        if (toolbarHeight < 1) {
            toolbarHeight = binding.includeToolbar.getHeight();
        }
        if (userInfoHeight < 1) {
            userInfoHeight = binding.userInfoParent.getHeight();
        }

        if (youtubeView == null) {
            return;
        }
        if (youtubeFragment == null) {
            youtubeFragment = (YoutubeFragment) getSupportFragmentManager().findFragmentByTag(YOUTUBE_FRAGMENT_TAG);
        }

        if (originalY < 0) {
            originalY = youtubeView.getY();
//            scrollOffset = binding.includeToolbar.getHeight() + ScreenUtil.dpToPixel(this, 10) + (youtubeView.getChildAt(0).getVisibility() == View.VISIBLE ? ScreenUtil.dpToPixel(this, 18) : 0);
            scrollOffset = (youtubeView.getChildAt(0).getVisibility() == View.VISIBLE ? ScreenUtil.dpToPixel(18) : 0) - 0;
        }

        if (scrollY > oldScrollY) {
            if (youtubeFragment.getPlayer() == null || !youtubeFragment.getPlayer().isPlaying()) {
                return;
            }
            if (scrollY >= originalY + scrollOffset + (toolbarHeight + userInfoHeight + layoutMargin)) {
                float moveDown = (scrollY - originalY) - scrollOffset - (toolbarHeight + userInfoHeight + layoutMargin);
                youtubeView.setTranslationY(moveDown);
            }
        } else {
            if (youtubeView.getTranslationY() > 0) {
                float moveUp = Math.max(0, youtubeView.getTranslationY() - (oldScrollY - scrollY));
                youtubeView.setTranslationY(moveUp);
            }
        }
        */
    }
}
