package com.dk.project.post.manager;

import com.dk.project.post.base.BaseApplication;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.utils.Utils;

public class LoginManager {

    private static LoginManager loginManager;

    private String userCode = ""; // 유저 고유 번호, 아이디 같은것
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

    public LoginInfoModel getLoginInfoModel() {
        return loginInfoModel;
    }

    public void setLoginInfoModel(LoginInfoModel loginInfoModel) {
        this.loginInfoModel = loginInfoModel;
        if (loginInfoModel == null) {
            userCode = "";
        } else {
            userCode = loginInfoModel.getUserId();
        }
    }

    public String getEncodeId() {
        try {
            return Utils.Encrypt(getUserCode(), Utils.getHashKey(BaseApplication.getGlobalApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
