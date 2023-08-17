package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/21
 * description:
 */
data class DynamicVquReportBean(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("type")
    var type: Int
)