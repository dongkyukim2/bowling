package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.ResponseModel;

import java.util.ArrayList;


public class ReadGameViewModel extends BaseViewModel {

    private ReadGameModel readGameModel;

    private MutableLiveData<Pair<ResponseModel<ArrayList<LoginInfoModel>>, ResponseModel<ReadGameModel>>> gameUserAndGameLiveData;

    public ReadGameViewModel(@NonNull Application application) {
        super(application);
        gameUserAndGameLiveData = new MutableLiveData<>();
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        readGameModel = mContext.getIntent().getParcelableExtra(Define.READ_GAME_MODEL);
        requestGameModel();
    }

    @Override
    public void onThrottleClick(View view) {
    }

    public ReadGameModel getReadGameModel() {
        return readGameModel;
    }

    public void setReadGameModel(ReadGameModel readGameModel) {
        this.readGameModel = readGameModel;
    }

    public MutableLiveData<Pair<ResponseModel<ArrayList<LoginInfoModel>>, ResponseModel<ReadGameModel>>> getGameUserAndGameLiveData() {
        return gameUserAndGameLiveData;
    }

    public void requestGameModel() {
        BowlingApi.getInstance().getGameAndUserList(readGameModel.getGameId(),
                receivedData -> gameUserAndGameLiveData.setValue(receivedData), errorData -> {
                });
    }
}
