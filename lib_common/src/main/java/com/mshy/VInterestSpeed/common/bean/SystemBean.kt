package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:48
 *
 */
data class SystemBean(
    val appurl: String,
    @SerializedName("audit_status")
    val auditStatus: Int,
    @SerializedName("image_server")
    val imageServer: String,
    val oneKeyLoginKey: String,
    val timestamp: Long
)