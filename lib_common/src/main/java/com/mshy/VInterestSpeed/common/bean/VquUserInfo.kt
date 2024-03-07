package com.mshy.VInterestSpeed.common.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * author: Lau
 * date: 2022/4/7
 * description:
 */
@Parcelize
data class VquUserInfo(
    var usercode: String,
    var nickname: String,
    var type: Int,
    var mobile: String,
    var avatar: String,
    //男=2，女=1
    var gender: Int,
    var age: Int,
    var birthday: String,
    var city: String,
    @SerializedName("finish_status")
    var finishStatus: Int,
    @SerializedName("im_token")
    var imToken: String,
    @SerializedName("is_anchor")
    var isAnchor: Int,
    var voice: String,
    @SerializedName("voice_time")
    var voiceTime: Long,
    var token: String,
    @SerializedName("user_id")
    var userId: String,
    var createtime: Long,
    var expiretime: Long,
    @SerializedName("expires_in")
    var expiresIn: String,
    var vip: Int? = 0,
    @SerializedName("vip_expire")
    var vip_expire: String?,
    @SerializedName("vip_des")
    var vipDes: String?,
    @SerializedName("vip_icon")
    var vipIcon: String?,
    @SerializedName("avatar_frame")
    var avatarFrame: String?,
    @SerializedName("is_auth")
    var isAuth: Int?,
    @SerializedName("is_rp_auth")
    var isRpAuth: Int?,
    @SerializedName("is_star_scout")
    var isStarScout: Int?,//1是星推官，0不是
    @SerializedName("scout_model")
    var scoutModel: Int,//1是工会，0不是
    @SerializedName("invite_code")
    var inviteCode: String?,
    var isShowCommonWord: Boolean,
    @SerializedName("guardian_num")
    var guardianNum: Int
) : Parcelable
