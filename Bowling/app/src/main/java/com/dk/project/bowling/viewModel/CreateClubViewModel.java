package com.dk.project.bowling.viewModel;

import android.app.Activity;
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
import com.dk.project.post.retrofit.CountingRequestBody;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.ui.activity.MediaSelectActivity;
import com.dk.project.post.utils.GlideApp;
import com.dk.project.post.utils.PermissionsUtil;
import com.dk.project.post.utils.ProgressUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ToastUtil;

/**
 * Created by dkkim on 2017-10-04.
 */

public class CreateClubViewModel extends BaseViewModel<CreateClubActivity> {

    private int defaultImageIndex = 1;

    private boolean guardRequest;

    // 수정할때는 널이 아님
    private ClubModel clubModel;

    public CreateClubViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        clubModel = mContext.getIntent().getParcelableExtra(Define.CLUB_MODEL);
        if (clubModel == null) {
            GlideApp.with(mContext).load(R.drawable.team_default_1).centerCrop().into(mContext.getBinding().clubTitleImageView);
        } else {
            GlideApp.with(mContext).load(clubModel.getClubImage().length() == 1 ? R.drawable.team_default_1 : Define.IMAGE_URL + clubModel.getClubImage()).centerCrop()
                    .into(mContext.getBinding().clubTitleImageView);
        }
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
                    ToastUtil.showWaitToastCenter(mContext);
                    return;
                }
                if (mContext.getBinding().clubTitleTextView.getText().toString().trim().isEmpty()) {
                    ToastUtil.showToastCenter(mContext, "제목을 입력해주세요.");
                    return;
                }
                if (mContext.getBinding().clubSubTitleTextView.getText().toString().trim().isEmpty()) {
                    ToastUtil.showToastCenter(mContext, "내용을 입력해주세요.");
                    return;
                }
                ProgressUtil.INSTANCE.showProgress(mContext);
                guardRequest = true;
                ClubModel clubModel = new ClubModel();
                clubModel.setClubTitle(mContext.getBinding().clubTitleTextView.getText().toString().trim());
                clubModel.setClubInfo(mContext.getBinding().clubSubTitleTextView.getText().toString().trim());

                if (ListController.getInstance().getMediaSelectList().isEmpty()) {
                    createClub(clubModel, String.valueOf(defaultImageIndex));
                } else {
                    executeRx(PostApi.getInstance().uploadFile(mContext, ListController.getInstance().getMediaSelectList(), receivedData -> {
                        createClub(clubModel, receivedData.get(0).getFilePath());
                    }, errorData -> {
                        ProgressUtil.INSTANCE.hideProgress();
                        guardRequest = false;
                    }, new CountingRequestBody.Listener() {
                        @Override
                        public void onUploadStart(String fileName) {

                        }

                        @Override
                        public void onRequestProgress(long bytesWritten, long contentLength) {
                        }

                        @Override
                        public void onUploadEnd(String fileName) {

                        }
                    }));
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
        if (this.clubModel == null) {
            clubModel.setClubImage(filePath);
        } else { // 클럽 수정
            clubModel.setClubImage(this.clubModel.getClubImage());
            if (filePath.length() < 2) {
                clubModel.setClubNewImage(this.clubModel.getClubImage());
            } else {
                clubModel.setClubNewImage(filePath);
            }
            clubModel.setClubId(this.clubModel.getClubId());
        }
        executeRx(BowlingApi.getInstance().createClub(clubModel, receivedData -> {
            RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
            Intent intent = new Intent();
            intent.putExtra(Define.CLUB_MODEL, clubModel);
            mContext.setResult(Activity.RESULT_OK, intent);
            mContext.finish();
            guardRequest = false;
            ProgressUtil.INSTANCE.hideProgress();
        }, errorData -> {
            Toast.makeText(mContext, "클럽 만들기 실패", Toast.LENGTH_LONG).show();
            guardRequest = false;
            ProgressUtil.INSTANCE.hideProgress();
        }));
    }

    public ClubModel getClubModel() {
        return clubModel;
    }
}
