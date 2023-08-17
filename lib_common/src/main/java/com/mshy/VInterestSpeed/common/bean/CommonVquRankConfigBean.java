package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;

/**
 * FileName: com.live.vquonline.model.live
 * Author: Reisen
 * Date: 2021/9/2 10:50
 * Description:排名相关配置
 * History:
 */
public class CommonVquRankConfigBean implements Serializable {
    private String img;
    private String rank;
    private int sort;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
