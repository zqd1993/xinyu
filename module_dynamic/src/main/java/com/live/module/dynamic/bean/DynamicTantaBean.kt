package com.live.module.dynamic.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/3/31
 * description:
 */
data class DynamicVquBean(
    @SerializedName("list")
    var list: List<DynamicBean>,
    @SerializedName("page")
    var page: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("total_page")
    var totalPage: Int
)

data class DynamicBean(
    @SerializedName("age")
    var age: Int,
    @SerializedName("is_vip")
    var is_vip: Int = 1,
    @SerializedName("height")
    var height: String,
    @SerializedName("occupation")
    var occupation: String,
    @SerializedName("weight")
    var weight: String,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("content")
    var content: String,
    @SerializedName("cover_url")
    var coverUrl: String,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("file_url")
    var fileUrl: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("give_score")
    var giveScore: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("images")
    var images: MutableList<Image>,
    @SerializedName("info")
    var info: Info,
    @SerializedName("is_anchor")
    var isAnchor: Int,
    @SerializedName("is_follow")
    var isFollow: Int,
    @SerializedName("is_like")
    var isLike: Int,
    @SerializedName("is_live")
    var isLive: Int,
    @SerializedName("like_count")
    var likeCount: Int,
    @SerializedName("comment_count")
    var commentCount: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("reward_count")
    var rewardCount: Int,
    @SerializedName("size")
    var size: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("type")
    var type: Int,
    @SerializedName("user_id")
    var userId: Int,
    @SerializedName("video_id")
    var videoId: String,
    @SerializedName("vip")
    var vip: Int,
    @SerializedName("is_beckon")
    var isBeckon: Boolean
)

data class Image(
    @SerializedName("exts")
    var exts: String,
    @SerializedName("height")
    var height: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("url")
    var url: String,
    @SerializedName("width")
    var width: Int
)

data class Info(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("avatar_frame")
    var avatarFrame: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("grade")
    var grade: Grade,
    @SerializedName("id")
    var id: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("usercode")
    var usercode: String,
    @SerializedName("vip")
    var vip: Int,
    @SerializedName("vip_icon")
    var vipIcon: String
)


data class Grade(
    @SerializedName("grade")
    var grade: Int,
    @SerializedName("imgInfo")
    var imgInfo: com.mshy.VInterestSpeed.common.bean.CommonVquGradeConfigBean
)

data class CommonVquGradeConfigBean(
    @SerializedName("grade")
    var grade: Int,
    @SerializedName("grade_name")
    var gradeName: String,
    @SerializedName("icon1_width")
    var icon1Width: String,
    @SerializedName("icon_width")
    var iconWidth: String,
    @SerializedName("img1")
    var img1: String,
    @SerializedName("img2")
    var img2: String
)



