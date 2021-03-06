package com.dk.project.post.retrofit;

import com.dk.project.post.model.LikeModel;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.model.TokenModel;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import java.util.ArrayList;

public interface RetroBaseApiService {

//    String Base_URL = "http://project-dk.iptime.org:8081/";
    String Base_URL = "http://133.186.143.97:8081/";

    @GET("ping")
    Observable<ResponseModel> pingCheck();


    @POST("community/image/token/")
    Observable<ResponseModel<TokenModel>> getToken();


    @POST("community/post/")
    Observable<ResponseModel<PostModel>> writePost(@Header("modify") boolean modify, @Body PostModel parameters);

    @DELETE("community/post/{postId}/")
    Observable<ResponseModel<PostModel>> deletePost(@Path("postId") String postId);

    @GET("community/post/")
    Observable<ResponseModel<ArrayList<PostModel>>> postList(@Query("clubId") String clubId, @Query("search") String search, @Query("page") int page);

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
    Observable<ResponseModel<String>> uploadFile(@Part MultipartBody.Part file);

    @Multipart
    @PUT
    Observable<ResponseBody> uploadImage(@Url String url, @Header("X-Auth-Token") String token, @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("community/post/like/")
    Observable<ResponseModel> setLike(@Field("postId") String postId, @Field("check") boolean check);


    @POST("community/signUp/")
    Observable<ResponseModel<LoginInfoModel>> signUp(@Body LoginInfoModel parameters);

    @GET("community/alreadySignUp/")
    Observable<ResponseModel<LoginInfoModel>> getUserInfo(@Query("userId") String userId);


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
