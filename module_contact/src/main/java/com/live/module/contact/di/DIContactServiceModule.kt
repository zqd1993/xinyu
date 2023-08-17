package com.live.module.contact.di

import com.live.module.contact.net.ContactVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DIContactServiceModule {
    @Singleton
    @Provides
    fun provideContactVquApiService(retrofit: Retrofit): ContactVquApiService {
        return retrofit.create(ContactVquApiService::class.java)
    }
}