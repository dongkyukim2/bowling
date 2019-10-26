package com.dk.project.post.bowling.model;

import android.os.Parcel;

import com.dk.project.post.model.LoginInfoModel;

public class ScoreClubUserModel extends ClubUserModel {

    // 0이면 팀 , 1이면 유저 정보
    private int viewType = 1;
    private String teamName;
    private boolean check;
    private Integer[] scoreList = {0, 0, 0, 0, 0, 0};

    public ScoreClubUserModel() {

    }

    public ScoreClubUserModel(String teamName) {
        this.viewType = 0;
        this.teamName = teamName;
    }

    public ScoreClubUserModel(LoginInfoModel loginInfoModel) {
        viewType = 1;
        setUserId(loginInfoModel.getUserId());
        setUserName(loginInfoModel.getUserName());
        setUserPhoto(loginInfoModel.getUserPhoto());
    }

    protected ScoreClubUserModel(Parcel in) {
        super(in);
        viewType = in.readInt();
        check = in.readByte() != 0;
        teamName = in.readString();
    }

    public static final Creator<ScoreClubUserModel> CREATOR = new Creator<ScoreClubUserModel>() {
        @Override
        public ScoreClubUserModel createFromParcel(Parcel in) {
            return new ScoreClubUserModel(in);
        }

        @Override
        public ScoreClubUserModel[] newArray(int size) {
            return new ScoreClubUserModel[size];
        }
    };

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

    public boolean isUserType() {
        return viewType == 1;
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

    public void setScore(int index, int score) {
        scoreList[index] = score;
    }

    public int getScore(int index) {
        return scoreList[index];
    }
}
