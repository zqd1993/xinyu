package com.mshy.VInterestSpeed.common.event;

/**
 * FileName: com.live.vquonline.controller.eventBus
 * Author: Reisen
 * Date: 2022/3/9 14:26
 * Description:显示底部完善资料提示的事件
 * History:
 */
public class HomeVquShowBottomEvent {
    private boolean isShow;

    public HomeVquShowBottomEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }
}
