package com.mshy.VInterestSpeed.uikit

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.app.ApplicationLifecycle
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.mixpush.MixPushConfig
import com.netease.nimlib.sdk.util.NIMUtil
import java.io.IOException


/**
 * FileName: com.netease.nim.uikit
 * Date: 2022/4/11 11:04
 * Description:
 * History:
 */
abstract class NiMUIKitVquApplication : ApplicationLifecycle {

    val tag = javaClass.simpleName


    companion object {
        private var instance: NiMUIKitVquApplication? = null
        fun getInstance(): NiMUIKitVquApplication? {
            return instance
        }
    }

    override fun onCreate(application: Application) {
        if (NIMUtil.isMainProcess(application)) {
            vquPushInit()
        }
        vquInitNIM()
        instance = this
        if (NIMUtil.isMainProcess(application)) {
            vquInitNimUIKit()
            vquRegisterMsgViewHolder()
            vquRegisterNIMObserver()
            vquOtherNIMConfig()
        }
    }

    /**
     * 注册网易云信相关的viewHolder
     */
    abstract fun vquRegisterMsgViewHolder()

    /**
     * 配置其他网易云信的配置
     */
    abstract fun vquOtherNIMConfig()

    /**
     * 注册网易云信观察者
     */
    abstract fun vquRegisterNIMObserver()

    /**
     * 一些推送的提前配置
     */
    abstract fun vquPushInit()


    public fun vquInitNIM() {
        NIMClient.init(BaseApplication.context, getLoginInfo().invoke(), vquGetSDKOptions())
        Log.d(tag, "NIM -->> init complete")
    }

    /**
     * 初始化网易云信ui组件
     */
    private fun vquInitNimUIKit() {
        com.mshy.VInterestSpeed.uikit.api.NimUIKit.init(BaseApplication.context)
        Log.d(tag, "NIM -->> 初始化网易云信ui组件complete")
    }


    private fun vquGetSDKOptions(): SDKOptions {
        val sdkOptions = SDKOptions()
        sdkOptions.statusBarNotificationConfig = getStatusConfig(
            SpUtils.getBoolean(SpKey.RING_NOTICE, true)!!,
            SpUtils.getBoolean(SpKey.SHAKE_NOTICE, true)!!
        )
        sdkOptions.sessionReadAck = true
        val sdkPath: String = getAppCacheDir(BaseApplication.context).toString() + "/nim"
        sdkOptions.sdkStorageRootPath = sdkPath
        sdkOptions.preloadAttach = true
        sdkOptions.mixPushConfig = vquBuildMixPushConfig()
        return sdkOptions
    }

    open fun vquBuildMixPushConfig(): MixPushConfig {
        return MixPushConfig()
    }


    //获取IM登录信息
    open fun getLoginInfo(): () -> LoginInfo? = {
        null
    }

    override fun onAttachBaseContext(context: Context) {

    }


    override fun onTerminate(application: Application) {
    }

    override fun initByFrontDesk(): MutableList<() -> String> {
        return mutableListOf()
    }

    override fun initByBackstage() {
//        vquPushInit()
    }

    override fun initByEnd(): MutableList<() -> String> {
        return mutableListOf()
    }

    fun getStatusConfig(ring: Boolean, vibrate: Boolean): StatusBarNotificationConfig? {
        val config = StatusBarNotificationConfig()
        config.notificationEntrance = com.mshy.VInterestSpeed.uikit.common.activity.FakeActivity::class.java
        config.ledARGB = Color.GREEN
        config.ledOnMs = 1000
        config.ledOffMs = 1500
        config.ring = ring
        config.vibrate = vibrate
        return config
    }

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    private fun getAppCacheDir(context: Context): String? {
        var storageRootPath: String? = null
        try {
            if (context.externalCacheDir != null) {
                storageRootPath = context.externalCacheDir!!.canonicalPath
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            storageRootPath =
                Environment.getExternalStorageState() + "/" + context.packageName
        }
        return storageRootPath
    }
}