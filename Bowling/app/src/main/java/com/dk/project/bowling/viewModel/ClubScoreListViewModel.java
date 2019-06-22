package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.bowling.model.ReadGameModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;

import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubScoreListViewModel extends BaseViewModel {


    private ClubModel clubModel;

    private MutableLiveData<Pair<ArrayList<ReadGameModel>, Boolean>> gameMutableLiveData = new MutableLiveData<>();

    public ClubScoreListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        clubModel = getBindFragment().getArguments().getParcelable(Define.CLUB_MODEL);
        setGameList(0);
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

    public void setGameList(int count) {

        BowlingApi.getInstance().getGameAndScoreList(clubModel.getClubId(), count, receivedData ->
                gameMutableLiveData.setValue(new Pair<ArrayList<ReadGameModel>,Boolean>(receivedData.getData(),true)), errorData ->
                Toast.makeText(mContext, "게임 목록 에러", Toast.LENGTH_SHORT).show());
    }
}
