package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.Utils;

import static com.dk.project.post.base.Define.USER_CODE;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainViewModel extends BaseViewModel {

    //  Note : 메인스레드에서 LiveData를 갱신하려면 반드시 setValue(T) 메서드를 호출해야 한다.
    // 만약, 작업스레드에서 LiveData를 갱신하려면 postValue(T) 메서드를 호출해야 한다.



    private final MutableLiveData<ScoreModel> scoreListLiveData = new MutableLiveData<>();

    // 점수 등록 했을 때
    public MutableLiveData<ScoreModel> getScoreListLiveData() {
        return scoreListLiveData;
    }


    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
        LoginManager.getInstance().setUserCode(mContext.getIntent().getLongExtra(USER_CODE, 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onThrottleClick(View view) {
    }

    public void writeScore(ScoreModel scoreModel) {
        executeRx(BowlingApi.getInstance().writeScore(scoreModel,
                receivedData -> scoreListLiveData.setValue(receivedData.getData()),
                errorData -> Toast.makeText(mContext, "점수등록 실패", Toast.LENGTH_SHORT).show()));
    }

    public void checkShare() {
        Intent shareIntent = mContext.getIntent();
        if (Utils.isShareIntent(shareIntent)) {
            Intent intent = new Intent(mContext, WriteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = shareIntent.getExtras();
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            mContext.startActivity(intent);
        }
    }
}
