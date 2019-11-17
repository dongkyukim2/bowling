package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.dk.project.bowling.R;
import com.dk.project.bowling.ui.activity.CreateClubActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.controller.ListController;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.retrofit.ProgressRequestBody;
import com.dk.project.post.ui.activity.MediaSelectActivity;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.PermissionsUtil;
import com.dk.project.post.utils.RxBus;

/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateClubViewModel extends BaseViewModel {

    private int defaultImageIndex = 1;

    private boolean guardRequest;

    public CreateClubViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        GlideApp.with(mContext).load(R.drawable.team_default_1)
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
                if (guardRequest) {
                    return;
                }
                guardRequest = true;
                ClubModel clubModel = new ClubModel();
                clubModel.setClubTitle(((CreateClubActivity) mContext).getBinding().clubTitleTextView.getText().toString());
                clubModel.setClubInfo(((CreateClubActivity) mContext).getBinding().clubSubTitleTextView.getText().toString());

                if (ListController.getInstance().getMediaSelectList().isEmpty()) {
                    createClub(clubModel, String.valueOf(defaultImageIndex));
                } else {
                    PostApi.getInstance().test(mContext, ListController.getInstance().getMediaSelectList(), receivedData -> {
                        createClub(clubModel, receivedData.get(0));
                    }, errorData -> {
                        guardRequest = false;
                    }, new ProgressRequestBody.ProgressListener() {
                        @Override
                        public void onUploadStart(String fileName) {

                        }

                        @Override
                        public void onRequestProgress(long bytesWritten, long contentLength) {

                        }

                        @Override
                        public void onUploadEnd(String fileName) {

                        }
                    });
                }
                break;
            case R.id.image_attach:
                PermissionsUtil.isPermission(mContext, granted -> {
                    if (granted) {
                        Intent intent = new Intent(mContext, MediaSelectActivity.class);
                        intent.putExtra(Define.IMAGE_MULTI_SELECT, false);
                        mContext.startActivityForResult(intent, Define.MEDIA_ATTACH_LIST);
                    }
                });
                break;
        }
    }

    private void createClub(ClubModel clubModel, String filePath) {
        clubModel.setClubImage(filePath);
        BowlingApi.getInstance().createClub(clubModel, receivedData -> {
            RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
            mContext.finish();
            guardRequest = false;
        }, errorData -> {
            Toast.makeText(mContext, "클럽 만들기 실패", Toast.LENGTH_LONG).show();
            guardRequest = false;
        });
    }
}
