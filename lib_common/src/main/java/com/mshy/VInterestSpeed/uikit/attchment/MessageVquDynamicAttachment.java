package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhangbin on 2019/3/20.
 */

public class MessageVquDynamicAttachment extends MessageVquBaseAttachment {

    private int userid;
    private String nickname;
    private String image;
    private String title;
    private String time;
    private String sign;
    private String avatar;
    private int sex;
    private int age;
    private JSONObject grade;
    private MessageVquOnlineNoticeAttachment.GradeBean gradeBean;

    public MessageVquOnlineNoticeAttachment.GradeBean getGradeBean() {
        return gradeBean;
    }

    public void setGradeBean(MessageVquOnlineNoticeAttachment.GradeBean gradeBean) {
        this.gradeBean = gradeBean;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    MessageVquDynamicAttachment(int type) {
        super(type);
    }

    @Override
    protected void parseData(JSONObject data) {
        userid =  data.getIntValue("userid");
        nickname = data.getString("nickname");
        image = data.getString("image");
        title = data.getString("title");
        time = data.getString("time");
        sign = data.getString("sign");
        sex = data.getIntValue("sex");
        age = data.getIntValue("age");
        avatar = data.getString("avatar");
        grade = data.getJSONObject("grade");
        gradeBean = JSONObject.parseObject(grade.toJSONString(), MessageVquOnlineNoticeAttachment.GradeBean.class);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data =new JSONObject();
        data.put("userid", userid);
        data.put("nickname", nickname);
        data.put("image", image);
        data.put("title", title);
        data.put("time", time);
        data.put("sign", sign);
        data.put("sex", sex);
        data.put("age", age);
        data.put("avatar", avatar);
        data.put("grade", grade);
        return data;
    }
}
