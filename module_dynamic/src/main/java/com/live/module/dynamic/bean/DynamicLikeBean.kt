package com.live.module.dynamic.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/6/9
 * description:
 */
data class DynamicLikesBean(
    var list: MutableList<DynamicLikeBean>,
)

data class DynamicLikeBean(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("comment_id")
    var commentId: Int,
    @SerializedName("content")
    var content: String,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("dynamic_id")
    var dynamicId: Int,
    @SerializedName("dynamic_type")
    var dynamicType: Int,
    @SerializedName("from_user_avatar")
    var fromUserAvatar: String,
    @SerializedName("from_user_id")
    var fromUserId: Int,
    @SerializedName("from_user_nickname")
    var fromUserNickname: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("image_url")
    var imageUrl: String,
    @SerializedName("is_delete")
    var isDelete: Int,
    @SerializedName("is_type")
    var isType: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("video_url")
    var videoUrl: String,
)