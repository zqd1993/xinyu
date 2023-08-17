package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
data class TantaWalletBean(
    @SerializedName("account")
    val account: Account,
    @SerializedName("cash")
    val cash: Int,
    @SerializedName("webUrl")
    val webUrl: WebUrl
)

data class Account(
    @SerializedName("coin")
    val coin: String,
    @SerializedName("free_coin")
    val freeCoin: Int,
    @SerializedName("income_coin")
    val incomeCoin: Int,
    @SerializedName("income_coin_money")
    val incomeCoinMoney: String,
    @SerializedName("income_money")
    val incomeMoney: String,
    @SerializedName("love_value")
    val loveValue: Int,
    @SerializedName("money")
    val money: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("total_buy_coin")
    val totalBuyCoin: Int,
    @SerializedName("today_income")
    val todayIncome: String,
    @SerializedName("hebdo_income")
    val hebdoIncome: String

)

//data class WebUrl(
//    @SerializedName("anchorDividedinfo")
//    val anchorDividedinfo: String,
//    @SerializedName("anchorStarlevel")
//    val anchorStarlevel: String,
//    @SerializedName("chatintimate")
//    val chatintimate: String,
//    @SerializedName("help")
//    val help: String,
//    @SerializedName("juvenile_protection")
//    val juvenileProtection: String,
//    @SerializedName("myGrade")
//    val myGrade: String,
//    @SerializedName("publish")
//    val publish: String,
//    @SerializedName("recharge_agreement")
//    val rechargeAgreement: String,
//    @SerializedName("share")
//    val share: String,
//    @SerializedName("taskCenter")
//    val taskCenter: String,
//    @SerializedName("tv")
//    val tv: String,
//    @SerializedName("wx_official")
//    val wxOfficial: String
//)
