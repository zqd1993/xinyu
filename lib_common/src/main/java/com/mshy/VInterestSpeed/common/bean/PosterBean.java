package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;

/**
 * FileName: com.live.vquonline.model.main
 * Author: Reisen
 * Date: 2021/9/28 16:20
 * Description:
 * History:
 */
public class PosterBean implements Serializable {

    /**
     * posterPicUrl : PosterPicUrl
     * bottomColor : 4F4B5B
     * content : Content
     * contentColor : 4F4B5B
     * qCodeContent : qCodeContent
     * buttonStartColor : 4F4B5B
     * buttonEndColor : 4F4B5B
     * buttonText : buttonText
     */

    private String posterPicUrl;
    private String bottomColor;
    private String content;
    private String contentColor;
    private String qCodeContent;
    private String buttonStartColor;
    private String buttonEndColor;
    private String buttonText;

    public String getPosterPicUrl() {
        return posterPicUrl;
    }

    public void setPosterPicUrl(String posterPicUrl) {
        this.posterPicUrl = posterPicUrl;
    }

    public String getBottomColor() {
        return bottomColor;
    }

    public void setBottomColor(String bottomColor) {
        this.bottomColor = bottomColor;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public String getqCodeContent() {
        return qCodeContent;
    }

    public void setqCodeContent(String qCodeContent) {
        this.qCodeContent = qCodeContent;
    }

    public String getButtonStartColor() {
        return buttonStartColor;
    }

    public void setButtonStartColor(String buttonStartColor) {
        this.buttonStartColor = buttonStartColor;
    }

    public String getButtonEndColor() {
        return buttonEndColor;
    }

    public void setButtonEndColor(String buttonEndColor) {
        this.buttonEndColor = buttonEndColor;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
