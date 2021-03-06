package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.dk.project.bowling.BuildConfig;
import com.dk.project.bowling.ui.activity.MainActivity;
import com.dk.project.post.base.BaseActivity;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.ImageUtil;
import com.dk.project.post.utils.KakaoLoginUtils;
import com.dk.project.post.utils.Utils;
import com.dk.project.post.utils.YoutubeUtil;

/**
 * Created by dkkim on 2017-10-04.
 */

public class IntroViewModel extends BaseViewModel {

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
            BowlingApi.getInstance().getVersion(serverVersion -> {
                try {
                    String appVersion = BuildConfig.VERSION_NAME;
                    String[] serverArray = serverVersion.getData().split("\\.");
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
                    mContext.finish();
                }


            }, errorData -> {

            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    private void startMainActivity() {
        if (BuildConfig.FLAVOR == "real") {
            if (mContext.getIntent().hasExtra(Define.INTRO_DELAY)) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                mContext.finish();
            } else {
                if (LoginManager.getInstance().autoLogIn()) {
                    KakaoLoginUtils.getInstance().getUserInfo(mContext, receivedData -> {
                        if (receivedData.getSecond() != null) {
                            LoginManager.getInstance().setLoginInfoModel(receivedData.getSecond());
                            Intent intent = new Intent(mContext, MainActivity.class);
                            Intent shareIntent = mContext.getIntent();
                            if (Utils.isShareIntent(shareIntent)) {
                                Bundle bundle = shareIntent.getExtras();
                                if (bundle != null) {
                                    intent.putExtras(bundle);
                                }
                            }
                            mContext.startActivity(intent); // 로그인 된 상태로 메인 진입
                        } else {
                            LoginManager.getInstance().setLoginInfoModel(null);
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                        }
                        mContext.finish();
                    });
                } else { // 로그아웃 상태
                    LoginManager.getInstance().setLoginInfoModel(null);
                    mContext.startActivity(new Intent(mContext, MainActivity.class));
                    mContext.finish();
                }
            }
        } else {
            LoginInfoModel loginInfoModel = new LoginInfoModel();
            if (BuildConfig.FLAVOR == "guest") {
                loginInfoModel.setUserId("1087708737");
                loginInfoModel.setUserName("박철수");
            } else if (BuildConfig.FLAVOR == "guest2") {
                loginInfoModel.setUserId("1087708738");
                loginInfoModel.setUserName("최길동");
            }
            LoginManager.getInstance().setLoginInfoModel(loginInfoModel);
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            mContext.finish();
        }
    }

//    private void loginCheck() {
//        KakaoLoginUtils.getUserInfo(receivedData -> {
//            if (receivedData.first > 0 && receivedData.second != null) { // 카카오에서 유저정보 가져오고, 회원정보 있음
//                LoginManager.getInstance().setLoginInfoModel(receivedData.second);
//                Intent intent = new Intent(mContext, MainActivity.class);
//                Intent shareIntent = mContext.getIntent();
//                if (Utils.isShareIntent(shareIntent)) {
//                    Bundle bundle = shareIntent.getExtras();
//                    if (bundle != null) {
//                        intent.putExtras(bundle);
//                    }
//                }
//                mContext.startActivity(intent); // 로그인 된 상태로 메인 진입
//                mContext.finish();
////                Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_LONG).show();
//            } else if (receivedData.first > 0 && receivedData.second == null) { // 카카오에서 유저정보 가져오고, 회원정보 없음, 가입안된 상태
//                LoginManager.getInstance().setLoginInfoModel(null);
//                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
//                mContext.finish();
////                Toast.makeText(mContext, "로그인 실패 - 가입안된 상태", Toast.LENGTH_LONG).show();
//            } else if (receivedData.first <= 0) { // 카카오에서 유저정보 못가져옴, 결국 로그인 실패
//                LoginManager.getInstance().setLoginInfoModel(null);
//                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
//                mContext.finish();
////                Toast.makeText(mContext, "로그인 실패 - 카카오 로그인 실패", Toast.LENGTH_LONG).show();
//            } else { // 이거 타는 경우가 있나??
//                LoginManager.getInstance().setLoginInfoModel(null);
//                mContext.startActivity(new Intent(mContext, MainActivity.class)); // 비로그인 된 상태로 메인 진입
//                mContext.finish();
////                Toast.makeText(mContext, "로그인 실패 - 이유 모름", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    private void goMarket() {
        String appPackageName = mContext.getPackageName();
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
