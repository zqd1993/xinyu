package com.mshy.VInterestSpeed.common.bean.video;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangbin on 2020/1/9.
 */
public class VideoVquGuardBean implements Parcelable {

    private int diff_num;
    private int guard_price;

    public int getDiff_num() {
        return diff_num;
    }

    public void setDiff_num(int diff_num) {
        this.diff_num = diff_num;
    }

    public int getGuard_price() {
        return guard_price;
    }

    public void setGuard_price(int guard_price) {
        this.guard_price = guard_price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.diff_num);
        dest.writeInt(this.guard_price);
    }

    public void readFromParcel(Parcel source) {
        this.diff_num = source.readInt();
        this.guard_price = source.readInt();
    }

    public VideoVquGuardBean() {
    }

    protected VideoVquGuardBean(Parcel in) {
        this.diff_num = in.readInt();
        this.guard_price = in.readInt();
    }

    public static final Creator<VideoVquGuardBean> CREATOR = new Creator<VideoVquGuardBean>() {
        @Override
        public VideoVquGuardBean createFromParcel(Parcel source) {
            return new VideoVquGuardBean(source);
        }

        @Override
        public VideoVquGuardBean[] newArray(int size) {
            return new VideoVquGuardBean[size];
        }
    };
}
