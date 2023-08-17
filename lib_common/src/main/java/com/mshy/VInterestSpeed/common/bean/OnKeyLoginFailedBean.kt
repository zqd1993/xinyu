package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/29
 * description:
 */
data class OnKeyLoginFailedBean(
    @SerializedName("carrierFailedResultData")
    val carrierFailedResultData: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("requestCode")
    val requestCode: Int,
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("vendorName")
    val vendorName: String
)