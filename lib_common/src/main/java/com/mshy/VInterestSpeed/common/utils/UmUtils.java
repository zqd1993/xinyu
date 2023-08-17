package com.mshy.VInterestSpeed.common.utils;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

public class UmUtils {
    static boolean isOpen = true;
    public static final String ENTERREGISTRATION = "EnterRegistration";//进入注册填写资料页上报
    public static final String COMPLETEREGISTRATION = "CompleteRegistration";//点击开启缘分按钮上报
    public static final String FRONTPAGE = "FrontPage";//进入首页
    public static final String VIDEOSPEEDMATCHING = "VideoSpeedMatching";//点击视频速配按钮时上报。
    public static final String VOICESPEEDMATCHING = "VoiceSpeedMatching";//点击语音速配按钮时上报
    public static final String TRUNONVIDEOMATCH = "TrunOnVideoMatch";//视频速配-接通成功时上报
    public static final String TRUNONPHONEMATCH = "TrunOnPhoneMatch";//语音速配-接通成功时上报
    public static final String CLICKTOCHAT = "ClickToChat";//点击搭讪按钮时触发。位置包括：首页、动态、用户详情页、粉丝、关注、访客、足迹
    public static final String PRIVATECHAT = "PrivateChat";//点击IM-发送按钮时触发。
    public static final String STARTVIDEO = "StartVideo";//点击-发起视频通话时触发。位置包括：用户详情页、IM页
    public static final String TURNONTHEVIDEO = "TurnOnTheVideo";//点击接通视频通话按钮时触发
    public static final String STARTPHONE = "StartPhone";//点击发起语音通话时触发。位置包括：用户详情页、IM页。
    public static final String TURNONTHEPHONE = "TurnOnThePhone";//点击接通语音通话按钮时触发
    public static final String GIVEAWAYGIFT = "GiveawayGift";//点击“赠送礼物”按钮时触发。位置包括：IM中赠送、视频/语音中赠送
    public static final String RECHARGECLICK = "RechargeClick";//点击“充值”按钮时触发。位置包括：首页半屏弹窗、动态半屏弹窗、IM页半屏弹窗、视频/语音通话半屏弹窗、充值页
    public static final String ENTERDYNAMICPAGE = "EnterDynamicPage";//点击底部“动态”按钮时触发
    public static final String CLICKTOPUBLISH = "ClickToPublish";//发动态-点击“发布”按钮时触发
    public static final String ENTERPERSONALCENTER = "EnterPersonalCenter";//进入“用户详情页“时上触发上报
    public static final String ENTERMESSAGELIST = "EnterMessageList";//点击底部“消息”按钮触发
    public static final String ENTERMYPAGE = "EnterMyPage";//点击底部“我的”按钮时触发
    public static final String TASKCENTER = "TaskCenter";//点击“我的-任务中心”按钮时触发
    public static final String ENTERRECHARGEPAGE = "EnterRechargePage";//进入“充值”页时触发
    public static final String RECHARGEPOPUP = "RechargePopUp";//触发充值弹窗即上报。位置包括：首页半屏弹窗、动态半屏弹窗、IM页半屏弹窗、视频/语音通话半屏弹窗
    public static final String INITIATEPRIVATECHAT = "InitiatePrivateChat";//用户点击私信按钮即上报。位置包括：首页、动态、用户详情页、粉丝、关注、访客、足迹
    public static final String ENTERCHATPAGE = "EnterChatPage";//用户每进入一次聊天页即上报


    public static void setUmEvent(Activity activity, String event) {
        if (activity == null) {
            return;
        }
        if (activity.isDestroyed()) {
            return;
        }
        if (isOpen) {
            MobclickAgent.onEvent(activity, event);
        }
    }

}
