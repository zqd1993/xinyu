package com.mshy.VInterestSpeed.uikit.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallbackWrapper
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.nos.NosService
import com.netease.nimlib.sdk.nos.model.NosThumbParam

import com.netease.nimlib.sdk.nos.util.NosThumbImageUtil
import com.netease.nimlib.sdk.robot.model.RobotAttachment
import com.netease.nimlib.sdk.superteam.SuperTeam
import com.netease.nimlib.sdk.team.model.Team


/**
 * FileName: com.live.module.message.widget
 * Date: 2022/4/18 18:54
 * Description:
 * History:
 */
class NIMImageView : AppCompatImageView {
    private val defaultAvatarSize = com.mshy.VInterestSpeed.uikit.api.NimUIKit.getContext().dp2px(60f)

    private val defaultAvatarIconSize = com.mshy.VInterestSpeed.uikit.api.NimUIKit.getContext().dp2px(48f)

    private val defaultAvatarResId = R.mipmap.ic_common_head_circle_def

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }


    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param url 头像地址
     */
    fun loadAvatar(url: String?) {
        changeUrlBeforeLoad(
            url,
            defaultAvatarResId,
            defaultAvatarIconSize
        )
    }


    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param account 用户账号
     */
    fun loadBuddyAvatar(account: String?) {
        val userInfo = com.mshy.VInterestSpeed.uikit.api.NimUIKit.getUserInfoProvider().getUserInfo(account)
        changeUrlBeforeLoad(
            if (userInfo != null) NetBaseUrlConstant.IMAGE_URL + userInfo.avatar else null,
            defaultAvatarResId,
            defaultAvatarSize
        )
    }

    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param message 消息
     */
    fun loadBuddyAvatar(message: IMMessage) {
        var account = message.fromAccount
        if (message.msgType == MsgTypeEnum.robot) {
            val attachment = message.attachment as RobotAttachment
            if (attachment.isRobotSend) {
                account = attachment.fromRobotAccount
            }
        }
        loadBuddyAvatar(account)
    }

    /**
     * 加载群头像（默认大小的缩略图）
     *
     * @param team 群
     */
    fun loadTeamIconByTeam(team: Team?) {
        changeUrlBeforeLoad(
            team?.icon, R.drawable.nim_avatar_group,
            defaultAvatarSize
        )
    }

    /**
     * 加载群头像（默认大小的缩略图）
     *
     * @param team 群
     */
    fun loadSuperTeamIconByTeam(team: SuperTeam?) {
        changeUrlBeforeLoad(
            team?.icon, R.drawable.nim_avatar_group,
            defaultAvatarSize
        )
    }


    /**
     * 如果图片是上传到云信服务器，并且用户开启了文件安全功能，那么这里可能是短链，需要先换成源链才能下载。
     * 如果没有使用云信存储或没开启文件安全，那么不用这样做
     */
    private fun changeUrlBeforeLoad(url: String?, defaultResId: Int, thumbSize: Int) {
        if (TextUtils.isEmpty(url)) {
            // avoid useless call
            loadImage(url, defaultResId, thumbSize)
        } else {
            /*
             * 若使用网易云信云存储，这里可以设置下载图片的压缩尺寸，生成下载URL
             * 如果图片来源是非网易云信云存储，请不要使用NosThumbImageUtil
             */
            NIMClient.getService(NosService::class.java).getOriginUrlFromShortUrl(url).setCallback(
                object : RequestCallbackWrapper<String?>() {
                    override fun onResult(code: Int, result: String?, exception: Throwable?) {
                        var imageUrl: String? = result
                        if (TextUtils.isEmpty(imageUrl)) {
                            imageUrl = url
                        }
                        val thumbUrl = makeAvatarThumbNosUrl(imageUrl, thumbSize)
                        loadImage(thumbUrl, defaultResId, thumbSize)
                    }
                })
        }
    }


    fun loadImage(url: String?, defaultResId: Int, thumbSize: Int) {
//        vquLoadRoundImage("${url}?x-oss-process=image/resize,w_150", defaultResId, thumbSize, BaseApplication.context.dp2px(10f))
        vquLoadCircleImage(
            "${url}?x-oss-process=image/resize,w_150",
            defaultResId
        )
    }

    /**
     * 解决ViewHolder复用问题
     */
    fun resetImageView() {
        setImageBitmap(null)
    }

    private fun makeAvatarThumbNosUrl(
        url: String? = "",
        thumbSize: Int = defaultAvatarSize
    ): String {
        if (TextUtils.isEmpty(url)) {
            return ""
        }
        return if (thumbSize > 0) NosThumbImageUtil.makeImageThumbUrl(
            url,
            NosThumbParam.ThumbType.Crop,
            thumbSize,
            thumbSize
        ) else ""
    }

    fun getAvatarCacheKey(url: String = ""): String {
        return makeAvatarThumbNosUrl(url)
    }
}