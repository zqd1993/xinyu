package com.mshy.VInterestSpeed.common.event;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/6/23 10:56
 */
public class CommonBackstageEvent {
    int type;
    int isMatchCancle;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsMatchCancle() {
        return isMatchCancle;
    }

    public void setIsMatchCancle(int isMatchCancle) {
        this.isMatchCancle = isMatchCancle;
    }

    public CommonBackstageEvent(int type, int isMatchCancle) {
        this.type = type;
        this.isMatchCancle = isMatchCancle;
    }

    public CommonBackstageEvent() {
    }
}
