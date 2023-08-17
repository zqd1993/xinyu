package com.live.module.relation.di

import com.live.module.relation.net.RelationVquApiService
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
class DIRelationVquServiceModule {
    @Singleton
    @Provides
    fun provideRelationVquApiService(retrofit: Retrofit): RelationVquApiService {
        return retrofit.create(RelationVquApiService::class.java)
    }
}