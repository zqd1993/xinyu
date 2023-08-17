package com.mshy.VInterestSpeed.common.event;

import com.mshy.VInterestSpeed.common.bean.CommonVquRedPacketBean;

/**
 * author: Tany
 * date: 2022/5/5
 * description:
 */
public class RedPacketEvent {
    private CommonVquRedPacketBean data;

    public RedPacketEvent(CommonVquRedPacketBean data) {
        this.data = data;
    }

    public CommonVquRedPacketBean getData() {
        return data;
    }
}
