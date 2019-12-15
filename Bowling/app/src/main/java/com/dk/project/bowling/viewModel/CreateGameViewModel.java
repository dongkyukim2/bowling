package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.dk.project.bowling.shareData.ShareData;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;

import java.util.ArrayList;

import static com.dk.project.post.base.Define.CLUB_MODEL;

/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateGameViewModel extends BaseViewModel {


    private ClubModel clubModel;
    private int adapterMode;
    private ArrayList<ScoreClubUserModel> gameScoreList;


    public CreateGameViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    protected void onCreate() {
        super.onCreate();

        clubModel = mContext.getIntent().getParcelableExtra(CLUB_MODEL);

        gameScoreList = new ArrayList<>(ShareData.getInstance().getScoreList());


        if (gameScoreList != null && !gameScoreList.isEmpty()) {
            adapterMode = Define.MODEFY_MODE;
        } else {
            adapterMode = Define.CREATE_MODE;
        }


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

    public ClubModel getClubModel() {
        return clubModel;
    }

    public int getAdapterMode() {
        return adapterMode;
    }


    public ArrayList<ScoreClubUserModel> getGameScoreList() {
        return gameScoreList;
    }

}
