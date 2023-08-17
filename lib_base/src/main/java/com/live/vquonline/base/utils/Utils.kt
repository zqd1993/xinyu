package com.live.vquonline.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.NetworkInterface
import java.util.*


/**
 * 使用 Flow 做的简单的轮询
 * 请使用单独的协程来进行管理该 Flow
 * Flow 仍有一些操作符是实验性的 使用时需添加 @InternalCoroutinesApi 注解
 * @param intervals 轮询间隔时间/毫秒
 * @param block 需要执行的代码块
 */
suspend fun startPolling(intervals: Long, block: () -> Unit) {
    flow {
        while (true) {
            delay(intervals)
            emit(0)
        }
    }
        .catch { Log.e("flow", "startPolling: $it") }
        .flowOn(Dispatchers.Main)
        .collect { block.invoke() }
}
/**************************************************************************************************/

/**
 * 发送普通EventBus事件
 */
fun sendEvent(event: Any) = EventBusUtils.postEvent(event)

/**************************************************************************************************/
/**
 * 阿里路由不带参数跳转
 * @param routerUrl String 路由地址
 */
fun aRouterJump(routerUrl: String) {
    ARouter.getInstance().build(routerUrl).navigation()
}

/**************************************************************************************************/
/**
 * toast
 * @param msg String 文案
 * @param duration Int 时间
 */
fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    if(msg.isNullOrEmpty()){
        return
    }
    ToastUtils.showToast(msg, duration)
}

/**
 * toast
 * @param msgId Int String资源ID
 * @param duration Int 时间
 */
fun toast(msgId: Int, duration: Int = Toast.LENGTH_SHORT) {
    ToastUtils.showToast(msgId, duration)
}
/**************************************************************************************************/
/**
 * 获取App版本号
 * @return Long App版本号
 */
fun getVersionCode(): Long {
    val packageInfo = BaseApplication.context
        .packageManager
        .getPackageInfo(BaseApplication.context.packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode
    } else {
        packageInfo.versionCode.toLong()
    }
}

/**************************************************************************************************/
/**
 * 获取App版本名
 * @return String App版本名
 */
fun getVersionName(): String {
    return BaseApplication.context
        .packageManager
        .getPackageInfo(BaseApplication.context.packageName, 0)
        .versionName
}

/**************************************************************************************************/
/**
 * 获取App包名
 * @return String App包名
 */
fun getPackageManagerName(): String {
    return BaseApplication.context
        .packageManager
        .getPackageInfo(BaseApplication.context.packageName, 0).packageName

}

/**************************************************************************************************/
/**
 * 获取手机版本号
 * @return String 手机版本号
 */
fun getPhoneVersion(): Int {
    return android.os.Build.VERSION.SDK_INT
}

/**************************************************************************************************/
/**
 * 获取手机设备名
 * @return String 设备名
 */
fun getSystemModel(): String? {
    return Build.MODEL
}

/**************************************************************************************************/
/**
 * 获取 androidId
 * @return String androidId
 */
fun getDeviceImei(): String? {
    return Settings.System.getString(
        BaseApplication.context.getContentResolver(),
        Settings.Secure.ANDROID_ID
    )
}

/**************************************************************************************************/
/**
 * 获取是否有Sim卡
 */
fun hasSimCard(): Boolean {
    val systemService =
        BaseApplication.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val simState = systemService.simState

    val result = when (simState) {
        TelephonyManager.SIM_STATE_ABSENT -> false
        TelephonyManager.SIM_STATE_UNKNOWN -> false
        else -> true
    }


    return result
}

fun getClipboardMessage(): String? {
    val clipboardManager =
        BaseApplication.application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val data = clipboardManager.primaryClip

    var message = data?.getItemAt(0)?.text?.toString()
    if (!message.isNullOrEmpty()) {
        if (message.contains("#vqu#")) {
            message = message.replace("#vqu#", "")
            return message
        }
    }
    return null
}

fun copyString(content: String) {
    val clipboardManager =
        BaseApplication.application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, content))
}

fun getMac(): String? {
    try {
        val all: List<NetworkInterface> =
            Collections.list(NetworkInterface.getNetworkInterfaces())
        for (nif in all) {
            if (!nif.name.equals("wlan0", ignoreCase = true)) continue
            val macBytes: ByteArray = nif.hardwareAddress ?: return null
            val res1 = StringBuilder()
            for (b in macBytes) {
                res1.append(String.format("%02X:", b))
            }
            if (res1.isNotEmpty()) {
                res1.deleteCharAt(res1.length - 1)
            }
            return res1.toString()
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return null
}


