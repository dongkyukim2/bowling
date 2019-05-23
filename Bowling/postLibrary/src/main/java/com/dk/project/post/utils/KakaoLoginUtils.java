package com.dk.project.post.utils;

import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;

public class KakaoLoginUtils {


    public static boolean checkLogin() {
        return Session.getCurrentSession().checkAndImplicitOpen();
    }


    // 세션 열려있으면 유저정보 요청
    public static void getUserInfo(MeV2ResponseCallback loginCallback) {
        UserManagement.getInstance().me(loginCallback);
    }
}
