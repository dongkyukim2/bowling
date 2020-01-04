package com.dk.project.post.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityLoginInfoBinding;
import com.dk.project.post.utils.AlertDialogUtil;
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
}