package com.mshy.VInterestSpeed.common.bean.notification;


import com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean;

public class NotificationEvent {

    VideoRequestBean mVideoRequestBean;

    public NotificationEvent(VideoRequestBean mVideoRequestBean) {
        this.mVideoRequestBean = mVideoRequestBean;
    }

    public VideoRequestBean getCustomNotification() {
        return mVideoRequestBean;
    }
}
