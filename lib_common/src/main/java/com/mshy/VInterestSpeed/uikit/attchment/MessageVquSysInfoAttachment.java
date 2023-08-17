package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;
import com.mshy.VInterestSpeed.uikit.bean.NIMVquSysInfoTextBean;

/**
 * Created by zhangbin on 2018/12/11.
 */

public class MessageVquSysInfoAttachment extends MessageVquBaseAttachment {


    NIMVquSysInfoTextBean mSysInfoTextBean;

    MessageVquSysInfoAttachment(int type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {
        mSysInfoTextBean = JSONObject.toJavaObject(data, NIMVquSysInfoTextBean.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("mSysInfoTextBean", mSysInfoTextBean);
        return object;
    }

    public NIMVquSysInfoTextBean getSysInfoTextBean() {
        return mSysInfoTextBean;
    }

    public void setSysInfoTextBean(NIMVquSysInfoTextBean sysInfoTextBean) {
        mSysInfoTextBean = sysInfoTextBean;
    }
}
