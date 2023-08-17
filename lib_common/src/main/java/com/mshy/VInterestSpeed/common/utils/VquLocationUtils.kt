package com.mshy.VInterestSpeed.common.utils

/**
 * author: Lau
 * date: 2022/4/9
 * description:
 */
object VquLocationUtils {


//    fun location(
//        isOnce: Boolean = true,
//        listener: VquLocationListener
//    ) {
//        AMapLocationClient.updatePrivacyAgree(BaseApplication.application, true)
//        AMapLocationClient.updatePrivacyShow(BaseApplication.application, true, true)
//        val locationClient = AMapLocationClient(BaseApplication.application)
//        val locationOption = AMapLocationClientOption()
//        locationOption.isOnceLocation = isOnce
//        locationClient.setLocationListener {
//            Log.d("VquLocationUtils", "location: " + it.errorCode)
//            Log.d("VquLocationUtils", "location: " + it.errorInfo)
//            if (it.errorCode == 0) {
//                val vquLocationBean = VquLocationBean()
//                vquLocationBean.city = it.city.replace("市", "")
//                vquLocationBean.cityCode = it.cityCode
//                vquLocationBean.province = it.province
//                vquLocationBean.latitude = it.latitude
//                vquLocationBean.longitude = it.longitude
//                listener.vquSuccess(locationClient, vquLocationBean)
//            } else {
//                listener.vquError(locationClient, it.errorCode, it.errorInfo)
//            }
//        }
//        locationClient.startLocation()
//    }
}