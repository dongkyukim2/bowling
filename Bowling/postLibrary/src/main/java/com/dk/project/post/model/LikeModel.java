package com.dk.project.post.model;

import androidx.room.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * 특정 게시문에 좋아요 눌렀는지
 */
@Entity(tableName = "TBL_POST_LIKE", primaryKeys = {"userId", "postId"})
public class LikeModel {

    @NotNull
    private String userId;

    @NotNull
    private String postId;


    public LikeModel(String userId, String postId) {
        this.userId = userId;
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
