package com.dk.project.bowling.model;

import com.dk.project.post.model.LoginInfoModel;

public class UserModel extends LoginInfoModel {

    // 0이면 팀 , 1이면 유저 정보
    private int viewType;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
