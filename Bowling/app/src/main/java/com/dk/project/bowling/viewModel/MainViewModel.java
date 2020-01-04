package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.util.Pair;

import com.dk.project.bowling.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ScoreModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.ui.activity.LoginInfoActivity;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.Utils;
import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

import java.util.Calendar;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainViewModel extends BaseViewModel {

    //  Note : 메인스레드에서 LiveData를 갱신하려면 반드시 setValue(T) 메서드를 호출해야 한다.
    // 만약, 작업스레드에서 LiveData를 갱신하려면 postValue(T) 메서드를 호출해야 한다.

    private ISessionCallback iSessionCallback;

    public MainViewModel(@NonNull Application application) {
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
            case R.id.login:
                if (KakaoLoginUtils.checkLogin()) { // 카카오 세션이 열려있나?
                    requestLogin();
                } else {
                    mContext.onBackPressed();
                    if (iSessionCallback == null) {
                        iSessionCallback = new ISessionCallback() {
                            @Override
                            public void onSessionOpened() {
                                KakaoLoginUtils.removeCallback(iSessionCallback);
                                requestLogin();
                            }

                            @Override
                            public void onSessionOpenFailed(KakaoException exception) {
                                KakaoLoginUtils.removeCallback(iSessionCallback);
                            }
                        };
                    }
                    KakaoLoginUtils.openSession(mContext, iSessionCallback);
                }
                break;
            case R.id.logout:
                AlertDialogUtil.showAlertDialog(mContext, "알림", "로그아웃 하시겠습니까?", (dialog, which) -> {
                    mContext.onBackPressed();
                    KakaoLoginUtils.logout(mContext);
                });
                break;
        }
    }

    public void writeScore(ScoreModel scoreModel) {
        executeRx(BowlingApi.getInstance().writeScore(scoreModel,
                receivedData -> {
                    RxBus.getInstance().eventPost(Pair.create(Define.EVENT_REFRESH_SCORE, receivedData.getData()));
                },
                errorData -> Toast.makeText(mContext, "점수등록 실패", Toast.LENGTH_SHORT).show()));
    }

    public void checkShare() {
        if (LoginManager.getInstance().getLoginInfoModel() == null) {
            return;
        }
        Intent shareIntent = mContext.getIntent();
        if (Utils.isShareIntent(shareIntent)) {
            Intent intent = new Intent(mContext, WriteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = shareIntent.getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            mContext.startActivity(intent);
        }
    }

    public void setDate(AppCompatTextView textView, Calendar calendar) {
        String.format("%02d", (calendar.get(Calendar.MONTH) + 1));
        textView.setText(calendar.get(Calendar.YEAR) + "-" +
                String.format("%02d", (calendar.get(Calendar.MONTH) + 1)) + "-" + String
                .format("%02d", calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public void setTime(AppCompatTextView textView, Calendar calendar) {
        textView.setText((calendar.get(Calendar.AM_PM) == 0 ? "오전 " : "오후 ") +
                String.format("%02d", calendar.get(Calendar.HOUR)) + ":" +
                String.format("%02d", calendar.get(Calendar.MINUTE)));
    }

    private void requestLogin() {
        KakaoLoginUtils.getUserInfo(receivedData -> {
            if (receivedData.second == null) { // 디비에 가입된 이력 없음
                Intent intent = new Intent(mContext, LoginInfoActivity.class);
                intent.putExtra(Define.ID, String.valueOf(receivedData.first));
                mContext.startActivity(intent);
            } else {
                LoginManager.getInstance().setLoginInfoModel(receivedData.second);
                RxBus.getInstance().eventPost(new Pair(Define.EVENT_LOGIN_SUCCESS, true));
            }
        });
    }
}
