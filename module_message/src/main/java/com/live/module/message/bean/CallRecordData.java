package com.live.module.message.bean;

import java.io.Serializable;
import java.util.List;

/**
 * FileName: com.rei.miss.main.utils
 * Author: Reisen
 * Date: 2022/5/21 20:59
 * Description:
 * History:
 */
public class CallRecordData implements Serializable {

    /**
     * total : 1
     * page : 1
     * total_page : 1
     * list : [{"intimate":"1408.60","id":284,"type":1,"otherType":0,"calltype":1,"status":4,"status_txt":"已接通","call_time":6,"call_amount":"50.00","call_income":"20.00","vip":0,"vip_icon":"","userid":31,"usercode":"1000031","nickname":"百合花在身边","grade":{"grade":0,"imgInfo":{"grade_name":"凡人","img1":"/v1.3/dress/grade/fanren.png","img2":"/v1.3/dress/grade/fanren2.png","icon_width":"20","icon1_width":"32","grade":0}},"avatar":"uploads/album/31/2021_12_08_17_56_17_841107.jpg","gender":1,"age":21,"city":"深圳","create_time":"2022-05-21 18:03:01","date":"2022.05.21","time":"18:03:01"}]
     */

    private int total;
    private String page;
    private int total_page;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * intimate : 1408.60
         * id : 284
         * type : 1
         * otherType : 0
         * calltype : 1
         * status : 4
         * status_txt : 已接通
         * call_time : 6
         * call_amount : 50.00
         * call_income : 20.00
         * vip : 0
         * vip_icon :
         * userid : 31
         * usercode : 1000031
         * nickname : 百合花在身边
         * grade : {"grade":0,"imgInfo":{"grade_name":"凡人","img1":"/v1.3/dress/grade/fanren.png","img2":"/v1.3/dress/grade/fanren2.png","icon_width":"20","icon1_width":"32","grade":0}}
         * avatar : uploads/album/31/2021_12_08_17_56_17_841107.jpg
         * gender : 1
         * age : 21
         * city : 深圳
         * create_time : 2022-05-21 18:03:01
         * date : 2022.05.21
         * time : 18:03:01
         */

        private String intimate;
        private int id;
        private int type;
        private int otherType;
        private int calltype;
        private int status;
        private String status_txt;
        private int call_time;
        private String call_amount;
        private String call_income;
        private int vip;
        private String vip_icon;
        private int userid;
        private String usercode;
        private String nickname;
        private GradeBean grade;
        private String avatar;
        private int gender;
        private int age;
        private String city;
        private String create_time;
        private String date;
        private String time;
        private int is_match;

        public String getIntimate() {
            return intimate;
        }

        public void setIntimate(String intimate) {
            this.intimate = intimate;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOtherType() {
            return otherType;
        }

        public void setOtherType(int otherType) {
            this.otherType = otherType;
        }

        public int getCalltype() {
            return calltype;
        }

        public void setCalltype(int calltype) {
            this.calltype = calltype;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatus_txt() {
            return status_txt;
        }

        public void setStatus_txt(String status_txt) {
            this.status_txt = status_txt;
        }

        public int getCall_time() {
            return call_time;
        }

        public void setCall_time(int call_time) {
            this.call_time = call_time;
        }

        public String getCall_amount() {
            return call_amount;
        }

        public void setCall_amount(String call_amount) {
            this.call_amount = call_amount;
        }

        public String getCall_income() {
            return call_income;
        }

        public void setCall_income(String call_income) {
            this.call_income = call_income;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public String getVip_icon() {
            return vip_icon;
        }

        public void setVip_icon(String vip_icon) {
            this.vip_icon = vip_icon;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getUsercode() {
            return usercode;
        }

        public void setUsercode(String usercode) {
            this.usercode = usercode;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public GradeBean getGrade() {
            return grade;
        }

        public void setGrade(GradeBean grade) {
            this.grade = grade;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
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

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getIs_match() {
            return is_match;
        }

        public void setIs_match(int is_match) {
            this.is_match = is_match;
        }

        public static class GradeBean implements Serializable {
            /**
             * grade : 0
             * imgInfo : {"grade_name":"凡人","img1":"/v1.3/dress/grade/fanren.png","img2":"/v1.3/dress/grade/fanren2.png","icon_width":"20","icon1_width":"32","grade":0}
             */

            private int grade;
            private ImgInfoBean imgInfo;

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public ImgInfoBean getImgInfo() {
                return imgInfo;
            }

            public void setImgInfo(ImgInfoBean imgInfo) {
                this.imgInfo = imgInfo;
            }

            public static class ImgInfoBean implements Serializable {
                /**
                 * grade_name : 凡人
                 * img1 : /v1.3/dress/grade/fanren.png
                 * img2 : /v1.3/dress/grade/fanren2.png
                 * icon_width : 20
                 * icon1_width : 32
                 * grade : 0
                 */

                private String grade_name;
                private String img1;
                private String img2;
                private String icon_width;
                private String icon1_width;
                private int grade;

                public String getGrade_name() {
                    return grade_name;
                }

                public void setGrade_name(String grade_name) {
                    this.grade_name = grade_name;
                }

                public String getImg1() {
                    return img1;
                }

                public void setImg1(String img1) {
                    this.img1 = img1;
                }

                public String getImg2() {
                    return img2;
                }

                public void setImg2(String img2) {
                    this.img2 = img2;
                }

                public String getIcon_width() {
                    return icon_width;
                }

                public void setIcon_width(String icon_width) {
                    this.icon_width = icon_width;
                }

                public String getIcon1_width() {
                    return icon1_width;
                }

                public void setIcon1_width(String icon1_width) {
                    this.icon1_width = icon1_width;
                }

                public int getGrade() {
                    return grade;
                }

                public void setGrade(int grade) {
                    this.grade = grade;
                }
            }
        }
    }
}
