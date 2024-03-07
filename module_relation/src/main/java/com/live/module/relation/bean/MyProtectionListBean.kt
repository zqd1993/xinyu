package com.live.module.relation.bean

import com.google.gson.annotations.SerializedName

data class MyProtectionListBean(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("guardian_user_id")
    val guardianUserId: String,
    @SerializedName("accumulate_day")
    val accumulateDay: Int,
    @SerializedName("expire_time")
    val expireTime: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("age")
    val age: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("height")
    val height: String,
    @SerializedName("occupation")
    val occupation: String,
    @SerializedName("guardian_options_snapshot")
    var guardianOptionsSnapshot: GuardianOptionsSnapshot,
)

data class GuardianOptionsSnapshot(
    @SerializedName("icon")
    var icon: String,
)
