package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

data class ProtectionOptionBean(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("summary")
    var summary: String,
    @SerializedName("original_amount")
    var originalAmount: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("amount")
    var amount: String,
    @SerializedName("expire_day")
    var expireDay: String,
    @SerializedName("sort")
    var sort: String,
    @SerializedName("status")
    var status: String,
    var isSelected:Boolean
)
