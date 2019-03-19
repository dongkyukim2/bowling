package com.dk.project.post.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MediaSelectListModel implements Parcelable {

    private long postIdx;
    private String postId;
    private String coverId;
    private String filePath;
    private String originalFileName;

    private int orientation;
    private int width;
    private int height;

    private boolean isGif;
    private boolean isWebp;
    private String youtubeUrl;

    private int sort = 1;

    public MediaSelectListModel() {
    }

    public MediaSelectListModel(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }


    protected MediaSelectListModel(Parcel in) {
        postIdx = in.readLong();
        postId = in.readString();
        coverId = in.readString();
        filePath = in.readString();
        originalFileName = in.readString();
        orientation = in.readInt();
        width = in.readInt();
        height = in.readInt();
        isGif = in.readByte() != 0;
        isWebp = in.readByte() != 0;
        youtubeUrl = in.readString();
        sort = in.readInt();
    }

    public static final Creator<MediaSelectListModel> CREATOR = new Creator<MediaSelectListModel>() {
        @Override
        public MediaSelectListModel createFromParcel(Parcel in) {
            return new MediaSelectListModel(in);
        }

        @Override
        public MediaSelectListModel[] newArray(int size) {
            return new MediaSelectListModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(postIdx);
        parcel.writeString(postId);
        parcel.writeString(coverId);
        parcel.writeString(filePath);
        parcel.writeString(originalFileName);
        parcel.writeInt(orientation);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeByte((byte) (isGif ? 1 : 0));
        parcel.writeByte((byte) (isWebp ? 1 : 0));
        parcel.writeString(youtubeUrl);
        parcel.writeInt(sort);
    }

    public boolean isWebp() {
        return isWebp;
    }

    public void setWebp(boolean webp) {
        isWebp = webp;
    }

    public long getPostIdx() {
        return postIdx;
    }

    public void setPostIdx(long postIdx) {
        this.postIdx = postIdx;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getConvertWidth() {
        return (orientation == 90 || orientation == 270) ? height : width;
    }

    public int getConvertHeight() {
        return (orientation == 90 || orientation == 270) ? width : height;
    }

    public void setMinusOrder() {
        sort--;
    }



    @Override
    public int describeContents() {
        return 0;
    }
}