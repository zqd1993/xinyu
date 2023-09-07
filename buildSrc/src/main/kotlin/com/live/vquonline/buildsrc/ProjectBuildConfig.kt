package com.live.vquonline.buildsrc

/**
 * 项目相关参数配置
 *
 * ""
 * @since 4/24/21 5:56 PM
 */
object ProjectBuildConfig {
    const val compileSdkVersion = 31
    const val buildToolsVersion = "30.0.2"
    const val applicationId = "xinyu.love"
    const val minSdkVersion = 21
    const val targetSdkVersion = 30
    const val versionCode = 4
    const val versionName = "1.0.3"
    const val isAppMode = false

    /**
     * 项目当前的版本状态
     * 该状态直接反映当前App是测试版 还是正式版 或者预览版
     * 正式版:RELEASE、预览版(α)-内部测试版:ALPHA、测试版(β)-公开测试版:BETA
     */
    object Version {

        const val RELEASE = "VERSION_STATUS_RELEASE"

        const val ALPHA = "VERSION_STATUS_ALPHA"

        const val BETA = "VERSION_STATUS_BETA"
    }
}