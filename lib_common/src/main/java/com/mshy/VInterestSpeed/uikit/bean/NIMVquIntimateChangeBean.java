package com.mshy.VInterestSpeed.uikit.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 心动值变动通知实体类
 */

public class NIMVquIntimateChangeBean {

    private int id;
    private DataBean data;


    public static class DataBean {
        @SerializedName("from_uid")
        private int fromUid;
        @SerializedName("to_uid")
        private int toUid;
        private String score;
        private String grade;
        private String sign;

        public int getFromUid() {
            return fromUid;
        }

        public void setFromUid(int fromUid) {
            this.fromUid = fromUid;
        }

        public int getToUid() {
            return toUid;
        }

        public void setToUid(int toUid) {
            this.toUid = toUid;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
