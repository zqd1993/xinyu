package com.mshy.VInterestSpeed.common.bean.websocket;

public class WebSocketChatBean<T> {
    private int type;
    private T data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
