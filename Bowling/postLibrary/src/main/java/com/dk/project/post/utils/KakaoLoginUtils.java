package com.dk.project.post.utils;

import android.app.Activity;
import android.util.Pair;

import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.PostApi;
import com.dk.project.post.retrofit.SuccessCallback;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

public class KakaoLoginUtils {

    public static boolean checkLogin() {
        return Session.getCurrentSession().checkAndImplicitOpen();
    }


    // 세션 열려있으면 유저정보 요청
    public static void getUserInfo(SuccessCallback<Pair<Long, LoginInfoModel>> callback) {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSuccess(MeV2Response result) {
                long userKakaoCode = result.getId();
                // 회원가입되어있나 로그아웃한것인가 확인
                PostApi.getInstance().getUserInfo(String.valueOf(userKakaoCode),
                        receivedData -> callback.onSuccess(new Pair(userKakaoCode, receivedData.getData())),
                        errorData -> {
                            callback.onSuccess(new Pair(0, null));
                        });
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                callback.onSuccess(new Pair(0, null));
            }
        });
    }

    public static void openSession(Activity activity, ISessionCallback callback) {
        Session session = Session.getCurrentSession();
        session.addCallback(callback);
        session.open(AuthType.KAKAO_LOGIN_ALL, activity);
    }

    public static void logout(Activity activity) {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                LoginManager.getInstance().setLoginInfoModel(null);
                activity.finish();
            }
        });
    }
}
