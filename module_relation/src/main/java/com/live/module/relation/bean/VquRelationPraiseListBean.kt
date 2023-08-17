package com.live.module.relation.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
data class VquRelationPraiseListBean(
    @SerializedName("list")
    val list: MutableList<VquRelationPraiseBean>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
)

data class VquRelationPraiseBean(
    @SerializedName("age")
    val age: Int,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("avatar_frame")
    val avatarFrame: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("cover_url")
    val coverUrl: String?,
    @SerializedName("create_time")
    val createTime: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("file_url")
    val fileUrl: String?,
    @SerializedName("from_user_id")
    val fromUserId: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("like_count")
    val likeCount: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("view_count")
    val viewCount: Int,
    @SerializedName("vt_id")
    val vtId: Int,
    @SerializedName("type")
    val type: Int
)