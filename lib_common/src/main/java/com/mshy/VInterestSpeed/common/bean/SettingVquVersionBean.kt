package com.mshy.VInterestSpeed.common.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/5/4
 * description:
 */
data class SettingVquVersionBean(

    @SerializedName("downloadurl")
    var downloadurl: String,
    @SerializedName("enforce")
    var enforce: Int=0,
    @SerializedName("newversion")
    var newversion: String,
    @SerializedName("packagesize")
    var packagesize: String,
    @SerializedName("upgradetext")
    var upgradetext: String,
    @SerializedName("version")
    var version: String,
    @SerializedName("versioncode")
    var versioncode: Int,
    @SerializedName("is_tips")
    var isTips: Int,//主页1弹框 0不弹框
    @SerializedName("is_force")
    var isForce: Boolean=false
)