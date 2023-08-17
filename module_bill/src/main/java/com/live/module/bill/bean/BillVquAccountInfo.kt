package com.live.module.bill.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/5/20
 * description:
 */
data class BillVquAccountInfo(
    @SerializedName("card_account")
    val cardAccount: String?,
    @SerializedName("card_name")
    val cardName: String,
    @SerializedName("card_type", alternate = ["type"])
    val cardType: Int,
    @SerializedName("id_card")
    val idCard: String?,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("isMonthLimit")
    val isMonthLimit: Int,
    @SerializedName("is_use")
    val isUse: Int,
    @SerializedName("is_master")
    val isMaster: Int,
    @SerializedName("bank")
    val bank: String,
    @SerializedName("bank_code")
    val bankCode: String,
    @SerializedName("create_time")
    val createTime: Int,
    @SerializedName("create_time_text")
    val createTimeText: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("status_text")
    val statusText: String,
    @SerializedName("type_text")
    val typeText: String,
    @SerializedName("update_time")
    val updateTime: Int,
    @SerializedName("update_time_text")
    val updateTimeText: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("cardTypes")
    val cardTypes:MutableList<TypeBean>
)
data class TypeBean(
    @SerializedName("bank")
    val bank: String,
    @SerializedName("bank_code")
    val bankCode: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("type")
    val type: Int
)
