package com.dk.project.post.ui.activity;

import androidx.lifecycle.ViewModelProvider;

import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityLoginBinding;
import com.dk.project.post.viewModel.LoginViewModel;


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
}
