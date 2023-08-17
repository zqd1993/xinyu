package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mshy.VInterestSpeed.uikit.bean.NIMVquSysInfoImage;

import java.util.ArrayList;
import java.util.List;

public class SysInfoDoubleImageAttachment extends MessageVquBaseAttachment {
    List<NIMVquSysInfoImage> list = new ArrayList<>();

    SysInfoDoubleImageAttachment(int type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {
        JSONArray array = data.getJSONArray("list");
        list = JSONObject.parseArray(array.toJSONString(), NIMVquSysInfoImage.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put("list", list);
        return data;
    }

    public void parseArray(String data) {
        list = JSONObject.parseArray(data, NIMVquSysInfoImage.class);
    }

    public List<NIMVquSysInfoImage> getImageList() {
        return list;
    }

    public void setImageList(List<NIMVquSysInfoImage> imageList) {
        this.list = imageList;
    }
}
