package com.live.module.home.di

import com.live.module.home.net.HomeVquApiService
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
class DIHomeNetServiceModule {

    /**
     * Home模块的[HomeVquApiService]依赖提供方法
     *
     * @param retrofit Retrofit
     * @return HomeApiService
     */
    @Singleton
    @Provides
    fun provideHomeApiService(retrofit: Retrofit): HomeVquApiService {
        return retrofit.create(HomeVquApiService::class.java)
    }
}