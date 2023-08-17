package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;

public class SysInfoSingleImageAttachment extends MessageVquBaseAttachment {
    private int act_type;
    private String act_string;
    private String image;
    private String title;
    private String txt;
    private int link_type;
    private String link_url;


    SysInfoSingleImageAttachment(int type) {
        super(type);
    }

    public int getAct_type() {
        return act_type;
    }

    public void setAct_type(int act_type) {
        this.act_type = act_type;
    }

    public String getAct_string() {
        return act_string;
    }

    public void setAct_string(String act_string) {
        this.act_string = act_string;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
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

    @Override
    protected void parseData(JSONObject data) {
        act_type = data.getIntValue("act_type");
        act_string = data.getString("act_string");
        image = data.getString("image");
        title = data.getString("title");
        txt = data.getString("txt");
        link_type = data.getIntValue("link_type");
        link_url = data.getString("link_url");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data =new JSONObject();
        data.put("act_type", act_type);
        data.put("act_string", act_string);
        data.put("image", image);
        data.put("title", title);
        data.put("txt", txt);
        data.put("link_type", link_type);
        data.put("link_url", link_url);

        return data;
    }
}
