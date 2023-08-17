package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
data class TodayBean(
    @SerializedName("today")
    val today: MutableList<Today>,
    @SerializedName("today_count")
    val todayCount: Int,
    @SerializedName("today_status")
    val todayStatus: Boolean
)

data class Today(
    @SerializedName("list")
    val list: List<GiftData>,
    @SerializedName("now_day")
    val nowDay: Boolean,
    @SerializedName("status")
    val status: Boolean
)

data class GiftData(
    @SerializedName("gift_id")
    val giftId: Int,
    @SerializedName("img")
    val img: String,
    @SerializedName("money")
    val money: String,
    @SerializedName("time_second")
    val timeSecond: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: Int
)