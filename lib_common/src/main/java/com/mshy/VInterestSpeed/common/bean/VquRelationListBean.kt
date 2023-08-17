package com.mshy.VInterestSpeed.common.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/13
 * description:
 */
data class VquRelationListBean(
    @SerializedName("list")
    val list: MutableList<VquRelationBean>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
)

data class VquRelationBean(
    @SerializedName("add_time")
    val addTime: String,
    @SerializedName("visitor_text")
    val visitorText: String?,
    @SerializedName("age")
    val age: String?,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("avatar_frame")
    val avatarFrame: String,
    @SerializedName("city")
    val city: String?,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("is_fans")
    val isFans: Int,
    @SerializedName("is_follow")
    var isFollow: Int,
    @SerializedName("is_live")
    val isLive: Int,
    @SerializedName("is_watch")
    val isWatch: Int,
    @SerializedName("live_status")
    val liveStatus: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("sign")
    val sign: String?,
    @SerializedName("usercode")
    val usercode: String,
    @SerializedName("userid", alternate = ["user_id"])
    var userid: String?,
    @SerializedName("vip")
    val vip: Int,
    @SerializedName("vip_icon")
    val vipIcon: String,
    @SerializedName("height")
    val height: String?,
    @SerializedName("occupation")
    val occupation: String?,
    @SerializedName("weight")
    val weight: String?,
    @SerializedName("is_beckon")
    var isBeckon: Boolean?,
    @SerializedName("is_beckon_text")
    var isBeckonText: String?,
    override var itemType: Int
) : MultiItemEntity {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VquRelationBean

        if (userid != other.userid) return false

        return true
    }

    override fun hashCode(): Int {
        return userid.hashCode()
    }
}