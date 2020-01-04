package com.dk.project.post.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.dk.project.post.base.BaseApplication;
import com.dk.project.post.base.Define;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.utils.Utils;

import org.apache.commons.lang3.StringUtils;


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


    public static void clear() {
        loginManager = null;
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
            saveAutoLogIn(false);
        } else {
            userCode = loginInfoModel.getUserId();
            saveAutoLogIn(true);
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

    private void saveAutoLogIn(boolean isLogin) {

        SharedPreferences sharedPreferences = BaseApplication.getGlobalApplicationContext().getSharedPreferences(Define.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("login", isLogin);
        editor.commit();
    }

    public boolean autoLogIn() {
        SharedPreferences sharedPreferences = BaseApplication.getGlobalApplicationContext().getSharedPreferences(Define.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("login", false);
    }

    public boolean ispermissionUser(String userId) {
        if (loginInfoModel == null || StringUtils.isBlank(userId)) {
            return false;
        }
        return loginInfoModel.getUserId().equalsIgnoreCase(userId);
    }
}
