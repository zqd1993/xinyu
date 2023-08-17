package com.live.module.dynamic.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/7/20
 * description:
 */
data class DynamicTantaCommentBean(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("comment_nickname")
    var commentNickname: String,
    @SerializedName("comment_user_id")
    var commentUserId: Int,
    @SerializedName("content")
    var content: String,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("is_type")
    var isType: Int,
    @SerializedName("is_vip")
    var isVip: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("user_id")
    var userId: Int
)