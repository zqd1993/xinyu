package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/6/8
 * description:
 */
data class FirstRechargeGiftBean(
    val name: String,
    val money: String,
    val level: Int,
    val img: String,
    val pid: String,
    val type: Int,
    val number: String,
    @SerializedName(value = "gift_id")
    val giftId:String
)