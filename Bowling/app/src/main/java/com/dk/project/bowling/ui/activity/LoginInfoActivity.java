package com.dk.project.bowling.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ActivityLoginInfoBinding;
import com.dk.project.bowling.viewModel.LoginInfoViewModel;
import com.dk.project.post.base.BindActivity;
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

            }
        });
    }
}
