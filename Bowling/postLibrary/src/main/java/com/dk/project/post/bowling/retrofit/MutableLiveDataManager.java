package com.dk.project.post.bowling.retrofit;

import androidx.lifecycle.MutableLiveData;

import com.dk.project.post.bowling.model.ScoreAvgModel;

public class MutableLiveDataManager {

    // 월별 일평균 최대 최소 목록
    private MutableLiveData<ScoreAvgModel> scoreMonthAvgList;

    private static MutableLiveDataManager instance;

    private MutableLiveDataManager() {
        scoreMonthAvgList = new MutableLiveData<>();
    }

    public static MutableLiveDataManager getInstance() {
        if (instance == null) {
            instance = new MutableLiveDataManager();
        }
        return instance;
    }

    public MutableLiveData<ScoreAvgModel> getScoreMonthAvgList() {
        return scoreMonthAvgList;
    }

    public static void clear() {
        instance = null;
    }
}
