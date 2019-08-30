package com.dk.project.bowling.ui.activity;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ActivityLoginBinding;
import com.dk.project.bowling.viewModel.LoginViewModel;
import com.dk.project.post.base.BindActivity;
import com.kakao.auth.Session;

public class LoginActivity extends BindActivity<ActivityLoginBinding, LoginViewModel> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(LoginViewModel.class);
    }

    @Override
    protected void subscribeToModel() {
        viewModel.kakaoLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
