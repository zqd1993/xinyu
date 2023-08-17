package com.live.module.mine.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
data class MineVquCouponBean(
    @SerializedName("bg_img")
    val bgImg: String,
    @SerializedName("dis")
    val dis: String,
    @SerializedName("discount")
    val discount: String,
    @SerializedName("expire")
    val expire: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("name")
    val name: String
)