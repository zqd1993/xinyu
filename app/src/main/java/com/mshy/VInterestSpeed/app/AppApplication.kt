package com.mshy.VInterestSpeed.app

import com.live.vquonline.base.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import com.huawei.hms.support.common.ActivityMgr

/**
 * App壳
 *
 * ""
 * @since 4/23/21 6:08 PM
 */
@HiltAndroidApp
class AppApplication : BaseApplication() {

    override fun onCreate() {

        super.onCreate()
        //华为推送初始化
        ActivityMgr.INST.init(this)//华为
    }
}