package com.dk.project.post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginInfoModel implements Parcelable {

    private String userId;
    private String userName;
    private String userPhoto;
    private String loginType;
    private boolean modify;

    public LoginInfoModel() {
    }


    protected LoginInfoModel(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userPhoto = in.readString();
        loginType = in.readString();
        modify = in.readByte() != 0;
    }

    public static final Creator<LoginInfoModel> CREATOR = new Creator<LoginInfoModel>() {
        @Override
        public LoginInfoModel createFromParcel(Parcel in) {
            return new LoginInfoModel(in);
        }

        @Override
        public LoginInfoModel[] newArray(int size) {
            return new LoginInfoModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPhoto);
        dest.writeString(loginType);
        dest.writeByte((byte) (modify ? 1 : 0));
    }
}
