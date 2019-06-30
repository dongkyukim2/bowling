package com.dk.project.bowling.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
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
        return ViewModelProviders.of(this).get(IntroViewModel.class);
    }

    @Override
    protected void subscribeToModel() {


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        toolbarLeftButton.setColorFilter(Color.BLUE);
        super.onCreate(savedInstanceState);
    }
}
