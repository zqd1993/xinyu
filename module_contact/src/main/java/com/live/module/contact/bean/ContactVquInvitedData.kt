package com.live.module.contact.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/6/22
 * description:
 */
data class ContactVquInvitedData(
    @SerializedName("id")
    val id:String?,
    @SerializedName("usercode")
    val usercode:String?,
    @SerializedName("nickname")
    val nickname:String,
    @SerializedName("avatar")
    val avatar:String,
    @SerializedName("gender")
    val gender:Int,
    @SerializedName("age")
    val age:Int,
    @SerializedName("city")
    val city:String,
    @SerializedName("add_time")
    val addTime:String
)