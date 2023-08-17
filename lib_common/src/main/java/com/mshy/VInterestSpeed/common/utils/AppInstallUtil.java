package com.mshy.VInterestSpeed.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.live.vquonline.base.BaseApplication;

/**
 * author: Tany
 * date: 2022/5/10
 * description:
 */
public class AppInstallUtil {
    // 返回 true 则已安装
    // 返回 false 则为未安装
    public static boolean isAppInstalled(Context context, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo != null) {
            return true;
        }
        return false;
    }

    public static boolean isWxInstalled(){
        return isAppInstalled(BaseApplication.context,"com.tencent.mm");
    }
    public static boolean isQqInstalled(){
        return isAppInstalled(BaseApplication.context,"com.tencent.mobileqq");
    }

}
