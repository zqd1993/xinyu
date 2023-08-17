package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhangbin on 2018/12/6.
 * 邀请通话
 */

public class MessageVquInviteChatAttachment extends MessageVquBaseAttachment{

    private int call_type;
    private String from_uid;
    private String title;
    private String msg;

    MessageVquInviteChatAttachment(int type) {
        super(type);
    }

    public int getCalltype() {
        return call_type;
    }

    public void setCalltype(int calltype) {
        this.call_type = calltype;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected void parseData(JSONObject data) {
        call_type =  data.getIntValue("call_type");
        from_uid = data.getString("from_uid");
        title = data.getString("title");
        msg =  data.getString("msg");
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("calltype", call_type);
        data.put("status", from_uid);
        data.put("from_uid", from_uid);
        data.put("title", title);
        data.put("msg", msg);
        return data;
    }

    @Override
    public String toString() {
        return "MessageVquInviteChatAttachment{" +
                "calltype=" + call_type +
                ", from_uid='" + from_uid + '\'' +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
