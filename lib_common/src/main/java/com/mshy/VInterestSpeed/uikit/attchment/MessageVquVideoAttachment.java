package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhangbin on 2018/12/6.
 * 自定义视频消息接收参数
 */

public class MessageVquVideoAttachment extends MessageVquBaseAttachment{

    private int calltype;
    private int status;
    private String from_uid;
    private String to_uid;
    private int call_time;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    MessageVquVideoAttachment(int type) {
        super(type);
    }

    public int getCalltype() {
        return calltype;
    }

    public void setCalltype(int calltype) {
        this.calltype = calltype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public int getCall_time() {
        return call_time;
    }

    public void setCall_time(int call_time) {
        this.call_time = call_time;
    }

    @Override
    protected void parseData(JSONObject data) {
        calltype =  data.getIntValue("calltype");
        status =  data.getIntValue("status");
        from_uid = data.getString("from_uid");
        to_uid = data.getString("to_uid");
        call_time =  data.getIntValue("call_time");
        content =  data.getString("content");

    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("calltype", calltype);
        data.put("status", status);
        data.put("from_uid", from_uid);
        data.put("to_uid", to_uid);
        data.put("call_time", call_time);
        data.put("content", content);
        return data;
    }

    @Override
    public String toString() {
        return "MessageVquVideoAttachment{" +
                "calltype=" + calltype +
                ", status=" + status +
                ", from_uid='" + from_uid + '\'' +
                ", to_uid='" + to_uid + '\'' +
                ", call_time=" + call_time +
                ", content='" + content + '\'' +
                '}';
    }
}
