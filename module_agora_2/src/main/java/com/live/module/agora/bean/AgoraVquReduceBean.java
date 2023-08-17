package com.live.module.agora.bean;

import java.io.Serializable;
import java.util.List;


public class AgoraVquReduceBean implements Serializable {

    private int price;
    private List<TimeBean> time;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public static class TimeBean implements Serializable {
        /**
         * time_id : 1
         * time : 1分钟
         */

        private int time_id;
        private String time;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public int getTime_id() {
            return time_id;
        }

        public void setTime_id(int time_id) {
            this.time_id = time_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
