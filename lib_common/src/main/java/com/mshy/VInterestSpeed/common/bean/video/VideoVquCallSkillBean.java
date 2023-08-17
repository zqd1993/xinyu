package com.mshy.VInterestSpeed.common.bean.video;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoVquCallSkillBean implements Parcelable {
    private int id;
    private String name;
    private String price;
    private String score;
    private int service_count;
    private int service_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getService_count() {
        return service_count;
    }

    public void setService_count(int service_count) {
        this.service_count = service_count;
    }

    public int getService_time() {
        return service_time;
    }

    public void setService_time(int service_time) {
        this.service_time = service_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.price);
        dest.writeString(this.score);
        dest.writeInt(this.service_count);
        dest.writeInt(this.service_time);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.price = source.readString();
        this.score = source.readString();
        this.service_count = source.readInt();
        this.service_time = source.readInt();
    }

    public VideoVquCallSkillBean() {
    }

    protected VideoVquCallSkillBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readString();
        this.score = in.readString();
        this.service_count = in.readInt();
        this.service_time = in.readInt();
    }

    public static final Creator<VideoVquCallSkillBean> CREATOR = new Creator<VideoVquCallSkillBean>() {
        @Override
        public VideoVquCallSkillBean createFromParcel(Parcel source) {
            return new VideoVquCallSkillBean(source);
        }

        @Override
        public VideoVquCallSkillBean[] newArray(int size) {
            return new VideoVquCallSkillBean[size];
        }
    };
}
