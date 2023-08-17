package com.live.module.home.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/9/19
 * description:
 */
data class HomeNewDataBean(
    @SerializedName("activity")
    var activity: HomeVquChannelAnchorBean.ActivityBean,
    @SerializedName("list")
    var list: ListBean
)


data class ListBean(
    @SerializedName("list")
    var list: ArrayList<HomeDataItemBean>,
    @SerializedName("page")
    var page: Int,
    @SerializedName("total")
    var total: Int,
    @SerializedName("total_page")
    var totalPage: Int
)

data class HomeDataItemBean(
    @SerializedName("age")
    var age: String,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("height")
    var height: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("is_auth")
    var isAuth: Int,
    @SerializedName("is_beckon")
    var isBeckon: Boolean,
    @SerializedName("is_online")
    var isOnline: Int,
    @SerializedName("is_rp_auth")
    var isRpAuth: Int,
    @SerializedName("is_vip")
    var isVip: Int,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("occupation")
    var occupation: String,
    @SerializedName("sign")
    var sign: String,
    @SerializedName("usercode")
    var usercode: String,
    @SerializedName("weight")
    var weight: String
)