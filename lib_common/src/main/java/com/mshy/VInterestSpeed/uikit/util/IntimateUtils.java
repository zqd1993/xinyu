package com.mshy.VInterestSpeed.uikit.util;


import com.mshy.VInterestSpeed.uikit.bean.ChatIntimateBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * FileName: com.live.vquonline.utils
 * Author: Reisen
 * Date: 2022/3/21 9:56
 * Description:
 * History:
 */
public class IntimateUtils {
    /**
     * 亲密度列表 与用户id关联
     */
    private final Map<Integer, ChatIntimateBean.ListData> scoreDataMap = new HashMap<>();


    private IntimateUtils() {

    }

    public static IntimateUtils getInstance() {
        return InstanceHolder.instance;
    }

    static class InstanceHolder {
        final static IntimateUtils instance = new IntimateUtils();
    }

    /**
     * 添加一条数据
     */
    public void putData(ChatIntimateBean.ListData data) {
        scoreDataMap.put(data.getUser_id(), data);
    }

    /**
     * 添加一条数据
     */
    public void putData(int userId, String score, int grade) {
        ChatIntimateBean.ListData data;
        if (findData(userId) != null) {
            data = findData(userId);
        } else {
            data = new ChatIntimateBean.ListData();
        }
        data.setGrade(grade);
        data.setScore(score);
        scoreDataMap.put(userId, data);
    }

    /**
     * 查询一条数据
     */
    public ChatIntimateBean.ListData findData(int userId) {
        return scoreDataMap.get(userId);
    }

    /**
     * 是否需要显示在亲密列表 代表亲密等级大于1
     *
     * @return
     */
    public boolean isNeedShow(int userId) {
        return findData(userId) != null && findData(userId).getGrade() > 1;
    }

    public Iterator<Map.Entry<Integer, ChatIntimateBean.ListData>> getIterator() {
        return scoreDataMap.entrySet().iterator();
    }

    /**
     * 清除数据
     */
    public void clearData() {
        scoreDataMap.clear();
    }

}
