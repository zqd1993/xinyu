package com.live.module.agora.bean;

public class VquGiftBean {
    private String gift_id;
    private int gift_count;
    private int room_id;
    private String gift_type_id;

    public String getGift_type_id() {
        return gift_type_id;
    }

    public void setGift_type_id(String gift_type_id) {
        this.gift_type_id = gift_type_id;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public int getGift_count() {
        return gift_count;
    }

    public void setGift_count(int gift_count) {
        this.gift_count = gift_count;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }
}
