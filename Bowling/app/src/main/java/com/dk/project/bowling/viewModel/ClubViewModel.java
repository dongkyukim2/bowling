package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.ClubSearchActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.manager.LoginManager;
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
        if (LoginManager.getInstance().getLoginInfoModel() == null) {
            BowlingApi.getInstance().getRecommendClubList(receivedData -> {
                ResponseModel<ArrayList<ClubModel>> emptyModel = new ResponseModel<>();
                emptyModel.setCode("0000");
                emptyModel.setData(new ArrayList<>());
                clubListLiveData.setValue(new Pair(emptyModel, receivedData));
            }, errorData -> {
            });
        } else {
            getClubList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                mContext.startActivity(new Intent(mContext, ClubSearchActivity.class));
                break;
        }

    }

    public MutableLiveData<Pair<ResponseModel<ArrayList<ClubModel>>, ResponseModel<ArrayList<ClubModel>>>> getClubListLiveData() {
        return clubListLiveData;
    }

    public void getClubList() {
        BowlingApi.getInstance().getSignUpAndRecommendClubList(clubListLiveData::setValue, errorData -> {
        });
    }
}
