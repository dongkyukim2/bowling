package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

    private MutableLiveData<ArrayList<ReadGameModel>> gameMutableLiveData = new MutableLiveData<>();

    public ClubScoreListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        clubModel = getBindFragment().getArguments().getParcelable(Define.CLUB_MODEL);

        BowlingApi.getInstance().getGameAndScoreList(clubModel.getClubId(), 0, receivedData ->
                gameMutableLiveData.setValue(receivedData.getData()), errorData ->
                Toast.makeText(mContext, "게임 목록 에러", Toast.LENGTH_SHORT).show());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public MutableLiveData<ArrayList<ReadGameModel>> getGameMutableLiveData() {
        return gameMutableLiveData;
    }
}
