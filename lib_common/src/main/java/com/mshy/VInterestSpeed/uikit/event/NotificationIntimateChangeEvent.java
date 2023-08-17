package com.mshy.VInterestSpeed.uikit.event;


import com.mshy.VInterestSpeed.uikit.bean.NIMVquIntimateChangeBean;

/**
 * Created by zhangbin on 2018/12/24.
 */

public class NotificationIntimateChangeEvent {

    private NIMVquIntimateChangeBean data;

    public NotificationIntimateChangeEvent(NIMVquIntimateChangeBean data) {
        this.data = data;
    }

    public NIMVquIntimateChangeBean getData() {
        return data;
    }
}
