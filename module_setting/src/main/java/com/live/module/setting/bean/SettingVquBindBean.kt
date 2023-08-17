package com.live.module.setting.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/14
 * description:
 */
data class SettingVquBindBean(
    @SerializedName("mobile")
    var mobile: String,
    @SerializedName("qq")
    var qq: String,
    @SerializedName("wechat")
    var wechat: String,
    @SerializedName("weibo")
    var weibo: String
)