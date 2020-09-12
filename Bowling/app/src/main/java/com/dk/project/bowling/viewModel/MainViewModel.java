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
import com.dk.project.bowling.ui.activity.MainActivity;
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

import java.util.Calendar;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainViewModel extends BaseViewModel<MainActivity> {

    //  Note : 메인스레드에서 LiveData를 갱신하려면 반드시 setValue(T) 메서드를 호출해야 한다.
    // 만약, 작업스레드에서 LiveData를 갱신하려면 postValue(T) 메서드를 호출해야 한다.

    public MainViewModel(@NonNull Application application) {
        super(application);
        executeRx(RxBus.getInstance().registerRxObserver(busModel -> {
            switch (busModel.first) {
                case Define.EVENT_LOGIN_SUCCESS:
                case Define.EVENT_PROFILE_SUCCESS:
                    mContext.setUserInfoView();
                    break;
            }
        }));
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
            case R.id.setting:
                mContext.onBackPressed();
                Intent intent = new Intent(mContext, LoginInfoActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.login:
                mContext.onBackPressed();
                KakaoLoginUtils.getInstance().login(mContext, receivedData -> {
                    if (receivedData.getSecond() == null) { // 디비에 가입된 이력 없음
                        Intent tempIntent = new Intent(mContext, LoginInfoActivity.class);
                        tempIntent.putExtra(Define.ID, String.valueOf(receivedData.getFirst()));
                        mContext.startActivity(tempIntent);
                    } else {
                        LoginManager.getInstance().setLoginInfoModel(receivedData.getSecond());
                        RxBus.getInstance().eventPost(new Pair(Define.EVENT_LOGIN_SUCCESS, true));
                    }
                });
                break;
            case R.id.logout:
                AlertDialogUtil.showAlertDialog(mContext, "알림", "로그아웃 하시겠습니까?", (dialog, which) -> {
                    KakaoLoginUtils.getInstance().logout(mContext);
                });
                mContext.onBackPressed();
                break;
        }
    }

    public void writeScore(ScoreModel scoreModel) {
        executeRx(BowlingApi.getInstance().writeScore(scoreModel,
                receivedData -> {
                    RxBus.getInstance().eventPost(Pair.create(Define.EVENT_REFRESH_SCORE, receivedData.getData()));
                    mContext.requestWriteSore = false;
                },
                errorData -> {
                    Toast.makeText(mContext, "점수등록 실패", Toast.LENGTH_SHORT).show();
                    mContext.requestWriteSore = false;
                }));
    }

    public void checkShare() {
        if (!LoginManager.getInstance().isLogIn()) {
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
}
