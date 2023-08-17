package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
data class CommonTantaRechargeOptions(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("btn_text")
    val btnText: MutableList<String>?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("ios_product_id")
    val iosProductId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("remark")
    val remark: String,
    @SerializedName("reward")
    val reward: Int,
    @SerializedName("vip_describe")
    val vipDescribe: String,
    var isSelected:Boolean
)