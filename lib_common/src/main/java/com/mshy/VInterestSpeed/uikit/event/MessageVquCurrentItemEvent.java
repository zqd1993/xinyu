package com.mshy.VInterestSpeed.uikit.event;

/**
 * FileName: com.live.vquonline.controller.eventBus
 * Author: Reisen
 * Date: 2022/3/9 19:19
 * Description:
 * History:
 */
public class MessageVquCurrentItemEvent {

    private final int currentItemPos;

    public MessageVquCurrentItemEvent(int currentItemPos) {
        this.currentItemPos = currentItemPos;
    }

    public int getCurrentItemPos() {
        return currentItemPos;
    }
}
