package com.live.module.message.mixpush;


import com.mshy.VInterestSpeed.uikit.api.model.main.CustomPushContentProvider;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * 示例：
 * 1.自定义的推送文案
 * 2.自定义推送 payload 实现特定的点击通知栏跳转行为{@linkDemoMixPushMessageHandler}
 * <p>
 * 如果自定义文案和payload，请开发者在各端发送消息时保持一致。
 */

public class DemoPushContentProvider implements CustomPushContentProvider {

    @Override
    public String getPushContent(IMMessage message) {

        String pushContent = null;

        if (null != message) {
            MsgTypeEnum msgType = message.getMsgType();
            int value = msgType.getValue();
            if (value == 0) {  //文本
                pushContent = "收到一条信息";
            }else if (value == 1) {
                pushContent = "收到一张图片";
            }else if (value == 3) {
                pushContent = "收到一段小视频";
            }else if (value == 100) {
                pushContent = "神秘惊喜";
            }else if (value == 5) {
                pushContent = "通知";
            }
        }

        return pushContent;
    }

    @Override
    public Map<String, Object> getPushPayload(IMMessage message) {
        return getPayload(message);
    }

    private Map<String, Object> getPayload(IMMessage message) {
        if (message == null) {
            return null;
        }
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("sessionType", message.getSessionType().getValue());
        if (message.getSessionType() == SessionTypeEnum.Team) {
            payload.put("sessionID", message.getSessionId());
        } else if (message.getSessionType() == SessionTypeEnum.P2P) {
            payload.put("sessionID", message.getFromAccount());
        }

        return payload;
    }
}
