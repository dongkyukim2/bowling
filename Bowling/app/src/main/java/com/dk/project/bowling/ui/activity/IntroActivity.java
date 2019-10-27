package com.dk.project.bowling.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.crashlytics.android.Crashlytics;
import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ActivityIntroBinding;
import com.dk.project.bowling.viewModel.IntroViewModel;
import com.dk.project.post.base.BindActivity;

import io.fabric.sdk.android.Fabric;

public class IntroActivity extends BindActivity<ActivityIntroBinding, IntroViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_intro;
    }

    @Override
    protected IntroViewModel getViewModel() {
        return new ViewModelProvider(getViewModelStore(), getDefaultViewModelProviderFactory()).get(IntroViewModel.class);
    }

    @Override
    protected void subscribeToModel() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
    }
}
