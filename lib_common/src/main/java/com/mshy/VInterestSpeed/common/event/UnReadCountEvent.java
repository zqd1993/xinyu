package com.mshy.VInterestSpeed.common.event;

/**
 * Created by zhangbin on 2018/12/15.
 */

public class UnReadCountEvent {

    private final int count;

    public UnReadCountEvent(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}
