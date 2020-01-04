package com.dk.project.post.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MediatorLiveData;

import com.dk.project.post.R;
import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.retrofit.ResponseModel;
import com.dk.project.post.retrofit.SuccessCallback;
import com.dk.project.post.ui.activity.ReplyMoreActivity;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.TextViewUtil;
import com.dk.project.post.utils.YoutubeUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static com.dk.project.post.base.Define.EVENT_POST_REFRESH_MODIFY;
import static com.dk.project.post.base.Define.IMAGE_DIVIDER;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ReadViewModel extends BaseViewModel {

    private PostModel postModel;
    private final MediatorLiveData<ArrayList<ReplyModel>> replyItemList;
    private final MediatorLiveData<String> replyCountLiveData;
    private final MediatorLiveData<ReplyModel> replyItemDelete;
    private final MediatorLiveData<PostModel> likeCountLiveData;
    private SuccessCallback<ResponseModel<ArrayList<ReplyModel>>> replyModelListCallback;

    private PublishSubject<Boolean> likeClickSubject;

    private RelativeLayout postLayout;

    public ReadViewModel(@NonNull Application application) {
        super(application);
        replyItemList = new MediatorLiveData<>();
        replyCountLiveData = new MediatorLiveData<>();
        replyItemDelete = new MediatorLiveData<>();
        likeCountLiveData = new MediatorLiveData<>();
        replyItemList.setValue(new ArrayList<>());
        executeRx(RxBus.getInstance().registerRxObserver(pair -> {
            switch (pair.first) {
                case EVENT_POST_REFRESH_MODIFY:
                    PostModel modifyPostModel = (PostModel) pair.second;
                    if (TextUtils.equals(modifyPostModel.getPostId(), postModel.getPostId())) {
                        //todo 여기부터
                        int count = postLayout.getChildCount();
                        postLayout.removeViews(0, count - 2);
                        setReadPost(postLayout, modifyPostModel);
                    }
                    break;
            }
        }));
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        setLikeClickSubject();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getReplyList(0);
        setReplyCount();
    }

    @Override
    public void onThrottleClick(View view) {
        if (!LoginManager.getInstance().isLogIn()) {
            AlertDialogUtil.showLoginAlertDialog(mContext);
            return;
        }
        int id = view.getId();
        if (id == R.id.reply_more) {

            Intent intent = new Intent(mContext, ReplyMoreActivity.class);
            intent.putExtra(Define.POST_MODEL, postModel);
            mContext.startActivity(intent);

        } else if (id == R.id.like_image_view) {

            if (postModel.isLikeSelected()) {
                postModel.subLikeCount();
                postModel.setLikeSelected(false);
            } else {
                postModel.sumLikeCount();
                postModel.setLikeSelected(true);
            }

            likeCountLiveData.setValue(postModel);
            likeClickSubject.onNext(postModel.isLikeSelected());


        } else if (id == R.id.reply_send_parent) {
            AppCompatEditText editText = ((AppCompatEditText) ((ViewGroup) view.getParent()).getChildAt(1));

            if (editText.getText().toString().trim().isEmpty()) {
                Toast.makeText(mContext, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            executeRx(PostApi.getInstance().writeReply(new ReplyModel(postModel.getPostId(), editText.getText().toString().trim()),
                    getReplyModelListCallback(),
                    errorData -> {
                    }));
            editText.setText("");
        }
    }

    @BindingAdapter({"setReadPost"})
    public static void setReadPost(RelativeLayout layout, PostModel postModel) {
        Context context = layout.getContext();
        String inputString = postModel.getInputText();
        String tempString;
        int imageIndex = 0;
        int startIndex = 0;
        int endIndex;

        int addViewIndex = 0;

        while (true) {
            endIndex = inputString.indexOf(IMAGE_DIVIDER, startIndex);
            if (endIndex < 0) {
                tempString = inputString.substring(startIndex).trim();
                if (!TextUtils.isEmpty(tempString)) {
                    layout.addView(TextViewUtil.getTextView(context, tempString), addViewIndex++);
                }
                break;
            }
            tempString = inputString.substring(startIndex, endIndex).trim();
            startIndex = endIndex + IMAGE_DIVIDER.length();
            if (!TextUtils.isEmpty(tempString)) {
                layout.addView(TextViewUtil.getTextView(context, tempString), addViewIndex++);
            }

            MediaSelectListModel model = postModel.getImageList().get(imageIndex);
            if (!TextUtils.isEmpty(model.getYoutubeUrl())) {
                LinearLayout linearLayout = YoutubeUtil.setYoutubeFragment((BaseActivity) context, model.getYoutubeUrl(), null, true);
//                linearLayout.setTag(FIX_POSITION);
                layout.addView(linearLayout, addViewIndex++);
            } else {
                int[] size = ImageUtil.getResizeWidthHeight(context, model.getConvertWidth(), model.getConvertHeight());
                if (model.isGif() || model.isWebp()) {
                    layout.addView(ImageUtil.getImageGifThumbnail(context, size[0], size[1], model.getFilePath(), false, null), addViewIndex++);
                } else {
                    layout.addView(ImageUtil.getImageViewThumbnail(context, size[0], size[1], model.getFilePath(), null), addViewIndex++);
                }
            }
            imageIndex++;
        }

        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            view.setId(View.generateViewId());
            if (i > 0) {
                View prevView = layout.getChildAt(i - 1);
                int prevViewId = prevView.getId();
                RelativeLayout.LayoutParams relativeLayout = (RelativeLayout.LayoutParams) view.getLayoutParams();
                relativeLayout.addRule(RelativeLayout.BELOW, prevViewId);
                view.invalidate();

                View preView = layout.getChildAt(i - 1);  // 이미지 뷰 공백 처리
                if (preView instanceof LinearLayout && view instanceof LinearLayout) {
                    ((LinearLayout) view).getChildAt(0).setVisibility(View.VISIBLE);
                }
            } else {
                if (view instanceof TextView) {
                    int padding = ScreenUtil.dpToPixel(8);
                    view.setPadding(padding + padding / 2, 0, padding + padding / 2, padding);
                }
            }
        }

//        View fixPositionView = layout.findViewWithTag(FIX_POSITION);
//        if (fixPositionView != null) {
//            fixPositionView.bringToFront();
//            fixPositionView.invalidate();
//        }
    }

    public void setPostModel(PostModel postModel) {
        this.postModel = postModel;
    }

    public PostModel getPostModel() {
        return postModel;
    }

    private void getReplyList(int page) {
        executeRx(PostApi.getInstance().getReplyList(page, postModel.getPostId(),
                getReplyModelListCallback(),
                errorData -> {
                }));
    }

    private void setLikeClickSubject() {
        likeClickSubject = PublishSubject.create();
        executeRx(likeClickSubject.map(aBoolean -> {
            return aBoolean;
        }).debounce(1, TimeUnit.SECONDS).
                flatMap(aBoolean -> PostApi.getInstance().getLikeObservable(postModel.getPostId(), aBoolean)).
                observeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(responseModel -> {
                        },
                        throwable -> {
                        }));
    }

    private SuccessCallback<ResponseModel<ArrayList<ReplyModel>>> getReplyModelListCallback() {
        if (replyModelListCallback == null) {
            replyModelListCallback = receivedData -> {
                replyItemList.setValue(receivedData.getData());
                setReplyCount();
            };
        }
        return replyModelListCallback;
    }

    public void setPostLayout(RelativeLayout postLayout) {
        this.postLayout = postLayout;
    }

    private void setReplyCount() {
        executeRx(PostApi.getInstance().getReplyCount(postModel.getPostId(), receivedReplyCount -> {
            int replyCount = (int) ((double) new Gson().fromJson(receivedReplyCount.getData().toString(), HashMap.class).get("replyCount"));
            replyCountLiveData.setValue(String.valueOf(replyCount));
        }, errorData -> Toast.makeText(mContext, "댓글 갯수 실패", Toast.LENGTH_SHORT).show()));
    }

    public MediatorLiveData<ArrayList<ReplyModel>> getReplyItemList() {
        return replyItemList;
    }

    public MediatorLiveData<ReplyModel> getReplyItemDelete() {
        return replyItemDelete;
    }

    public MediatorLiveData<String> getReplyCountLiveData() {
        return replyCountLiveData;
    }

    public MediatorLiveData<PostModel> getLikeCountLiveData() {
        return likeCountLiveData;
    }
}
