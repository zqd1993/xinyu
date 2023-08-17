package com.live.module.message.di

import com.live.module.message.net.MessageVquApiService
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
class DIMsgServiceModule {

    @Singleton
    @Provides
    fun provideDynamicVquApiService(retrofit: Retrofit): MessageVquApiService {
        return retrofit.create(MessageVquApiService::class.java)
    }

}