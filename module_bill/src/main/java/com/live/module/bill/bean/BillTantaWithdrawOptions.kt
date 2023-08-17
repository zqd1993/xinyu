package com.live.module.bill.bean

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
data class BillTantaWithdrawOptions(
    val money: String,
    val myMoney: String,
    var selected: Boolean = false
)