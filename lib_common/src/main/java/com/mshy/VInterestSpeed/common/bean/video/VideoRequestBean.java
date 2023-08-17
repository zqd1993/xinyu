package com.mshy.VInterestSpeed.common.bean.video;

import java.io.Serializable;

public class VideoRequestBean implements Serializable {
    private int id;
    private VideoRequestDataBean data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VideoRequestDataBean getData() {
        return data;
    }

    public void setData(VideoRequestDataBean data) {
        this.data = data;
    }
}
