package com.live.module.info.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/4/27
 * description:
 */
data class InfoVquGiftBean(
    @SerializedName("all_list")
    var allList: MutableList<GiftBean>,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("gift_total")
    var giftTotal: Int,
    @SerializedName("list")
    var list: List<GiftBean>,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("user_gift_total")
    var userGiftTotal: Int,
    @SerializedName("userid")
    var userid: Int
)
