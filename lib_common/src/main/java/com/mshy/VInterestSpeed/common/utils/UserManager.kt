package com.mshy.VInterestSpeed.common.utils

import com.mshy.VInterestSpeed.common.bean.VquUserInfo
import com.mshy.VInterestSpeed.common.bean.WebUrl
import com.tencent.mmkv.MMKV

/**
 * author: Lau
 * date: 2022/4/7
 * description:
 */
object UserManager {
    var chattingId: String = ""
    var isChatting: Boolean = false
    var inviteResult: Boolean = false
    var userInfo: VquUserInfo? = null
    var inviteCode: String? = null
    var isStartForegroundService = false
    var isVideo: Boolean = false  //记录当前是否在视频
    var isMatch: Boolean = false  //记录当前是否在视频
        set(value) {
            field = value
            MMKV.defaultMMKV()?.putBoolean("UserManagerMatch", value)
        }


    var isMatchReturn: Boolean = true  //记录当前是否速配返回
    var webUrl: WebUrl? = null
    var videoRequestBean: com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean? = null


}