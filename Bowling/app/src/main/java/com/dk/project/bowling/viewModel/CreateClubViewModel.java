package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.CreateClubActivity;
import com.dk.project.bowling.utils;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.RxBus;

/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateClubViewModel extends BaseViewModel {

    private int defaultImageIndex;

    public CreateClubViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        defaultImageIndex = utils.getDefaultImageIndex();

        GlideApp.with(mContext).load(utils.getDefaultImage(defaultImageIndex))
                .into(((CreateClubActivity) mContext).getBinding().clubTitleImageView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

        switch (view.getId()) {
            case R.id.create_club_btn:

                ClubModel clubModel = new ClubModel();
                clubModel.setClubTitle(((CreateClubActivity) mContext).getBinding().clubTitleTextView.getText().toString());
                clubModel.setClubInfo(((CreateClubActivity) mContext).getBinding().clubSubTitleTextView.getText().toString());
                clubModel.setClubImage(String.valueOf(defaultImageIndex));

                BowlingApi.getInstance().createClub(clubModel, receivedData -> {
                    RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
                    mContext.finish();
                }, errorData -> Toast.makeText(mContext, "클럽 만들기 실패", Toast.LENGTH_LONG).show());
                break;
        }

    }
}
