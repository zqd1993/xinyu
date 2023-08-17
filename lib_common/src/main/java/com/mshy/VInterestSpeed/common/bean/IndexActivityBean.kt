package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

/**
 * author: Lau
 * date: 2022/6/8
 * description:
 */
data class IndexActivityBean(
    @SerializedName(value = "is_activity")
    var isActivity: Boolean?,

    var list:MutableList<FirstRechargeGiftBean>?
)