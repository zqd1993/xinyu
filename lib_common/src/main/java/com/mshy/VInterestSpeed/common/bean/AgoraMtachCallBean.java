package com.mshy.VInterestSpeed.common.bean;

import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/6/11 18:04
 */
public class AgoraMtachCallBean {

    String status;
    String uid;
    String type;
    VideoVquCallBean data;
    int match_id;
    String send_anchor_uid;
    String avatar;
    String nickname;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public String getSend_anchor_uid() {
        return send_anchor_uid;
    }

    public void setSend_anchor_uid(String send_anchor_uid) {
        this.send_anchor_uid = send_anchor_uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VideoVquCallBean getData() {
        return data;
    }

    public void setData(VideoVquCallBean data) {
        this.data = data;
    }
}
