package com.live.module.message.net

import com.google.gson.Gson
import com.live.vquonline.base.constant.VersionStatus
import com.mshy.VInterestSpeed.common.BuildConfig
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.converter.JsonConverterFactory
import com.mshy.VInterestSpeed.common.interceptor.CommonVquHttpLoggingInterceptor
import com.mshy.VInterestSpeed.common.interceptor.CommonVquPublicInterceptor
import com.mshy.VInterestSpeed.common.interceptor.GlobalHeaderInterceptor
import com.mshy.VInterestSpeed.common.ssl.MyTrustManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * FileName: com.live.module.message.net
 * Date: 2022/4/25 11:53
 * Description:
 * History:
 */
object MsgServiceManage {

    private fun getMsgHttpClient(): OkHttpClient {
        // 日志拦截器部分
        val level =
            if (BuildConfig.VERSION_TYPE != VersionStatus.RELEASE) CommonVquHttpLoggingInterceptor.Level.BODY else CommonVquHttpLoggingInterceptor.Level.NONE
        val logInterceptor = CommonVquHttpLoggingInterceptor().setLevel(level)
        var okHttpClient = OkHttpClient.Builder()
        okHttpClient
            .connectTimeout(15L * 1000L, TimeUnit.MILLISECONDS)
            .readTimeout(20L * 1000L, TimeUnit.MILLISECONDS)
            .addInterceptor(GlobalHeaderInterceptor())
            .addInterceptor(CommonVquPublicInterceptor())
            .addInterceptor(logInterceptor)
            .retryOnConnectionFailure(true)
        if (createSSLSocketFactory() != null) {
            okHttpClient.sslSocketFactory(createSSLSocketFactory()!!, MyTrustManager())
        }
        return okHttpClient.build()
    }

    private fun getMsgRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetBaseUrlConstant.MAIN_URL)
            .addConverterFactory(JsonConverterFactory(Gson()))
            .client(getMsgHttpClient())
            .build()
    }

    fun getMsgService(): MessageVquApiService {
        return getMsgRetrofit().create(MessageVquApiService::class.java)
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {
        var ssfFactory: SSLSocketFactory? = null
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(MyTrustManager()), SecureRandom())
            ssfFactory = sc.socketFactory
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
        return ssfFactory
    }
}