package com.live.module.bill.di

import com.live.module.bill.net.BillVquApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
@Module
@InstallIn(SingletonComponent::class)
class DIBillVquServiceModule {

    @Singleton
    @Provides
    fun provideBillVquApiService(retrofit: Retrofit): BillVquApiService {
        return retrofit.create(BillVquApiService::class.java)
    }
}