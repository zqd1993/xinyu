package com.live.module.bill.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/19
 * description:
 */
data class BillVquWithdrawPriceBean(
    @SerializedName("account")
    val account: String,
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("receive_money")
    val receiveMoney: String,
    @SerializedName("withdraw_money")
    val withdrawMoney: String
)