package com.mshy.VInterestSpeed.common.bean.pay

import com.google.gson.annotations.SerializedName

data class BillPaymentData(
    val name: String,
    @SerializedName("is_manual")
    val isManual: Int,
    @SerializedName("icon_url")
    val iconUrl: String,
    val payType: String,
    val rechargeRoute: MutableList<RechargeRoute>
)

data class RechargeRoute(
    val id: Int,
    val tips: String,
    @SerializedName("pay_code")
    val payCode: String,
    val amounts: String,
    @SerializedName("limit_low")
    val limitLow: Int,
    @SerializedName("limit_high")
    val limitHigh: Int,
    val alias: String,
    @SerializedName("post_url")
    val postUrl: String,
    @SerializedName("order_url")
    val orderUrl: String,
    @SerializedName("notifyUrl")
    val notifyUrl: String,
    var checked: Boolean = false
)

