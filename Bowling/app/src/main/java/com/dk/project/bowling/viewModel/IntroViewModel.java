package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dk.project.bowling.BuildConfig;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.dk.project.post.utils.Utils;
import com.dk.project.post.utils.YoutubeUtil;
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
    private ISessionCallback iSessionCallback;

    public IntroViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        ImageUtil.getImagePath(mContext);

        YoutubeUtil.deleteYoutubeDb();

        Intent shareIntent = mContext.getIntent();
        if (Utils.isShareIntent(shareIntent) && LoginManager.getInstance().isLogIn() && BaseActivity.isLoadMainActivity()) {
            Intent intent = new Intent(mContext, WriteActivity.class);

            Bundle bundle = shareIntent.getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }

            mContext.startActivity(intent); // 로그인 된 상태로 메인 진입
            mContext.finish();
        } else {
            database = FirebaseDatabase.getInstance();
            dbRef = database.getReference("version");
            database.goOnline();
            checkVersion();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KakaoLoginUtils.removeCallback(iSessionCallback);
    }

    @Override
    public void onThrottleClick(View view) {

    }

    private void startMainActivity() {
        if (Build.MODEL.equalsIgnoreCase("SM-G965N")) {
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
                        iSessionCallback = new ISessionCallback() {
                            @Override
                            public void onSessionOpened() {
                                loginCheck();
                            }

                            @Override
                            public void onSessionOpenFailed(KakaoException exception) { // 카카오 로그인 화면 열었다가 닫기하면
                                LoginManager.getInstance().setLoginInfoModel(null);
                                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                                mContext.finish();
                                Toast.makeText(mContext, "카카오 로그인 안함", Toast.LENGTH_LONG).show();
                            }
                        };
                        KakaoLoginUtils.openSession(mContext, iSessionCallback);
                    }
                } else { // 로그아웃 상태
                    LoginManager.getInstance().setLoginInfoModel(null);
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    mContext.finish();
                    Toast.makeText(mContext, "로그아웃 상태로 진입", Toast.LENGTH_LONG).show();
                }
            }
        } else if (Build.MODEL.equalsIgnoreCase("SM-G900K")) {
            // todo LoginInfoModel, setUserCode 중복 수정해야함
            LoginInfoModel loginInfoModel = new LoginInfoModel();
            loginInfoModel.setUserId("1087708737");
            loginInfoModel.setUserName("박철수");
            LoginManager.getInstance().setLoginInfoModel(loginInfoModel);
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        } else if (Build.MODEL.equalsIgnoreCase("SM-N900L")) {
            // todo LoginInfoModel, setUserCode 중복 수정해야함
            LoginInfoModel loginInfoModel = new LoginInfoModel();
            loginInfoModel.setUserId("1087708739");
            loginInfoModel.setUserName("최길동");
            LoginManager.getInstance().setLoginInfoModel(loginInfoModel);
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        }
    }

    private void loginCheck() {
        KakaoLoginUtils.getUserInfo(receivedData -> {
            if (receivedData.first > 0 && receivedData.second != null) { // 카카오에서 유저정보 가져오고, 회원정보 있음
                LoginManager.getInstance().setLoginInfoModel(receivedData.second);
                Intent intent = new Intent(mContext, MainActivity.class);
                Intent shareIntent = mContext.getIntent();
                if (Utils.isShareIntent(shareIntent)) {
                    Bundle bundle = shareIntent.getExtras();
                    if (bundle != null) {
                        intent.putExtras(bundle);
                    }
                }
                mContext.startActivity(intent); // 로그인 된 상태로 메인 진입
                mContext.finish();
                Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_LONG).show();
            } else if (receivedData.first > 0 && receivedData.second == null) { // 카카오에서 유저정보 가져오고, 회원정보 없음, 가입안된 상태
                LoginManager.getInstance().setLoginInfoModel(null);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                mContext.finish();
                Toast.makeText(mContext, "로그인 실패 - 가입안된 상태", Toast.LENGTH_LONG).show();
            } else if (receivedData.first <= 0) { // 카카오에서 유저정보 못가져옴, 결국 로그인 실패
                LoginManager.getInstance().setLoginInfoModel(null);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                mContext.finish();
                Toast.makeText(mContext, "로그인 실패 - 카카오 로그인 실패", Toast.LENGTH_LONG).show();
            } else { // 이거 타는 경우가 있나??
                LoginManager.getInstance().setLoginInfoModel(null);
                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
                mContext.finish();
                Toast.makeText(mContext, "로그인 실패 - 이유 모름", Toast.LENGTH_LONG).show();
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
