package com.dk.project.bowling.viewModel;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dk.project.bowling.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ClubUserModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.utils.AlertDialogUtil;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubDetailHomeViewModel extends BaseViewModel {

    private boolean clubSign;
    private ClubModel clubModel;

    public ClubDetailHomeViewModel(@NonNull Application application) {
        super(application);
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
        switch (view.getId()) {
            case R.id.sign_up_btn:
                if (clubSign) {
                    AlertDialogUtil.showAlertDialog(mContext, "알림", "정말 탈퇴 하시겠습니까?", (dialog, which) -> {
                        ClubUserModel clubUserModel = new ClubUserModel();
                        clubUserModel.setClubId(clubModel.getClubId());
                        clubUserModel.setUserId(LoginManager.getInstance().getUserCode());
                        clubUserModel.setType(Define.USER_TYPE_SECESSION);
                        executeRx(BowlingApi.getInstance().setModifyClubUserType(clubUserModel, receivedData -> {
                            if (receivedData.isSuccess()) {
                                Intent intent = new Intent();
                                intent.putExtra(Define.CLUB_ID, clubUserModel.getClubId());
                                mContext.setResult(Activity.RESULT_OK, intent);
                                mContext.finish();
                            } else {
                                Toast.makeText(mContext, "탈퇴하기 실패", Toast.LENGTH_SHORT).show();
                            }

                        }, errorData -> Toast.makeText(mContext, "탈퇴하기 실패", Toast.LENGTH_SHORT).show()));
                    });
                } else {
                    Toast.makeText(mContext, "가입하기", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.club_user_list:
                Toast.makeText(mContext,"회원 목록 보기, 가입 신청 대기 화면", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void setClubSign(boolean clubSign) {
        this.clubSign = clubSign;
    }

    public void setClubModel(ClubModel clubModel) {
        this.clubModel = clubModel;
    }
}
