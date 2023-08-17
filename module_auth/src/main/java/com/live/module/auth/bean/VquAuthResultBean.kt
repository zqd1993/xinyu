package com.live.module.auth.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/5/19
 * description:
 */
data class VquAuthResultBean(
    @SerializedName("auth")
    val auth: Int
)