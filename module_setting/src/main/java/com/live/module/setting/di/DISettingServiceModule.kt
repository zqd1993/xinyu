package com.live.module.setting.di

import com.live.module.setting.net.SettingVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/7
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DISettingServiceModule {
    @Singleton
    @Provides
    fun provideSettingVquApiService(retrofit: Retrofit): SettingVquApiService {
        return retrofit.create(SettingVquApiService::class.java)
    }
}