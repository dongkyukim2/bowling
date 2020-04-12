package com.dk.project.post.bowling.retrofit;

import com.dk.project.post.BuildConfig;
import com.dk.project.post.manager.LoginManager;
import com.dk.project.post.retrofit.ErrorCallback;
import com.dk.project.post.retrofit.ErrorResponse;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {

    public static String baseUrl = RetroBaseApiService.Base_URL;

    private final int CONNECT_TIMEOUT = 3;
    private final int WRITE_TIMEOUT = 3;
    private final int READ_TIMEOUT = 3;

    private static RetroClient instance;


    private RetroBaseApiService apiService;
    private Retrofit retrofit;


    private RetroClient() {
        if (retrofit == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
            }
            builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS) //쓰기 타임아웃 시간 설정
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS) //읽기 타임아웃 시간 설정
                    .connectionPool(new ConnectionPool(10,10,TimeUnit.SECONDS))
                    .addInterceptor((chain) -> {
                        Request.Builder requestBuilder = chain.request().newBuilder();
                        String userId = "";
                        if (LoginManager.getInstance().isLogIn()) {
                            userId = LoginManager.getInstance().getEncodeId();
                        }
                        requestBuilder.addHeader("userId", userId);
                        return chain.proceed(requestBuilder.build());
                    }).addNetworkInterceptor(new StethoInterceptor());

            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .build();
            apiService = retrofit.create(RetroBaseApiService.class);
        }
    }

    public static RetroClient getInstance() {
        if (instance == null) {
            instance = new RetroClient();
        }
        return instance;
    }

    public static RetroBaseApiService getApiService() {
        if (instance == null) {
            instance = new RetroClient();
        }
        return instance.apiService;
    }

    public static void clear() {
        instance = null;
    }

    public void errorHandling(Throwable throwable, ErrorCallback errorCallback) {
        System.out.println("==========     error     ================ " + throwable.getMessage());
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            System.out.println(stackTraceElement.getFileName() + "   " + stackTraceElement.getMethodName() + "   " + stackTraceElement.getLineNumber());
        }
        ErrorResponse errorResponse;
        if (throwable instanceof HttpException) {
            ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
            errorResponse = new ErrorResponse(1, 1, getErrorMessage(responseBody));

            HttpException httpException = (HttpException) throwable;
            int statusCode = httpException.code();
            // handle different HTTP error codes here (4xx)

        } else if (throwable instanceof SocketTimeoutException) {  // handle timeout from Retrofit
            errorResponse = new ErrorResponse(1, throwable.getMessage());
        } else if (throwable instanceof IOException) {  // file was not found, do something
            errorResponse = new ErrorResponse(1, throwable.getMessage());
        } else {
            errorResponse = new ErrorResponse(1, throwable.getMessage());// generic error handling
        }
        errorCallback.onError(errorResponse);
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
