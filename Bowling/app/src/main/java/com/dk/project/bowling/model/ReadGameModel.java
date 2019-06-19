package com.dk.project.bowling.model;

import java.util.ArrayList;

public class ReadGameModel {

    private String clubId;
    private String gameId;
    private String gameName;
    private String createUserId;

    private String playDateTime;
    private ArrayList<GameScoreModel> scoreList;

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
}