package com.mshy.VInterestSpeed.common.event;

/**
 * FileName: com.live.vquonline.controller.eventBus
 * Author: Reisen
 * Date: 2021/9/13 16:22
 * Description:微信支付成功事件
 * History:
 */
public class WxPaySuccessEvent {
    private String payType;

    public WxPaySuccessEvent(String payType) {
        this.payType = payType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
