package com.dk.project.post.retrofit;


import android.content.Context;
import android.text.TextUtils;

import com.dk.project.post.base.Define;
import com.dk.project.post.model.LoginInfoModel;
import com.dk.project.post.model.MediaSelectListModel;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.model.ReplyModel;
import com.dk.project.post.retrofit.ProgressRequestBody.ProgressListener;
import com.dk.project.post.utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

/**
 * Created by dkkim on 2017-12-12.
 */

public class PostApi implements Define {

    private static PostApi instance;

    private RetroClient retroClient;
    private RetroBaseApiService apiService;

    private PostApi() {
        retroClient = RetroClient.getInstance();
        apiService = RetroClient.getApiService();
    }

    public static PostApi getInstance() {
        if (instance == null) {
            instance = new PostApi();
        }
        return instance;
    }

    // Rx로 조합할때 사용
    public RetroBaseApiService getApiService() {
        return apiService;
    }


    public Disposable signUp(LoginInfoModel loginInfoModel, SuccessCallback<ResponseModel<LoginInfoModel>> callback, ErrorCallback errorCallback) {
        return apiService.signUp(loginInfoModel)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    public Disposable getUserInfo(String userId, SuccessCallback<ResponseModel<LoginInfoModel>> callback, ErrorCallback errorCallback) {
        return apiService.getUserInfo(userId)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable writePost(PostModel postModel, boolean modify, SuccessCallback<ResponseModel> callback, ErrorCallback errorCallback) {
        return apiService.writePost(modify, postModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    public Observable<ResponseModel> getLikeObservable(String postId, boolean value) {
        return apiService.setLike(postId, value);
    }

    public Disposable modifyPost(PostModel postModel, SuccessCallback<ResponseModel<PostModel>> callback, ErrorCallback errorCallback) {
        return apiService.modifyPost(postModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    public Disposable writeReply(ReplyModel replyModel, SuccessCallback<ResponseModel<ArrayList<ReplyModel>>> callback, SuccessCallback<String> alreadyDeleteCallback,
                                 ErrorCallback errorCallback) {
        return apiService.writeReply(replyModel)
                .map(replyModelResponseModel -> {
                    if (!TextUtils.isEmpty(replyModelResponseModel.getMessage()) &&
                            replyModelResponseModel.getMessage().equalsIgnoreCase("ALREADY_DELETE")) {
                        String postId = replyModelResponseModel.getData().getPostId();
                        alreadyDeleteCallback.onSuccess(postId);
                    }
                    return replyModelResponseModel;
                })
                .filter(replyModelResponseModel -> replyModelResponseModel.isSuccess())
                .flatMap(responseModel -> apiService.replyList(0, responseModel.getData().getPostId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    public Disposable getPostList(int page, String clubId, String search, SuccessCallback<ResponseModel<ArrayList<PostModel>>> callback, ErrorCallback errorCallback) {
        return apiService.postList(clubId, search, page)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    public Disposable getReplyList(int page, String postId, SuccessCallback<ResponseModel<ArrayList<ReplyModel>>> callback,
                                   ErrorCallback errorCallback) {
        return apiService.replyList(page, postId)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable getReplyCount(String postId, SuccessCallback<ResponseModel<Object>> callback,
                                    ErrorCallback errorCallback) {
        return apiService.replyCount(postId)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable uploadFile(MultipartBody.Part file, SuccessCallback<ResponseModel<String>> callback,
                                 ErrorCallback errorCallback) {
        return apiService.uploadFile(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable uploadFile(Context context, PostModel postModel, SuccessCallback<Object> callback, ErrorCallback errorCallback,
                                 ProgressListener progressListener) {
        return ImageUtil.compressImageObservable(context, postModel).flatMap(mediaSelectListModels -> {
            return Observable.fromIterable(mediaSelectListModels).map(selectModel -> {

                Part body;
                if (TextUtils.isEmpty(selectModel.getYoutubeUrl()) && !selectModel.getFilePath().startsWith("http")) {
                    File file = new File(selectModel.getFilePath());

                    ProgressRequestBody fileBody = new ProgressRequestBody(file, MediaType.parse("image"), progressListener);
                    body = Part.createFormData("file", file.getName(), fileBody);
                    selectModel.setFilePath(file.getName());
                    progressListener.onUploadStart(selectModel.getOriginalFileName());
                } else {
                    body = Part.createFormData("file", "file", RequestBody.create(MediaType.parse("image"), new File("")));
                }
                return body;
            }).filter(part -> part.body().contentLength() != 0).
                    flatMap(part -> apiService.uploadFile(part).retry(10, throwable -> {
                        Thread.sleep(500);
                        System.out.println("======================    retry");
                        return true;
                    }), 1).map(objectResponseModel -> {
                progressListener.onUploadEnd("uploadEnd");
                return objectResponseModel.getData();
            }).toList();
        }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(callback::onSuccess,
                throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    //todo 전반적인 파일 업로드 로직 수정
    public Disposable test(Context context, ArrayList<MediaSelectListModel> fileList,
                           SuccessCallback<List<String>> callback,
                           ErrorCallback errorCallback,
                           ProgressListener progressListener) {
        return ImageUtil.compressImage(context, fileList).flatMap(mediaSelectListModels ->
                Observable.fromIterable(mediaSelectListModels).filter(selectModel -> {
                    if (TextUtils.isEmpty(selectModel.getYoutubeUrl()) && !selectModel.getFilePath().startsWith("http")) {
                        return true;
                    }
                    return false;
                }).map(selectModel -> {
                    File file = new File(selectModel.getFilePath());
                    ProgressRequestBody fileBody = new ProgressRequestBody(file, MediaType.parse("image"), progressListener);
                    Part part = Part.createFormData("file", file.getName(), fileBody);
                    selectModel.setFilePath(file.getName());
                    progressListener.onUploadStart(selectModel.getOriginalFileName());
                    return part;
                }).filter(part -> part.body().contentLength() != 0)
                        .flatMap(part -> apiService.uploadFile(part).retry(3, throwable -> {
                            Thread.sleep(300);
                            System.out.println("======================    retry");
                            return true;
                        }), 1).map(objectResponseModel -> {
                    progressListener.onUploadEnd("uploadEnd");
                    return objectResponseModel.getData();
                }).toList()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(callback::onSuccess,
                throwable -> retroClient.errorHandling(throwable, errorCallback));
    }


    public Disposable deletePost(String postId, SuccessCallback<ResponseModel<PostModel>> callback,
                                 ErrorCallback errorCallback) {
        return apiService.deletePost(postId)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }

    public Disposable deleteReply(String replyId, SuccessCallback<ResponseModel<ReplyModel>> callback, ErrorCallback errorCallback) {
        return apiService.deleteReply(replyId)
                .doOnError(throwable -> Thread.sleep(1000))
                .retry(3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onSuccess,
                        throwable -> retroClient.errorHandling(throwable, errorCallback));
    }
}
