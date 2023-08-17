package com.mshy.VInterestSpeed.common.interceptor

import com.live.vquonline.base.BaseApplication
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * author: Lau
 * date: 2022/5/23
 * description:
 */
class CommonVquPublicInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!com.mshy.VInterestSpeed.uikit.common.util.sys.NetworkUtil.isNetAvailable(BaseApplication.context)) {
            throw IOException("网络不可用，请检查网络是否可用")
        }

        try {
            return chain.proceed(chain.request())
        } catch (e: SocketTimeoutException) {
            throw IOException("请求超时")
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("网络不可用")
        }
    }
}