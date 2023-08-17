package com.live.module.agora.bean;

/**
 * FileName: com.live.vquonline.model.main.websocket
 * Author: Reisen
 * Date: 2021/7/20 18:27
 * Description:减免成功提示
 * History:
 */
public class AgoraVquReduceSuccess {
    private int reduceid; //减免id
    private int reducetime;     //减免时长
    private int reducecost;  //减免金额

    public int getReduceid() {
        return reduceid;
    }

    public void setReduceid(int reduceid) {
        this.reduceid = reduceid;
    }

    public int getReducetime() {
        return reducetime;
    }

    public void setReducetime(int reducetime) {
        this.reducetime = reducetime;
    }

    public int getReducecost() {
        return reducecost;
    }

    public void setReducecost(int reducecost) {
        this.reducecost = reducecost;
    }
}
