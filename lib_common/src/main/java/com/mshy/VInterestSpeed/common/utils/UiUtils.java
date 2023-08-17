package com.mshy.VInterestSpeed.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by chenqihong on 2017/1/17.
 */

public class UiUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 检查权限是否同意
     * @param permission 权限名称
     * @return true 具有该权限
     */
    public static boolean checkPermission(Context context, String permission) {
        return context.getPackageManager().checkPermission(permission, context.getPackageName()) == PackageManager
                .PERMISSION_GRANTED;
    }
}
