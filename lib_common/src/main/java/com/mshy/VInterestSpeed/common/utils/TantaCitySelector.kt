package com.mshy.VInterestSpeed.common.utils

import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.constant.RouteUrl

/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
object TantaCitySelector {

    var vquCityMap = mutableMapOf<String, String>()

    const val REQUEST_CODE = 1101

    const val CITY_RESULT = "cityResult"

    fun start(activity: Activity) {
        ARouter.getInstance().build(RouteUrl.Common.CommonVquCitySelectorActivity)
            .navigation(activity, REQUEST_CODE)
    }

    fun getCityId(cityName: String): String? {
        return vquCityMap[cityName]
    }
}