package com.live.module.bill.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/6/21
 * description:
 */
data class BillVquWithdrawalListBean(
    @SerializedName("list")
    val list: MutableList<WithdrawalDetailItem>?,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_page")
    val totalPage: Int
)

data class WithdrawalDetailItem(
    @SerializedName("admin_status")
    val adminStatus: Int,
    @SerializedName("bank")
    val bank: String,
    @SerializedName("card_account")
    val cardAccount: String,
    @SerializedName("cash_money")
    val cashMoney: String,
    @SerializedName("create_time")
    val createTime: Int,
    @SerializedName("create_time_text")
    val createTimeText: String,
    @SerializedName("execute_status")
    val executeStatus: Int,
    @SerializedName("finance_status")
    val financeStatus: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("order_no")
    val orderNo: String,
    @SerializedName("real_cash_money")
    val realCashMoney: String,
    @SerializedName("status_color")
    val statusColor: String,
    @SerializedName("status_text")
    val statusText: String,
    @SerializedName("user_id")
    val userId: String
)