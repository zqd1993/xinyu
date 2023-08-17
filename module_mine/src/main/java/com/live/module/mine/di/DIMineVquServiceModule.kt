package com.live.module.mine.di

import com.live.module.mine.net.MineVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/12
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DIMineVquServiceModule {
    @Singleton
    @Provides
    fun provideMineVquApiService(retrofit: Retrofit): MineVquApiService {
        return retrofit.create(MineVquApiService::class.java)
    }
}