package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.retrofit.ResponseModel;

import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubViewModel extends BaseViewModel {

    private MutableLiveData<Pair<ResponseModel<ArrayList<ClubModel>>, ResponseModel<ArrayList<ClubModel>>>> clubListLiveData = new MutableLiveData<>();

    public ClubViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        getClubList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public MutableLiveData<Pair<ResponseModel<ArrayList<ClubModel>>, ResponseModel<ArrayList<ClubModel>>>> getClubListLiveData() {
        return clubListLiveData;
    }

    public void getClubList() {
        BowlingApi.getInstance().getSignUpAndRecommendClubList(clubListLiveData::setValue, errorData -> {
        });
    }
}
