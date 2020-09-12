package com.dk.project.post.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.databinding.ActivityLoginInfoBinding;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.viewModel.LoginInfoViewModel;
import com.kakao.sdk.user.UserApiClient;

public class LoginInfoActivity extends BindActivity<ActivityLoginInfoBinding, LoginInfoViewModel> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_info;
    }

    @Override
    protected LoginInfoViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(LoginInfoViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setViewModel(viewModel);
        toolbarLeftButton.setVisibility(View.VISIBLE);
        if (LoginManager.getInstance().isLogIn()) {
            binding.signUpBtnText.setText("수 정 완 료");
        }
    }

    @Override
    public void onToolbarLeftClick() {
        super.onToolbarLeftClick();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

        String msg = "가입을 취소하겠습니까?";
        if (LoginManager.getInstance().isLogIn()) {
            msg = "수정을 취소하겠습니까?";
        }
        AlertDialogUtil.showAlertDialog(this, null, msg, (dialog, which) -> {
            UserApiClient.getInstance().unlink(throwable -> {
                finish();
                return null;
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case MEDIA_ATTACH_LIST:
                GlideApp.with(this).applyDefaultRequestOptions(ImageUtil.getGlideRequestOption())
                        .load(ListController.getInstance().getMediaSelectList().get(0).getFilePath())
                        .centerCrop()
                        .into(binding.userProfileImage);

                break;
        }
    }
}