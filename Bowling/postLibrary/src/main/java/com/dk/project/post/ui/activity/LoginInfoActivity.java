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
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.viewModel.LoginInfoViewModel;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

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
    }

    @Override
    public void onToolbarLeftClick() {
        super.onToolbarLeftClick();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AlertDialogUtil.showAlertDialog(this, null, "가입을 취소하겠습니까?", (dialog, which) -> {
            UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    finish();
                }
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