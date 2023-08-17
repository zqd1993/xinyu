package com.live.module.login.di

import com.live.module.login.net.LoginTantaApiService
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
class DILoginServiceModule {
    @Singleton
    @Provides
    fun provideLoginTantaApiService(retrofit: Retrofit): LoginTantaApiService {
        return retrofit.create(LoginTantaApiService::class.java)
    }
}