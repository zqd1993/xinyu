package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;

/**
 * FileName: com.live.vquonline.model.live
 * Author: Reisen
 * Date: 2021/9/2 10:47
 * Description: 等级相关的bean
 * History:
 */
public class CommonVquGradeConfigBean implements Serializable {

    /**
     * img1 : /v1.3/dress/grade/%E5%87%A1%E4%BA%BA%402x.png
     * img2 : /v1.3/dress/grade/%E8%92%99%E7%89%88%402x.png
     * icon_width : 18.5
     * icon1_width : 37.5
     */

    private String img1;
    private String img2;
    private String icon_width;
    private String icon1_width;
    private int grade;
    private String grade_name;

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getIcon_width() {
        return icon_width;
    }

    public void setIcon_width(String icon_width) {
        this.icon_width = icon_width;
    }

    public String getIcon1_width() {
        return icon1_width;
    }

    public void setIcon1_width(String icon1_width) {
        this.icon1_width = icon1_width;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getGrade_name() {
        return grade_name;
    }

    public void setGrade_name(String grade_name) {
        this.grade_name = grade_name;
    }
}
