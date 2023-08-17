package com.mshy.VInterestSpeed.common.bean.video;

import java.io.Serializable;

public class VideoRequestDataBean implements Serializable {

    /**
     *  "title": "您有一个新通知",   //标题
     "content": "通知内容",  //通知内容（暂时无用，备用）
     "link_type":""，//链接类型
     "link_url":"", //链接地址
     "type" => "customSystem"  //类型  customSystem（自定义系统通知）
     */

    private String callTime;
    private String roomid;
    private String title;
    private String content;
    private int link_type;
    private String link_url;
    private String type;
    private String nickname;
    private String avatar;
    private int id;
    private String gift_name;
    private int amount;
    private String tonickname;
    private String toavatar;
    private String giftimg;
    private int gifttotal;
    private int toid;
    private long time;
    private int vip;
    private String vip_icon;

    private String from_nickname;
    private String from_avatar;
    private String to_nickname;
    private String to_avatar;
    private String gift_img;
    private String gift_count;
    private int lock_time;
    private boolean isMatch;

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public String getTo_avatar() {
        return to_avatar;
    }

    public void setTo_avatar(String to_avatar) {
        this.to_avatar = to_avatar;
    }

    public String getGift_img() {
        return gift_img;
    }

    public void setGift_img(String gift_img) {
        this.gift_img = gift_img;
    }

    public String getGift_count() {
        return gift_count;
    }

    public void setGift_count(String gift_count) {
        this.gift_count = gift_count;
    }

    public int getLock_time() {
        return lock_time;
    }

    public void setLock_time(int lock_time) {
        this.lock_time = lock_time;
    }

    public String getVip_icon() {
        return vip_icon;
    }

    public void setVip_icon(String vip_icon) {
        this.vip_icon = vip_icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getGifttotal() {
        return gifttotal;
    }

    public void setGifttotal(int gifttotal) {
        this.gifttotal = gifttotal;
    }

    public String getGiftimg() {
        return giftimg;
    }

    public void setGiftimg(String giftimg) {
        this.giftimg = giftimg;
    }

    public String getToavatar() {
        return toavatar;
    }

    public void setToavatar(String toavatar) {
        this.toavatar = toavatar;
    }

    public String getTonickname() {
        return tonickname;
    }

    public void setTonickname(String tonickname) {
        this.tonickname = tonickname;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLink_type() {
        return link_type;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getToid() {
        return toid;
    }

    public void setToid(int toid) {
        this.toid = toid;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    @Override
    public String toString() {
        return "VideoRequestDataBean{" +
                "callTime='" + callTime + '\'' +
                ", roomid='" + roomid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", link_type=" + link_type +
                ", link_url='" + link_url + '\'' +
                ", type='" + type + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", id=" + id +
                ", giftname='" + gift_name + '\'' +
                ", amount=" + amount +
                ", tonickname='" + tonickname + '\'' +
                ", toavatar='" + toavatar + '\'' +
                ", giftimg='" + giftimg + '\'' +
                ", gifttotal=" + gifttotal +
                ", toid=" + toid +
                ", time=" + time +
                ", vip=" + vip +
                '}';
    }
}
