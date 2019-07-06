package com.dk.project.bowling.model;

import java.util.ArrayList;

public class ScoreAvgModel {

    private ScoreModel monthAvg;
    private ArrayList<ScoreModel> monthAvgList;

    public ScoreModel getMonthAvg() {
        return monthAvg;
    }

    public ArrayList<ScoreModel> getMonthAvgList() {
        return monthAvgList;
    }
}
