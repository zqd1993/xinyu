package com.mshy.VInterestSpeed.common.bean.video;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangbin on 2019/10/26.
 */

public class VideoVquPornCheckBean implements Parcelable {

    private int is_open;
    private int interval;
    private int period;

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.is_open);
        dest.writeInt(this.interval);
        dest.writeInt(this.period);
    }

    public void readFromParcel(Parcel source) {
        this.is_open = source.readInt();
        this.interval = source.readInt();
        this.period = source.readInt();
    }

    public VideoVquPornCheckBean() {
    }

    protected VideoVquPornCheckBean(Parcel in) {
        this.is_open = in.readInt();
        this.interval = in.readInt();
        this.period = in.readInt();
    }

    public static final Creator<VideoVquPornCheckBean> CREATOR = new Creator<VideoVquPornCheckBean>() {
        @Override
        public VideoVquPornCheckBean createFromParcel(Parcel source) {
            return new VideoVquPornCheckBean(source);
        }

        @Override
        public VideoVquPornCheckBean[] newArray(int size) {
            return new VideoVquPornCheckBean[size];
        }
    };
}
