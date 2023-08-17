package com.live.module.bill.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
data class BillVquEarningData(
    @SerializedName("alipay_account")
    val alipayAccount: String?,
    @SerializedName("alipay_name")
    val alipayName: String,
    @SerializedName("alipay_status")
    val alipayStatus: Int,
    @SerializedName("des")
    val des: String,
    @SerializedName("income_money")
    val incomeMoney: String,
    @SerializedName("list")
    val list: MutableList<String>,
    @SerializedName("min_money")
    val minMoney: Int,
    @SerializedName("money")
    val money: String,
    @SerializedName("alipay_is_master")
    val alipayIsMaster: Int,
    @SerializedName("alipay_id")
    val alipayId: String?,
    @SerializedName("isMonthLimit")
    val isMonthLimit: Int,
    @SerializedName("bank")
    val bank: String,
    @SerializedName("bank_code")
    val bankCode: String,
    @SerializedName("card_type")
    val cardType: Int,

    @SerializedName("icon")
    val icon: String,

    var options: MutableList<BillTantaWithdrawOptions>
)