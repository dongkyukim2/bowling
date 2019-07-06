package com.dk.project.bowling.retrofit;

import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ScoreAvgModel;
import com.dk.project.bowling.model.ScoreModel;

public class MutableLiveDataManager {

    // 월별 일평균 최대 최소 목록
    private MutableLiveData<ScoreAvgModel> scoreMonthAvgList;

    // 점수등록했을떄 갱신용
    private MutableLiveData<ScoreModel> writeScoreLiveData;

    private static MutableLiveDataManager instance;

    private MutableLiveDataManager() {
        scoreMonthAvgList = new MutableLiveData<>();
        writeScoreLiveData = new MutableLiveData<>();
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

    public MutableLiveData<ScoreModel> getWriteScoreLiveData() {
        return writeScoreLiveData;
    }
}
