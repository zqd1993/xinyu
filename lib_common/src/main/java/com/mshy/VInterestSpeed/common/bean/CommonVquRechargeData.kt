package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
data class CommonVquRechargeData(
    @SerializedName("coin")
    val coin: Int,
    @SerializedName("income_coin")
    val incomeCoin: String,
    @SerializedName("income_money")
    val incomeMoney: String,
    @SerializedName("list")
    val list: MutableList<CommonTantaRechargeOptions>,
    @SerializedName("money")
    val money: String,
    @SerializedName("sel_id")
    val selId: Int
)

