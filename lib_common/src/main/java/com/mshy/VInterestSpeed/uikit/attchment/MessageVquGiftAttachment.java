package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;

public class MessageVquGiftAttachment extends MessageVquBaseAttachment {
    private int gift_id;
    private String gift_name;
    private String gift_url;
    private int gift_count;
    private int from_uid;
    private int to_uid;
    private int link_type;
    private String link_url;
    private int gift_type;
    private String gift_svga;
    private String md5_string;
    private String from_avatar;
    private String from_nickname;
    private int send_gift_hide;

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
    }

    public String getGift_svga() {
        return gift_svga;
    }

    public void setGift_svga(String gift_svga) {
        this.gift_svga = gift_svga;
    }

    public String getMd5_string() {
        return md5_string;
    }

    public void setMd5_string(String md5_string) {
        this.md5_string = md5_string;
    }

    public int getGift_id() {
        return gift_id;
    }

    public void setGift_id(int gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_url() {
        return gift_url;
    }

    public void setGift_url(String gift_url) {
        this.gift_url = gift_url;
    }

    public int getGift_count() {
        return gift_count;
    }

    public void setGift_count(int gift_count) {
        this.gift_count = gift_count;
    }

    public int getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(int from_uid) {
        this.from_uid = from_uid;
    }

    public int getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(int to_uid) {
        this.to_uid = to_uid;
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

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public int getSend_gift_hide() {
        return send_gift_hide;
    }

    public void setSend_gift_hide(int send_gift_hide) {
        this.send_gift_hide = send_gift_hide;
    }

    MessageVquGiftAttachment(int type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {
        gift_id =  data.getIntValue("gift_id");
        gift_name = data.getString("gift_name");
        gift_url = data.getString("gift_url");
        gift_count =  data.getIntValue("gift_count");
        from_uid =  data.getIntValue("from_uid");
        to_uid = data.getIntValue("to_uid");
        link_type = data.getIntValue("link_type");
        link_url = data.getString("link_url");
        gift_type =  data.getIntValue("gift_type");
        gift_svga = data.getString("gift_svga");
        md5_string = data.getString("md5_string");
        from_avatar = data.getString("from_avatar");
        from_nickname = data.getString("from_nickname");
        send_gift_hide = data.getIntValue("send_gift_hide");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("gift_id", gift_id);
        data.put("gift_name", gift_name);
        data.put("gift_url", gift_url);
        data.put("gift_count", gift_count);
        data.put("from_uid", from_uid);
        data.put("to_uid", to_uid);
        data.put("link_type", link_type);
        data.put("link_url", link_url);
        data.put("gift_type", gift_type);
        data.put("gift_svga", gift_svga);
        data.put("md5_string", md5_string);
        data.put("from_avatar", from_avatar);
        data.put("from_nickname", from_nickname);
        data.put("send_gift_hide", send_gift_hide);
        return data;
    }

}
