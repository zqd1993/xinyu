package com.live.module.agora.bean;

import java.util.List;

public class VquVideoFeeBean {
    private String room_id;
    private int from_uid;
    private String from_nickname;
    private String from_avatar;
    private int to_uid;
    private String to_nickname;
    private String to_avatar;
    private String socket_url;
    private String preference_type="";
    private String discount_amount;
    private VquCallSkillBean skill;
    private long call_time;
    private String des;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    //支出金额
    private double pay_money;
    //收入金额
    private double income_money;
    //用户送礼礼物金额
    private double gift_money;
    //女神送礼礼物金额
    private double gift_anchor_price;
    //守护金额
    private double guard_price;
    //减免金额
    private double reduce_costs;
    //减免时长
    private int reduce_time;
    //守护数量
    private int guard_num;
    //累计支出
    private double total_pay;
    //累计收入
    private double total_income;
    private List<Label> label;
    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * pay_money : - 799
         * total_pay : 799
         * guard_num : 0
         * guard_price : - 0
         * gift_anchor_price : + 0
         * gift_money : - 0
         * income_money : 320
         * call_income : + 320
         * des : {"des3":"女神减免1分钟","des1":"","des2":""}
         */

        private String pay_money;
        private String total_pay;
        private int guard_num;
        private String guard_price;
        private String gift_anchor_price;
        private String gift_money;
        private String income_money;
        private String call_income;
        private DesBean des;

        public String getPay_money() {
            return pay_money;
        }

        public void setPay_money(String pay_money) {
            this.pay_money = pay_money;
        }

        public String getTotal_pay() {
            return total_pay;
        }

        public void setTotal_pay(String total_pay) {
            this.total_pay = total_pay;
        }

        public int getGuard_num() {
            return guard_num;
        }

        public void setGuard_num(int guard_num) {
            this.guard_num = guard_num;
        }

        public String getGuard_price() {
            return guard_price;
        }

        public void setGuard_price(String guard_price) {
            this.guard_price = guard_price;
        }

        public String getGift_anchor_price() {
            return gift_anchor_price;
        }

        public void setGift_anchor_price(String gift_anchor_price) {
            this.gift_anchor_price = gift_anchor_price;
        }

        public String getGift_money() {
            return gift_money;
        }

        public void setGift_money(String gift_money) {
            this.gift_money = gift_money;
        }

        public String getIncome_money() {
            return income_money;
        }

        public void setIncome_money(String income_money) {
            this.income_money = income_money;
        }

        public String getCall_income() {
            return call_income;
        }

        public void setCall_income(String call_income) {
            this.call_income = call_income;
        }

        public DesBean getDes() {
            return des;
        }

        public void setDes(DesBean des) {
            this.des = des;
        }

        public static class DesBean {
            /**
             * des3 : 女神减免1分钟
             * des1 :
             * des2 :
             */

            private String des3;
            private String des1;
            private String des2;

            public String getDes3() {
                return des3;
            }

            public void setDes3(String des3) {
                this.des3 = des3;
            }

            public String getDes1() {
                return des1;
            }

            public void setDes1(String des1) {
                this.des1 = des1;
            }

            public String getDes2() {
                return des2;
            }

            public void setDes2(String des2) {
                this.des2 = des2;
            }
        }
    }


    public String getPreference_type() {
        return preference_type;
    }

    public void setPreference_type(String preference_type) {
        this.preference_type = preference_type;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getGift_anchor_price() {
        return gift_anchor_price;
    }

    public void setGift_anchor_price(double gift_anchor_price) {
        this.gift_anchor_price = gift_anchor_price;
    }

    public double getTotal_pay() {
        return total_pay;
    }

    public void setTotal_pay(double total_pay) {
        this.total_pay = total_pay;
    }

    public double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public int getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(int from_uid) {
        this.from_uid = from_uid;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public int getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(int to_uid) {
        this.to_uid = to_uid;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public String getTo_avatar() {
        return to_avatar;
    }

    public void setTo_avatar(String to_avatar) {
        this.to_avatar = to_avatar;
    }

    public String getSocket_url() {
        return socket_url;
    }

    public void setSocket_url(String socket_url) {
        this.socket_url = socket_url;
    }

    public VquCallSkillBean getSkill() {
        return skill;
    }

    public void setSkill(VquCallSkillBean skill) {
        this.skill = skill;
    }

    public long getCall_time() {
        return call_time;
    }

    public void setCall_time(long call_time) {
        this.call_time = call_time;
    }

    public double getPay_money() {
        return pay_money;
    }

    public void setPay_money(double pay_money) {
        this.pay_money = pay_money;
    }

    public double getIncome_money() {
        return income_money;
    }

    public void setIncome_money(double income_money) {
        this.income_money = income_money;
    }

    public List<Label> getLabel() {
        return label;
    }

    public void setLabel(List<Label> label) {
        this.label = label;
    }

    public double getGift_money() {
        return gift_money;
    }

    public void setGift_money(double gift_money) {
        this.gift_money = gift_money;
    }

    public double getGuard_price() {
        return guard_price;
    }

    public void setGuard_price(double guard_price) {
        this.guard_price = guard_price;
    }

    public double getReduce_costs() {
        return reduce_costs;
    }

    public void setReduce_costs(double reduce_costs) {
        this.reduce_costs = reduce_costs;
    }

    public int getReduce_time() {
        return reduce_time;
    }

    public void setReduce_time(int reduce_time) {
        this.reduce_time = reduce_time;
    }

    public int getGuard_num() {
        return guard_num;
    }

    public void setGuard_num(int guard_num) {
        this.guard_num = guard_num;
    }

    public class Label {
        private String id;
        private String name;
        private String start_color;
        private String over_color;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
