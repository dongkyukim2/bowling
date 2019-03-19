package com.dk.project.post.model;

public class MediaSelectModel {
    public static final int ALBUM_ITEM = 0;
    public static final int ALBUM_ALL = 1;

    private Long coverId = 0L;
    private Long duration;
    private String path;
    private String bucketId = "";
    private String name = "";
    private int albumCount = 0;
    private int type = ALBUM_ITEM;

    private boolean isGif;

    public Long getCoverId() {
        return coverId;
    }

    public void setCoverId(Long coverId) {
        this.coverId = coverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(int albumCount) {
        this.albumCount = albumCount;
    }

    public void increaseCount() {
        albumCount++;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

    public void setPath(String path) {
        this.path = path;
    }

}