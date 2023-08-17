package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/6/13
 * description:
 */
data class NotificationBean(//运营推送
    @SerializedName("content")
    var content: String,
    @SerializedName("link_type")
    var linkType: Int,
    @SerializedName("link_url")
    var linkUrl: String,
    @SerializedName("title")
    var title: String
)