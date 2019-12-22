package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;

import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubScoreListViewModel extends BaseViewModel {

    private ClubModel clubModel;
    private MutableLiveData<Pair<ArrayList<ReadGameModel>, Boolean>> gameMutableLiveData = new MutableLiveData<>();
    private boolean isLoading;

    public ClubScoreListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        clubModel = getBindFragment().getArguments().getParcelable(Define.CLUB_MODEL);
        requestGameList(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public MutableLiveData<Pair<ArrayList<ReadGameModel>, Boolean>> getGameMutableLiveData() {
        return gameMutableLiveData;
    }

    public void requestGameList(int count) {
        BowlingApi.getInstance().getGameList(clubModel.getClubId(), count, receivedData -> {
            if (!receivedData.getData().isEmpty()) {
                gameMutableLiveData.setValue(new Pair<>(receivedData.getData(), count == 0));
            } else {
                gameMutableLiveData.setValue(new Pair<>(new ArrayList<>(), count == 0));
            }
            isLoading = false;
        }, errorData -> isLoading = false);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
