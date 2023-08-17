package com.mshy.VInterestSpeed.common.bean;

import java.util.ArrayList;

public class CommonVquAnchorListBean {
    private int skill_id;
    private String skill_name;
    private String skill_icon;
    private int total;
    private ArrayList<CommonVquAnchorBean> anchors = new ArrayList<>();
    private ArrayList<CommonVquAnchorBean> list = new ArrayList<>();

    public ArrayList<CommonVquAnchorBean> getAnchors() {
        return anchors;
    }

    public void setAnchors(ArrayList<CommonVquAnchorBean> anchors) {
        this.anchors = anchors;
    }

    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public String getSkill_icon() {
        return skill_icon;
    }

    public void setSkill_icon(String skill_icon) {
        this.skill_icon = skill_icon;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<CommonVquAnchorBean> getList() {
        return list;
    }

    public void setList(ArrayList<CommonVquAnchorBean> list) {
        this.list = list;
    }
}
