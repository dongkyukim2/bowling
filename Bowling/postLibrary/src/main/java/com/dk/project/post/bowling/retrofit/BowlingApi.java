package com.dk.project.post.bowling.retrofit;

import androidx.core.util.Pair;

import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ClubUserModel;
import com.dk.project.post.bowling.model.GameModel;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.model.ScoreAvgModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.bowling.model.ScoreModel;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.ErrorCallback;
import com.dk.project.post.retrofit.ResponseModel;
import com.dk.project.post.retrofit.SuccessCallback;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dkkim on 2017-12-12.
 */

public class BowlingApi {

    private static BowlingApi instance;

    private RetroClient retroClient;
    private RetroBaseApiService apiService;

    private BowlingApi() {
        retroClient = RetroClient.getInstance();
        apiService = RetroClient.getApiService();
    }

    public static BowlingApi getInstance() {
        if (instance == null) {
            instance = new BowlingApi();
        }
        return instance;
    }

    // Rx로 조합할때 사용
    public RetroBaseApiService getApiService() {
        return apiService;
    }

    // 클럽에 가입한 유저 목록
    public Disposable getClubUserList(String clubId,
                                      SuccessCallback<ResponseModel<ArrayList<ScoreClubUserModel>>> callback,
                                      ErrorCallback errorCallback) {
        return apiService.getClubUserList(clubId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    // 클럽 만들기
    public Disposable createClub(ClubModel clubModel,
                                 SuccessCallback<ResponseModel<ClubModel>> callback,
                                 ErrorCallback errorCallback) {
        return apiService.createClub(clubModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 클럽 삭제
    public Disposable deleteClub(ClubModel clubModel,
                                 SuccessCallback<ResponseModel<ClubModel>> callback,
                                 ErrorCallback errorCallback) {
        return apiService.deleteClub(clubModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 내가 가입한 클럽 목록
    public Disposable getSignUpClubList(SuccessCallback<ResponseModel<ArrayList<ClubModel>>> callback,
                                        ErrorCallback errorCallback) {
        return apiService.getSignUpClub()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 추천하는 클럽 목록
    public Disposable getRecommendClubList(SuccessCallback<ResponseModel<ArrayList<ClubModel>>> callback,
                                           ErrorCallback errorCallback) {
        return apiService.getRecommendClubList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable getSearchClubList(String clubId, int page, SuccessCallback<ResponseModel<ArrayList<ClubModel>>> callback,
                                        ErrorCallback errorCallback) {
        return apiService.getSearchClubList(clubId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 내가 가입하고 추천하는 클럽 목록
    public void getSignUpAndRecommendClubList(SuccessCallback<Pair<ResponseModel<ArrayList<ClubModel>>, ResponseModel<ArrayList<ClubModel>>>> callback, ErrorCallback errorCallback) {
        Observable.zip(apiService.getSignUpClub().subscribeOn(Schedulers.io()),
                apiService.getRecommendClubList().subscribeOn(Schedulers.io()),
                (signUpClubList, recommendClubList) -> Pair.create(signUpClubList, recommendClubList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess, throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 점수 등록
    public Disposable writeScore(ScoreModel scoreModel,
                                 SuccessCallback<ResponseModel<ScoreModel>> callback,
                                 ErrorCallback errorCallback) {
        return apiService.writeScore(scoreModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 점수 목록
    public Disposable getScoreList(
            SuccessCallback<ResponseModel<List<ScoreModel>>> callback,
            ErrorCallback errorCallback) {
        return apiService.getScoreList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 일 평균, 최소, 최대 점수목록
    public Disposable getScoreAvgList(int count,
                                      SuccessCallback<ResponseModel<ArrayList<ScoreModel>>> callback,
                                      ErrorCallback errorCallback) {
        return apiService.getScoreAvgList(count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    // 이번달 평균, 최소, 최대 점수
    public Disposable getScoreMonthAvg(
            SuccessCallback<ResponseModel<ScoreModel>> callback,
            ErrorCallback errorCallback) {
        return apiService.getScoreMonthAvg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 월별 평균, 최대, 최소 목록 (일단위)
    public Disposable getScoreMonthAgvDayList(String date,
                                              SuccessCallback<ResponseModel<ScoreAvgModel>> callback,
                                              ErrorCallback errorCallback) {
        return apiService.getScoreMonthAgvDayList(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 일평균 목록과 이번달 평균 목록 요청
    public Disposable getTest(SuccessCallback<ResponseModel<ScoreModel>> recentAgvCallback,
                              SuccessCallback<ResponseModel<ScoreModel>> monthAvgCallback,
                              SuccessCallback<ResponseModel<ArrayList<ScoreModel>>> avgListCallback,
                              ErrorCallback errorCallback) {
        return Observable.zip(apiService.getScoreRecentAgv(10).retry(3).subscribeOn(Schedulers.io()),
                apiService.getScoreMonthAvg().retry(3).subscribeOn(Schedulers.io()),
                apiService.getScoreAvgList(0).retry(3).subscribeOn(Schedulers.io()),
                (recentAgvModel, monthAvgModel, avgListModel) ->
                        new Object[]{recentAgvModel, monthAvgModel, avgListModel}).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(objects -> {
                    recentAgvCallback.onSuccess((ResponseModel<ScoreModel>) objects[0]);
                    monthAvgCallback.onSuccess((ResponseModel<ScoreModel>) objects[1]);
                    avgListCallback.onSuccess((ResponseModel<ArrayList<ScoreModel>>) objects[2]);
                }, throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 특정일 점수 목록
    public Disposable getScoreDayList(String date,
                                      SuccessCallback<ResponseModel<ArrayList<ScoreModel>>> callback,
                                      ErrorCallback errorCallback) {
        return apiService.getScoreDayList(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    // 클럽 게임만들기 및 점수 등록
    public Disposable setGameAndScoreList(GameModel gameModel,
                                          SuccessCallback<ResponseModel<GameModel>> callback,
                                          ErrorCallback errorCallback) {
        return apiService.writeGame(gameModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 클럽 게임 삭제
    public Disposable requestDeleteGame(String gameId,
                                        SuccessCallback<ResponseModel<String>> callback,
                                        ErrorCallback errorCallback) {
        return apiService.deleteGame(gameId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    // 클럽 게임 및 점수 목록
    public Disposable getGameAndScoreList(String clubId, int count,
                                          SuccessCallback<ResponseModel<ArrayList<ReadGameModel>>> callback,
                                          ErrorCallback errorCallback) {
        return apiService.getGameAndScoreList(clubId, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 클럽 게임 및 점수
    public Disposable getGameAndScore(String gameId,
                                      SuccessCallback<ResponseModel<ReadGameModel>> callback,
                                      ErrorCallback errorCallback) {
        return apiService.getGameAndScore(gameId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 클럽 게임에 참여한 유저 목록
    public Disposable getGameUserList(String gameId,
                                      SuccessCallback<ResponseModel<ArrayList<LoginInfoModel>>> callback,
                                      ErrorCallback errorCallback) {
        return apiService.getGameUserList(gameId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable getGameAndUserList(String gameId, SuccessCallback<Pair<ResponseModel<ArrayList<LoginInfoModel>>, ResponseModel<ReadGameModel>>> callback,
                                         ErrorCallback errorCallback) {

        return Observable.zip(apiService.getGameUserList(gameId), apiService.getGameAndScore(gameId), (arrayListResponseModel, readGameModelResponseModel)
                -> new Pair<>(arrayListResponseModel, readGameModelResponseModel)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    // 클럽 가입하거나 상태값 변경
    public Disposable setModifyClubUserType(ClubUserModel clubUserModel,
                                            SuccessCallback<ResponseModel<ClubUserModel>> callback,
                                            ErrorCallback errorCallback) {
        return apiService.setModifyClubUserType(clubUserModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

}
