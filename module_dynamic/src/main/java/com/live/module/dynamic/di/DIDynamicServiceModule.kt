package com.live.module.dynamic.di

import com.live.module.dynamic.net.DynamicVquApiService
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
class DIDynamicServiceModule {
    @Singleton
    @Provides
    fun provideDynamicVquApiService(retrofit: Retrofit): DynamicVquApiService {
        return retrofit.create(DynamicVquApiService::class.java)
    }
}