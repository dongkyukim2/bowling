package com.dk.project.post.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Created by dkkim on 2017-04-09.
 */
@Entity(tableName = "TBL_Reply")
public class ReplyModel implements Parcelable {

    @PrimaryKey
    @NotNull
    private String replyId;
    private String postId;
    private String userId;
    private String userName;
    private String userPhoto;
    private String replyContents;
    private String replyDate;

    public ReplyModel(String postId, String replyContents) {
        this.postId = postId;
        this.replyContents = replyContents;
    }

    protected ReplyModel(Parcel in) {
        replyId = in.readString();
        postId = in.readString();
        userId = in.readString();
        userName = in.readString();
        userPhoto = in.readString();
        replyContents = in.readString();
        replyDate = in.readString();
    }

    public static final Creator<ReplyModel> CREATOR = new Creator<ReplyModel>() {
        @Override
        public ReplyModel createFromParcel(Parcel in) {
            return new ReplyModel(in);
        }

        @Override
        public ReplyModel[] newArray(int size) {
            return new ReplyModel[size];
        }
    };

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfile() {
        return userPhoto;
    }

    public void setUserProfile(String userProfile) {
        this.userPhoto = userProfile;
    }

    public String getReplyContents() {
        return replyContents;
    }

    public void setReplyContents(String replyContents) {
        this.replyContents = replyContents;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(replyId);
        dest.writeString(postId);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPhoto);
        dest.writeString(replyContents);
        dest.writeString(replyDate);
    }
}