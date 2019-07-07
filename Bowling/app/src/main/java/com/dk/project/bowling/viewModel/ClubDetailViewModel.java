package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import androidx.annotation.NonNull;
import com.dk.project.bowling.model.ClubModel;
import com.dk.project.post.base.BaseViewModel;

import static com.dk.project.post.base.Define.CLUB_MODEL;
import static com.dk.project.post.base.Define.CLUB_SIGN;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubDetailViewModel extends BaseViewModel {

    private ClubModel clubModel;
    private boolean isSign;

    public ClubDetailViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        clubModel = mContext.getIntent().getParcelableExtra(CLUB_MODEL);
        isSign = mContext.getIntent().getBooleanExtra(CLUB_SIGN,false);
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

    public boolean isSign() {
        return isSign;
    }
}
