package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.post.base.BaseViewModel;

import static com.dk.project.post.base.Define.CLUB_MODEL;

/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateGameViewModel extends BaseViewModel {


    private ClubModel clubModel;

    public CreateGameViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        clubModel = mContext.getIntent().getParcelableExtra(CLUB_MODEL);
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
}
