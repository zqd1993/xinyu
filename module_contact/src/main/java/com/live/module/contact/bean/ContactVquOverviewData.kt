package com.live.module.contact.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
data class ContactVquOverviewData(
    @SerializedName("after_key")
    val afterKey: Int,
    @SerializedName("list")
    val list: MutableList<ContactVquOverviewBean>,
    @SerializedName("total_income")
    val totalIncome: String,
    @SerializedName("total_page")
    val totalPage: Int,
    @SerializedName("total_people")
    val totalPeople: String,
    @SerializedName("is_audit_channel")
    val isAuditChannel: Int = 0
)

data class ContactVquOverviewBean(
    @SerializedName("age")
    val age: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("contribution")
    val contribution: String,
    @SerializedName("createtime")
    val createtime: String,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("is_anchor")
    val isAnchor: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("usercode")
    val usercode: String,
    @SerializedName("last_live_time")
    val lastLiveTime:String,
    @SerializedName("chat_num")
    val chatNum:String,
    @SerializedName("date")
    val date:String,
    @SerializedName("toUserRechargeCoin")
    val toUserRechargeCoin:String,
    @SerializedName("toUserLetterIncome")
    val toUserLetterIncome:String,
    @SerializedName("toUserVideoIncome")
    val toUserVideoIncome:String,
    @SerializedName("toUserVoiceIncome")
    val toUserVoiceIncome:String,
    @SerializedName("toUserGiftIncome")
    val toUserGiftIncome:String,
    @SerializedName("total_income")
    val totalIncome:String,
    @SerializedName("heartNum")
    val heartNum:String,
    @SerializedName("onlineTime")
    val onlineTime:Long,
) {
    var isSelected = false
}