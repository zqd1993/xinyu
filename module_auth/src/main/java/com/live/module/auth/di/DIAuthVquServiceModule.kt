package com.live.module.auth.di

import com.live.module.auth.net.AuthVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DIAuthVquServiceModule {
    @Singleton
    @Provides
    fun provideAuthVquApiService(retrofit: Retrofit): AuthVquApiService {
        return retrofit.create(AuthVquApiService::class.java)
    }
}