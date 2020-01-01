package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.dk.project.bowling.BuildConfig;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ISessionCallback;
import com.kakao.util.exception.KakaoException;

/**
 * Created by dkkim on 2017-10-04.
 */

public class IntroViewModel extends BaseViewModel {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;

    public IntroViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        ImageUtil.getImagePath(mContext);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("version");
        database.goOnline();
        checkVersion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    private void startMainActivity() {
        if (Define.TEST_USER) {
            // todo LoginInfoModel, setUserCode 중복 수정해야함
            LoginInfoModel loginInfoModel = new LoginInfoModel();
            loginInfoModel.setUserId(Define.TEST_ID);
            loginInfoModel.setUserName(Define.TEST_NAME);
            LoginManager.getInstance().setLoginInfoModel(loginInfoModel);
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        } else {
            // todo 여기도 수정해야함
            if (mContext.getIntent().hasExtra(Define.INTRO_DELAY)) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                mContext.finish();
            } else {

                if (LoginManager.getInstance().autoLogIn()) {
                    // 세션이 열려있는지
                    // todo 로그인되어있으면 열려있는듯
                    if (KakaoLoginUtils.checkLogin()) {
                        loginCheck();
                    } else {
                        KakaoLoginUtils.openSession(mContext, new ISessionCallback() {
                            @Override
                            public void onSessionOpened() {
                                loginCheck();
                            }

                            @Override
                            public void onSessionOpenFailed(KakaoException exception) {
                                LoginManager.getInstance().setLoginInfoModel(null);
                                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                                mContext.finish();
                            }
                        });
                    }
                } else { // 로그아웃 상태
                    LoginManager.getInstance().setLoginInfoModel(null);
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    mContext.finish();
                }
            }
        }
    }

    private void loginCheck(){
        KakaoLoginUtils.getUserInfo(receivedData -> {
            if (receivedData.first > 0 && receivedData.second != null) { // 카카오에서 유저정보 가져오고, 회원정보 있음
                LoginManager.getInstance().setLoginInfoModel(receivedData.second);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 로그인 된 상태로 메인 진입
                mContext.finish();
            } else if (receivedData.first > 0 && receivedData.second == null) { // 카카오에서 유저정보 가져오고, 회원정보 없음, 가입안된 상
                LoginManager.getInstance().setLoginInfoModel(null);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                mContext.finish();
            } else if (receivedData.first <= 0) { // 카카오에서 유저정보 못가져옴, 결국 로그인 실패
                LoginManager.getInstance().setLoginInfoModel(null);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                mContext.finish();
            } else { // 이거 타는 경우가 있나??
                LoginManager.getInstance().setLoginInfoModel(null);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                mContext.finish();
            }
        });
    }

    private void checkVersion() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String serverVersion = dataSnapshot.getValue(String.class);
                    String appVersion = BuildConfig.VERSION_NAME;

                    String[] serverArray = serverVersion.split("\\.");
                    String[] appArray = appVersion.split("\\.");

                    if (Integer.parseInt(serverArray[0]) > Integer.parseInt(appArray[0])) {// 첫번째 자리가 크면 강업
                        AlertDialogUtil.showAlertDialog(mContext, null, "최신버전이 있습니다.\n업데이트후 사용해주세요.",
                                (dialog, which) -> goMarket(), (dialog, which) -> mContext.finish());
                    } else if (Integer.parseInt(serverArray[1]) > Integer.parseInt(appArray[1])) {  // 두번째 자리가 크면 강업
                        AlertDialogUtil.showAlertDialog(mContext, null, "최신버전이 있습니다.\n업데이트후 사용해주세요.",
                                (dialog, which) -> goMarket(), (dialog, which) -> mContext.finish());
                    } else {
                        startMainActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    startMainActivity();
                } finally {
                    dbRef.removeEventListener(valueEventListener);
                    database.goOffline();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                startMainActivity();
                dbRef.removeEventListener(valueEventListener);
                database.goOffline();
            }
        };

        dbRef.addValueEventListener(valueEventListener);
    }

    private void goMarket() {
        String appPackageName = mContext.getPackageName();
        appPackageName = "com.kakao.talk";
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (Exception e) {
            e.printStackTrace();
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        } finally {
            mContext.finish();
        }
    }
}
