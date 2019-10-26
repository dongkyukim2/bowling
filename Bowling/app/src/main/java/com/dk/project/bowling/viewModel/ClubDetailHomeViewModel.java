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
                    AlertDialogUtil.showAlertDialog(mContext, "알림", "정말 탈퇴 하시겠습니까?", (dialog, which) -> {
                        ClubUserModel clubUserModel = new ClubUserModel();
                        clubUserModel.setClubId(clubModel.getClubId());
                        clubUserModel.setUserId(LoginManager.getInstance().getUserCode());
                        clubUserModel.setType(Define.USER_TYPE_SECESSION);
                        executeRx(BowlingApi.getInstance().setModifyClubUserType(clubUserModel, receivedData -> {
                            if (receivedData.isSuccess()) {
                                RxBus.getInstance().eventPost(new Pair(Define.EVENT_REFRESH_MY_CLUB_LIST, true));
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
                if (!ShareData.getInstance().getClubUserList().isEmpty()) {
                    Intent intent = new Intent(mContext, ClubUserListActivity.class);
                    intent.putExtra(Define.CLUB_MODEL, clubModel);
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    public void setClubModel(ClubModel clubModel) {
        this.clubModel = clubModel;
    }
}
