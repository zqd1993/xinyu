package com.live.module.vip.di

import com.live.module.vip.net.VipVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DIVipVquServiceModule {
    @Singleton
    @Provides
    fun provideRelationVquApiService(retrofit: Retrofit): VipVquApiService {
        return retrofit.create(VipVquApiService::class.java)
    }
}