package com.mshy.VInterestSpeed.common.bean.notification;


public class CallNotificationBean {

    private String vquNoCallName;
    private String vquNoCallType;
    private String vquNoCallZS;

    public String getVquNoCallName() {
        return vquNoCallName;
    }

    public void setVquNoCallName(String vquNoCallName) {
        this.vquNoCallName = vquNoCallName;
    }

    public String getVquNoCallType() {
        return vquNoCallType;
    }

    public void setVquNoCallType(String vquNoCallType) {
        this.vquNoCallType = vquNoCallType;
    }

    public String getVquNoCallZS() {
        return vquNoCallZS;
    }

    public void setVquNoCallZS(String vquNoCallZS) {
        this.vquNoCallZS = vquNoCallZS;
    }

    public CallNotificationBean() {
    }

    public CallNotificationBean(String vquNoCallName, String vquNoCallType, String vquNoCallZS) {
        this.vquNoCallName = vquNoCallName;
        this.vquNoCallType = vquNoCallType;
        this.vquNoCallZS = vquNoCallZS;
    }
}
