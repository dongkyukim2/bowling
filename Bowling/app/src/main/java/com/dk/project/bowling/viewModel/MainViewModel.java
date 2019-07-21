package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ScoreModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.ui.activity.WriteActivity;
import com.dk.project.post.utils.RxBus;
import com.dk.project.post.utils.Utils;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainViewModel extends BaseViewModel {

    //  Note : 메인스레드에서 LiveData를 갱신하려면 반드시 setValue(T) 메서드를 호출해야 한다.
    // 만약, 작업스레드에서 LiveData를 갱신하려면 postValue(T) 메서드를 호출해야 한다.

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCreated() {
        super.onCreated();
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
                receivedData -> {
                    RxBus.getInstance().eventPost(Pair.create(Define.EVENT_REFRESH_SCORE, receivedData.getData()));
                },
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
