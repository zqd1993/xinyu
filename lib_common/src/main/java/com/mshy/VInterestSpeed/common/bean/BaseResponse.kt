package com.mshy.VInterestSpeed.common.bean

/**
 * 公共Response
 *
 * @author Qu Yunshuo
 * @since 2021/8/5 11:33 下午
 */
data class BaseResponse<D>(
    val `data`: D,
    val code: Int,
    val message: String
)