package com.live.module.info.di

import com.live.module.info.net.InfoVquApiService
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
class DIInfoServiceModule {
    @Singleton
    @Provides
    fun provideInfoVquApiService(retrofit: Retrofit): InfoVquApiService {
        return retrofit.create(InfoVquApiService::class.java)
    }
}