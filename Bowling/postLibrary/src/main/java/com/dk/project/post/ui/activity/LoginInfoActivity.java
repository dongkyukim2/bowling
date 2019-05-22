package com.dk.project.post.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import com.dk.project.post.R;
import com.dk.project.post.base.BindActivity;
import com.dk.project.post.databinding.ActivityLoginInfoBinding;
import com.dk.project.post.viewModel.LoginViewModel;

public class LoginInfoActivity extends BindActivity<ActivityLoginInfoBinding, LoginViewModel> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_info;
    }

    @Override
    protected LoginViewModel getViewModel() {
        return ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setViewModel(viewModel);
    }
}
