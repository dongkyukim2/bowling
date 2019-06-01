package com.dk.project.bowling.model;

import android.os.Parcel;
import com.dk.project.post.model.LoginInfoModel;

public class UserModel extends LoginInfoModel {

    // 0이면 팀 , 1이면 유저 정보
    private int viewType;
    private String teamName;
    private boolean check;

    public UserModel() {

    }

    public UserModel(String teamName) {
        this.viewType = 0;
        this.teamName = teamName;
    }

    public UserModel(LoginInfoModel loginInfoModel) {
        viewType = 1;
        setUserId(loginInfoModel.getUserId());
        setUserName(loginInfoModel.getUserName());
        setUserPhoto(loginInfoModel.getUserPhoto());
    }

    protected UserModel(Parcel in) {
        super(in);
        viewType = in.readInt();
        check = in.readByte() != 0;
        teamName = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(viewType);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeString(teamName);
    }
}
