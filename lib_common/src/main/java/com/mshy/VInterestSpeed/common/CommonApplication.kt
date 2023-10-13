package com.mshy.VInterestSpeed.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.alipay.face.api.ZIMFacade
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ellison.glide.translibrary.ImageUtils
import com.fm.openinstall.OpenInstall
import com.google.auto.service.AutoService
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.app.ApplicationLifecycle
import com.live.vquonline.base.constant.VersionStatus
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.ProcessUtils
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.network.NetworkStateClient
import com.mshy.VInterestSpeed.common.bean.FloatingEvent
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.helper.ARouterHelper
import com.mshy.VInterestSpeed.common.ui.activity.CommonErrorActivity
import com.mshy.VInterestSpeed.common.ui.activity.SplashActivity
import com.mshy.VInterestSpeed.common.ui.loadsir.EmptyCallback
import com.mshy.VInterestSpeed.common.ui.loadsir.ErrorCallback
import com.mshy.VInterestSpeed.common.ui.loadsir.LoadingCallback
import com.mshy.VInterestSpeed.common.ui.loadsir.TimeoutCallback
import com.mshy.VInterestSpeed.common.ui.view.CustomRefreshHeader
import com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.TestImageLoader
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.ZoomMediaLoader
import com.mshy.VInterestSpeed.common.utils.AgoraUtils
import com.mshy.VInterestSpeed.common.utils.CustomParams
import com.mshy.VInterestSpeed.common.utils.ShareManager
import com.mshy.VInterestSpeed.common.utils.glide.transformation.GlideLoader
import com.qiyukf.nimlib.sdk.StatusBarNotificationConfig
import com.qiyukf.unicorn.api.ImageLoaderListener
import com.qiyukf.unicorn.api.Unicorn
import com.qiyukf.unicorn.api.UnicornImageLoader
import com.qiyukf.unicorn.api.YSFOptions
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.sdk.engine.AIDParams
import com.sdk.engine.RiskControlEngine
import com.tencent.bugly.Bugly
import com.tencent.tauth.Tencent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.ExecutionException


/**
 * 项目相关的Application
 *
 * ""
 * @since 4/16/21 3:37 PM
 */
@AutoService(ApplicationLifecycle::class)
class CommonApplication : ApplicationLifecycle, ViewModelStoreOwner {
    private var mFactory: ViewModelProvider.Factory? = null
    private lateinit var mAppViewModelStore: ViewModelStore
    private var myApplication: Application? = null

    companion object {
        // 全局CommonApplication
        @SuppressLint("StaticFieldLeak")
        lateinit var mCommonApplication: CommonApplication

        @SuppressLint("StaticFieldLeak")
        var ysfOptions: YSFOptions? = null
    }

    /**
     * 同[Application.attachBaseContext]
     * @param context Context
     */
    override fun onAttachBaseContext(context: Context) {
        mCommonApplication = this
    }

    /**
     * 同[Application.onCreate]
     * @param application Application
     */
    override fun onCreate(application: Application) {
        initAutoSize()
        myApplication = application
        mAppViewModelStore = ViewModelStore()
        ZoomMediaLoader.getInstance().init(TestImageLoader())
//        if (ProcessUtils.isMainProcess(application)) {
        initARouter()
        initLoadSir()
        initMMKV()
        initGlideImageUtils()

        FloatWindow.with(application) //application上下文
            .setLayoutId(R.layout.common_agora_floting_view) //悬浮布局
            //.setFilter(Test1_1Activity.class)//过滤activity
            //.setLayoutParam()//设置悬浮布局layoutParam
            .build()


//            FloatWindow.get().view?.findViewById<FrameLayout>(R.id.floating_video_parent)
//                ?.setViewClickListener {
//                    ARouter.getInstance().build(RouteUrl.Agora2.AgoraVquVideoActivity)
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .navigation()
//                    EventBus.getDefault().post(FloatingEvent())
//                }

//            FloatWindow.get().view?.findViewById<CardView>(R.id.floating_audio_contact_parent)
//                ?.setViewClickListener {
//                    ARouter.getInstance().build(RouteUrl.Agora2.AgoraVquAudioActivity)
//                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        .navigation()
//                    EventBus.getDefault().post(FloatingEvent())
//                }

        FloatWindow.get().setOnClickListener {

            if (AgoraUtils.contactType == AgoraUtils.CONTACT_TYPE_VIDEO) {
                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                    .navigation()
            } else if (AgoraUtils.contactType == AgoraUtils.CONTACT_TYPE_AUDIO) {
                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                    .navigation()
            }

            EventBus.getDefault().post(FloatingEvent())
        }
//        }

    }


    /**
     * 同[Application.onTerminate]
     * @param application Application
     */
    override fun onTerminate(application: Application) {}

    /**
     * 主线程前台初始化
     * @return MutableList<() -> String> 初始化方法集合
     */
    override fun initByFrontDesk(): MutableList<() -> String> {
        val list = mutableListOf<() -> String>()
        // 以下只需要在主进程当中初始化 按需要调整
        if (ProcessUtils.isMainProcess(BaseApplication.application)) {
//            list.add { initNetworkStateClient() }
//            list.add { initLoadSir() }
            list.add { initOpenInstall() }
            list.add { initDeviceManager() }
            list.add { initRiskControlSDK() }
//            list.add { initGlideImageUtils() }
//            list.add { initRPVerify() }
//            list.add { initUmeng() }
        }
//        list.add { initTencentBugly() }
        return list
    }

    /**
     * 初始化风控SDK
     */
    private fun initRiskControlSDK(): String {
        val params = AIDParams()
        params.setIDParams(
            CustomParams(
                myApplication
            )
        )

        //设置是否打印调试日志，默认不打印
//        RiskControlEngine.setLogEnable(true);
        //初始化SDK
        val ret = RiskControlEngine.init(myApplication, params)
        //ret:返回值，0成功，-1失败
        Log.i("RiskControl", "RiskControlEngine init::$ret")
        return "RiskControl"
    }

    private fun initUmeng(): String {
        //QQ官方sdk授权
        Tencent.setIsPermissionGranted(true)
        UMConfigure.init(
            BaseApplication.context,
            "6527a72358a9eb5b0aecc1aa",
            DeviceManager.getInstance().getChannel(),
            UMConfigure.DEVICE_TYPE_PHONE,
            ""
        );
        PlatformConfig.setWXFileProvider("queniang.love.fileProvider")
        ShareManager.getInstance().configQQ(
            "1112111172",
            "WvtHEdQDDmfJ4wWh"
        )
        ShareManager.getInstance().configWeixin(
            "wx61b5a6be779642f4",
            "ca310c154e431eebdfc5a0ca1c1ca9eb"
        )
        return "Umeng"
    }

    private fun initRPVerify(): String {
//        RPVerify.init(BaseApplication.context)
        ZIMFacade.install(BaseApplication.context)
        return "RPVerify -->> init complete"
    }

    private fun initOpenInstall(): String {
        OpenInstall.init(BaseApplication.context)
//        System.loadLibrary("msaoaidsec")
        return "OpenInstall -->> init complete"
    }

    private fun initDeviceManager(): String {
        DeviceManager.getInstance().init(BaseApplication.context)
        DeviceManager.getInstance().setChannelName(DeviceManager.getInstance().aiJiaMiChannel);
        return "DeviceManager -->> init complete"
    }

    private fun initGlideImageUtils(): String {
        ImageUtils.init(
            BaseApplication.context,
            GlideLoader()
        )
        return "initGlideImageUtils -->> init complete"
    }


    private fun initLoadSir(): String {
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback())
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(TimeoutCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .commit()
        return "LoadSir -->> init complete"
    }

    /**
     * 不需要立即初始化的放在这里进行后台初始化
     */
    override fun initByBackstage() {

        initNetworkStateClient()
//        initX5WebViewCore()
        initTencentBugly()
        initUmeng()
        initRPVerify()
        initQiyu()
    }

    override fun initByEnd(): MutableList<() -> String> {
        return mutableListOf()
    }


    /**
     * 初始化网络状态监听客户端
     * @return Unit
     */
    private fun initNetworkStateClient(): String {
        NetworkStateClient.register()
        return "NetworkStateClient -->> init complete"
    }

    /**
     * 腾讯TBS WebView X5 内核初始化
     */
    private fun initX5WebViewCore() {
//        // dex2oat优化方案
//        val map = HashMap<String, Any>()
//        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//        QbSdk.initTbsSettings(map)
//
//        // 允许使用非wifi网络进行下载
//        QbSdk.setDownloadWithoutWifi(true)
//
//        // 初始化
//        QbSdk.initX5Environment(BaseApplication.context, object : PreInitCallback {
//
//            override fun onCoreInitFinished() {
//                Log.d("ApplicationInit", " TBS X5 init finished")
//            }
//
//            override fun onViewInitFinished(p0: Boolean) {
//                // 初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核
//                Log.d("ApplicationInit", " TBS X5 init is $p0")
//            }
//        })
    }

    /**
     * 腾讯 MMKV 初始化
     */
    private fun initMMKV(): String {
        val result = SpUtils.initMMKV(BaseApplication.context)
        return "MMKV -->> $result"
    }

    /**
     * 阿里路由 ARouter 初始化
     */
    private fun initARouter(): String {
        // 测试环境下打开ARouter的日志和调试模式 正式环境需要关闭
        ARouterHelper.init(
            BaseApplication.application,
            BuildConfig.VERSION_TYPE != VersionStatus.RELEASE
        )
        return "ARouter -->> init complete"
    }

    /**
     * 初始化 腾讯Bugly
     * 测试环境应该与正式环境的日志收集渠道分隔开
     * 目前有两个渠道 测试版本/正式版本
     */
    private fun initTencentBugly(): String {//暂时隐藏
        Bugly.init(
            BaseApplication.context,
            BaseApplication.context.getString(R.string.BUGLY_APP_ID),
            BuildConfig.VERSION_TYPE != VersionStatus.RELEASE
        )
        initCrash()
        return "Bugly -->> init complete"
    }

    private fun initCrash() {
        initXlog()
        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
            .restartActivity(SplashActivity::class.java) // 重启的activity
            .errorActivity(CommonErrorActivity::class.java) //发生错误跳转的activity
            .apply()

    }

    fun initXlog() {
//        System.loadLibrary("c++_shared")
//        System.loadLibrary("marsxlog")
//        val logPath = myApplication?.getExternalFilesDir(null)!!.path.toString() + "/xlog"
//        val xlog = Xlog()
//        com.tencent.mars.xlog.Log.setLogImp(xlog)
//        com.tencent.mars.xlog.Log.setConsoleLogOpen(true)
//        com.tencent.mars.xlog.Log.appenderOpen(
//            Xlog.LEVEL_INFO,
//            Xlog.AppednerModeAsync,
//            "",
//            logPath,
//            "aitanglogs",
//            0
//        )
    }

    /**
     * 初始化 腾讯Bugly
     * 测试环境应该与正式环境的日志收集渠道分隔开
     * 目前有两个渠道 测试版本/正式版本
     */
    private fun initAutoSize(): String {
        AutoSize.initCompatMultiProcess(BaseApplication.context)
        AutoSizeConfig.getInstance().isBaseOnWidth = false
        return "AutoSize -->> init complete"
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(myApplication!!)
        }
        return mFactory as ViewModelProvider.Factory
    }

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(DefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.ps_color_transparent, R.color.ps_color_transparent)
            CustomRefreshHeader(context)
        })
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context).setDrawableSize(20.0f)
        }
    }

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    fun initQiyu() {
        val listener = object : UnicornImageLoader {
            override fun loadImageSync(uri: String?, width: Int, height: Int): Bitmap? {
                var bitmap: Bitmap? = null
                try {
                    bitmap = Glide.with(BaseApplication.context)
                        .asBitmap()
                        .load(uri)
                        .submit().get()
                } catch (e: ExecutionException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                return bitmap
            }

            override fun loadImage(
                uri: String?,
                width: Int,
                height: Int,
                listener: ImageLoaderListener?
            ) {
                var picWidth = width;
                var picHeight = height;
                if (picWidth <= 0) {
                    picWidth = Int.MIN_VALUE
                }

                if (picHeight <= 0) {
                    picHeight = Int.MIN_VALUE
                }

                Glide.with(BaseApplication.context).asBitmap()
                    .load(uri)
                    .into(object : CustomTarget<Bitmap?>(picWidth, picHeight) {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            listener?.onLoadComplete(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }
        Unicorn.config(
            BaseApplication.context,
            "55784fa47b0bea9d19ad412f1d529426",
            options(),
            listener
        )
    }

    // 如果返回值为null，则全部使用默认参数。
    private fun options(): YSFOptions? {
        ysfOptions = YSFOptions()
        ysfOptions!!.statusBarNotificationConfig = StatusBarNotificationConfig()
        return ysfOptions
    }

}
