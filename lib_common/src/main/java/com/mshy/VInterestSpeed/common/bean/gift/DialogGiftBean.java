package com.mshy.VInterestSpeed.common.bean.gift;

public class DialogGiftBean {
    //礼物类型 0普通 1svga动画礼物
    private int type;
    //svga地址
    private String svga;
    //svgaMD5值
    private String md5_string;
    private int id;
    private String name;
    private int price;
    private String img;
    private int isSelect;  //1选中
    private int num;
    private String sign;
    private String gift_type_id;

    private String toUid="";

    public String getToUid() {
        if(null!= toUid && toUid.length()>=3){
            toUid =  toUid.substring(1,toUid.length()-1);
            return toUid;
        }
        return "";
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getGift_type_id() {
        return gift_type_id;
    }

    public void setGift_type_id(String gift_type_id) {
        this.gift_type_id = gift_type_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSvga() {
        return svga;
    }

    public void setSvga(String svga) {
        this.svga = svga;
    }

    public String getMd5_string() {
        return md5_string;
    }

    public void setMd5_string(String md5_string) {
        this.md5_string = md5_string;
    }
}
