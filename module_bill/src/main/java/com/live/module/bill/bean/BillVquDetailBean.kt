package com.live.module.bill.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/20
 * description:
 */
data class BillVquDetailBean(
    @SerializedName("list")
    val list: List<BillVquDetailListBean>,
    @SerializedName("new_list")
    val newList: List<BillVquDetailNewListBean>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
)

data class BillVquDetailListBean(
    @SerializedName("account_type")
    val accountType: Int,
    @SerializedName("change_value")
    val changeValue: String,
    @SerializedName("change_value_new")
    val changeValueNew: String,
    @SerializedName("create_time")
    val createTime: String,
    @SerializedName("from_username")
    val fromUsername: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("platform_name")
    val platformName: String,
    @SerializedName("platform_type")
    val platformType: Int,
    @SerializedName("recharge_money")
    val rechargeMoney: String,
    @SerializedName("system_str")
    val systemStr: String,
    @SerializedName("type")
    val type: Int
)

data class BillVquDetailNewListBean(
    @SerializedName("date")
    val date: String,
    @SerializedName("expend")
    val expend: Int,
    @SerializedName("income")
    val income: String,
    @SerializedName("list")
    val list: List<BillVquItemData>
)

data class BillVquItemData(
    @SerializedName("account_type")
    val accountType: Int,
    @SerializedName("change_value")
    val changeValue: String,
    @SerializedName("change_value_new")
    val changeValueNew: String,
    @SerializedName("create_time")
    val createTime: String,
    @SerializedName("from_username")
    val fromUsername: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("platform_name")
    val platformName: String,
    @SerializedName("platform_type")
    val platformType: Int,
    @SerializedName("recharge_money")
    val rechargeMoney: String,
    @SerializedName("system_str")
    val systemStr: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("track_user_id")
    var tracUserId: String? = null
)