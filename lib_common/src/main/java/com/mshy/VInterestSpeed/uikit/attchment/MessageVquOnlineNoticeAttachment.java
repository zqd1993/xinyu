package com.mshy.VInterestSpeed.uikit.attchment;

import com.alibaba.fastjson.JSONObject;
import com.mshy.VInterestSpeed.common.bean.CommonVquGradeConfigBean;

import java.io.Serializable;

/**
 * Created by zhangbin on 2020/2/8.
 */
public class MessageVquOnlineNoticeAttachment extends MessageVquBaseAttachment {

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
    private GradeBean gradeBean;

    public GradeBean getGradeBean() {
        return gradeBean;
    }

    public void setGradeBean(GradeBean gradeBean) {
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

    MessageVquOnlineNoticeAttachment(int type) {
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
        gradeBean = JSONObject.parseObject(grade.toJSONString(), GradeBean.class);
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


    public static class GradeBean implements Serializable {
        /**
         * grade : 等级
         * imgInfo : {"img":""}
         */

        private int grade;
        private CommonVquGradeConfigBean imgInfo;
        private String total_growth;

        public String getTotal_growth() {
            return total_growth;
        }

        public void setTotal_growth(String total_growth) {
            this.total_growth = total_growth;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public CommonVquGradeConfigBean getImgInfo() {
            return imgInfo;
        }

        public void setImgInfo(CommonVquGradeConfigBean imgInfo) {
            this.imgInfo = imgInfo;
        }

    }
}
