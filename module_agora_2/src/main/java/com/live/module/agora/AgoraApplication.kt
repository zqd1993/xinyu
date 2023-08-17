package com.live.module.agora

import android.app.Application
import android.content.Context
import com.faceunity.nama.FURenderer
import com.google.auto.service.AutoService
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.app.ApplicationLifecycle
import com.live.vquonline.base.utils.ProcessUtils

/**
 * author: Lau
 * date: 2022/7/19
 * description:
 */
@AutoService(ApplicationLifecycle::class)
class AgoraApplication : ApplicationLifecycle {
    override fun onAttachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {

    }

    override fun onTerminate(application: Application) {
    }

    override fun initByFrontDesk(): MutableList<() -> String> {

        return mutableListOf()
    }

    override fun initByBackstage() {
        initFURenderer()
    }

    private fun initFURenderer() {
        if (ProcessUtils.isMainProcess(BaseApplication.application)) {
            RtcEngineManager.init(BaseApplication.application)
            FURenderer.getInstance().setup(BaseApplication.application)
            VideoManager.init(BaseApplication.application)
        }
    }

    override fun initByEnd(): MutableList<() -> String> {
        return mutableListOf()
    }
}