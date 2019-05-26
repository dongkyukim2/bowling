package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.post.base.BaseViewModel;

import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubViewModel extends BaseViewModel {


    private MutableLiveData<ArrayList<ClubModel>> clubListLiveData = new MutableLiveData<>();

    public ClubViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        BowlingApi.getInstance().getSignUpClubList(receivedData -> clubListLiveData.setValue(receivedData.getData()),
                errorData -> {

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public MutableLiveData<ArrayList<ClubModel>> getClubListLiveData() {
        return clubListLiveData;
    }
}
