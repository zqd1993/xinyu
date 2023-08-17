package com.mshy.VInterestSpeed.common.di


import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.PhoneLoginApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * 全局作用域的Home组件网络接口代理依赖注入模块
 *
 * ""
 * @since 6/4/21 5:51 PM
 */
@Module
@InstallIn(SingletonComponent::class)
class DIGlobalNetServiceModule {

    /**
     * 全局[GlobalApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return GlobalApiService
     */
    @Singleton
    @Provides
    fun provideGlobalApiService(retrofit: Retrofit): GlobalApiService {
        return retrofit.create(GlobalApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePhoneLoginApiService(retrofit: Retrofit): PhoneLoginApiService {
        return retrofit.create(PhoneLoginApiService::class.java)
    }


}