package com.mshy.VInterestSpeed.uikit.bean;

import java.util.List;

/**
 * 亲密实体类
 */

public class ChatIntimateBean {

    private List<ListData> list;

    public static class ListData {
        private int user_id;
        private String usercode;
        private String avatar;
        private String nickname;
        private String sign;
        private String score;
        private String time;
        private int is_hide;
        private int grade;

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUsercode() {
            return usercode;
        }

        public void setUsercode(String usercode) {
            this.usercode = usercode;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getIs_hide() {
            return is_hide;
        }

        public void setIs_hide(int is_hide) {
            this.is_hide = is_hide;
        }
    }

    public List<ListData> getList() {
        return list;
    }

    public void setList(List<ListData> list) {
        this.list = list;
    }
}
