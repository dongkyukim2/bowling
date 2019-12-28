package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.dk.project.bowling.shareData.ShareData;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;

import java.util.ArrayList;


/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateGameViewModel extends BaseViewModel {


    private ClubModel clubModel;
    private String gameName;
    private String gameId;
    private String playDateTime;

    private int adapterMode;
    private ArrayList<ScoreClubUserModel> gameScoreList;


    public CreateGameViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    protected void onCreate() {
        super.onCreate();

        clubModel = mContext.getIntent().getParcelableExtra(Define.CLUB_MODEL);


        ReadGameModel readGameModel = mContext.getIntent().getParcelableExtra(Define.READ_GAME_MODEL);
        if (clubModel == null && readGameModel != null) { // 수정할때
            clubModel = new ClubModel();
            clubModel.setClubId(readGameModel.getClubId());
            gameName = readGameModel.getGameName();
            gameId = readGameModel.getGameId();
            playDateTime = readGameModel.getPlayDateTime();
        }

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

    public void setClubModel(ClubModel clubModel) {
        this.clubModel = clubModel;
    }

    public int getAdapterMode() {
        return adapterMode;
    }

    public void setAdapterMode(int adapterMode) {
        this.adapterMode = adapterMode;
    }

    public ArrayList<ScoreClubUserModel> getGameScoreList() {
        return gameScoreList;
    }

    public void setGameScoreList(ArrayList<ScoreClubUserModel> gameScoreList) {
        this.gameScoreList = gameScoreList;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayDateTime() {
        return playDateTime;
    }
}
