package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhangbin on 2019/1/11.
 */

public class MessageVquAttentionAttachment extends MessageVquBaseAttachment {

    /**
     "nickname":"小墨",
     "avatar":"",
     "age":19,
     "city":"深圳",
     "time":"2017-01-11 22:11:23"
     * @param type
     */

    private int userid;
    private String nickname;
    private String avatar;
    private int age;
    private String city;
    private String time;
    private int sex;
    private String sign;
    private JSONObject grade;
    private MessageVquOnlineNoticeAttachment.GradeBean gradeBean;
    public MessageVquOnlineNoticeAttachment.GradeBean getGradeBean() {
        return gradeBean;
    }

    public void setGradeBean(MessageVquOnlineNoticeAttachment.GradeBean gradeBean) {
        this.gradeBean = gradeBean;
    }
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    MessageVquAttentionAttachment(int type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {

        userid =  data.getIntValue("userid");
        nickname = data.getString("nickname");
        avatar = data.getString("avatar");
        age = data.getIntValue("age");
        city = data.getString("city");
        time = data.getString("time");
        sex = data.getIntValue("sex");
        sign = data.getString("sign");
        grade = data.getJSONObject("grade");
        gradeBean = JSONObject.parseObject(grade.toJSONString(), MessageVquOnlineNoticeAttachment.GradeBean.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data =new JSONObject();
        data.put("userid", userid);
        data.put("nickname", nickname);
        data.put("avatar", avatar);
        data.put("age", age);
        data.put("city", city);
        data.put("time", time);
        data.put("sex", sex);
        data.put("sign", sign);
        data.put("grade", grade);
        return data;
    }
}
