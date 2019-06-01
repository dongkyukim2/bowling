package com.dk.project.post.manager;

import com.dk.project.post.model.LoginInfoModel;

public class LoginManager {

    private static LoginManager loginManager;

    private long userCode; // 유저 고유 번호, 아이디 같은것
    private LoginInfoModel loginInfoModel;

    public static LoginManager getInstance() {
        if (loginManager == null) {
            loginManager = new LoginManager();
        }
        return loginManager;
    }

    public String getUserCode() {
        return String.valueOf(userCode);
    }

    public void setUserCode(long userCode) {
        this.userCode = userCode;
        if (userCode == 0) {
            loginInfoModel = null;
        }
    }

    public static LoginManager getLoginManager() {
        return loginManager;
    }

    public static void setLoginManager(LoginManager loginManager) {
        LoginManager.loginManager = loginManager;
    }

    public LoginInfoModel getLoginInfoModel() {
        return loginInfoModel;
    }

    public void setLoginInfoModel(LoginInfoModel loginInfoModel) {
        this.loginInfoModel = loginInfoModel;
    }
}
