package com.dk.project.post.utils;

import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

public class SessionCallback  implements ISessionCallback {

    @Override
    public void onSessionOpened() {
        System.out.println("==============   0");
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSuccess(MeV2Response result) {
                long userCode = result.getId(); // 로그인 성공후 유저 고유 키
                System.out.println("==============   2");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                System.out.println("==============   1");
            }
        });
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        System.out.println("==============   3");
        if (exception != null) {
            Logger.e(exception);
        }
    }


}