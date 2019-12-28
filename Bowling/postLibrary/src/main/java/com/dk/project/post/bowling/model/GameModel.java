package com.dk.project.post.bowling.model;

import java.util.ArrayList;
import java.util.Date;

public class GameModel {

    private String clubId;
    private String gameId;
    private String gameName;
    private String playDateTime;

    private ArrayList<ScoreClubUserModel> userList;



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


    public String getPlayDateTime() {
        return playDateTime;
    }

    public void setPlayDateTime(String playDateTime) {
        this.playDateTime = playDateTime;
    }

    public ArrayList<ScoreClubUserModel> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<ScoreClubUserModel> userList) {
        this.userList = userList;
    }
}