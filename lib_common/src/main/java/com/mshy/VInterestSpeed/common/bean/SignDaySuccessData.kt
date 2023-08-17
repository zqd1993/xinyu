package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
data class SignDaySuccessData(
    @SerializedName("img")
    val img: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("number")
    val number: String,
    @SerializedName("title")
    val title: String
)