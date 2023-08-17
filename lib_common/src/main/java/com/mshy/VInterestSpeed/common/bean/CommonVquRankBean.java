package com.mshy.VInterestSpeed.common.bean;

import java.util.List;

public class CommonVquRankBean {
    private List<CommonVquRankUserBean> love_rank;
    private List<CommonVquRankUserBean> wealth_rank;

    public List<CommonVquRankUserBean> getLove_rank() {
        return love_rank;
    }

    public void setLove_rank(List<CommonVquRankUserBean> love_rank) {
        this.love_rank = love_rank;
    }

    public List<CommonVquRankUserBean> getWealth_rank() {
        return wealth_rank;
    }

    public void setWealth_rank(List<CommonVquRankUserBean> wealth_rank) {
        this.wealth_rank = wealth_rank;
    }
}
