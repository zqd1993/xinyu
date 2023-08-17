package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/10/25
 * description:
 */
class WarningBean {
    var msg: String? = null
    var isClickRechargeBtn = false

    @SerializedName("is_pop")
    var isPop: Int = 0
}