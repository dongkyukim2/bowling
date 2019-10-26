package com.dk.project.post.bowling.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ClubModel implements Parcelable {

    private String clubId;
    private String clubTitle;
    private String createUserId;
    private String createDate;
    private String clubInfo;
    private String clubImage;
    private int count;
    /*
     0 : 가입 완료
     1 : 클럽 주인 (당연 가립된 상태)태
     2 : 가입 승인대기
     3 : 탈퇴
     4 : 강퇴
     5 : 아직 아무것도 안함 . 신청 안한 상
    */
    private int type;

    public ClubModel(String clubId) {
        this.clubId = clubId;
    }

    public ClubModel() {
    }

    protected ClubModel(Parcel in) {
        clubId = in.readString();
        clubTitle = in.readString();
        createUserId = in.readString();
        createDate = in.readString();
        clubInfo = in.readString();
        clubImage = in.readString();
        count = in.readInt();
        type = in.readInt();
    }

    public static final Creator<ClubModel> CREATOR = new Creator<ClubModel>() {
        @Override
        public ClubModel createFromParcel(Parcel in) {
            return new ClubModel(in);
        }

        @Override
        public ClubModel[] newArray(int size) {
            return new ClubModel[size];
        }
    };

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubTitle() {
        return clubTitle;
    }

    public void setClubTitle(String clubTitle) {
        this.clubTitle = clubTitle;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getClubInfo() {
        return clubInfo;
    }

    public void setClubInfo(String clubInfo) {
        this.clubInfo = clubInfo;
    }

    public String getClubImage() {
        return clubImage;
    }

    public void setClubImage(String clubImage) {
        this.clubImage = clubImage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clubId);
        dest.writeString(clubTitle);
        dest.writeString(createUserId);
        dest.writeString(createDate);
        dest.writeString(clubInfo);
        dest.writeString(clubImage);
        dest.writeInt(count);
        dest.writeInt(type);
    }
}
