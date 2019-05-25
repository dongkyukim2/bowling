package com.dk.project.bowling.manager;

public class LoginManager {

    private static LoginManager loginManager;

    private long userCode; // 유저 고유 번호, 아이디 같은것

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
    }
}
