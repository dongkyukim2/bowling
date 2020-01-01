package com.dk.project.post.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.dk.project.post.base.BaseViewModel;

/**
 * Created by dkkim on 2017-10-04.
 */

public class LoginViewModel extends BaseViewModel {

    public LoginViewModel(@NonNull Application application) {
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

    }

    public void kakaoLogin() {
//        Session.getCurrentSession().addCallback(new ISessionCallback() { // 로그인 버튼 눌렀을때 (회원가입시)
//            @Override
//            public void onSessionOpened() {
//                KakaoLoginUtils.getUserInfo(new MeV2ResponseCallback() {
//                    @Override
//                    public void onSuccess(MeV2Response result) {
//                        long userKakaoCode = result.getId();
//                        // 회원가입되어있나 로그아웃한것인가 확인
//                        executeRx(PostApi.getInstance().getUserInfo(String.valueOf(userKakaoCode),
//                                receivedData -> {
//                                    if (receivedData.getData() == null) { // 디비에 가입된 이력 없음
//                                        Intent intent = new Intent(mContext, LoginInfoActivity.class);
//                                        intent.putExtra(Define.ID, String.valueOf(userKakaoCode));
//                                        mContext.startActivity(intent);
//                                        mContext.finish();
//                                    } else {
//                                        LoginManager.getInstance().setLoginInfoModel(receivedData.getData());
////                                        Intent intent = new Intent(mContext, MainActivity.class);
////                                        mContext.startActivity(intent);
////                                        mContext.finish();
//                                    }
//
//                                }, errorData -> {
//
//                                }));
//                    }
//
//                    @Override
//                    public void onSessionClosed(ErrorResult errorResult) {
//                        System.out.println("====================    2");
//                    }
//                });
//
//            }
//
//            @Override
//            public void onSessionOpenFailed(KakaoException exception) {
//                System.out.println("====================    3");
//            }
//        });
    }
}
