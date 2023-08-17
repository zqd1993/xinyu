package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
data class VquFollowBean(
    var action: String,
    @SerializedName("user_id")
    var userId: String
) {
    var position: Int = -1
}