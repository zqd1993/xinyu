package com.live.module.anchor.di

import com.live.module.anchor.net.AnchorVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DIAnchorVquServiceModule {
    @Singleton
    @Provides
    fun provideAnchorVquApiService(retrofit: Retrofit): AnchorVquApiService {
        return retrofit.create(AnchorVquApiService::class.java)
    }
}