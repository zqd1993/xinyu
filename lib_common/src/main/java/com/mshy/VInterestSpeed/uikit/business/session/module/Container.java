package com.mshy.VInterestSpeed.uikit.business.session.module;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

/**
 * Created by zhoujianghua on 2015/7/6.
 */
public class Container {

    public final Activity activity;

    public final String account;

    public final SessionTypeEnum sessionType;

    public final ModuleProxy proxy;

    public final boolean proxySend;

    public Container(Activity activity, String account, SessionTypeEnum sessionType, ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
        this.proxySend = false;
    }

    public Container(Activity activity, String account, SessionTypeEnum sessionType, ModuleProxy proxy,
                     boolean proxySend) {
        this.activity = activity;
        this.account = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
        this.proxySend = proxySend;
    }
}
