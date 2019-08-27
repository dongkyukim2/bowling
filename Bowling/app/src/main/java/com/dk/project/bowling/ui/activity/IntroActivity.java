package com.dk.project.bowling.ui.activity;

import androidx.lifecycle.ViewModelProvider;

import com.dk.project.bowling.R;
import com.dk.project.bowling.databinding.ActivityIntroBinding;
import com.dk.project.bowling.viewModel.IntroViewModel;
import com.dk.project.post.base.BindActivity;

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
}
