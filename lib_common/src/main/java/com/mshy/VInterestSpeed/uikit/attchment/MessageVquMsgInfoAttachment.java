package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

/**
 * Created by zhangbin on 2018/12/6.
 *
 */

public class MessageVquMsgInfoAttachment implements MsgAttachment {

    private JSONObject jsonData;


    public JSONObject getJsonData() {
        return jsonData;
    }

    public void setJsonData(JSONObject jsonData) {
        this.jsonData = jsonData;
    }


    @Override
    public String toJson(boolean b) {
        return "";
    }
}
