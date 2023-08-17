package com.live.module.home.activity

import com.live.module.home.net.HomeVquApiService
import com.live.vquonline.base.mvvm.m.BaseRepository

import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * 首页M层
 *
 * ""
 * @since 5/25/21 5:42 PM
 */
class HomeRepository @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mApi: HomeVquApiService

    /**
     * 模拟获取数据
     */
    suspend fun getData() = request<String> {
        delay(1000L)
        emit("Hello Hilt")
    }
}