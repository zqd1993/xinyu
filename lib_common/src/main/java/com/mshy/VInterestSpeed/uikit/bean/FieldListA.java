package com.mshy.VInterestSpeed.uikit.bean;

import java.io.Serializable;

public class FieldListA implements Serializable{
    private String n;
    private String v;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "FieldListA{" +
                "n='" + n + '\'' +
                ", v='" + v + '\'' +
                '}';
    }
}
