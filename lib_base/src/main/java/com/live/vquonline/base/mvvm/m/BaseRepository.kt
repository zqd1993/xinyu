package com.live.vquonline.base.mvvm.m

import com.live.vquonline.base.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.HttpException

/**
 * 仓库层 Repository 基类
 *
 * ""
 * @since 8/27/20
 */
open class BaseRepository {

    /**
     * 发起请求封装
     * 该方法将flow的执行切换至IO线程
     *
     * @param requestBlock 请求的整体逻辑
     * @return Flow<T>
     */
    protected fun <T> request(
        isCatch: Boolean = true,
        requestBlock: suspend FlowCollector<T>.() -> Unit
    ): Flow<T> {
        return flow(block = requestBlock).flowOn(Dispatchers.IO).catch {
            if (isCatch && it is HttpException) {
                toast("网络异常，请稍后再试")
            } else {
                throw it
            }
        }
    }
}