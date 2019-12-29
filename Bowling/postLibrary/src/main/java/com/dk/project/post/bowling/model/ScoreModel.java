package com.dk.project.post.bowling.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreModel {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int idx;

    private String userId;
    private int score;
    private String playDateTime;
    private String comment;


    private int avgScore;
    private int maxScore;
    private int minScore;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayDate() {
        return playDateTime.split(" ")[0];
    }

    public String getPlayDateTime() {
        return playDateTime;
    }

    public void setPlayDateTime(long playDateTime) {
        Date date = new Date();
        date.setTime(playDateTime);
        this.playDateTime = dateFormat.format(date);
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setPlayDateTime(String playDateTime) {
        this.playDateTime = playDateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }
}