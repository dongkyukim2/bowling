package com.dk.project.post.controller;

public class LoginController {

    private static LoginController loginController;

    private boolean isLogin = true;

    public static LoginController getInstance() {
        if (loginController == null) {
            loginController = new LoginController();
        }
        return loginController;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
