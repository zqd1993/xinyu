package com.live.vquonline.base.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

/**
 * author: Lau
 * date: 2022/7/14
 * description:
 */
public class DisplayUtils {
    /**
     * 保持字体大小不随系统设置变化（用在界面加载之前）
     * 要重写Activity的getResources()
     */
    public static Resources getResources(Context context, Resources resources, float fontScale) {
        Configuration config = resources.getConfiguration();
//        if(config.fontScale != fontScale) {
            config.fontScale = fontScale;
            return context.createConfigurationContext(config).getResources();
//        } else {
//            return resources;
//        }
    }

}
