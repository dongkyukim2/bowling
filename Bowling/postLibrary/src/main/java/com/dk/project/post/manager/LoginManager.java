package com.dk.project.post.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.dk.project.post.base.BaseApplication;
import com.dk.project.post.base.Define;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.utils.Utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class LoginManager {

    private static LoginManager loginManager;

    private String userCode = ""; // 유저 고유 번호, 아이디 같은것
    private LoginInfoModel loginInfoModel;

    private AtomicLong time;
    private Disposable disposable;

    public static LoginManager getInstance() {
        if (loginManager == null) {
            loginManager = new LoginManager();
        }
        return loginManager;
    }

    public void startTimer() {
        stopTimer();
        if (disposable == null) {
            time = new AtomicLong(System.currentTimeMillis());
            disposable = Observable.interval(0, 10, TimeUnit.SECONDS)
                    .map(aLong -> System.currentTimeMillis())
                    .subscribe(aLong -> time.set(aLong));
        }
    }

    public void stopTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
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
            System.out.println("+++++++++++   requestTime = " + Utils.DateFormat_0.format(new Date(time.get())));
            return Utils.Encrypt(getUserCode() + "|" + time.get(), Utils.getHashKey(BaseApplication.getGlobalApplicationContext()));
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

    public int isPermissionUser(String userId) {
        if (loginInfoModel == null || StringUtils.isBlank(userId)) {
            return Define.LOGOUT;
        }
        if (loginInfoModel.getUserId().equalsIgnoreCase(userId)) {
            return Define.OK;
        }
        return Define.NO_PERMISSION;
    }

    public boolean isLogIn() {
        return (loginInfoModel != null && !StringUtils.isBlank(loginInfoModel.getUserId()));
    }
}
