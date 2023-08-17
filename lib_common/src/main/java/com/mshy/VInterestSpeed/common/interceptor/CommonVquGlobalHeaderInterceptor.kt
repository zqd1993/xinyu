package com.mshy.VInterestSpeed.common.interceptor


import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.HeaderUtil
import com.live.vquonline.base.utils.getPackageManagerName
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *
 * @Description: 全局请求头
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 22:10
 *
 */
class GlobalHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val requestBuilder: Request.Builder = original.newBuilder()
            .header("source-id", "1")
            .header("ver-code", DeviceManager.getInstance().phoneVersion.toString())
            .header("token", DeviceManager.getInstance().token ?: "")
            .header("phone-brand", DeviceManager.getInstance().systemModel)
            .header("version", DeviceManager.getInstance().appVersion)
            .header("channel", DeviceManager.getInstance().channel)
            .header("package-name", getPackageManagerName())
            .header("uuid", DeviceManager.getInstance().deviceImei ?: "")
            .header("network-status", DeviceManager.getInstance().apnType)
            .header("theme","vqu-white")
            .header("oaid",DeviceManager.getInstance().oaid?:"")
        HeaderUtil.setHeader(requestBuilder)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}