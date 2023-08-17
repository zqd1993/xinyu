package com.live.module.message.utils;

import com.netease.nimlib.sdk.event.model.Event;

import java.util.HashMap;

/**
 * author: Tany
 * date: 2022/6/23
 * description:
 */
public class OnlineMapUtil {
    private static HashMap<String, Event> conversationOnlineMap = new HashMap();

    public static void putOnline(String id, Event event) {
        conversationOnlineMap.put(id, event);
    }

    public static HashMap<String, Event> getOnline() {
        return conversationOnlineMap;
    }

    public static Event getOnlineEvent(String id) {
        if (conversationOnlineMap.get(id) != null) {
            return conversationOnlineMap.get(id);
        }
        return null;
    }

}
