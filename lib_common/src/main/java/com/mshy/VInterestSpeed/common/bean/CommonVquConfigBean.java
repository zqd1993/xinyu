package com.mshy.VInterestSpeed.common.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommonVquConfigBean {
    private String sysId;
    private String servId;
    private String image_server;
    private String vt_watermark_open;
    private List<Integer> serviceId;
    private int free;
    //显示广告的时间
    private int timeOut;
    //2 微信支付关闭 1 微信支付打开
    private int wechat_open;
    //2 直播关闭 1 直播打开
    private int is_live;

    private int switch_index = 1;
    //女用户挂断提示
    private int woman_hang_up_tips = 0;
    //是否音频最小化0关1开
    private String is_audio_video_mini = "0";
    @SerializedName("no_info")
    private List<String> noInfoIdList;
    //需要自动置顶的消息id
    @SerializedName("top_list")
    private List<String> topInfoIdList;

    public List<String> getNoInfoIdList() {
        return noInfoIdList;
    }

    public void setNoInfoIdList(List<String> noInfoIdList) {
        this.noInfoIdList = noInfoIdList;
    }

    public List<String> getTopInfoIdList() {
        return topInfoIdList;
    }

    public void setTopInfoIdList(List<String> topInfoIdList) {
        this.topInfoIdList = topInfoIdList;
    }

    public int getSwitch_index() {
        return switch_index;
    }

    public void setSwitch_index(int switch_index) {
        this.switch_index = switch_index;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getServId() {
        return servId;
    }

    public void setServId(String servId) {
        this.servId = servId;
    }

    public String getImage_server() {
        return image_server;
    }

    public void setImage_server(String image_server) {
        this.image_server = image_server;
    }

    public String getVt_watermark_open() {
        return vt_watermark_open;
    }

    public void setVt_watermark_open(String vt_watermark_open) {
        this.vt_watermark_open = vt_watermark_open;
    }

    public int getWechat_open() {
        return wechat_open;
    }

    public void setWechat_open(int wechat_open) {
        this.wechat_open = wechat_open;
    }

    public int getIs_live() {
        return is_live;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public List<Integer> getServiceId() {
        return serviceId;
    }

    public void setServiceId(List<Integer> serviceId) {
        this.serviceId = serviceId;
    }

    public int getWoman_hang_up_tips() {
        return woman_hang_up_tips;
    }

    public void setWoman_hang_up_tips(int woman_hang_up_tips) {
        this.woman_hang_up_tips = woman_hang_up_tips;
    }

    public boolean getIs_audio_video_mini_open() {
        return Integer.parseInt(is_audio_video_mini) == 1;
    }

    public void setIs_audio_video_mini(String is_audio_video_mini) {
        this.is_audio_video_mini = is_audio_video_mini;
    }
}
