package com.mshy.VInterestSpeed.common.bean.websocket;

public class WebSocketRequestBean<T> {
    private String method;
    private T data;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebSocketRequestBean{" +
                "method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
