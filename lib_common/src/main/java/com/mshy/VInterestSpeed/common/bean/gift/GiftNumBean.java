package com.mshy.VInterestSpeed.common.bean.gift;


import java.util.List;

public class GiftNumBean {
    /**
     * coin : 1366290
     * gift_list : [{"id":1,"type":0,"svga":"","md5_string":"","name":"玫瑰花","price":100,"img":"images/gift/ic_video_gift1.png","sign":"限时","num":5}]
     */

    private int coin;
    private List<DialogGiftBean> gift_list;

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public List<DialogGiftBean> getGift_list() {
        return gift_list;
    }

    public void setGift_list(List<DialogGiftBean> gift_list) {
        this.gift_list = gift_list;
    }


}
