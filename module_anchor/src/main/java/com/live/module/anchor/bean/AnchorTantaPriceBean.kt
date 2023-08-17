package com.live.module.anchor.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
data class AnchorTantaPriceBean(
    @SerializedName("msg_price")
    val msgPrice: String,
    @SerializedName("open_video_status")
    val openVideoStatus: Int,
    @SerializedName("video_price")
    val videoPrice: String,
    @SerializedName("voice_price")
    val voicePrice: String
)