package com.mshy.VInterestSpeed.uikit.event;


import com.mshy.VInterestSpeed.uikit.bean.NIMVquIntimateLeveUpBean;

/**
 * Created by zhangbin on 2018/12/24.
 */

public class NotificationIntimateUpEvent {

    private NIMVquIntimateLeveUpBean data;

    public NotificationIntimateUpEvent(NIMVquIntimateLeveUpBean data) {
        this.data = data;
    }

    public NIMVquIntimateLeveUpBean getData() {
        return data;
    }
}
