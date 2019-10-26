package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.dk.project.bowling.R;
import com.dk.project.bowling.shareData.ShareData;
import com.dk.project.bowling.ui.activity.ClubUserListActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ClubUserModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.ToastUtil;

/**
 * Created by dkkim on 2017-10-04.
 */

public class ClubDetailHomeViewModel extends BaseViewModel {

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
                if (clubModel.getType() <= Define.USER_TYPE_OWNER) {
                    setUserType(Define.USER_TYPE_SECESSION);
                } else {
                    setUserType(Define.USER_TYPE_JOIN_WAIT);
                }
                break;
            case R.id.club_user_list:
                switch (clubModel.getType()) {
                    case Define.USER_TYPE_JOIN:
                    case Define.USER_TYPE_OWNER:
                        if (!ShareData.getInstance().getClubUserList().isEmpty()) {
                            Intent intent = new Intent(mContext, ClubUserListActivity.class);
                            intent.putExtra(Define.CLUB_MODEL, clubModel);
                            mContext.startActivity(intent);
                        }
                        break;
                    case Define.USER_TYPE_JOIN_WAIT:
                        ToastUtil.showToastCenter(mContext, "가입신청 대기중입니다.");
                        break;
                    default:
                        ToastUtil.showToastCenter(mContext, "가입신청을 해주세요.");
                }
                break;
        }
    }

    public void setClubModel(ClubModel clubModel) {
        this.clubModel = clubModel;
    }

    private void setUserType(int userType) {
        String msg;
        if (userType == Define.USER_TYPE_JOIN_WAIT) {
            msg = "가입 신청";
        } else {
            msg = "탈퇴";
        }

        AlertDialogUtil.showAlertDialog(mContext, "알림", "정말 " + msg + " 하시겠습니까?", (dialog, which) -> {
            ClubUserModel clubUserModel = new ClubUserModel();
            clubUserModel.setClubId(clubModel.getClubId());
            clubUserModel.setUserId(LoginManager.getInstance().getUserCode());
            clubUserModel.setType(userType);
            executeRx(BowlingApi.getInstance().setModifyClubUserType(clubUserModel, receivedData -> {
                if (receivedData.isSuccess()) {
                    RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
                    mContext.finish();
                } else {
                    Toast.makeText(mContext, msg + "하기 실패", Toast.LENGTH_SHORT).show();
                }
            }, errorData -> Toast.makeText(mContext, msg + "하기 실패", Toast.LENGTH_SHORT).show()));

        });


    }
}
