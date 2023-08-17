package com.live.module.message.bean;

import java.util.List;

/**
 * Created by zhangbin on 2019/2/20.
 */

public class ChatSettingBean {

    private int is_black;
    private ChatSettingInfoBean info;
    private List<String> dynamic;

    public int getIs_black() {
        return is_black;
    }

    public void setIs_black(int is_black) {
        this.is_black = is_black;
    }

    public ChatSettingInfoBean getInfo() {
        return info;
    }

    public void setInfo(ChatSettingInfoBean info) {
        this.info = info;
    }

    public List<String> getDynamic() {
        return dynamic;
    }

    public void setDynamic(List<String> dynamic) {
        this.dynamic = dynamic;
    }
}
