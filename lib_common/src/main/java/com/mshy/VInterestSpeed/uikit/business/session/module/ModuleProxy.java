package com.mshy.VInterestSpeed.uikit.business.session.module;

import com.netease.nimlib.sdk.msg.model.IMMessage;

/**
 * 会话窗口提供给子模块的代理接口。
 */
public interface ModuleProxy {
    // 发送消息
    boolean sendMessage(IMMessage msg);

    // 消息输入区展开时候的处理
    void onInputPanelExpand();

    // 应当收起输入区
    void shouldCollapseInputPanel();

    // 是否正在录音
    boolean isLongClickEnabled();

    void onItemFooterClick(IMMessage message);

    //发送提示消息
    void onSendTipMsg();

    //获取聊天资料卡信息
    void getChatInfo();

    //拨打语音
    void callAudio(String account);

    //拨打视频
    void callVideo(String account);
}
