package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/18
 * description:
 */
data class VquListBean<T>(
    @SerializedName("list")
    var list: MutableList<T>,
    @SerializedName("page")
    var page: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("total_page")
    var totalPage: Int
)