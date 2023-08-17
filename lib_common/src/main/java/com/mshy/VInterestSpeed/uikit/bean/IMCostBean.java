package com.mshy.VInterestSpeed.uikit.bean;

public class IMCostBean {
    private int is_cut;
    private int cut_coin;
    private int coin;
    private int filter;
    private int vip;
    private String content;
    private String money;
    private int is_free;
    public int getVip() {
        return vip;
    }

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getIs_cut() {
        return is_cut;
    }

    public void setIs_cut(int is_cut) {
        this.is_cut = is_cut;
    }

    public int getCut_coin() {
        return cut_coin;
    }

    public void setCut_coin(int cut_coin) {
        this.cut_coin = cut_coin;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
