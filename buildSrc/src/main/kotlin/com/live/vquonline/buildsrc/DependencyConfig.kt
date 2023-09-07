package com.live.vquonline.buildsrc

/**
 * 项目依赖版本统一管理
 *
 * ""
 * @since 4/24/21 4:00 PM
 */
object DependencyConfig {

    /**
     * 依赖版本号
     *
     * ""
     * @since 4/24/21 4:01 PM
     */
    object Version {

        // AndroidX--------------------------------------------------------------
        const val AppCompat = "1.2.0"
        const val CoreKtx = "1.3.1"
        const val ConstraintLayout = "2.0.1"                // 约束布局
        const val TestExtJunit = "1.1.2"
        const val TestEspresso = "3.3.0"
        const val ActivityKtx = "1.1.0"
        const val FragmentKtx = "1.2.5"
        const val MultiDex = "2.0.1"

        // Android---------------------------------------------------------------
        const val Junit = "4.13"
        const val Material = "1.2.0"                        // 材料设计UI套件

        // Kotlin----------------------------------------------------------------
        const val Kotlin = "1.5.10"
        const val Coroutines = "1.5.0"                      // 协程

        // JetPack---------------------------------------------------------------
        const val Lifecycle =
            "2.3.1"                       // Lifecycle相关（ViewModel & LiveData & Lifecycle）
        const val Hilt = "2.37"                           // DI框架-Hilt
        const val HiltAndroidx = "1.0.0"

        // GitHub----------------------------------------------------------------
        const val OkHttp = "4.9.0"                          // OkHttp
        const val OkHttpInterceptorLogging = "4.9.1"        // OkHttp 请求Log拦截器
        const val Retrofit = "2.9.0"                        // Retrofit
        const val RetrofitConverterGson = "2.9.0"           // Retrofit Gson 转换器
        const val Gson = "2.8.7"                            // Gson
        const val MMKV = "1.2.9"                            // 腾讯 MMKV 替代SP
        const val AutoSize = "1.2.1"                        // 屏幕适配
        const val ARoute = "1.5.1"                          // 阿里路由
        const val ARouteCompiler = "1.5.1"                  // 阿里路由 APT
        const val RecyclerViewAdapter = "3.0.7"             // RecyclerViewAdapter
        const val EventBus = "3.2.0"                        // 事件总线
        const val PermissionX = "1.6.1"                     // 权限申请
        const val LeakCanary = "2.7"                        // 检测内存泄漏
        const val AutoService = "1.0"                       // 自动生成SPI暴露服务文件
        const val Coil = "1.3.0"                            // Kotlin图片加载框架
        const val Loadsir = "1.3.8"                            // 管理界面状态库
        const val SmartRefreshLayout = "2.0.1"                //上下拉刷线加载
        const val Fastjson = "1.1.72"                        //Fastjson
        const val EasyDialog = "1.4"                         //交互对话框库
        const val ZXing = "3.5.0"                               //ZXing二维码

        // 第三方SDK--------------------------------------------------------------
        const val TencentBugly = "3.3.9"                    // 腾讯Bugly 异常上报
        const val TencentBuglyNative = "3.8.0"              // Bugly native异常上报
        const val TencentTBSX5 = "43939"                    // 腾讯X5WebView
        const val WechatSDK = "6.8.0"                       //微信开放SDK
        const val AlipaySDK = "15.8.09@aar"                 //支付宝SDK

        // 网易云信---------------------------------------------------------------
        private const val NIMVersion = "9.2.1"
        const val NIMBase = NIMVersion                      //基础功能包
        const val NIMChatroom = NIMVersion                  //聊天室
        const val NIMPush = NIMVersion                      //推送
        const val NIMSuperTeam = NIMVersion                 //超大群
        const val NIMLucene = NIMVersion                    //全文检索

        // 友盟---------------------------------------------------------------
        const val UMCommon = "9.4.7"
        const val UMAsms = "1.4.1"
        const val UMShare = "7.1.7"

        // 声网---------------------------------------------------------------
        const val Agora = "3.7.0.2"
//        const val Agora = "3.0.1"
    }

    /**
     * AndroidX相关依赖
     *
     * ""
     * @since 4/24/21 4:01 PM
     */
    object AndroidX {
        const val AndroidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val AppCompat = "androidx.appcompat:appcompat:${Version.AppCompat}"
        const val CoreKtx = "androidx.core:core-ktx:${Version.CoreKtx}"
        const val ConstraintLayout =
            "androidx.constraintlayout:constraintlayout:${Version.ConstraintLayout}"
        const val TestExtJunit = "androidx.test.ext:junit:${Version.TestExtJunit}"
        const val TestEspresso = "androidx.test.espresso:espresso-core:${Version.TestEspresso}"
        const val ActivityKtx = "androidx.activity:activity-ktx:${Version.ActivityKtx}"
        const val FragmentKtx = "androidx.fragment:fragment-ktx:${Version.FragmentKtx}"
        const val MultiDex = "androidx.multidex:multidex:${Version.MultiDex}"
    }

    /**
     * Android相关依赖
     *
     * ""
     * @since 4/24/21 4:02 PM
     */
    object Android {
        const val Junit = "junit:junit:${Version.Junit}"
        const val Material = "com.google.android.material:material:${Version.Material}"
    }

    /**
     * JetPack相关依赖
     *
     * ""
     * @since 4/24/21 4:02 PM
     */
    object JetPack {
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.Lifecycle}"
        const val ViewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.Lifecycle}"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.Lifecycle}"
        const val Lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.Lifecycle}"
        const val LifecycleCompilerAPT =
            "androidx.lifecycle:lifecycle-compiler:${Version.Lifecycle}"
        const val HiltCore = "com.google.dagger:hilt-android:${Version.Hilt}"
        const val HiltApt = "com.google.dagger:hilt-compiler:${Version.Hilt}"
        const val HiltAndroidx = "androidx.hilt:hilt-compiler:${Version.HiltAndroidx}"
    }

    /**
     * Kotlin相关依赖
     *
     * ""
     * @since 4/24/21 4:02 PM
     */
    object Kotlin {
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Version.Kotlin}"
        const val CoroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.Coroutines}"
        const val CoroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.Coroutines}"
    }

    /**
     * GitHub及其他相关依赖
     *
     * ""
     * @since 4/24/21 4:02 PM
     */
    object GitHub {
        const val OkHttp = "com.squareup.okhttp3:okhttp:${Version.OkHttp}"
        const val OkHttpInterceptorLogging =
            "com.squareup.okhttp3:logging-interceptor:${Version.OkHttpInterceptorLogging}"
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Version.Retrofit}"
        const val RetrofitConverterGson =
            "com.squareup.retrofit2:converter-gson:${Version.RetrofitConverterGson}"
        const val Gson = "com.google.code.gson:gson:${Version.Gson}"
        const val MMKV = "com.tencent:mmkv-static:${Version.MMKV}"
        const val AutoSize = "me.jessyan:autosize:${Version.AutoSize}"
        const val ARoute = "com.alibaba:arouter-api:${Version.ARoute}"
        const val ARouteCompiler = "com.alibaba:arouter-compiler:${Version.ARouteCompiler}"
        const val RecyclerViewAdapter =
            "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Version.RecyclerViewAdapter}"
        const val EventBus = "org.greenrobot:eventbus:${Version.EventBus}"
        const val EventBusAPT = "org.greenrobot:eventbus-annotation-processor:${Version.EventBus}"
        const val PermissionX = "com.guolindev.permissionx:permissionx:${Version.PermissionX}"

        const val LeakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.LeakCanary}"
        const val AutoService = "com.google.auto.service:auto-service:${Version.AutoService}"
        const val AutoServiceAnnotations =
            "com.google.auto.service:auto-service-annotations:${Version.AutoService}"
        const val Coil = "io.coil-kt:coil:${Version.Coil}"
        const val CoilGIF = "io.coil-kt:coil-gif:${Version.Coil}"
        const val CoilSVG = "io.coil-kt:coil-svg:${Version.Coil}"
        const val CoilVideo = "io.coil-kt:coil-video:${Version.Coil}"

        // 基础依赖包，必须要依赖
        const val immersionbar = "com.gyf.immersionbar:immersionbar:3.0.0";

        // fragment快速实现（可选）
        const val immersionbar_fragment = "com.gyf.immersionbar:immersionbar-components:3.0.0";

        // kotlin扩展（可选）
        const val immersionbar_kotlin = "com.gyf.immersionbar:immersionbar-ktx:3.0.0";

        //管理界面状态
        const val LoadSir = "com.kingja.loadsir:loadsir:${Version.Loadsir}";

        const val Glide = "com.github.bumptech.glide:glide:4.13.0";
        const val GlideCompiler = "com.github.bumptech.glide:compiler:4.13.0";
        const val PickerView = "com.contrarywind:Android-PickerView:4.1.9";

        const val Pictureselector = "io.github.lucksiege:pictureselector:v3.0.8";
        const val Compress = "io.github.lucksiege:compress:v3.0.8";
        const val Ucrop = "io.github.lucksiege:ucrop:v3.0.8";
        const val Lucksiege = "io.github.lucksiege:camerax:v3.0.8";

        //流式布局
        const val Flowlayout = "com.hyman:flowlayout-lib:1.1.2";

        //流式布局
        const val Logutils = "com.apkfuns.logutils:library:1.7.5";

        //底部tab
        const val PagerBottom = "me.majiajie:pager-bottom-tab-strip:2.4.0";

        const val SmartRefreshLayout = "com.scwang.smart:"
        const val SmartRefreshLayoutKernel =
            "${SmartRefreshLayout}refresh-layout-kernel:${Version.SmartRefreshLayout}"

        const val SmartRefreshLayoutHeaderClassics =
            "${SmartRefreshLayout}refresh-header-classics:${Version.SmartRefreshLayout}"

        const val SmartRefreshLayoutFooterClassics =
            "${SmartRefreshLayout}refresh-footer-classics:${Version.SmartRefreshLayout}"

        //fastJson
        const val FastJson = "com.alibaba:fastjson:${Version.Fastjson}.android"
        const val EasyDialog = "com.github.michaelye.easydialog:easydialog:${Version.EasyDialog}"


        //
        const val Banner = "io.github.youth5201314:banner:2.2.2"

        //轮播图
        const val BannerViewPager = "com.github.zhpanvip:BannerViewPager:3.1.5"

        //        const val YokeyWord = "me.yokeyword:fragmentation:1.3.4"
        const val GSYVideoPlayer = "com.github.CarGuo.GSYVideoPlayer:GSYVideoPlayer:v8.1.6-jitpack"

        const val AliUpload = "com.aliyun.video.android:upload:1.6.2"
        const val AliOos = "com.aliyun.dpa:oss-android-sdk:2.9.9"
        const val AliSvideos = "com.aliyun.video.android:svideostandard:3.17.0"//短视频标准版SDK必须依赖
        const val AliCore = "com.aliyun.video.android:core:1.2.2" //核心库必须依赖
        const val AliAlivcConan = "com.alivc.conan:AlivcConan:1.0.3"//核心库必须依赖
        const val AliAlivcFFmpeg = "com.aliyun.video.android:AlivcFFmpeg:2.0.0"//必须依赖

        //SVG动画库
        const val SVGAPlayer = "com.github.yyued:SVGAPlayer-Android:2.6.1"
//        const val VideoRangeSlider = "com.github.waynell:VideoRangeSlider:1.0.1"
        const val Bubble = "com.github.xujiaji:happy-bubble:1.2.5"
        const val Rxandroid = "io.reactivex.rxjava2:rxandroid:2.0.2"
        const val Rxjava = "io.reactivex.rxjava2:rxjava:2.1.12"

        const val ZXing = "com.google.zxing:core:${Version.ZXing}"

        const val Lottie = "com.airbnb.android:lottie:5.2.0"
        const val Apng = "com.github.penfeizhou.android.animation:apng:2.22.0"

        //阴影控件，类似CardView
        const val Shadow = "com.github.lihangleo2:ShadowLayout:3.3.3"
        const val Oaid = "com.github.gzu-liyujiang:Android_CN_OAID:4.2.4"

    }

    /**
     * SDK相关依赖
     *
     * ""
     * @since 4/24/21 4:02 PM
     */
    object SDK {
        const val TencentBugly = "com.tencent.bugly:crashreport:${Version.TencentBugly}"
        const val TencentBuglyNative =
            "com.tencent.bugly:nativecrashreport:${Version.TencentBuglyNative}"
        const val TencentBuglyUpgrade = "com.tencent.bugly:crashreport_upgrade:1.4.5"
        const val TencentTBSX5 = "com.tencent.tbs.tbssdk:sdk:${Version.TencentTBSX5}"
        const val WechatSDK = "com.tencent.mm.opensdk:wechat-sdk-android:${Version.WechatSDK}"
        const val AlipaySDK = "com.alipay.sdk:alipaysdk-android:${Version.AlipaySDK}"
        const val XlogSDK = "com.tencent.mars:mars-xlog:1.2.5"
        const val CrashSDK = "cat.ereza:customactivityoncrash:2.4.0"
        const val Qiyu = "com.qiyukf.unicorn:unicorn:8.7.0"
        const val Vasdolly = "com.tencent.vasdolly:helper:3.0.4"
    }

    /**
     * 网易云信
     */
    object NIM {
        // 基础功能 (必需)
        const val Base = "com.netease.nimlib:basesdk:${Version.NIMBase}"

        // 聊天室
        const val Chatroom = "com.netease.nimlib:chatroom:${Version.NIMChatroom}"

        // 通过云信来集成小米等厂商推送
        const val Push = "com.netease.nimlib:push:${Version.NIMPush}"

        // 超大群
        const val SuperTeam = "com.netease.nimlib:superteam:${Version.NIMSuperTeam}"

        // 全文检索插件
        const val Lucene = "com.netease.nimlib:lucene:${Version.NIMLucene}"
    }

    /**
     * 友盟
     */
    object UMSDK {
        // 基础功能 (必需必选)
        const val common = "com.umeng.umsdk:common:${Version.UMCommon}"

        // 必需必选
        const val asms = "com.umeng.umsdk:asms:${Version.UMAsms}"

        // 分享核心库，必选
        const val share_core = "com.umeng.umsdk:share-core:${Version.UMShare}"

        // 分享面板功能，可选
        const val share_board = "com.umeng.umsdk:share-board:${Version.UMShare}"

        // 微信完整版
        const val share_wx = "com.umeng.umsdk:share-wx:${Version.UMShare}"

        // 微信官方依赖库，必选
        const val tencent_share_wx = "com.tencent.mm.opensdk:wechat-sdk-android-without-mta:6.8.0"

        // QQ完整版
        const val share_qq = "com.umeng.umsdk:share-qq:${Version.UMShare}"

        //        implementation files('libs/open_sdk_3.5.7.4_r1bc9afe_lite.jar') //QQ官方依赖库，必选
        // 新浪微博完整版
        const val share_sina = "com.umeng.umsdk:share-sina:${Version.UMShare}"

        // 新浪微博官方SDK依赖库，必选
        const val share_sina_must = "io.github.sinaweibosdk:core:11.11.1@aar"

        // 钉钉完整版
        const val share_dingding = "com.umeng.umsdk:share-dingding:${Version.UMShare}"

        // 钉钉官方依赖库，必选
        const val share_dingding_must = "com.alibaba.android:ddsharesdk:1.2.0@jar"

        // 支付宝完整版
        const val share_alipay = "com.umeng.umsdk:share-alipay:${Version.UMShare}"

    }

    object Agora {
        // 视频 SDK
        const val agora_builder = "io.agora.rtc:full-rtc-basic:${Version.Agora}"
        const val agora_ains = "io.agora.rtc:ains:${Version.Agora}"
    }

}