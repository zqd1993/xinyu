package com.mshy.VInterestSpeed.common.bean.video;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class VideoVquPornCheckBeanV2 implements Parcelable {
    private int is_open;
    private int interval;
    private int check_from_user;
    private ArrayList<PornCheckV2> period_array;

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

    public ArrayList<PornCheckV2> getPeriodArray() {
        return period_array;
    }

    public void setPeriodArray(ArrayList<PornCheckV2> period_array) {
        this.period_array = period_array;
    }

    public int getCheck_from_user() {
        return check_from_user;
    }

    public void setCheck_from_user(int check_from_user) {
        this.check_from_user = check_from_user;
    }

    public static class PornCheckV2 implements Parcelable {
        private long start;
        private long end;

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.start);
            dest.writeLong(this.end);
        }

        public void readFromParcel(Parcel source) {
            this.start = source.readLong();
            this.end = source.readLong();
        }

        public PornCheckV2() {
        }

        protected PornCheckV2(Parcel in) {
            this.start = in.readLong();
            this.end = in.readLong();
        }

        public static final Creator<PornCheckV2> CREATOR = new Creator<PornCheckV2>() {
            @Override
            public PornCheckV2 createFromParcel(Parcel source) {
                return new PornCheckV2(source);
            }

            @Override
            public PornCheckV2[] newArray(int size) {
                return new PornCheckV2[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.is_open);
        dest.writeInt(this.interval);
        dest.writeInt(this.check_from_user);
        dest.writeList(this.period_array);
    }

    public void readFromParcel(Parcel source) {
        this.is_open = source.readInt();
        this.interval = source.readInt();
        this.check_from_user = source.readInt();
        this.period_array = new ArrayList<PornCheckV2>();
        source.readList(this.period_array, PornCheckV2.class.getClassLoader());
    }

    public VideoVquPornCheckBeanV2() {
    }

    protected VideoVquPornCheckBeanV2(Parcel in) {
        this.is_open = in.readInt();
        this.interval = in.readInt();
        this.check_from_user = in.readInt();
        this.period_array = new ArrayList<PornCheckV2>();
        in.readList(this.period_array, PornCheckV2.class.getClassLoader());
    }

    public static final Creator<VideoVquPornCheckBeanV2> CREATOR = new Creator<VideoVquPornCheckBeanV2>() {
        @Override
        public VideoVquPornCheckBeanV2 createFromParcel(Parcel source) {
            return new VideoVquPornCheckBeanV2(source);
        }

        @Override
        public VideoVquPornCheckBeanV2[] newArray(int size) {
            return new VideoVquPornCheckBeanV2[size];
        }
    };
}
