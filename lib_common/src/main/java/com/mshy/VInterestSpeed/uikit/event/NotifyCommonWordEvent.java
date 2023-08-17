package com.mshy.VInterestSpeed.uikit.event;

import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean;

import java.util.List;

/**
 * FileName: com.live.vquonline.controller.eventBus
 * Author: Reisen
 * Date: 2022/3/8 18:07
 * Description:
 * History:
 */
public class NotifyCommonWordEvent {

    private final List<NIMCommonWordBean> mData;

    public NotifyCommonWordEvent(List<NIMCommonWordBean> data) {
        this.mData = data;
    }

    public List<NIMCommonWordBean> getData() {
        return mData;
    }
}
