package com.mshy.VInterestSpeed.common.bean.video;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoVquFairyBean implements Parcelable {
    private String price;
    private int diff_num;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDiff_num() {
        return diff_num;
    }

    public void setDiff_num(int diff_num) {
        this.diff_num = diff_num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeInt(this.diff_num);
    }

    public void readFromParcel(Parcel source) {
        this.price = source.readString();
        this.diff_num = source.readInt();
    }

    public VideoVquFairyBean() {
    }

    protected VideoVquFairyBean(Parcel in) {
        this.price = in.readString();
        this.diff_num = in.readInt();
    }

    public static final Creator<VideoVquFairyBean> CREATOR = new Creator<VideoVquFairyBean>() {
        @Override
        public VideoVquFairyBean createFromParcel(Parcel source) {
            return new VideoVquFairyBean(source);
        }

        @Override
        public VideoVquFairyBean[] newArray(int size) {
            return new VideoVquFairyBean[size];
        }
    };
}
