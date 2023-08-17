package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/5/17
 * description:
 */
data class CommonCallPriceBean(
    @SerializedName("msg_price")
    var msgPrice: String,
    @SerializedName("open_video_status")
    var openVideoStatus: Int,
    @SerializedName("is_lock")
    var isLock: Int,//1显示锁 2不显示
    @SerializedName("video_price")
    var videoPrice: String,
    @SerializedName("voice_price")
    var voicePrice: String,
    @SerializedName("video_free")
    var videoFree: String,
    @SerializedName("voice_free")
    var voiceFree: String,
    @SerializedName("video_vip_txt")
    var videoVip: String,
    @SerializedName("voice_vip_txt")
    var voiceVip: String,
    @SerializedName("vip")
    var vip: Int=0,

)