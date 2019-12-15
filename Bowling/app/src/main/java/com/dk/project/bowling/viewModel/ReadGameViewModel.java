package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.model.LoginInfoModel;

import java.util.ArrayList;


public class ReadGameViewModel extends BaseViewModel {

    private ReadGameModel readGameModel;

    private MutableLiveData<ArrayList<LoginInfoModel>> gameUserLiveData;

    public ReadGameViewModel(@NonNull Application application) {
        super(application);
        gameUserLiveData = new MutableLiveData<>();
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        readGameModel = mContext.getIntent().getParcelableExtra(Define.READ_GAME_MODEL);

        BowlingApi.getInstance().getGameUserList(readGameModel.getGameId(),
                receivedData -> gameUserLiveData.setValue(receivedData.getData()),
                errorData -> {
                });
    }

    @Override
    public void onThrottleClick(View view) {
    }

    public ReadGameModel getReadGameModel() {
        return readGameModel;
    }


    public MutableLiveData<ArrayList<LoginInfoModel>> getGameUserLiveData() {
        return gameUserLiveData;
    }
}
