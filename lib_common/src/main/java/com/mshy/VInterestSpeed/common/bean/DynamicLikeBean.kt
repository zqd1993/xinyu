package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/6/11
 * description:
 */
data class DynamicLikeBean(
    @SerializedName("msg")
    var msg: String,
    @SerializedName("uid")
    var uid: Int
)