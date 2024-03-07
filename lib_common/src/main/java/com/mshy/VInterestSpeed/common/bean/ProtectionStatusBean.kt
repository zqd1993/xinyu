package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

data class ProtectionStatusBean(
    @SerializedName("man_url")
    var manUrl: String,
    @SerializedName("woman_url")
    var womanUrl: String,
    @SerializedName("accumulate_day")
    var accumulateDay: String,
    @SerializedName("guardian_options_snapshot")
    var guardianOptionsSnapshotList: GuardianOptionsSnapshot,
)

data class GuardianOptionsSnapshot(
    @SerializedName("icon")
    var icon: String,
    @SerializedName("expire_day")
    var expireDay: Int,
)
