package com.live.module.vip.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/15
 * description:
 */
data class TantaVipInfoBean(
    @SerializedName("list")
    val list: MutableList<VipRechargeBean>,
    @SerializedName("info")
    val info: VipInfo,
    @SerializedName("privilege")
    val privilege: MutableList<VipPrivilegeBean>
)

data class VipInfo(
    @SerializedName("expire_time")
    val expire_time: String,
    @SerializedName("usercode")
    val usercode: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("vip_id")
    val vip_id: Int,
    @SerializedName("gif_visible")
    val gif_visible: Int,
    @SerializedName("gift")
    val gift: GifInfo? = null
)

data class VipPrivilegeBean(
    @SerializedName("id")
    val id: Int,
    @SerializedName("img_has")
    val img_has: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("des")
    val des: String,
    @SerializedName("sort")
    val sort: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("show_img")
    val show_img: String,
    @SerializedName("width")
    val width: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("is_show")
    val is_show: Int
)

data class VipRechargeBean(
    @SerializedName("id")
    val id: Int,
    @SerializedName("vip_id")
    val vip_id: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("old_price")
    val oldPrice: Double,
    @SerializedName("renew_price")
    val renewPrice: Double,
    @SerializedName("expire")
    val expire: Int,
    @SerializedName("start_ios_product_id")
    val start_ios_product_id: String,
    @SerializedName("platform")
    val platform: Int,
    @SerializedName("province")
    val province: String,
    @SerializedName("vip_suggest")
    val vip_suggest: Int,
    @SerializedName("vip_duration")
    val vip_duration: String

)

data class GifInfo(
    @SerializedName("num")
    val num: Int,
    @SerializedName("img")
    val img: String,
    @SerializedName("name")
    val name: String,
    //0,隐藏每天礼物panel;1,显示每天礼物panel
    @SerializedName("is_show")
    val is_show: Int,
    @SerializedName("gender")
    val gender: Int,
    //0,没有领取;1,已经领取
    @SerializedName("is_reward")
    val is_reward: Int
)