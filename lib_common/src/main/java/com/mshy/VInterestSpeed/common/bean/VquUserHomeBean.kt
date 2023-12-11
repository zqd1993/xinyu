package com.mshy.VInterestSpeed.common.bean

import com.google.gson.annotations.SerializedName


/**
 * author: Lau
 * date: 2022/4/12
 * description:
 */
data class VquUserHomeBean(
    @SerializedName("anchor")
    val anchor: Anchor,
    @SerializedName("game_info")
    val gameInfo: GameInfo,
    @SerializedName("gr")
    val gr: Gr,
    @SerializedName("guild")
    val guild: Int,
    @SerializedName("is_complete_info")
    val isCompleteInfo: Int,
    //是否有未领取的任务奖励 1有 0没有
    @SerializedName("is_task_rewards")
    val isTaskRewards: Int = 0,
    @SerializedName("red_dot")
    val redDot: RedDot,
    @SerializedName("tip")
    val tip: String,
    @SerializedName("usercount")
    val usercount: Usercount,
    @SerializedName("userinfo")
    val userinfo: Userinfo,
    @SerializedName("webUrl")
    val webUrl: WebUrl,
    @SerializedName("show_location")
    val showLocation: Int,
    @SerializedName("is_show_invite")
    val isShowInvite: Int,
    @SerializedName("is_show_my_network")
    val isShowMyNetwork: Int
)

data class Anchor(
    @SerializedName("open_video_status")
    val openVideoStatus: Int,
    @SerializedName("order_switch")
    val orderSwitch: Int
)

data class GameInfo(
    @SerializedName("tip_num")
    val tipNum: String
)

data class Gr(
    @SerializedName("grade")
    val grade: Grade,
    @SerializedName("rank")
    val rank: Rank
)

data class RedDot(
    @SerializedName("badges_watch")
    val badgesWatch: Int,
    @SerializedName("dress_watch")
    val dressWatch: Int,
    @SerializedName("is_edit")
    val isEdit: Int,
    @SerializedName("package_watch")
    val packageWatch: Int
)

data class Usercount(
    @SerializedName("fans_count")
    val fansCount: Int,
    @SerializedName("follow_count")
    val followCount: Int,
    @SerializedName("new_fans_count")
    val newFansCount: Int,
    @SerializedName("new_videoTrendsLike_count")
    val newVideoTrendsLikeCount: Int,
    @SerializedName("new_visitor_count")
    val newVisitorCount: Int,
    @SerializedName("videoTrendsLike_count")
    val videoTrendsLikeCount: Int,
    @SerializedName("viewer_count")
    val viewerCount: Int,
    @SerializedName("visitor_count")
    val visitorCount: Int
)

data class Userinfo(
    @SerializedName("age")
    val age: Int,
    @SerializedName("album_list")
    val albumList: List<String>,
    @SerializedName("album_list_new")
    val albumListNew: List<AlbumNew>,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("avatar_frame")
    val avatarFrame: String,
    @SerializedName("badge_all")
    val badgeAll: List<Any>,
    @SerializedName("badge_info")
    val badgeInfo: com.mshy.VInterestSpeed.common.bean.CommonVquNormalBadgeConfigBean?,
    @SerializedName("badge_num")
    val badgeNum: Int,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("createtime")
    val createtime: Int,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("expiretime")
    val expiretime: Int,
    @SerializedName("finish_status")
    val finishStatus: Int,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("im_token")
    val imToken: String,
    @SerializedName("is_anchor")
    val isAnchor: Int,
    @SerializedName("is_auth")
    val isAuth: Int,
    @SerializedName("is_rp_auth")
    val isRpAuth: Int,
    @SerializedName("is_live")
    val isLive: Int,
    @SerializedName("is_msg_refuse")
    val isMsgRefuse: Int,
    @SerializedName("is_star_scout")
    val isStarScout: Int,

    @SerializedName("scout_model")
    val scoutModel: Int,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("sign")
    val sign: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("usercode")
    val usercode: String,
    @SerializedName("vip")
    val vip: Int,
    @SerializedName("vip_expire")
    val vip_expire: String,
    @SerializedName("vip_des")
    val vipDes: String,
    @SerializedName("vip_icon")
    val vipIcon: String,
    @SerializedName("voice")
    val voice: String,
    @SerializedName("voice_status")
    val voiceStatus: Int,
    @SerializedName("voice_time")
    val voiceTime: Int
)

data class WebUrl(
    @SerializedName("anchorDividedinfo")
    val anchorDividedinfo: String,
    @SerializedName("anchorStarlevel")
    val anchorStarlevel: String,
    @SerializedName("chatintimate")
    val chatintimate: String,
    @SerializedName("help")
    val help: String,
    @SerializedName("juvenile_protection")
    val juvenileProtection: String,
    @SerializedName("myGrade")
    val myGrade: String,
    @SerializedName("publish")
    val publish: String,
    @SerializedName("recharge_agreement")
    val rechargeAgreement: String,
    @SerializedName("share")
    val share: String,
    @SerializedName("taskCenter")
    val taskCenter: String,
    @SerializedName("tv")
    val tv: String,
    @SerializedName("wx_official")
    val wxOfficial: String
)

data class Grade(
    @SerializedName("grade")
    val grade: Int,
    @SerializedName("imgInfo")
    val imgInfo: com.mshy.VInterestSpeed.common.bean.CommonVquGradeConfigBean,
    @SerializedName("total_growth")
    val totalGrowth: String
)

data class Rank(
    @SerializedName("img")
    val img: String,
    @SerializedName("rank")
    val rank: String,
    @SerializedName("sort")
    val sort: Int
)

data class ImgInfo(
    @SerializedName("grade")
    val grade: Int,
    @SerializedName("grade_name")
    val gradeName: String,
    @SerializedName("icon1_width")
    val icon1Width: String,
    @SerializedName("icon_width")
    val iconWidth: String,
    @SerializedName("img1")
    val img1: String,
    @SerializedName("img2")
    val img2: String
)

data class AlbumNew(
    @SerializedName("status")
    val status: Int,
    @SerializedName("url")
    val url: String
)

