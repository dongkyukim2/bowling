package com.dk.project.post.bowling.retrofit;

import com.dk.project.post.bowling.model.ClubModel;
import com.dk.project.post.bowling.model.ClubUserModel;
import com.dk.project.post.bowling.model.GameModel;
import com.dk.project.post.bowling.model.GameScoreModel;
import com.dk.project.post.bowling.model.ReadGameModel;
import com.dk.project.post.bowling.model.ScoreAvgModel;
import com.dk.project.post.bowling.model.ScoreClubUserModel;
import com.dk.project.post.bowling.model.ScoreModel;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.retrofit.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroBaseApiService {

    String Base_URL = "http://project-dk.iptime.org:8080/";

    @GET("ping")
    Observable<ResponseModel> pingCheck();

    @GET("community/bowling/version/")
    Observable<ResponseModel<String>> getVersion(@Query("appName") String appName);

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

    @POST("community/bowling/club/delete/")
    Observable<ResponseModel<ClubModel>> deleteClub(@Body ClubModel clubModel);

    @GET("community/bowling/club/me/")
    Observable<ResponseModel<ArrayList<ClubModel>>> getSignUpClub();

    @GET("community/bowling/club/recommend/")
    Observable<ResponseModel<ArrayList<ClubModel>>> getRecommendClubList();

    @GET("community/bowling/club/search/")
    Observable<ResponseModel<ArrayList<ClubModel>>> getSearchClubList(@Query("clubName") String clubName, @Query("page") int page);

    @GET("community/bowling/club/user/")
    Observable<ResponseModel<ArrayList<ScoreClubUserModel>>> getClubUserList(@Query("clubId") String clubId);

    @POST("community/bowling/game/")
    Observable<ResponseModel<GameModel>> writeGame(@Body GameModel gameModel);

    @DELETE("community/bowling/game/{gameId}/")
    Observable<ResponseModel<String>> deleteGame(@Path("gameId") String gameId);

    @GET("community/bowling/game/list/")
    Observable<ResponseModel<ArrayList<ReadGameModel>>> getGameList(@Query("clubId") String clubId, @Query("count") int count);

    @GET("community/bowling/game/")
    Observable<ResponseModel<ReadGameModel>> getGame(@Query("gameId") String gameId);

    @GET("community/bowling/game/score/")
    Observable<ResponseModel<ArrayList<GameScoreModel>>> getGameScoreList(@Query("gameId") String gameId);

    @GET("community/bowling/game/user/")
    Observable<ResponseModel<ArrayList<LoginInfoModel>>> getGameUserList(@Query("gameId") String gameId);

    @POST("community/bowling/club/modifyType/")
    Observable<ResponseModel<ClubUserModel>> setModifyClubUserType(@Body ClubUserModel clubUserModel);

}
