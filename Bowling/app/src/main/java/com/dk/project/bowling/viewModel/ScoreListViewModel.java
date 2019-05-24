package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.post.base.BaseViewModel;

import static com.dk.project.post.base.Define.SCORE_DATE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ScoreListViewModel extends BaseViewModel {

    private String scoreDate;

    public ScoreListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        scoreDate = mContext.getIntent().getStringExtra(SCORE_DATE);
        System.out.println("++++++++++++++++++ 000    "+scoreDate);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }
}
