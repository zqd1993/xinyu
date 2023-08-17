package com.mshy.VInterestSpeed.uikit.common.badger

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.SystemUtils
import com.mshy.VInterestSpeed.common.constant.SpKey


/**
 * FileName: com.mshy.VInterestSpeed.uikit.common.badger
 * Date: 2022/6/11 14:
 * Description:
 * History:
 */
object BadgerNum {
    fun setBadgerNum(context: Context,num: Int) {
        var count = num.coerceAtMost(99)
        if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
            count=0
        }
        SystemUtils.getDeviceBrand()?.let {
            when (it) {
                SystemUtils.PHONE_HUAWEI,SystemUtils.PHONE_HONOR,SystemUtils.PHONE_NOVA -> {
                    setBadgerHW(context,count)
                }
                SystemUtils.PHONE_OPPO -> {
                    setBadgeOPPO(context,count)
                }
                SystemUtils.PHONE_VIVO -> {
                    setBadgeVIVO(context,count)
                }
                else -> {
                }
            }
        }
    }

    private fun setBadgerHW(context: Context, number: Int) {
        try {
            val bundle = Bundle()
            bundle.putString("package", context.packageName)
            val launchClassName =
                context.packageManager.getLaunchIntentForPackage(context.packageName)!!
                    .component!!.className
            bundle.putString("class", launchClassName)
            bundle.putInt("badgenumber", number)
            context.contentResolver.call(
                Uri.parse("content://com.huawei.android.launcher.settings/badge/"),
                "change_badge",
                null,
                bundle
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun setBadgeOPPO(context: Context, number: Int) {
        try {
            val intent = Intent("com.oppo.unsettledevent")
            intent.putExtra("pakeageName", context.packageName)
            intent.putExtra("number", number)
            intent.putExtra("upgradeNumber", number)
            if (canResolveBroadcast(context, intent)) {
                context.sendBroadcast(intent)
            } else {
                try {
                    val extras = Bundle()
                    extras.putInt("app_badge_count", number)
                    context.contentResolver.call(
                        Uri.parse("content://com.android.badge/badge"),
                        "setAppBadgeCount",
                        null,
                        extras
                    )
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun setBadgeVIVO(context: Context, number: Int) {
        try {
            val intent = Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM")
            intent.putExtra("packageName", context.packageName)
            val launchClassName =
                context.packageManager.getLaunchIntentForPackage(context.packageName)!!
                    .component!!.className
            intent.putExtra("className", launchClassName)
            intent.putExtra("notificationNum", number)
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
//                intent.addFlags(Intent.FLAG_FROM_BACKGROUND)
//            }
            context.sendBroadcast(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun canResolveBroadcast(context: Context, intent: Intent): Boolean {
        val packageManager: PackageManager = context.packageManager
        val receivers: List<ResolveInfo> = packageManager.queryBroadcastReceivers(intent, 0)
        return receivers.isNotEmpty()
    }

}