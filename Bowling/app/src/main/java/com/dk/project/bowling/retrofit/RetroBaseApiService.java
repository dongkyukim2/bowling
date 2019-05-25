package com.dk.project.bowling.retrofit;

import com.dk.project.bowling.model.ScoreModel;
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

  @GET("community/bowling/monthAvgDays/")
  Observable<ResponseModel<ArrayList<ScoreModel>>> getScoreMonthAgvDayList(@Query("date") String date);

  @GET("community/bowling/monthAvg/")
  Observable<ResponseModel<ScoreModel>> getScoreMonthAvg();

  @GET("community/bowling/recentAgv/")
  Observable<ResponseModel<ScoreModel>> getScoreRecentAgv(@Query("count") int count);

  @GET("community/bowling/")
  Observable<ResponseModel<List<ScoreModel>>> getScoreList();

  @GET("community/bowling/days/")
  Observable<ResponseModel<ArrayList<ScoreModel>>> getScoreDayList(@Query("userId") String userId, @Query("date") String date);
}
