package com.mshy.VInterestSpeed.common.bean.gift;

import java.util.ArrayList;
import java.util.List;

public class GiftListBean {
    private List<DialogGiftBean> gift_list = new ArrayList<>();
    private String coin;
    /**
     * code : 0
     * message : 获取成功!
     * time : 1536985916
     * data : {"gift_list":[{"id":1,"name":"玫瑰花","price":88,"img":"images/gift/video_gift1.png"},{"id":2,"name":"棒棒糖","price":188,"img":"images/gift/video_gift2.png"},{"id":4,"name":"蓝玫瑰","price":888,"img":"images/gift/video_gift3.png"}],"coin":210}
     */

    private int code;
    private String message;
    private String time;


    public List<DialogGiftBean> getGift_list() {
        return gift_list;
    }

    public void setGift_list(List<DialogGiftBean> gift_list) {
        this.gift_list = gift_list;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
