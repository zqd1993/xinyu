package com.mshy.VInterestSpeed.common.bean;

/**
 * FileName: com.live.vquonline.model.common
 * Author: Reisen
 * Date: 2022/1/10 18:45
 * Description:分享实体类
 * History:
 */
public class ShareNewBean {
    //分享标题
    private String title;
    //封面图
    private String thumbUrl;
    //描述
    private String des;
    //分享链接
    private String linkUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}
