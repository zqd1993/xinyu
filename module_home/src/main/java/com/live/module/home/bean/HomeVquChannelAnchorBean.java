package com.live.module.home.bean;

import com.mshy.VInterestSpeed.common.bean.CommonVquAnchorListBean;
import com.mshy.VInterestSpeed.common.bean.CommonVquBannerBean;
import com.mshy.VInterestSpeed.common.bean.CommonVquRankBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeVquChannelAnchorBean implements Serializable {
    private int skill_id;
    private String skill_name;
    private int type;
    private List<CommonVquBannerBean> banner = new ArrayList<>();
    private CommonVquAnchorListBean list;
    private CommonVquRankBean rank;
    private String declare;
    /**
     * activity : {"is_today_fate":false,"is_today_sign":false,"is_info":false}
     */

    private ActivityBean activity;

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public int getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(int skill_id) {
        this.skill_id = skill_id;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CommonVquBannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<CommonVquBannerBean> banner) {
        this.banner = banner;
    }

    public CommonVquAnchorListBean getList() {
        return list;
    }

    public void setList(CommonVquAnchorListBean list) {
        this.list = list;
    }

    public CommonVquRankBean getRank() {
        return rank;
    }

    public void setRank(CommonVquRankBean rank) {
        this.rank = rank;
    }

    public String getDeclare() {
        return declare;
    }

    public void setDeclare(String declare) {
        this.declare = declare;
    }

    @Override
    public String toString() {
        return "ChannelAnchorBean{" +
                "skill_id=" + skill_id +
                ", skill_name='" + skill_name + '\'' +
                ", type=" + type +
                ", banner=" + banner +
                ", list=" + list +
                '}';
    }

    public static class ActivityBean implements Serializable {
        /**
         * is_today_fate : false
         * is_today_sign : false
         * is_info : false
         */

        private boolean is_today_fate;
        private boolean is_today_sign;
        private boolean is_info;

        public boolean isIs_today_fate() {
            return is_today_fate;
        }

        public void setIs_today_fate(boolean is_today_fate) {
            this.is_today_fate = is_today_fate;
        }

        public boolean isIs_today_sign() {
            return is_today_sign;
        }

        public void setIs_today_sign(boolean is_today_sign) {
            this.is_today_sign = is_today_sign;
        }

        public boolean isIs_info() {
            return is_info;
        }

        public void setIs_info(boolean is_info) {
            this.is_info = is_info;
        }
    }
}
