package com.live.module.info.bean
import com.google.gson.annotations.SerializedName


/**
 * author: Tany
 * date: 2022/8/24
 * description:
 */
data class InfoVquInfoBean(
    @SerializedName("age")
    var age: Int,
    @SerializedName("albums")
    var albums: ArrayList<Album>,
    @SerializedName("albums_list")
    var albumsList: ArrayList<Album>,
    @SerializedName("anchor")
    var anchor: Anchor,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("avatar_auth")
    var avatarAuth: Any,
    @SerializedName("avatar_auth_status")
    var avatarAuthStatus: Any,
    @SerializedName("avatar_state")
    var avatarState: Int,
    @SerializedName("basic_info")
    var basicInfo: MutableList<BasicInfo>,
    @SerializedName("basic_info_detail")
    var basicInfoDetail: BasicInfoDetail,
    @SerializedName("dynamic")
    var `dynamic`: MutableList<DynamicBean>,
    @SerializedName("dynamic_num")
    var dynamicNum: Int,
    @SerializedName("forbid_close")
    var forbidClose: Int,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("is_anchor")
    var isAnchor: Int,
    @SerializedName("is_auth")
    var isAuth: Int,
    @SerializedName("is_beckon")
    var isBeckon: Boolean,
    @SerializedName("is_beckon_text")
    var isBeckonText: String,
    @SerializedName("is_black")
    var isBlack: Int,
    @SerializedName("is_follow")
    var isFollow: Int,
    @SerializedName("is_rp_auth")
    var isRpAuth: Int,
    @SerializedName("label")
    var label: ArrayList<Tag>,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("online")
    var online: Online,
    @SerializedName("sign")
    var sign: String,
    @SerializedName("user_remark")
    var userRemark: String,
    @SerializedName("usercode")
    var usercode: String,
    @SerializedName("userid")
    var userid: Int,
    @SerializedName("video")
    var video: String,
    @SerializedName("video_auth")
    var videoAuth: String,
    @SerializedName("video_auth_status")
    var videoAuthStatus: String,
    @SerializedName("vip")
    var vip: Int,
    @SerializedName("vip_icon")
    var vipIcon: String,
    @SerializedName("voice")
    var voice: Voice,
    @SerializedName("gifts")
    var gifts: MutableList<GiftBean>,
)








