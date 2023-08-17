package com.live.module.info.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * author: Tany
 * date: 2022/4/24
 * description:
 */

data class InfoVquUserBean(
    @SerializedName("age")
    var age: Int,
    @SerializedName("is_vip")
    var is_vip: Int = 1,
    @SerializedName("albums")
    var albums: ArrayList<Album>,
    @SerializedName("album_videos")
    var albumVideos: ArrayList<Album>,
    @SerializedName("albums_list")
    var albumsList: List<Album>,
    @SerializedName("anchor")
    var anchor: Anchor,
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("avatar_auth")
    var avatarAuth: String,
    @SerializedName("avatar_frame")
    var avatarFrame: String,
    @SerializedName("avatar_state")
    var avatarState: Int,
    @SerializedName("basic_info")
    var basicInfo: MutableList<BasicInfo>,
    @SerializedName("basic_info_detail")
    var basicInfoDetail: BasicInfoDetail,
    @SerializedName("card_info")
    var cardInfo: CardInfo,
    @SerializedName("dynamic")
    var `dynamic`: MutableList<DynamicBean>,
    @SerializedName("dynamic_num")
    var dynamicNum: Int,
    @SerializedName("fans_count")
    var fansCount: Int,
    @SerializedName("follow_count")
    var followCount: Int,
    @SerializedName("gender")
    var gender: Int,
    @SerializedName("gifts")
    var gifts: MutableList<GiftBean>,
    @SerializedName("gr")
    var gr: Gr,
    @SerializedName("guard")
    var guard: Guard,
    @SerializedName("is_anchor")
    var isAnchor: Int,
    @SerializedName("is_auth")
    var isAuth: Int,
    @SerializedName("is_rp_auth")
    var isRpAuth: Int,
    @SerializedName("is_beckon")
    var isBeckon: Boolean,
    @SerializedName("is_beckon_text")
    var isBeckonText: String,
    @SerializedName("is_black")
    var isBlack: Int,
    @SerializedName("is_follow")
    var isFollow: Int,
    @SerializedName("is_star_scout")
    var isStarScout: Int,
    @SerializedName("label")
    var label: ArrayList<Tag>,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("online")
    var online: Online,
    @SerializedName("sign")
    var sign: String,
    @SerializedName("task")
    var task: Task,
    @SerializedName("usercode")
    var usercode: String,
    @SerializedName("userid")
    var userid: Int,
    @SerializedName("video")
    var video: String,
    @SerializedName("video_auth")
    var videoAuth: String,
    @SerializedName("vip")
    var vip: Int,
    @SerializedName("vip_icon")
    var vipIcon: String,
    @SerializedName("user_remark")
    var userRemark: String,
    @SerializedName("voice")
    var voice: Voice,
    @SerializedName("avatar_auth_status")
    var avatarAuthStatus: Int,//审核状态  0待审核   1审核通过
    @SerializedName("video_auth_status")
    var videoAuthStatus: Int,//审核状态  0待审核   1审核通过
    @SerializedName("location")
    var location : String//所在地
)


data class GiftBean(
    @SerializedName("id")
    var id: Int,
    @SerializedName("img")
    var img: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("price")
    var price: Int,
    @SerializedName("total")
    var total: String
)

data class DynamicBean(
    @SerializedName("type")
    var type: Int,
    @SerializedName("url")
    var url: String
)

data class Album(
    @SerializedName("is_video")
    var isVideo: Int,
    @SerializedName("status")
    var status: Int,//审核状态  0待审核   1审核通过
    @SerializedName("url")
    var url: String,
) : Serializable

data class Anchor(
    @SerializedName("open_video_status")
    var openVideoStatus: Int,
    @SerializedName("order_switch")
    var orderSwitch: Int,
    @SerializedName("video_status")
    var videoStatus: Int,
    @SerializedName("skill")
    var skill: ArrayList<Skill>
)

data class Skill(
    @SerializedName("dis_price")
    var disPrice: String,
    @SerializedName("icon")
    var icon: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("price")
    var price: Int,
    @SerializedName("rate")
    var rate: String,
    @SerializedName("score")
    var score: Int,
    @SerializedName("service_count")
    var serviceCount: Int,
    @SerializedName("service_time")
    var serviceTime: Int,
    @SerializedName("voice_price")
    var voicePrice: Int
)

data class BasicInfo(
    @SerializedName("key")
    var key: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("value")
    var value: String
)

data class BasicInfoDetail(
    @SerializedName("age")
    var age: String,
    @SerializedName("annual_income")
    var annualIncome: String,
    @SerializedName("birthday")
    var birthday: String,
    @SerializedName("city")
    var city: String,
    @SerializedName("education")
    var education: String,
    @SerializedName("gender")
    var gender: String,
    @SerializedName("height")
    var height: String,
    @SerializedName("is_marriage")
    var isMarriage: String,
    @SerializedName("occupation")
    var occupation: String,
    @SerializedName("signs")
    var signs: String,
    @SerializedName("usercode")
    var usercode: String,
    @SerializedName("weight")
    var weight: String
)

class CardInfo

data class Gr(
    @SerializedName("grade")
    var grade: Grade
)

data class Guard(
    @SerializedName("avatar")
    var avatar: String,
    @SerializedName("diff_num")
    var diffNum: Int,
    @SerializedName("guard_person_total")
    var guardPersonTotal: Int,
    @SerializedName("guard_price")
    var guardPrice: Int,
    @SerializedName("guard_symbol_total")
    var guardSymbolTotal: Int,
    @SerializedName("intimate_num")
    var intimateNum: Int,
    @SerializedName("list")
    var list: List<Any>,
    @SerializedName("nickname")
    var nickname: String,
    @SerializedName("user_id")
    var userId: Int
)

data class Label(
    @SerializedName("color")
    var color: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("over_color")
    var overColor: String,
    @SerializedName("start_color")
    var startColor: String
)

data class Online(
    @SerializedName("newColor")
    var newColor: String,
    @SerializedName("newMsg")
    var newMsg: String,
    @SerializedName("newStatus")
    var newStatus: Int,
    @SerializedName("online_status")
    var onlineStatus: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("statusMsg")
    var statusMsg: String,
    @SerializedName("statusMsgNew")
    var statusMsgNew: String
)

data class Task(
    @SerializedName("album_task")
    var albumTask: AlbumTask,
    @SerializedName("avatar_task")
    var avatarTask: AvatarTask,
    @SerializedName("over_task")
    var overTask: OverTask,
    @SerializedName("sign_task")
    var signTask: SignTask,
    @SerializedName("video_task")
    var videoTask: VideoTask
)

data class Voice(
    @SerializedName("voice_status")
    var voiceStatus: Int,//审核状态  0待审核   1审核通过
    @SerializedName("voice")
    var voice: String,
    @SerializedName("voice_time")
    var voiceTime: Int
)

data class Grade(
    @SerializedName("grade")
    var grade: Int,
    @SerializedName("imgInfo")
    var imgInfo: com.mshy.VInterestSpeed.common.bean.CommonVquGradeConfigBean,
    @SerializedName("total_growth")
    var totalGrowth: String
)


data class AlbumTask(
    @SerializedName("des")
    var des: String,
    @SerializedName("is_show")
    var isShow: Int,
    @SerializedName("task_id")
    var taskId: Int
)

data class AvatarTask(
    @SerializedName("des")
    var des: String,
    @SerializedName("is_show")
    var isShow: Int,
    @SerializedName("task_id")
    var taskId: Int
)

data class OverTask(
    @SerializedName("des")
    var des: String,
    @SerializedName("is_show")
    var isShow: Int,
    @SerializedName("task_id")
    var taskId: Int
)

data class SignTask(
    @SerializedName("des")
    var des: String,
    @SerializedName("is_show")
    var isShow: Int,
    @SerializedName("task_id")
    var taskId: Int
)

data class VideoTask(
    @SerializedName("des")
    var des: String,
    @SerializedName("is_show")
    var isShow: Int,
    @SerializedName("task_id")
    var taskId: Int
)