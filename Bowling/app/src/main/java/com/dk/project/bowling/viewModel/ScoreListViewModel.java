package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.manager.LoginManager;

import java.util.ArrayList;

import static com.dk.project.post.base.Define.SCORE_DATE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ScoreListViewModel extends BaseViewModel {

    private String scoreDate;
    private final MutableLiveData<ArrayList<ScoreModel>> scoreDayListLiveData = new MutableLiveData<>();

    public ScoreListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        scoreDate = mContext.getIntent().getStringExtra(SCORE_DATE);
        BowlingApi.getInstance().getScoreDayList(LoginManager.getInstance().getUserCode(), scoreDate,
                receivedData -> {
                    if (receivedData.getCode().equals("0000")) {
                        scoreDayListLiveData.setValue(receivedData.getData());
                    } else {

                    }
                }, errorData -> {

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public MutableLiveData<ArrayList<ScoreModel>> getScoreDayListLiveData() {
        return scoreDayListLiveData;
    }
}
