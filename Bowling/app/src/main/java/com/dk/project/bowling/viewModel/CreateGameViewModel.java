package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.bowling.model.ReadGameModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;

import static com.dk.project.post.base.Define.CLUB_MODEL;

/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateGameViewModel extends BaseViewModel {


    private ClubModel clubModel;
    private boolean readMode;
    private ReadGameModel readGameModel;

    public CreateGameViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    protected void onCreate() {
        super.onCreate();

        clubModel = mContext.getIntent().getParcelableExtra(CLUB_MODEL);
        readGameModel = mContext.getIntent().getParcelableExtra(Define.READ_GAME_MODEL);
        readMode = readGameModel != null;


        if (readMode) {
            BowlingApi.getInstance().getGameAndScoreList(readGameModel.getGameId(), receivedData -> {
                System.out.println();
            }, errorData -> {
                System.out.println();
            });
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

    public boolean isReadMode() {
        return readMode;
    }

    public ReadGameModel getReadGameModel() {
        return readGameModel;
    }
}
