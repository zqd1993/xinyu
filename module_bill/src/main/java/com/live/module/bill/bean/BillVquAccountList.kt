package com.live.module.bill.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/5/20
 * description:
 */
data class BillVquAccountList(
    @SerializedName("list")
    val list: MutableList<BillVquAccountInfo>?
)
