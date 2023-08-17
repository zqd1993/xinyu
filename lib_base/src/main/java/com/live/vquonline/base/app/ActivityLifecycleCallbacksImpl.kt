package com.live.vquonline.base.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.bean.AgoraFloatViewEvent
import com.live.vquonline.base.bean.AgoraServiceEvent
import com.live.vquonline.base.constant.CurrentActivityStatus
import com.live.vquonline.base.utils.ActivityStackManager
import com.live.vquonline.base.utils.AppManager
import org.greenrobot.eventbus.EventBus

/**
 * Activity生命周期监听
 *
 * ""
 * @since 4/20/21 9:10 AM
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {

    private val TAG = "ActivityLifecycle"

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        ActivityStackManager.addActivityToStack(activity)
        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {

//        if (AppManager.mActivityCount == 0
//            && !AppManager.isServiceRunning(
//                BaseApplication.context,
//                "com.live.module_agora.service.FloatingDialogService"
//            )
//            && ActivityStackManager.isHasActivity(Class.forName("com.live.module_agora.activity.AgoraVquVideoActivity"))
//        ) {
//            ARouter.getInstance()
//                .build("/agora/AgoraVquVideoActivity")
//                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                .navigation()
//        }
        if (AppManager.mActivityCount == 0) {
            EventBus.getDefault().post("goFront")
//            startAgora()
            if (AppManager.isMiniWindow) {
                EventBus.getDefault().post(AgoraFloatViewEvent())
            }
            Log.d(TAG, "onActivityStopped: 1")
            EventBus.getDefault().post(AgoraServiceEvent(false))
        }

        AppManager.mActivityCount++

        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivityStarted")

    }


    private fun startAgora() {
        val isNoVideo =
            !ActivityStackManager.isCurrentActivity(Class.forName("com.live.module.agora.activity.AgoraTantaVideoActivity"))
        val isNoVoice =
            !ActivityStackManager.isCurrentActivity(Class.forName("com.live.module.agora.activity.AgoraTantaAudioActivity"))

        if (
            !AppManager.isMiniWindow && isNoVideo && isNoVoice
        ) {
            if (ActivityStackManager.isHasActivity(Class.forName("com.live.module.agora.activity.AgoraTantaVideoActivity"))) {
                ARouter.getInstance()
                    .build("/agora2/AgoraTantaVideoActivity")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            } else if (ActivityStackManager.isHasActivity(Class.forName("com.live.module.agora.activity.AgoraTantaAudioActivity"))) {
                ARouter.getInstance()
                    .build("/agora2/AgoraTantaAudioActivity")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .navigation()
            }
        } else {
            if (AppManager.isMiniWindow) {
                EventBus.getDefault().post(AgoraFloatViewEvent())
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        CurrentActivityStatus.currentActivitySimpleName = activity.javaClass.simpleName
        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivityStopped")
        AppManager.mActivityCount--

        if (AppManager.mActivityCount == 0) {
            Log.d(TAG, "onActivityStopped: 5")
            EventBus.getDefault().post(AgoraServiceEvent(true))
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityStackManager.popActivityToStack(activity)
        Log.e(TAG, "${activity.javaClass.simpleName} --> onActivityDestroyed")

        if (activity.javaClass.simpleName == "AgoraTantaVideo2Activity" || activity.javaClass.simpleName == "AgoraTantaAudio2Activity") {
            EventBus.getDefault().post("agoraDestroy")
        }
    }
}