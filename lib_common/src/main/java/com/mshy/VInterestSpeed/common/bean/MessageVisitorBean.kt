package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/7/4
 * description:
 */
data class MessageVisitorBean(
    @SerializedName("lookMeInfo")
    var lookMeInfo: LookMeInfo
)

data class LookMeInfo(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("descriptive_copy")
    var descriptiveCopy: String?="",
    @SerializedName("new_visitor_count")
    var newVisitorCount: Int
)