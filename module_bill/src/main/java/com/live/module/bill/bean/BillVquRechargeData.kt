package com.live.module.bill.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
data class BillVquRechargeData(
    @SerializedName("coin")
    val coin: Int,
    @SerializedName("income_coin")
    val incomeCoin: String,
    @SerializedName("income_money")
    val incomeMoney: String,
    @SerializedName("list")
    val list: MutableList<BillVquRechargeOptions>,
    @SerializedName("money")
    val money: String,
    @SerializedName("sel_id")
    val selId:Int
)

