package com.mshy.VInterestSpeed.uikit.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangbin on 2018/12/11.
 */

public class NIMVquSysInfoTextBean implements Serializable {

    int action;
    String title;
    String date;
    String txt1;
    String txt2;
    int link_type;
    String link_url;
    List<FieldListA> fields;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTxt1() {
        return txt1;
    }

    public void setTxt1(String txt1) {
        this.txt1 = txt1;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
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

    public List<FieldListA> getFields() {
        return fields;
    }

    public void setFields(List<FieldListA> fields) {
        this.fields = fields;
    }
}
