package com.dk.project.bowling.viewModel;

import android.app.Application;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.dk.project.bowling.model.ScoreModel;
import com.dk.project.bowling.retrofit.BowlingApi;
import com.dk.project.post.base.BaseViewModel;
import com.dk.project.post.retrofit.ResponseModel;
import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public class MainInfoViewModel extends BaseViewModel {


  private final MutableLiveData<ResponseModel<ScoreModel>> recentAvgLiveData = new MutableLiveData<>();
  private final MutableLiveData<ResponseModel<ScoreModel>> monthAvgLiveData = new MutableLiveData<>();
  private final MutableLiveData<Pair<ResponseModel<ArrayList<ScoreModel>>, Boolean>> avgListLiveData =
      new MutableLiveData<>();

  public MainInfoViewModel(@NonNull Application application) {
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

  //처음만 실행
  public void getMainInfoData() {
    BowlingApi.getInstance().getTest(recentAvgLiveData::setValue,
        monthAvgLiveData::setValue,
        receivedData -> avgListLiveData.setValue(new Pair<>(receivedData, true)),
        errorData -> Toast.makeText(mContext, "메인정보 가져오기 실패", Toast.LENGTH_SHORT).show());
  }

  // 하루평균 페이징 처리
  public void getRecentAvgLive(int count) {
    BowlingApi.getInstance().getScoreAvgList(count, receivedData ->
        avgListLiveData.setValue(new Pair<>(receivedData, false)), errorData -> {
      Toast.makeText(mContext, "하루평균 가져오기 실패", Toast.LENGTH_SHORT).show();
    });
  }


  public MutableLiveData<ResponseModel<ScoreModel>> getRecentAvgLiveData() {
    return recentAvgLiveData;
  }

  public MutableLiveData<ResponseModel<ScoreModel>> getMonthAvgLiveData() {
    return monthAvgLiveData;
  }

  public MutableLiveData<Pair<ResponseModel<ArrayList<ScoreModel>>, Boolean>> getAvgListLiveData() {
    return avgListLiveData;
  }
}
