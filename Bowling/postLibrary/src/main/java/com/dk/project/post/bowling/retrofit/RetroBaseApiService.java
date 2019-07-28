package com.dk.project.post.bowling.retrofit;

import com.dk.project.post.bowling.model.*;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.ResponseModel;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.ArrayList;
import java.util.List;

public interface RetroBaseApiService {

    String Base_URL = "http://project-dk.iptime.org:8080/";

    @GET("ping")
    Observable<ResponseModel> pingCheck();

    @POST("community/bowling/")
    Observable<ResponseModel<ScoreModel>> writeScore(@Body ScoreModel scoreModel);

    @GET("community/bowling/avg/")
    Observable<ResponseModel<ArrayList<ScoreModel>>> getScoreAvgList(@Query("count") int count);

    // 특정월의 평균, 최소 최대 점수와 그에 따른 목록
    @GET("community/bowling/monthAvgDays/")
    Observable<ResponseModel<ScoreAvgModel>> getScoreMonthAgvDayList(@Query("date") String date);

    @GET("community/bowling/monthAvg/")
    Observable<ResponseModel<ScoreModel>> getScoreMonthAvg();

    @GET("community/bowling/recentAgv/")
    Observable<ResponseModel<ScoreModel>> getScoreRecentAgv(@Query("count") int count);

    @GET("community/bowling/")
    Observable<ResponseModel<List<ScoreModel>>> getScoreList();

    @GET("community/bowling/days/")
    Observable<ResponseModel<ArrayList<ScoreModel>>> getScoreDayList(@Query("date") String date);

    @POST("community/bowling/club/create/")
    Observable<ResponseModel<ClubModel>> createClub(@Body ClubModel clubModel);

    @GET("community/bowling/club/me/")
    Observable<ResponseModel<ArrayList<ClubModel>>> getSignUpClub();

    @GET("community/bowling/club/recommend/")
    Observable<ResponseModel<ArrayList<ClubModel>>> getRecommendClubList();

    @GET("community/bowling/club/user/")
    Observable<ResponseModel<ArrayList<UserModel>>> getClubUserList(@Query("clubId") String clubId);

    @POST("community/bowling/game/")
    Observable<ResponseModel<GameModel>> writeGame(@Body GameModel gameModel);

    @GET("community/bowling/game/")
    Observable<ResponseModel<ArrayList<ReadGameModel>>> getGameAndScoreList(@Query("clubId") String clubId, @Query("count") int count);

    @GET("community/bowling/game/user/")
    Observable<ResponseModel<ArrayList<LoginInfoModel>>> getGameUserList(@Query("gameId") String gameId);

}