package com.dk.project.post.impl;

import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import com.dk.project.post.model.PostModel;
import com.dk.project.post.retrofit.ErrorCallback;
import com.dk.project.post.retrofit.SuccessCallback;
import java.util.ArrayList;

/**
 * Created by dkkim on 2017-10-04.
 */

public interface ContentsListener {
    void onLikeClick(AppCompatImageView imageView, TextView textView, PostModel postModel);

    void requestPostList(String userId, SuccessCallback<ArrayList<PostModel>> callback, ErrorCallback errorCallback);

    void requestSearchPostList(String userId, String search, SuccessCallback<ArrayList<PostModel>> callback, ErrorCallback errorCallback);

}
