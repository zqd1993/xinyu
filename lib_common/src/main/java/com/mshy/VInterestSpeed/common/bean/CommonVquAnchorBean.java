package com.mshy.VInterestSpeed.common.bean;

import java.util.List;
import java.util.Objects;

public class CommonVquAnchorBean {


    private int userid;
    private String nickname;
    private String sign;
    private String cover;
    private String city;
    private int is_anchor;
    private int is_rp_auth;
    private int is_auth;
    private int vip ;
    private String height;
    private String occupation;
    private String is_beckon_text;
    private boolean is_beckon;
    private int gender;
    private int age;
    private float video_amount;
    private float give_score;
    private String price;
    private Online online;
    private String weight;

    public int getIs_rp_auth() {
        return is_rp_auth;
    }

    public int getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(int is_auth) {
        this.is_auth = is_auth;
    }

    public void setIs_rp_auth(int is_rp_auth) {
        this.is_rp_auth = is_rp_auth;
    }

    public int getIs_vip() {
        return vip;
    }

    public void setIs_vip(int is_vip) {
        this.vip = is_vip;
    }

    private String good_evaluate_rate;
    private int is_follow;
    private int is_call;
    private int is_same_city;
    private String discount;
    private boolean isPlaying;
    private boolean is_new_user;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(boolean is_new_user) {
        this.is_new_user = is_new_user;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * onlineNew : {"status":1,"statusMsg":"空闲","online_status":1,"OnlineStatus":1,"statusMsgNew":"在线"}
     */

    private Online onlineNew;
    private List<LaberBean> laber;
    /**
     * living : {"live_status":0}
     */

    private LivingBean living;

    private int voice;
    private String mp3;
    private int mp3_second;
    private String vip_icon;

    public int getIs_anchor() {
        return is_anchor;
    }

    public void setIs_anchor(int is_anchor) {
        this.is_anchor = is_anchor;
    }

    public String getVip_icon() {
        return vip_icon;
    }

    public void setVip_icon(String vip_icon) {
        this.vip_icon = vip_icon;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getVoice() {
        return voice;
    }

    public void setVoice(int voice) {
        this.voice = voice;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public int getMp3_second() {
        return mp3_second;
    }

    public void setMp3_second(int mp3_second) {
        this.mp3_second = mp3_second;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     * is_rank : {"type":"","sort":0,"imgInfo":{"des":"","start_color":"","over_color":""}}
     */

    private IsRankBean is_rank;


    private int playType;


    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }


    public String getGood_evaluate_rate() {
        return good_evaluate_rate;
    }

    public void setGood_evaluate_rate(String good_evaluate_rate) {
        this.good_evaluate_rate = good_evaluate_rate;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public int getIs_call() {
        return is_call;
    }

    public void setIs_call(int is_call) {
        this.is_call = is_call;
    }

    public int getIs_same_city() {
        return is_same_city;
    }

    public void setIs_same_city(int is_same_city) {
        this.is_same_city = is_same_city;
    }

    private List<GameSkillBean> game_skill;

    public List<GameSkillBean> getGame_skill() {
        return game_skill;
    }

    public void setGame_skill(List<GameSkillBean> game_skill) {
        this.game_skill = game_skill;
    }

    public IsRankBean getIs_rank() {
        return is_rank;
    }

    public void setIs_rank(IsRankBean is_rank) {
        this.is_rank = is_rank;
    }

    public Online getOnlineNew() {
        return onlineNew;
    }

    public void setOnlineNew(Online onlineNew) {
        this.onlineNew = onlineNew;
    }

    public List<LaberBean> getLaber() {
        return laber;
    }

    public void setLaber(List<LaberBean> laber) {
        this.laber = laber;
    }

    public LivingBean getLiving() {
        return living;
    }

    public void setLiving(LivingBean living) {
        this.living = living;
    }

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

    public String getIs_beckon_text() {
        return is_beckon_text;
    }

    public void setIs_beckon_text(String is_beckon_text) {
        this.is_beckon_text = is_beckon_text;
    }

    public boolean isIs_beckon() {
        return is_beckon;
    }

    public void setIs_beckon(boolean is_beckon) {
        this.is_beckon = is_beckon;
    }

    public static class GameSkillBean {
        /**
         * id : 3
         * user_id : 31
         * game_id : 1
         * screenshot : /uploads/game/img/2021xxxxxx.jpg
         * mp3 : /uploads/game/mp3/2021xxxxxx.mp3
         * mp3_second : 15
         * region : weixin
         * dan : 1
         * price : 3000
         * status : 1
         * create_time : 1619506552
         * update_time : 1619518689
         * status_text : Status 1
         * create_time_text : 2021-04-27 14:55:52
         * update_time_text : 2021-04-27 18:18:09
         * game_name : 王者荣耀
         * game_icon : uploads/admin/202104/26/b62db69dbe414f690fc0e2367b2855a9.png
         * region_text : 微信大区
         * dan_text : 青铜
         * mp3_url : https://asset.vqucp.com//uploads/game/mp3/2021xxxxxx.mp3
         */

        private int id;
        private int user_id;
        private int game_id;
        private String screenshot;
        private String mp3;
        private int mp3_second;
        private String region;
        private String dan;
        private int price;
        private int status;
        private int create_time;
        private int update_time;
        private String status_text;
        private String create_time_text;
        private String update_time_text;
        private String game_name;
        private String game_icon;
        private String region_text;
        private String dan_text;
        private String mp3_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getGame_id() {
            return game_id;
        }

        public void setGame_id(int game_id) {
            this.game_id = game_id;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }

        public String getMp3() {
            return mp3;
        }

        public void setMp3(String mp3) {
            this.mp3 = mp3;
        }

        public int getMp3_second() {
            return mp3_second;
        }

        public void setMp3_second(int mp3_second) {
            this.mp3_second = mp3_second;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getDan() {
            return dan;
        }

        public void setDan(String dan) {
            this.dan = dan;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
            this.update_time = update_time;
        }

        public String getStatus_text() {
            return status_text;
        }

        public void setStatus_text(String status_text) {
            this.status_text = status_text;
        }

        public String getCreate_time_text() {
            return create_time_text;
        }

        public void setCreate_time_text(String create_time_text) {
            this.create_time_text = create_time_text;
        }

        public String getUpdate_time_text() {
            return update_time_text;
        }

        public void setUpdate_time_text(String update_time_text) {
            this.update_time_text = update_time_text;
        }

        public String getGame_name() {
            return game_name;
        }

        public void setGame_name(String game_name) {
            this.game_name = game_name;
        }

        public String getGame_icon() {
            return game_icon;
        }

        public void setGame_icon(String game_icon) {
            this.game_icon = game_icon;
        }

        public String getRegion_text() {
            return region_text;
        }

        public void setRegion_text(String region_text) {
            this.region_text = region_text;
        }

        public String getDan_text() {
            return dan_text;
        }

        public void setDan_text(String dan_text) {
            this.dan_text = dan_text;
        }

        public String getMp3_url() {
            return mp3_url;
        }

        public void setMp3_url(String mp3_url) {
            this.mp3_url = mp3_url;
        }
    }

    public static class IsRankBean {
        /**
         * type :
         * sort : 0
         * imgInfo : {"des":"","start_color":"","over_color":""}
         */

        private String type;
        private int sort;
        private ImgInfoBean imgInfo;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public ImgInfoBean getImgInfo() {
            return imgInfo;
        }

        public void setImgInfo(ImgInfoBean imgInfo) {
            this.imgInfo = imgInfo;
        }

        public static class ImgInfoBean {
            /**
             * des :
             * start_color :
             * over_color :
             */

            private String des;
            private String start_color;
            private String over_color;

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public String getStart_color() {
                return start_color;
            }

            public void setStart_color(String start_color) {
                this.start_color = start_color;
            }

            public String getOver_color() {
                return over_color;
            }

            public void setOver_color(String over_color) {
                this.over_color = over_color;
            }
        }
    }

    public static class LaberBean {
        /**
         * name : 诱惑
         * color : {"start_color":"C8B2FF","over_color":"7175FF"}
         * num : 20
         */

        private String name;
        private IsRankBean.ImgInfoBean color;
        private int num;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public IsRankBean.ImgInfoBean getColor() {
            return color;
        }

        public void setColor(IsRankBean.ImgInfoBean color) {
            this.color = color;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }

    public static class LivingBean {
        /**
         * live_status : 0
         */
        private String room_id = "";
        private int live_status;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public int getLive_status() {
            return live_status;
        }

        public void setLive_status(int live_status) {
            this.live_status = live_status;
        }
    }

//    public static class VoiceBean {
//        /**
//         * mp3 : /v1.3/mp3/14601.mp3
//         * mp3_second : 1
//         */
//
//        private String mp3;
//        private int mp3_second;
//
//        public String getMp3() {
//            return mp3;
//        }
//
//        public void setMp3(String mp3) {
//            this.mp3 = mp3;
//        }
//
//        public int getMp3_second() {
//            return mp3_second;
//        }
//
//        public void setMp3_second(int mp3_second) {
//            this.mp3_second = mp3_second;
//        }
//    }


    public class Online {
        /**
         * status : 3
         * statusMsg : 勿扰
         * online_status : 0
         */
        private int OnlineStatus;
        private int status;
        private String statusMsg;
        private int online_status;
        private String statusMsgNew;
        private String newMsg;
        private String newColor;
        private int newStatus;

        public int getNewStatus() {
            return newStatus;
        }

        public void setNewStatus(int newStatus) {
            this.newStatus = newStatus;
        }

        public String getNewMsg() {
            return newMsg;
        }

        public void setNewMsg(String newMsg) {
            this.newMsg = newMsg;
        }

        public String getNewColor() {
            return newColor;
        }

        public void setNewColor(String newColor) {
            this.newColor = newColor;
        }

        public int getOnlineStatus() {
            return OnlineStatus;
        }

        public void setOnlineStatus(int onlineStatus) {
            OnlineStatus = onlineStatus;
        }

        public String getStatusMsgNew() {
            return statusMsgNew;
        }

        public void setStatusMsgNew(String statusMsgNew) {
            this.statusMsgNew = statusMsgNew;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStatusMsg() {
            return statusMsg;
        }

        public void setStatusMsg(String statusMsg) {
            this.statusMsg = statusMsg;
        }

        public int getOnline_status() {
            return online_status;
        }

        public void setOnline_status(int online_status) {
            this.online_status = online_status;
        }
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Online getOnline() {
        return online;
    }

    public void setOnline(Online online) {
        this.online = online;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public float getVideo_amount() {
        return video_amount;
    }

    public void setVideo_amount(float video_amount) {
        this.video_amount = video_amount;
    }

    public float getGive_score() {
        return give_score;
    }

    public void setGive_score(float give_score) {
        this.give_score = give_score;
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


    @Override
    public String toString() {
        return "AnchorBean{" +
                "userid=" + userid +
                ", nickname='" + nickname + '\'' +
                ", cover='" + cover + '\'' +
                ", city='" + city + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", video_amount=" + video_amount +
                ", give_score=" + give_score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonVquAnchorBean that = (CommonVquAnchorBean) o;
        return userid == that.userid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid);
    }

}
