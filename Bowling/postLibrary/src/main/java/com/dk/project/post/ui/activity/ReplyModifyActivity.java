package com.dk.project.post.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.base.Define;
import com.dk.project.post.databinding.ActivityReplyModifyBinding;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ScreenUtil;
import com.dk.project.post.utils.ToastUtil;
import com.dk.project.post.viewModel.ReplyModifyViewModel;

public class ReplyModifyActivity extends BindActivity<ActivityReplyModifyBinding, ReplyModifyViewModel> {

    private ReplyModel replyModel;
    private AlertDialog alertDialog;
    private boolean requestWriteReply = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reply_modify;
    }

    @Override
    protected ReplyModifyViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(ReplyModifyViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

        viewModel.executeRx(RxBus.getInstance().registerRxObserver(integerObjectPair -> {
            switch (integerObjectPair.first) {
                case Define.EVENT_ALREADY_DELETE_POST:
                    if (replyModel.getPostId().equalsIgnoreCase(integerObjectPair.second.toString())) {
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

        toolbarTitle.setText("댓글 수정");

        replyModel = getIntent().getParcelableExtra(REPLY_MODEL);
        binding.replyEditText.setText(replyModel.getReplyContents());

        toolbarRightButton.setVisibility(View.VISIBLE);

        toolbarRightButton.setTranslationY(-ScreenUtil.dpToPixel(56));
        toolbarRightButton.animate().translationY(0).setDuration(300).setStartDelay(300);

        toolbarTitle.setTranslationY(-ScreenUtil.dpToPixel(56));
        toolbarTitle.animate().translationY(0).setDuration(300).setStartDelay(300);
    }

    @Override
    public void onToolbarRightClick() {

        if (requestWriteReply) {
            ToastUtil.showWaitToastCenter(this);
            return;
        }
        requestWriteReply = true;

        replyModel.setReplyContents(binding.replyEditText.getText().toString().trim());

        viewModel.executeRx(PostApi.getInstance().writeReply(replyModel, receivedData -> {
                    dismissAlertDialog();
                    Intent intent = new Intent();
                    intent.putExtra(REPLY_MODEL, replyModel);
                    setResult(RESULT_OK, intent);
                    finish();
                }, postId -> {
                    RxBus.getInstance().eventPost(new Pair(Define.EVENT_ALREADY_DELETE_POST, postId));
                    requestWriteReply = false;
                },
                errorData -> {
                    dismissAlertDialog();
                    requestWriteReply = false;
                }));
    }

    @Override
    public void onBackPressed() {
        AlertDialogUtil.showAlertDialog(this, null, "댓글 수정을 취소하시겠습니까?", (dialog, which) -> finish(), (dialog, which) -> {
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void dismissAlertDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
