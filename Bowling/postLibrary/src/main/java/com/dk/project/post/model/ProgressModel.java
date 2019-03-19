package com.dk.project.post.model;

/**
 * Created by dkkim on 2017-12-31.
 */

public class ProgressModel {

    private int totalCount;
    private int currentCount;

    private long bytesTotal;
    private long bytesCurrent;

    public ProgressModel(int totalCount, int currentCount, long bytesTotal, long bytesCurrent) {
        this.totalCount = totalCount;
        this.currentCount = currentCount;
        this.bytesTotal = bytesTotal;
        this.bytesCurrent = bytesCurrent;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public long getBytesCurrent() {
        return bytesCurrent;
    }

    public void setBytesCurrent(long bytesCurrent) {
        this.bytesCurrent = bytesCurrent;
    }

    public long getBytesTotal() {
        return bytesTotal;
    }

    public void setBytesTotal(long bytesTotal) {
        this.bytesTotal = bytesTotal;
    }

    public int getPercent() {
        int progress = (int) ((100.0 * bytesCurrent) / bytesTotal);
        return progress;
    }
}
