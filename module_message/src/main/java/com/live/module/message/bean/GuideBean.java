package com.live.module.message.bean;

/**
 * FileName: com.live.vquonline.model.news
 * Author: Reisen
 * Date: 2022/3/23 18:19
 * Description:引导实体类
 * History:
 */
public class GuideBean {
    //是否是接收
    private boolean isReceive;
    //文字内容
    private String content;

    private String headUrl;

    private String coinHint;

    public boolean isReceive() {
        return isReceive;
    }

    public void setReceive(boolean receive) {
        isReceive = receive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getCoinHint() {
        return coinHint;
    }

    public void setCoinHint(String coinHint) {
        this.coinHint = coinHint;
    }
}
