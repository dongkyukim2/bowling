package com.dk.project.post.retrofit;

import com.dk.project.post.model.LikeModel;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.model.ReplyModel;
import io.reactivex.Observable;
import java.util.ArrayList;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetroBaseApiService {

  String Base_URL = "http://project-dk.iptime.org:8081/";

  @GET("ping")
  Observable<ResponseModel> pingCheck();


  @POST("community/post/")
  Observable<ResponseModel<PostModel>> writePost(@Header("modify") boolean modify, @Body PostModel parameters);

  @DELETE("community/post/{postId}/")
  Observable<ResponseModel<PostModel>> deletePost(@Path("postId") String postId);

  @GET("community/post/")
  Observable<ResponseModel<ArrayList<PostModel>>> postList(@Query("page") int page, @Query("search") String search);

  @POST("community/reply/")
  Observable<ResponseModel<ReplyModel>> writeReply(@Body ReplyModel parameters);


  @DELETE("community/reply/{replyId}/")
  Observable<ResponseModel<ReplyModel>> deleteReply(@Path("replyId") String replyId);


  @GET("community/reply/")
  Observable<ResponseModel<ArrayList<ReplyModel>>> replyList(@Query("page") int page, @Query("postId") String postId);

  @GET("community/reply/count/")
  Observable<ResponseModel<Object>> replyCount(@Query("postId") String postId);


  @Multipart
  @POST("community/image/")
  Observable<ResponseModel<Object>> uploadFile(@Part MultipartBody.Part file);


  @FormUrlEncoded
  @POST("community/post/like/")
  Observable<ResponseModel> setLike(@Field("postId") String postId, @Field("check") boolean check);



  @POST("community/signUp/")
  Observable<ResponseModel<LoginInfoModel>> signUp(@Body LoginInfoModel parameters);








  @POST("modifyPost")
  Observable<ResponseModel<PostModel>> modifyPost(@Body PostModel parameters);


















//  @POST("insertLike")
//  Observable<ResponseModel> setLike(@Body LikeModel parameters);

  @POST("deleteLike")
  Observable<ResponseModel> deleteLike(@Body LikeModel parameters);


  @POST("post/")
  @Headers({"content-type: application/json;charset=UTF-8"})
  Observable<String> test(@Body PostModel parameters);


  @GET("post/")
  @Headers({"content-type: application/json;charset=UTF-8"})
  Observable<PostModel> test2(@Query("postId") String postId);
}
