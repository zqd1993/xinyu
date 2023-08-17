package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
data class CommonVquAuthBean(
    @SerializedName("is_auth")
    val isAuth: Int,
    @SerializedName("mobile_status")
    val mobileStatus: Int,
    @SerializedName("is_rp_auth")
    val isRpAuth: Int
)