package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.ui.activity.ScoreListActivity;
import com.dk.project.post.base.BaseViewModel;

import static com.dk.project.post.base.Define.SCORE_DATE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainInfoViewModel extends BaseViewModel {

    // 점수등록했을떄 갱신용
    private MutableLiveData<ScoreModel> writeScoreLiveData = new MutableLiveData<>();

    public MainInfoViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {

    }

    public void getScoreDayList(ScoreModel scoreModel) {
        Intent intent = new Intent(mContext, ScoreListActivity.class);
        intent.putExtra(SCORE_DATE, scoreModel.getPlayDate());
        mContext.startActivity(intent);
    }

    public MutableLiveData<ScoreModel> getWriteScoreLiveData() {
        return writeScoreLiveData;
    }
}
