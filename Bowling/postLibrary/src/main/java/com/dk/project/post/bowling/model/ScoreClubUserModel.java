package com.dk.project.post.bowling.model;

import android.os.Parcel;

import com.dk.project.post.model.LoginInfoModel;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoreClubUserModel extends ClubUserModel {

    // 0이면 팀 , 1이면 유저 정보
    private int viewType = 1;
    private String teamName;
    private boolean check;
//    private int[] scoreList = {0, 0, 0, 0, 0, 0};

    private ArrayList<Integer> scoreList;


    public ScoreClubUserModel() {
        scoreList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
    }

    public ScoreClubUserModel(String teamName) {
        this.viewType = 0;
        this.teamName = teamName;
        scoreList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
    }

    public ScoreClubUserModel(LoginInfoModel loginInfoModel) {
        viewType = 1;
        setUserId(loginInfoModel.getUserId());
        setUserName(loginInfoModel.getUserName());
        setUserPhoto(loginInfoModel.getUserPhoto());
        scoreList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
    }

    protected ScoreClubUserModel(Parcel in) {
        super(in);
        viewType = in.readInt();
        teamName = in.readString();
        check = in.readByte() != 0;
//        scoreList = in.createIntArray();
        in.readList(scoreList, Integer.class.getClassLoader());
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
        dest.writeInt(viewType);
        dest.writeString(teamName);
        dest.writeByte((byte) (check ? 1 : 0));
//        dest.writeIntArray(scoreList);
        dest.writeList(scoreList);
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
//        scoreList[index] = score;
        scoreList.set(index, score);
    }

    public int getScore(int index) {
//        return scoreList[index];
        return scoreList.get(index);
    }
}
