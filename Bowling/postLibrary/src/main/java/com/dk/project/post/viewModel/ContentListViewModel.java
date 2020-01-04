package com.dk.project.post.viewModel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Application;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.MediatorLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dk.project.post.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.ErrorCallback;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.retrofit.ResponseModel;
import com.dk.project.post.retrofit.SuccessCallback;
import com.dk.project.post.ui.activity.SearchContentsActivity;
import com.dk.project.post.ui.adapter.ContentsListAdapter;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.AnimationUtil;
import com.dk.project.post.utils.RxBus;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.dk.project.post.base.Define.EVENT_DELETE_POST;
import static com.dk.project.post.base.Define.EVENT_POST_REFRESH;
import static com.dk.project.post.base.Define.EVENT_POST_REFRESH_MODIFY;
import static com.dk.project.post.base.Define.EVENT_POST_REFRESH_REPLY_LIKE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ContentListViewModel extends BaseViewModel {

    private final MediatorLiveData<ArrayList<PostModel>> postItemList = new MediatorLiveData<>();

    private PublishSubject<PostModel> likeClickSubject;
    private boolean searchMode;
    private ContentsListAdapter recyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;

    private String clubId;

    public ContentListViewModel(@NonNull Application application) {
        super(application);
        executeRx(RxBus.getInstance().registerRxObserver(pair -> {
            ArrayList<PostModel> newPostList;
            switch (pair.first) {
                case EVENT_POST_REFRESH:
                    if (TextUtils.isEmpty(clubId) && pair.second == null) {
                        getPostList(0, null, null, receivedData -> postItemList.setValue(receivedData.getData()),
                                errorData -> {
                                });
                    }
                    if (!TextUtils.isEmpty(clubId) && pair.second != null && clubId.equalsIgnoreCase(pair.second.toString())) {
                        getPostList(0, clubId, null, receivedData -> postItemList.setValue(receivedData.getData()),
                                errorData -> {
                                });
                    }

                    break;
                case EVENT_POST_REFRESH_MODIFY:
                    PostModel modifyPostModel = (PostModel) pair.second;
                    newPostList = new ArrayList<>(postItemList.getValue());
                    for (int i = 0; i < newPostList.size(); i++) {
                        PostModel postModel = newPostList.get(i);
                        if (TextUtils.equals(postModel.getPostId(), modifyPostModel.getPostId())) {
                            Gson gson = new Gson(); // 원본 바뀌지 않게
                            String temp = gson.toJson(postModel);

                            PostModel tempPostModel = gson.fromJson(temp, PostModel.class);
                            tempPostModel.setInputText(modifyPostModel.getInputText());
                            tempPostModel.setImageList(modifyPostModel.getImageList());
                            tempPostModel.setReplyCount(modifyPostModel.getReplyCount());
                            tempPostModel.setReplyList(modifyPostModel.getReplyList());
                            tempPostModel.setLikeCount(modifyPostModel.getLikeCount());
                            tempPostModel.setLikeSelected(modifyPostModel.isLikeSelected());
                            newPostList.set(i, tempPostModel);
                            postItemList.setValue(newPostList);
                            break;
                        }
                    }
                    break;
                case EVENT_POST_REFRESH_REPLY_LIKE:
                    //갱신할때 깜박거려서 수동 갱신
                    PostModel replyRefreshModel = (PostModel) pair.second;
                    for (int position = 0; position < postItemList.getValue().size(); position++) {
                        PostModel postModel = postItemList.getValue().get(position);
                        if (replyRefreshModel.getPostId().equalsIgnoreCase(postModel.getPostId())) {

                            postModel.setReplyCount(replyRefreshModel.getReplyCount());
                            postModel.setLikeCount(replyRefreshModel.getLikeCount());
                            postModel.setLikeSelected(replyRefreshModel.isLikeSelected());

//              if (postModel.isLikeSelected() != replyRefreshModel.isLikeSelected()) {
//                if (postModel.isLikeSelected()) {
//                  postModel.subLikeCount();
//                } else {
//                  postModel.sumLikeCount();
//                }
//                postModel.setLikeSelected(replyRefreshModel.isLikeSelected());
//              }

                            postModel.getReplyList().clear();
                            postModel.getReplyList().addAll(replyRefreshModel.getReplyList());
                            replyManualRefresh(position, replyRefreshModel);
                            break;
                        }
                    }
                    break;
                case EVENT_DELETE_POST:
                    PostModel deletePostModel = (PostModel) pair.second;
                    newPostList = new ArrayList<>(postItemList.getValue());
                    for (PostModel postModel : newPostList) {
                        if (TextUtils.equals(postModel.getPostId(), deletePostModel.getPostId())) {
                            newPostList.remove(postModel);
                            postItemList.setValue(newPostList);
                            break;
                        }
                    }
                    break;
            }
        }));
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        if (bindFragment.getArguments() != null) {
            clubId = bindFragment.getArguments().getString("POST_CLUB_ID");
        }

        searchMode = mContext instanceof SearchContentsActivity;
        if (!searchMode) {
            getPostList(0, clubId, null, receivedData -> postItemList.setValue(receivedData.getData()),
                    errorData -> {
                    });
        }
        setLikeClickSubject();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
        int id = view.getId();
        if (id == R.id.like_image_parent) {

        }
    }

    public void onLikeClick(AppCompatImageView imageView, TextView textView, PostModel postModel) {
        if (!LoginManager.getInstance().isLogIn()) {
            AlertDialogUtil.showLoginAlertDialog(imageView.getContext());
            return;
        }
        if (postModel.isLikeSelected()) {
            postModel.setLikeSelected(false);
            postModel.subLikeCount();
        } else {
            postModel.setLikeSelected(true);
            postModel.sumLikeCount();
        }


        textView.setText(String.valueOf(postModel.getLikeCount()));
        AnimationUtil.getLikeAnimation(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (postModel.isLikeSelected()) {
                    imageView.setImageResource(R.drawable.ic_heart_red);
                } else {
                    imageView.setImageResource(R.drawable.ic_heart_outline_grey);
                }
            }
        }, imageView);
        likeClickSubject.onNext(postModel);
    }

    private void setLikeClickSubject() {
        likeClickSubject = PublishSubject.create();
        executeRx(likeClickSubject.map(postModel -> {
            return postModel;
        }).debounce(1, TimeUnit.SECONDS).
                flatMap(postModel -> PostApi.getInstance().getLikeObservable(postModel.getPostId(), postModel.isLikeSelected())).
                observeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(responseModel -> {
                        },
                        throwable -> {
                        }));
    }

    public void getPostList(int page, String clubId, String search, SuccessCallback<ResponseModel<ArrayList<PostModel>>> callback, ErrorCallback errorCallback) {
        executeRx(PostApi.getInstance().getPostList(page, clubId, search, callback, errorCallback));
    }

    public void setRecyclerViewAdapterNmanager(ContentsListAdapter recyclerViewAdapter, LinearLayoutManager linearLayoutManager) {
        this.recyclerViewAdapter = recyclerViewAdapter;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void changeListViewHolderType() {
        if (recyclerViewAdapter != null) {
            recyclerViewAdapter.changeListViewHolderType();
        }
    }

    public MediatorLiveData<ArrayList<PostModel>> getPostItemList() {
        return postItemList;
    }

    //todo 이름, 사진, 시간, 댓글 수, 하트 수, 모두 수동갱신 해야함
    private void replyManualRefresh(int position, PostModel newPostModel) {
        View viewHolder = linearLayoutManager.findViewByPosition(position);

        TextView replyTextView = viewHolder.findViewById(R.id.reply_count_text);
        TextView likeCountTextView = viewHolder.findViewById(R.id.like_count_text);
        AppCompatImageView likeImageView = viewHolder.findViewById(R.id.like_image_view);

        replyTextView.setText(String.valueOf(newPostModel.getReplyCount()));
        likeCountTextView.setText(String.valueOf(newPostModel.getLikeCount()));

        if (newPostModel.isLikeSelected()) {
            likeImageView.setImageResource(R.drawable.ic_heart_red);
        } else {
            likeImageView.setImageResource(R.drawable.ic_heart_outline_grey);
        }
    }

    public String getClubId() {
        return clubId;
    }
}
