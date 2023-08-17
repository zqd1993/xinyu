package com.live.module.setting.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/2
 * description:
 */
data class BlackListBean(
    @SerializedName("age")
    var age: Int,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("create_time")
    var createTime: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("sign")
    var sign: String,
    @SerializedName("usercode")
    var usercode: String,
    @SerializedName("userid")
    var userid: Int,
    @SerializedName("vip")
    var vip: Int
)