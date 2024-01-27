package com.live.module.message.bean;

import com.mshy.VInterestSpeed.common.bean.VquUserInfo;
import com.mshy.VInterestSpeed.common.utils.UserSpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: com.live.vquonline.view.news.activity
 * Author: Reisen
 * Date: 2022/3/23 19:10
 * Description:
 * History:
 */
public class GuideData {
    //总数据
    private final List<GuideBean> mAllData = new ArrayList<>();
    private final String[] content = {"欢迎来到甜缘交友",
            "在甜缘交友，每回复一条消息，都可以获得现金，现金可无门槛提现哦",
            "快回复消息试试？", "好的", "回复消息越及时，得到的现金也越多",
            "通过真人认证后，所获得的现金将翻倍", "通过头像审核，才可开始获得现金哦，你准备好了吗？",
            "好的"};
    private final boolean[] isReceives = {true, true, true, false, true, true, true, false};

    private GuideData() {
    }

    public static GuideData getInstance() {
        return InstanceHolder.instance;
    }

    static class InstanceHolder {
        final static GuideData instance = new GuideData();
    }

    public void initData() {
        VquUserInfo info = UserSpUtils.INSTANCE.getUserBean();
        if (info != null) {
            String head = info.getAvatar();
            for (int i = 0; i < content.length; i++) {
                addData(isReceives[i] ? "" : head, content[i], isReceives[i], i < 3 ? "回复消息可获得现金":"+0.65元");
            }
        }
    }


    /**
     * 添加数据
     *
     * @param headUrl
     * @param content
     * @param isReceive
     */
    private void addData(String headUrl, String content, boolean isReceive, String coinHint) {
        GuideBean bean = new GuideBean();
        bean.setContent(content);
        bean.setReceive(isReceive);
        bean.setHeadUrl(headUrl);
        bean.setCoinHint(coinHint);
        mAllData.add(bean);
    }

    public List<GuideBean> getAllData() {
        return mAllData;
    }

    public void destroy() {
        mAllData.clear();
    }
}
