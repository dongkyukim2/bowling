package com.dk.project.post.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Created by dkkim on 2017-10-04.
 */
@Entity(tableName = "TBL_POST")
public class PostModel implements Parcelable {

    private long idx;
    private String userId;
    private String userName;
    private String userPhoto;
    @PrimaryKey
    @NotNull
    private String postId;
    private String inputText;
    private String writeDate;
    private int replyCount;
    private int likeCount;
    private int likeSelected;

    @Ignore
    private ArrayList<MediaSelectListModel> imageList = new ArrayList<>();
    @Ignore
    private ArrayList<ReplyModel> replyList = new ArrayList<>();
    @Ignore
    private int currentPosition;

    public PostModel() {
    }

    protected PostModel(Parcel in) {
        idx = in.readLong();
        userId = in.readString();
        userName = in.readString();
        userPhoto = in.readString();
        postId = in.readString();
        inputText = in.readString();
        likeCount = in.readInt();
        likeSelected = in.readInt();
        writeDate = in.readString();
        replyCount = in.readInt();
        currentPosition = in.readInt();
        imageList = in.createTypedArrayList(MediaSelectListModel.CREATOR);
        replyList = in.createTypedArrayList(ReplyModel.CREATOR);
    }

    public static final Creator<PostModel> CREATOR = new Creator<PostModel>() {
        @Override
        public PostModel createFromParcel(Parcel in) {
            return new PostModel(in);
        }

        @Override
        public PostModel[] newArray(int size) {
            return new PostModel[size];
        }
    };

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
        this.idx = idx;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public ArrayList<MediaSelectListModel> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<MediaSelectListModel> list) {
        imageList.clear();
        imageList.addAll(list);
    }
    public void setReplyList(ArrayList<ReplyModel> list) {
        replyList.clear();
        replyList.addAll(list);
    }

    public ArrayList<ReplyModel> getReplyList() {
        return replyList;
    }

    public void sumLikeCount() {
        likeCount++;
        System.out.println("==============  sumLikeCount  "+likeCount);
    }

    public void subLikeCount() {
        likeCount--;
        System.out.println("==============  subLikeCount  "+likeCount);
    }

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLikeSelected() {
        return likeSelected > 0;
    }

    public void setLikeSelected(boolean likeSelected) {
        if (likeSelected) {
            this.likeSelected = 1;
        } else {
            this.likeSelected = 0;
        }
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idx);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userPhoto);
        dest.writeString(postId);
        dest.writeString(inputText);
        dest.writeInt(likeCount);
        dest.writeInt(likeSelected);
        dest.writeString(writeDate);
        dest.writeInt(replyCount);
        dest.writeInt(currentPosition);
        dest.writeTypedList(imageList);
        dest.writeTypedList(replyList);
    }
}
