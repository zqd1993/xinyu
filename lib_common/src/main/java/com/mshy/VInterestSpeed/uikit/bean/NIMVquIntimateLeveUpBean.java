package com.mshy.VInterestSpeed.uikit.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 心动值升级通知实体类
 */

public class NIMVquIntimateLeveUpBean {

    private int id;
    private DataBean data;

    public static class DataBean {
        @SerializedName("from_uid")
        private int fromUid;
        @SerializedName("from_nickname")
        private String fromNickname;
        @SerializedName("to_uid")
        private int toUid;
        @SerializedName("to_nickname")
        private String toNickname;
        @SerializedName("current_score")
        private String currentScore;
        @SerializedName("grade")
        private String currentGrade;
        @SerializedName("current_sign")
        private String currentSign;
        @SerializedName("des_next_score")
        private String desNextScore;
        @SerializedName("next_grade")
        private String nextGrade;
        @SerializedName("next_sign")
        private String nextSign;
        @SerializedName("is_next")
        private int isNext;
        @SerializedName("msg2")
        private String msg;
        private String des;
        @SerializedName("grade_img")
        private String gradeImg;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("to_avatar")
        private String toAvatar;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getFromUid() {
            return fromUid;
        }

        public String getGradeImg() {
            return gradeImg;
        }

        public void setGradeImg(String gradeImg) {
            this.gradeImg = gradeImg;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getToAvatar() {
            return toAvatar;
        }

        public void setToAvatar(String toAvatar) {
            this.toAvatar = toAvatar;
        }

        public void setFromUid(int fromUid) {
            this.fromUid = fromUid;
        }

        public String getFromNickname() {
            return fromNickname;
        }

        public void setFromNickname(String fromNickname) {
            this.fromNickname = fromNickname;
        }

        public int getToUid() {
            return toUid;
        }

        public void setToUid(int toUid) {
            this.toUid = toUid;
        }

        public String getToNickname() {
            return toNickname;
        }

        public void setToNickname(String toNickname) {
            this.toNickname = toNickname;
        }

        public String getCurrentScore() {
            return currentScore;
        }

        public void setCurrentScore(String currentScore) {
            this.currentScore = currentScore;
        }

        public String getCurrentGrade() {
            return currentGrade;
        }

        public void setCurrentGrade(String currentGrade) {
            this.currentGrade = currentGrade;
        }

        public String getCurrentSign() {
            return currentSign;
        }

        public void setCurrentSign(String currentSign) {
            this.currentSign = currentSign;
        }

        public String getDesNextScore() {
            return desNextScore;
        }

        public void setDesNextScore(String desNextScore) {
            this.desNextScore = desNextScore;
        }

        public String getNextGrade() {
            return nextGrade;
        }

        public void setNextGrade(String nextGrade) {
            this.nextGrade = nextGrade;
        }

        public String getNextSign() {
            return nextSign;
        }

        public void setNextSign(String nextSign) {
            this.nextSign = nextSign;
        }

        public int getIsNext() {
            return isNext;
        }

        public void setIsNext(int isNext) {
            this.isNext = isNext;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
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
