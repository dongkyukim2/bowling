package com.dk.project.post.retrofit;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public interface SuccessCallback<T> {
    void onSuccess(T receivedData);
}
