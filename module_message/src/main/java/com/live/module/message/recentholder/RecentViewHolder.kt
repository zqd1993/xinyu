package com.live.module.message.recentholder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSONObject
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.message.R
import com.live.module.message.utils.OnlineMapUtil
import com.mshy.VInterestSpeed.uikit.attchment.MessageVquFateMatchAttachment
import com.mshy.VInterestSpeed.uikit.common.widget.NIMImageView
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment
import com.mshy.VInterestSpeed.uikit.business.session.emoji.MoonUtil
import com.mshy.VInterestSpeed.uikit.business.uinfo.UserInfoHelper
import com.mshy.VInterestSpeed.uikit.common.ui.drop.DropFake
import com.mshy.VInterestSpeed.uikit.common.ui.drop.DropFake.ITouchListener
import com.mshy.VInterestSpeed.uikit.common.ui.drop.DropManager
import com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil
import com.mshy.VInterestSpeed.uikit.common.util.sys.TimeUtil
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.event.model.Event
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.UserService


/**
 * FileName: com.live.module.message.recentholder
 * Date: 2022/4/18 17:03
 * Description:
 * History:
 */
abstract class RecentViewHolder {

    private var lastUnreadCount = 0


    protected lateinit var tvNickname: TextView

    protected lateinit var tvMessage: TextView
    protected lateinit var llFateMatch: LinearLayout

    protected lateinit var tvDatetime: TextView

    //亲密值
    protected lateinit var tvIntimateValue: TextView

    //官方图标
    protected lateinit var ivOfficial: View

    //在线绿点
    protected lateinit var onlineView: View

    // 消息发送错误状态标记，目前没有逻辑处理
    protected lateinit var imgMsgStatus: ImageView

    // 未读红点（一个占坑，一个全屏动画）
    protected lateinit var tvUnread: DropFake

    private lateinit var imgUnreadExplosion: ImageView

    protected lateinit var tvOnlineState: TextView

    //贵族徽章对应信息集合
//    private var nobleBadgeArray: JSONObject? = null

    private lateinit var context: Context

    private lateinit var llParent: LinearLayout

//    private lateinit var flNobleParent: View

    protected lateinit var imgHead: NIMImageView

    protected var callback: RecentContactsCallback? = null

    private lateinit var protectionHead: ImageView


    public fun convert(context: Context, holder: BaseViewHolder, recent: RecentContact) {
        this.context = context
        inflate(holder, recent)
        refresh(holder, recent)
    }

    private fun inflate(holder: BaseViewHolder, recent: RecentContact) {
        llParent = holder.getView(R.id.ll_parent)
//        flNobleParent = holder.getView(R.id.fl_noble_badge)
        imgHead = holder.getView(R.id.img_head)
        tvNickname = holder.getView(R.id.tv_nickname)
        tvMessage = holder.getView(R.id.tv_message)
        llFateMatch = holder.getView(R.id.ll_fate_match)
        tvUnread = holder.getView(R.id.unread_number_tip)
        imgUnreadExplosion = holder.getView(R.id.unread_number_explosion);
        tvDatetime = holder.getView(R.id.tv_date_time)
        imgMsgStatus = holder.getView(R.id.img_msg_status)
        tvOnlineState = holder.getView(R.id.tv_online_state)
        ivOfficial = holder.getView(R.id.img_msg_official)
        onlineView = holder.getView(R.id.view_online)
        tvIntimateValue = holder.getView(R.id.tv_intimate_value)
        protectionHead = holder.getView(R.id.protection_head)

        if (recent.extension != null) {
            if (recent.extension["online"] != null && (recent.extension["online"] == 1 || recent.extension["online"] == 10001)) {
                onlineView.visible()
            } else {
                onlineView.gone()
            }
        } else {
            if (OnlineMapUtil.getOnlineEvent(recent.contactId) != null) {
                var event: Event? = OnlineMapUtil.getOnlineEvent(recent.contactId)
                if (event != null && (event.eventValue == 1 || event.eventValue == 10001)) {
                    onlineView.visible()
                } else {
                    onlineView.gone()
                }
            } else {
                onlineView.gone()
            }

        }
        OnlineMapUtil.getOnline()
        tvUnread.setTouchListener(object : ITouchListener {
            override fun onDown() {
                DropManager.getInstance().currentId = recent
                DropManager.getInstance().down(tvUnread, tvUnread.text)
            }

            override fun onMove(curX: Float, curY: Float) {
                DropManager.getInstance().move(curX, curY)
            }

            override fun onUp() {
                DropManager.getInstance().up()
            }
        })
    }

    private fun refresh(holder: BaseViewHolder, recent: RecentContact) {

//        getBadgeConfig()
        // unread count animation
        val shouldBoom = lastUnreadCount > 0 && recent.unreadCount == 0 // 未读数从N->0执行爆裂动画;
        lastUnreadCount = recent.unreadCount

        val info = NIMClient.getService(UserService::class.java).getUserInfo(recent.contactId)

        if (info != null && info.extensionMap != null) {
            val vip = info.extensionMap["vip"].toString()
//            if (nobleBadgeArray != null && !TextUtils.isEmpty(vip)) {
//                val vipJson = nobleBadgeArray?.getJSONObject("vip")
////                vipJson ?: flNobleParent.gone()
//                val vipIcon = vipJson?.getString("vip")
//                CommonVquAddRankAndGradeViewHelper.addNobleView(context, llParent, vipIcon)
//            } else {
//                tvNickname.setTextColor(Color.parseColor("#273145"))
//            }
            if (!TextUtils.isEmpty(vip)) {
                if (vip == "0") {
                    tvNickname.setTextColor(Color.parseColor("#273145"))
                } else {
                    tvNickname.setTextColor(Color.parseColor("#934800"))
                }
            } else {
                tvNickname.setTextColor(Color.parseColor("#273145"))
            }
        } else {
            tvNickname.setTextColor(Color.parseColor("#273145"))
        }

        //去除置顶导致颜色不一致
        updateBackground(holder, recent)

        loadPortrait(recent)
        //TODO tag增加id判断，如果是系统和客服显示蓝色
        updateNickLabel(
            UserInfoHelper.getUserTitleName(recent.contactId, recent.sessionType),
            recent.contactId
        )

        updateOnlineState(recent)

        updateMsgLabel(holder, recent)

        updateNewIndicator(recent)

        if (shouldBoom) {
            val o = DropManager.getInstance().currentId
            if (o is String && o.equals("0")) {
                imgUnreadExplosion.setImageResource(R.drawable.nim_explosion)
                imgUnreadExplosion.visible()
                Handler().post {
                    (imgUnreadExplosion.drawable as AnimationDrawable).start()
                    // 解决部分手机动画无法播放的问题（例如华为荣耀）
//                    getAdapter().notifyItemChanged(
//                        getAdapter().getViewHolderPosition(
//                            position
//                        )
//                    )
                }
            }
        } else {
            imgUnreadExplosion.gone()
        }
    }


    private fun updateBackground(
        holder: BaseViewHolder,
        recent: RecentContact,
    ) {
        if (recent.tag and RecentContactsFragment.RECENT_TAG_STICKY == 0L) {
            holder.itemView.setBackgroundResource(R.drawable.nim_touch_bg)
        } else {
            holder.itemView
                .setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter)
        }
    }

    protected open fun loadPortrait(recent: RecentContact) {
        // 设置头像
        if (recent.sessionType == SessionTypeEnum.P2P) {
            imgHead.loadBuddyAvatar(recent.contactId)
        } else if (recent.sessionType == SessionTypeEnum.Team) {
            val team = NimUIKit.getTeamProvider().getTeamById(recent.contactId)
            imgHead.loadTeamIconByTeam(team)
        }
    }

    private fun updateNewIndicator(recent: RecentContact) {
        val unreadNum = recent.unreadCount
        tvUnread.visibility = if (unreadNum > 0) View.VISIBLE else View.GONE
        tvUnread.text = unreadCountShowRule(unreadNum)
    }

    private fun updateMsgLabel(holder: BaseViewHolder, recent: RecentContact) {
        tvMessage.visible()
        llFateMatch.gone()
        // 显示消息具体内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(
            context,
            tvMessage,
            getContent(recent),
            -1,
            0.45f
        )
//        tvMessage = getContent(recent)
        if (recent.msgType == MsgTypeEnum.tip) {
            tvMessage.text = recent.msgType.sendMessageTip
        } else if (recent.attachment is MessageVquFateMatchAttachment) {
            tvMessage.gone()
            llFateMatch.visible()
        }
        when (recent.msgStatus) {
            MsgStatusEnum.fail -> {
                imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small)
                imgMsgStatus.visible()
            }

            MsgStatusEnum.sending -> {
                imgMsgStatus.setImageResource(R.drawable.nim_recent_contact_ic_sending)
                imgMsgStatus.visible()
            }

            else -> imgMsgStatus.gone()
        }
        val timeString = TimeUtil.getTimeShowString(recent.time, true)
        tvDatetime.text = timeString
    }

    protected open fun getOnlineStateContent(recent: RecentContact?): String {
        return ""
    }

    protected open fun updateOnlineState(recent: RecentContact) {
        if (recent.sessionType == SessionTypeEnum.Team) {
            tvOnlineState.gone()
        } else {
            val onlineStateContent = getOnlineStateContent(recent)
            if (TextUtils.isEmpty(onlineStateContent)) {
                tvOnlineState.gone()
            } else {
                tvOnlineState.visible()
                tvOnlineState.text = getOnlineStateContent(recent)
            }
        }
    }

    protected open fun updateNickLabel(nick: String?, id: String) {
        var labelWidth = ScreenUtil.screenWidth
        labelWidth -= ScreenUtil.dip2px((50 + 70).toFloat()) // 减去固定的头像和时间宽度
        if (labelWidth > 0) {
            tvNickname.maxWidth = labelWidth
        }

        //TODO tag如果是系统和客服
        if (!TextUtils.isEmpty(id)) {
            if ("2" == id || "4" == id || "11" == id || "3" == id || "100091" == id || "6" == id || "7" == id || "8" == id || "9" == id || "10" == id) {
                ivOfficial.visible()
            } else {
                ivOfficial.gone()
            }
        }
        if (IntimateUtils.getInstance().findData(id.toInt()) == null
            || IntimateUtils.getInstance().findData(id.toInt()).grade == 0
        ) {
            tvIntimateValue.gone()
        } else {
            tvIntimateValue.visible()
            tvIntimateValue.text = IntimateUtils.getInstance().findData(id.toInt()).score
        }

        if (IntimateUtils.getInstance().findData(id.toInt()) != null) {
            if (IntimateUtils.getInstance().findData(id.toInt()).isGuardian == 1) {
                protectionHead.visibility = View.VISIBLE
            } else {
                protectionHead.visibility = View.GONE
            }
        } else {
            protectionHead.visibility = View.GONE
        }
        tvNickname.text = nick
    }

    protected open fun unreadCountShowRule(unread: Int): String {
        return unread.coerceAtMost(99).toString()
    }

    /**
     * 获取缓存徽章配置
     */
    private fun getBadgeConfig() {
        val badgeConfig = SpUtils.getString(SpKey.badgeConfig, "")
        if (!TextUtils.isEmpty(badgeConfig)) {
            val jsonObject = JSONObject.parseObject(badgeConfig)
//            nobleBadgeArray = jsonObject.getJSONObject("noble")
        }
    }

    // 子类覆写
    protected abstract fun getContent(recent: RecentContact): String

    public fun setCallBack(callback: RecentContactsCallback) {
        this.callback = callback
    }
}