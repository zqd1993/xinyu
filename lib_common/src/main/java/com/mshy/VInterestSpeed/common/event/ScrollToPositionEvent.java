package com.mshy.VInterestSpeed.common.event;

/**
 * Created by wenshi on 2018/6/20.
 * Description
 */
public class ScrollToPositionEvent {

    public int position = 0;
    public int delayDuration = 0;
    public boolean delayEnable;
    public OnRegionListener listener;
    public int[] arr=new int[5];

    public ScrollToPositionEvent(int position, int delayDuration, boolean delayEnable,int[] arrs,OnRegionListener listener) {
        this.position = position;
        this.delayDuration = delayDuration;
        this.delayEnable = delayEnable;
        this.listener = listener;
        this.arr = arrs;
    }

    public interface OnRegionListener {
        void onRegion(int l, int t, int r, int b, int w, int h);
    }

}
