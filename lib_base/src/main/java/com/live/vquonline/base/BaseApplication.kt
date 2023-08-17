package com.live.vquonline.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.github.gzuliyujiang.oaid.DeviceIdentifier
import com.live.vquonline.base.app.ActivityLifecycleCallbacksImpl
import com.live.vquonline.base.app.LoadModuleProxy
import com.live.vquonline.base.utils.SpUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.system.measureTimeMillis

/**
 * Application 基类
 *
 * ""
 * @since 4/24/21 5:30 PM
 */
open class BaseApplication : MultiDexApplication() {

    private val mCoroutineScope by lazy(mode = LazyThreadSafetyMode.NONE) { MainScope() }

    private val mLoadModuleProxy by lazy(mode = LazyThreadSafetyMode.NONE) { LoadModuleProxy() }

    companion object {
        // 全局Context
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var application: BaseApplication
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        context = base
        application = this
        mLoadModuleProxy.onAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        // 全局监听 Activity 生命周期n
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())

        mLoadModuleProxy.onCreate(this)

        // 策略初始化第三方依赖
//        initDepends()

        if (SpUtils.getBoolean("agreementIsShow", false) == true) {
            initDepends()
        }
    }

    /**
     * 初始化第三方依赖
     */
    fun initDepends() {
        DeviceIdentifier.register(this)
        // 开启一个 Default Coroutine 进行初始化不会立即使用的第三方
//        mCoroutineScope.launch(Dispatchers.Default) {
//            mLoadModuleProxy.initByBackstage()
//        }
        Thread{
            mLoadModuleProxy.initByBackstage()
        }.start()

        // 前台初始化
        val allTimeMillis = measureTimeMillis {
            val depends = mLoadModuleProxy.initByFrontDesk()
            val endPoint = mLoadModuleProxy.initByEnd()

            var dependInfo: String

            depends.forEach {
                val dependTimeMillis = measureTimeMillis { dependInfo = it() }
                Log.d("BaseApplication", "initDepends: $dependInfo : $dependTimeMillis ms")
            }
            endPoint.forEach {
                val endTimeMillis = measureTimeMillis { dependInfo = it() }
                Log.d("BaseApplication", "initByEnd: $dependInfo : $endTimeMillis ms")
            }
        }
        Log.d("BaseApplication", "初始化完成 $allTimeMillis ms")

    }

    override fun onTerminate() {
        super.onTerminate()
        mLoadModuleProxy.onTerminate(this)
        mCoroutineScope.cancel()
    }


}