package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/29
 * description:
 */
data class CommonAuthBean(
    @SerializedName("is_auth")
    var isAuth: Int,
    @SerializedName("mobile_status")
    val mobileStatus: Int,
    @SerializedName("is_rp_auth")
    val isRpAuth: Int,
)