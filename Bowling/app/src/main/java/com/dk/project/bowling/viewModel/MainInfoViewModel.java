package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.bowling.ui.activity.ScoreListActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.retrofit.ResponseModel;

import java.util.ArrayList;

import static com.dk.project.post.base.Define.SCORE_DATE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainInfoViewModel extends BaseViewModel {


    private final MutableLiveData<ResponseModel<ScoreModel>> recentAvgLiveData = new MutableLiveData<>();
    private final MutableLiveData<ResponseModel<ScoreModel>> monthAvgLiveData = new MutableLiveData<>();
    private final MutableLiveData<Pair<ResponseModel<ArrayList<ScoreModel>>, Boolean>> avgListLiveData =
            new MutableLiveData<>();

    public MainInfoViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public void getScoreDayList(ScoreModel scoreModel) {
        Intent intent = new Intent(mContext, ScoreListActivity.class);
        intent.putExtra(SCORE_DATE, scoreModel.getPlayDate());
        mContext.startActivity(intent);
    }


    public MutableLiveData<ResponseModel<ScoreModel>> getRecentAvgLiveData() {
        return recentAvgLiveData;
    }

    public MutableLiveData<ResponseModel<ScoreModel>> getMonthAvgLiveData() {
        return monthAvgLiveData;
    }

    public MutableLiveData<Pair<ResponseModel<ArrayList<ScoreModel>>, Boolean>> getAvgListLiveData() {
        return avgListLiveData;
    }
}
