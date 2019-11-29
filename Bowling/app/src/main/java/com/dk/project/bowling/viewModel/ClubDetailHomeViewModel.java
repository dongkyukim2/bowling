package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.dk.project.bowling.R;
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
                setUserType(clubModel.getType());
                break;
            case R.id.club_user_list:
                switch (clubModel.getType()) {
                    case Define.USER_TYPE_JOIN:
                    case Define.USER_TYPE_OWNER:
                        Intent intent = new Intent(mContext, ClubUserListActivity.class);
                        intent.putExtra(Define.CLUB_MODEL, clubModel);
                        mContext.startActivity(intent);
                        break;
                    case Define.USER_TYPE_JOIN_WAIT:
                        ToastUtil.showToastCenter(mContext, "가입신청 대기중입니다.");
                        break;
                    default:
                        ToastUtil.showToastCenter(mContext, "가입신청을 해주세요.");
                }
                break;
            case R.id.club_setting:
                Toast.makeText(mContext, "클럽 설정", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void setClubModel(ClubModel clubModel) {
        this.clubModel = clubModel;
    }

    public ClubModel getClubModel() {
        return clubModel;
    }

    private void setUserType(int userType) {
        //todo 상태값 전체적으로 정리해야 함
        String msg;
        int newType;
        if (userType <= Define.USER_TYPE_OWNER) {
            msg = "탈퇴";
            newType = Define.USER_TYPE_SECESSION;
        } else if (userType == Define.USER_TYPE_JOIN_WAIT) {
            msg = "가입신청 취소";
            newType = Define.USER_TYPE_NOT_JOIN;
        } else {
            msg = "가입신청";
            newType = Define.USER_TYPE_JOIN_WAIT;
        }

        if (clubModel.getCreateUserId().equals(LoginManager.getInstance().getUserCode())) {
            AlertDialogUtil.showAlertDialog(mContext, "알림", "클럽장 탈퇴시 클럽의 모든 정보가 삭제됩니다.\n\n정말 탈퇴하시겠습니까?", (dialog, which) -> {
                BowlingApi.getInstance().deleteClub(clubModel, receivedData -> {
                    RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
                    mContext.finish();
                }, errorData -> Toast.makeText(mContext, "클럽 탈퇴하기 실패", Toast.LENGTH_SHORT).show());

            }, (dialog, which) -> {
            });
        } else {
            AlertDialogUtil.showAlertDialog(mContext, "알림", "정말 " + msg + " 하시겠습니까?", (dialog, which) -> {
                ClubUserModel clubUserModel = new ClubUserModel();
                clubUserModel.setClubId(clubModel.getClubId());
                clubUserModel.setUserId(LoginManager.getInstance().getUserCode());
                clubUserModel.setType(newType);
                executeRx(BowlingApi.getInstance().setModifyClubUserType(clubUserModel, receivedData -> {
                    if (receivedData.isSuccess()) {
                        RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
                        mContext.finish();
                    } else {
                        Toast.makeText(mContext, msg + "하기 실패", Toast.LENGTH_SHORT).show();
                    }
                }, errorData -> Toast.makeText(mContext, msg + "하기 실패", Toast.LENGTH_SHORT).show()));

            }, (dialog, which) -> {
            });
        }
    }
}
