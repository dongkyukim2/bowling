package com.dk.project.post.bowling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GameScoreModel implements Parcelable {

    private String gameId;
    private int teamPosition;
    private int userPosition;
    private int scorePosition;
    private String teamName;
    private int score;
    private String clubId;
    private String userId;

    public GameScoreModel() {
    }

    protected GameScoreModel(Parcel in) {
        gameId = in.readString();
        teamPosition = in.readInt();
        userPosition = in.readInt();
        scorePosition = in.readInt();
        teamName = in.readString();
        score = in.readInt();
        clubId = in.readString();
        userId = in.readString();
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getTeamPosition() {
        return teamPosition;
    }

    public void setTeamPosition(int teamPosition) {
        this.teamPosition = teamPosition;
    }

    public int getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(int userPosition) {
        this.userPosition = userPosition;
    }

    public int getScorePosition() {
        return scorePosition;
    }

    public void setScorePosition(int scorePosition) {
        this.scorePosition = scorePosition;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gameId);
        dest.writeInt(teamPosition);
        dest.writeInt(userPosition);
        dest.writeInt(scorePosition);
        dest.writeString(teamName);
        dest.writeInt(score);
        dest.writeString(clubId);
        dest.writeString(userId);
    }

    public static final Creator<GameScoreModel> CREATOR = new Creator<GameScoreModel>() {
        @Override
        public GameScoreModel createFromParcel(Parcel in) {
            return new GameScoreModel(in);
        }

        @Override
        public GameScoreModel[] newArray(int size) {
            return new GameScoreModel[size];
        }
    };
}