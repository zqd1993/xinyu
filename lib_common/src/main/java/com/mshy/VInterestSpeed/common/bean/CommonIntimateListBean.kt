package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/6/28
 * description:
 */
data class CommonIntimateListBean(
    @SerializedName("current")
    var current: Current,
    @SerializedName("list")
    var list: MutableList<CommonIntimateBean>
)

data class Current(
    @SerializedName("des")
    var des: String,
    @SerializedName("icon_url")
    var iconUrl: String,
    @SerializedName("man_url")
    var manUrl: String,
    @SerializedName("score")
    var score: String,
    @SerializedName("sign")
    var sign: String,
    @SerializedName("woman_url")
    var womanUrl: String
)

data class CommonIntimateBean(
    @SerializedName("current_grade")
    var currentGrade: Int,
    @SerializedName("des")
    var des: String,
    @SerializedName("icon_url")
    var iconUrl: String,
    @SerializedName("next_grade")
    var nextGrade: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("unlock")
    var unlock: Int
)