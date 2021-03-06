package com.dk.project.post.bowling.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ReadGameModel implements Parcelable {

    private String clubId;
    private String gameId;
    private String gameName;
    private String createUserId;

    private String playDateTime;
    private ArrayList<GameScoreModel> scoreList;


    private String userName;
    private String userPhoto;

    public ReadGameModel() {
    }

    protected ReadGameModel(Parcel in) {
        clubId = in.readString();
        gameId = in.readString();
        gameName = in.readString();
        createUserId = in.readString();
        playDateTime = in.readString();
        scoreList = in.createTypedArrayList(GameScoreModel.CREATOR);

        userName = in.readString();
        userPhoto = in.readString();
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getPlayDateTime() {
        return playDateTime;
    }

    public void setPlayDateTime(String playDateTime) {
        this.playDateTime = playDateTime;
    }

    public ArrayList<GameScoreModel> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<GameScoreModel> scoreList) {
        this.scoreList = scoreList;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clubId);
        dest.writeString(gameId);
        dest.writeString(gameName);
        dest.writeString(createUserId);
        dest.writeString(playDateTime);
        dest.writeTypedList(scoreList);

        dest.writeString(userName);
        dest.writeString(userPhoto);
    }

    public static final Creator<ReadGameModel> CREATOR = new Creator<ReadGameModel>() {
        @Override
        public ReadGameModel createFromParcel(Parcel in) {
            return new ReadGameModel(in);
        }

        @Override
        public ReadGameModel[] newArray(int size) {
            return new ReadGameModel[size];
        }
    };
}