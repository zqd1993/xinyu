package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

data class NotifyMsgBean(
    var id: Int,
    @SerializedName("receive_id")
    var receiveId: Int,
    var content: String,
    @SerializedName("create_time")
    var createTime: Int,
    var nickname: String,
    var avatar: String,
    @SerializedName("user_id")
    var userId: Int,
)
