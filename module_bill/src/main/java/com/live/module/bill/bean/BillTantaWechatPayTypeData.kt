package com.live.module.bill.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
data class BillTantaWechatPayTypeData(
    @SerializedName("pay_type")
    val payType: Int,
    @SerializedName("pay_channel")
    val payChannel: String,
    @SerializedName("polling")
    val polling: Int = -1
)

