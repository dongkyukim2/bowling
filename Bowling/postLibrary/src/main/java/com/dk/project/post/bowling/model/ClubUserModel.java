package com.dk.project.post.bowling.model;

import android.os.Parcel;

import com.dk.project.post.model.LoginInfoModel;

public class ClubUserModel extends LoginInfoModel {

    private String clubId;

    /*
     0 : 가입 완료
     1 : 클럽 주인 (당연 가립된 상태)
     2 : 가입 승인대기
     3 : 탈퇴
     4 : 강퇴
    */
    private int type;
    private String signUpDate;

    public ClubUserModel() {
    }

    protected ClubUserModel(Parcel in) {
        clubId = in.readString();
        type = in.readInt();
        signUpDate = in.readString();
    }

    public static final Creator<ClubUserModel> CREATOR = new Creator<ClubUserModel>() {
        @Override
        public ClubUserModel createFromParcel(Parcel in) {
            return new ClubUserModel(in);
        }

        @Override
        public ClubUserModel[] newArray(int size) {
            return new ClubUserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clubId);
        dest.writeInt(type);
        dest.writeString(signUpDate);
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        this.signUpDate = signUpDate;
    }
}
