package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/18
 * description:
 */
data class CommonVquStatusBean(
    @SerializedName("is_open")
    var isOpen: Int,
    @SerializedName("is_set_adolescent")
    var isSetAdolescent: Int,
    @SerializedName("is_set_pwd")
    var isSetPwd: Int
)