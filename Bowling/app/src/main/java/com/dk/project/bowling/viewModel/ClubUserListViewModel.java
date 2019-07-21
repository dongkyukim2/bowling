package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.UserModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dk.project.post.base.Define.CLUB_MODEL;
import static com.dk.project.post.base.Define.SELECTED_USER_MAP;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubUserListViewModel extends BaseViewModel {

    private ClubModel clubModel;
    private HashMap<String, Boolean> userMap;

    private MutableLiveData<ArrayList<UserModel>> userListLiveData = new MutableLiveData();

    public ClubUserListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        clubModel = mContext.getIntent().getParcelableExtra(CLUB_MODEL);
        userMap = (HashMap<String, Boolean>) mContext.getIntent().getSerializableExtra(SELECTED_USER_MAP);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        BowlingApi.getInstance().getClubUserList(clubModel.getClubId(),
                receivedData -> userListLiveData.setValue(receivedData.getData()),
                errorData -> Toast.makeText(mContext, "유저 목록 가져오기 실패", Toast.LENGTH_SHORT).show());
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

    public MutableLiveData<ArrayList<UserModel>> getUserListLiveData() {
        return userListLiveData;
    }

    public HashMap<String, Boolean> getSelectedUserMap() {
        return userMap;
    }
}
