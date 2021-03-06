package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.utils.RxBus;

import java.util.HashMap;
import java.util.List;

import static com.dk.project.post.base.Define.CLUB_MODEL;
import static com.dk.project.post.base.Define.READ_GAME_MODEL;
import static com.dk.project.post.base.Define.SELECTED_USER_MAP;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubUserListViewModel extends BaseViewModel {

    private ClubModel clubModel;
    private ReadGameModel readGameModel;
    private boolean selectMode;
    private HashMap<String, Boolean> userMap;

    private MutableLiveData<List<ScoreClubUserModel>> userListLiveData = new MutableLiveData();

    public ClubUserListViewModel(@NonNull Application application) {
        super(application);

        RxBus.getInstance().registerRxObserver(integerObjectPair -> {
            switch (integerObjectPair.first) {
                case Define.EVENT_REFRESH_CLUB_USER_LIST:
                    getUserList();
                    break;
            }
        });

    }

    @Override
    protected void onCreate() {
        super.onCreate();

        Intent intent = mContext.getIntent();

        clubModel = intent.getParcelableExtra(CLUB_MODEL);
        readGameModel = intent.getParcelableExtra(READ_GAME_MODEL);
        if (mContext.getIntent().hasExtra(SELECTED_USER_MAP)) {
            userMap = (HashMap<String, Boolean>) intent.getSerializableExtra(SELECTED_USER_MAP);
            selectMode = true;
        } else {
            selectMode = false;
        }
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        getUserList();
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

    public MutableLiveData<List<ScoreClubUserModel>> getUserListLiveData() {
        return userListLiveData;
    }

    public HashMap<String, Boolean> getSelectedUserMap() {
        return userMap;
    }

    public boolean isSelectMode() {
        return selectMode;
    }

    private void getUserList() {
        String clubId;
        if (clubModel != null) {
            clubId = clubModel.getClubId();
        } else {
            clubId = readGameModel.getClubId();
        }
        BowlingApi.getInstance().getClubUserList(clubId,
                receivedData -> userListLiveData.setValue(receivedData.getData()),
                errorData -> Toast.makeText(mContext, "유저 목록 가져오기 실패", Toast.LENGTH_SHORT).show());
    }
}
