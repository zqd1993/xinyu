package com.live.module.message.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangbin on 2019/2/20.
 */

public class ChatSettingInfoBean {

    /**
     *  "userid":99,
     "nickname":"墨墨",
     "age":8,
     "gender":2,
     "avatar":"images/avatar/woman.jpg",
     "city":"北京"
     */

    private int user_id;
    private String nickname;
    private int age;
    private int gender;
    private String avatar;
    private String city;
    private String usercode;
    @SerializedName("remark_name")
    private String remarkName;
    private String height;
    private String occupation;
    private String sign;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
}
