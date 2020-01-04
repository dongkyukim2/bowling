package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.dk.project.bowling.R;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.base.Define;
import com.dk.project.post.bowling.model.ScoreAvgModel;
import com.dk.project.post.bowling.retrofit.BowlingApi;
import com.dk.project.post.bowling.retrofit.MutableLiveDataManager;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.utils.AlertDialogUtil;
import com.dk.project.post.utils.RxBus;

import java.util.Calendar;

public class GraphViewModel extends BaseViewModel {

    private Calendar calendar = Calendar.getInstance();

    private MutableLiveData<Integer> viewTypeLiveData = new MutableLiveData<>();
    private MutableLiveData<ScoreAvgModel> scoreAvgModelMutableLiveData;
    private boolean graphLoading;

    public GraphViewModel(@NonNull Application application) {
        super(application);
        executeRx(RxBus.getInstance().registerRxObserver(integerObjectPair -> {
            switch (integerObjectPair.first) {
                case Define.EVENT_REFRESH_SCORE:
                case Define.EVENT_LOGIN_SUCCESS:
                    getMonthAvgList();
                    break;
            }


        }));
    }

    @Override
    protected void onCreated() {
        super.onCreated();

        scoreAvgModelMutableLiveData = MutableLiveDataManager.getInstance().getScoreMonthAvgList();

        if (LoginManager.getInstance().getLoginInfoModel() == null) {
            return;
        }
        getMonthAvgList();

    }

    @Override
    public void onThrottleClick(View view) {
        if (LoginManager.getInstance().getLoginInfoModel() == null) {
            AlertDialogUtil.showLoginAlertDialog(mContext);
            return;
        }
        if (graphLoading && (view.getId() == R.id.prev_month || view.getId() == R.id.next_month)) {
            return;
        }
        switch (view.getId()) {
            case R.id.prev_month:
                calendar.add(Calendar.MONTH, -2);
            case R.id.next_month:
                graphLoading = true;
                calendar.add(Calendar.MONTH, 1);
                executeRx(BowlingApi.getInstance().getScoreMonthAgvDayList(getYearMonth(),
                        receivedData -> {
                            scoreAvgModelMutableLiveData.setValue(receivedData.getData());
                            graphLoading = false;
                        }, errorData -> graphLoading = false));
                break;
            case R.id.graph_view_type:
                AlertDialogUtil
                        .showListAlertDialog(mContext, null, new String[]{"평균점수", "최대점수", "최소점수"},
                                (dialog, which) -> viewTypeLiveData.setValue(which));
                break;
        }
    }

    private void getMonthAvgList() {
        graphLoading = true;
        executeRx(BowlingApi.getInstance().getScoreMonthAgvDayList(getYearMonth(),
                receivedData -> {
                    scoreAvgModelMutableLiveData.setValue(receivedData.getData());
                    graphLoading = false;
                }, errorData -> graphLoading = false));
    }

    public String getYearMonth() {
        return calendar.get(Calendar.YEAR) + "-" + String
                .format("%02d", (calendar.get(Calendar.MONTH) + 1));
    }


    public MutableLiveData<Integer> getViewTypeLiveData() {
        return viewTypeLiveData;
    }
}
