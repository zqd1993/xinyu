package com.live.module.dynamic.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/6/11
 * description:
 */
data class DynamicVquDetailBean(
    @SerializedName("comment_count")
    var commentCount: Int,
    @SerializedName("content")
    var content: String,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("images")
    var images: MutableList<Image>,
    @SerializedName("is_beckon")
    var isBeckon: Int,
    @SerializedName("is_beckon_text")
    var isBeckonText: String,
    @SerializedName("is_vip")
    var isVip: Int,
    @SerializedName("like_count")
    var likeCount: Int,
    @SerializedName("like_record")
    var likeRecord: MutableList<LikeBean>,
    @SerializedName("type")
    var type: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("video")
    var video: VideoBean,
    @SerializedName("is_like")
    var isLike: Int,
    @SerializedName("user")
    var user: User,
)

data class User(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("height")
    var height: String,
    @SerializedName("age")
    var age: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("occupation")
    var occupation: String,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("weight")
    var weight: String
)
data class VideoBean(
    @SerializedName("cover_url")
    var coverUrl: String,
    @SerializedName("file_url")
    var fileUrl: String,
)


data class LikeBean(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("create_time")
    var createTime: Int,
    @SerializedName("user_id")
    var id: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("usercode")
    var usercode: Int,
)