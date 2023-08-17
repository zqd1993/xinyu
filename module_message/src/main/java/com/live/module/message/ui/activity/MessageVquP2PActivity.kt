package com.live.module.message.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.message.R
import com.live.module.message.net.MessageVquApiService
import com.live.module.message.net.MsgServiceManage
import com.live.vquonline.base.ktx.invisible
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusUtils
import com.mshy.VInterestSpeed.common.bean.CommonVquGlobalConfigBean
import com.mshy.VInterestSpeed.common.bean.ShowDialogWhenUserInCallEvent
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.InviteNoBalanceEvent
import com.mshy.VInterestSpeed.common.event.InviteTipEvent
import com.mshy.VInterestSpeed.common.event.JumpToChatEvent
import com.mshy.VInterestSpeed.common.event.RedPacketEvent
import com.mshy.VInterestSpeed.common.helper.startARouterActivity
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquIntimateLevelUpDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquRedPacketDialog
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.api.model.session.SessionCustomization
import com.mshy.VInterestSpeed.uikit.api.model.session.SessionCustomization.OptionsButton
import com.mshy.VInterestSpeed.uikit.api.model.session.SessionEventListener
import com.mshy.VInterestSpeed.uikit.business.session.activity.P2PMessageActivity
import com.mshy.VInterestSpeed.uikit.common.http.CommonCallBack
import com.mshy.VInterestSpeed.uikit.event.NotificationIntimateUpEvent
import com.netease.nimlib.sdk.msg.model.IMMessage
import org.greenrobot.eventbus.Subscribe


/**
 * FileName: com.live.module.message.ui
 * Date: 2022/4/25 15:27
 * Description:
 * History:
 */
@Route(path = RouteUrl.Message.MessageVquP2PAct)
class MessageVquP2PActivity : P2PMessageActivity() {

    private val mApi: MessageVquApiService by lazy {
        MsgServiceManage.getMsgService()
    }

    protected val mPayViewModel: CommonPayViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MessageVquP2PActivity", "onCreate: ")
        EventBusUtils.register(this)
        UserManager.isChatting = true
        var isFromInfo: Boolean = false
        isFromInfo = intent.getBooleanExtra(RouteKey.FROM_INFO, false)
        NimUIKit.setSessionListener(object :
            SessionEventListener {
            override fun onAvatarClicked(context: Context?, message: IMMessage?) {
                //跳转用户详情界面
                message?.let {
                    if (isFromInfo) {
                        finish()
                    } else {
                        ARouter.getInstance()
                            .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                            .withInt(
                                RouteKey.USERID,
                                it.sessionId.toInt()
                            )
                            .withBoolean(RouteKey.FROM_CHAT, true)
                            .navigation()
                    }

                }
            }

            override fun onAvatarRightClicked(context: Context?, message: IMMessage?) {
                if (isFromInfo) {
                    finish()
                } else {
                    //跳转用户详情界面
                    UserSpUtils.getUserBean()?.let {
                        ARouter.getInstance()
                            .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                            .withInt(
                                RouteKey.USERID,
                                it.userId.toInt()
                            )
                            .withBoolean(RouteKey.FROM_CHAT, true)
                            .navigation()
                    }
                }

            }

            override fun onAvatarLongClicked(context: Context?, message: IMMessage?) {
            }

            override fun onAckMsgClicked(context: Context?, message: IMMessage?) {
            }

        })
        getConfigInfoList()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unRegister(this)
    }

    private fun getConfigInfoList() {
        mApi.getGlobalConfig()
            .enqueue(object : CommonCallBack<CommonVquGlobalConfigBean>() {
                override fun onSuccess(data: CommonVquGlobalConfigBean) {
                    var isHideUi = false
                    for (id in data.config.noInfoIdList) {
                        if (sessionId.equals(id)) {
                            isHideUi = true
                        }
                    }
                    if (buttonContainer != null) {
                        if (isHideUi) {
                            buttonContainer.invisible()
                        } else {
                            buttonContainer.visible()
                        }
                    }
                }

            })
    }

    @Subscribe
    fun onEventMainThread(event: NotificationIntimateUpEvent) {
        CommonVquIntimateLevelUpDialog()
            .setData(event.data.data)
            .show(supportFragmentManager)
    }

    @Subscribe
    fun onEventMainThread(event: RedPacketEvent) {
        var redPacketDialog = CommonVquRedPacketDialog()
        redPacketDialog.setData(event.data)
        redPacketDialog.show(supportFragmentManager, "red")
    }

    @Subscribe
    fun onEventMainThread(event: JumpToChatEvent) {
        NimUIKit.startP2PSession(
            this@MessageVquP2PActivity,
            event.chattingId
        )
        finish()
    }

    @Subscribe
    fun onEventMainThread(event: InviteNoBalanceEvent) {
        mPayViewModel.showRechargeDialog(this@MessageVquP2PActivity.supportFragmentManager)
    }

    @Subscribe
    fun onEventMainThread(event: InviteTipEvent) {
        CommonHintDialog()
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setTitle("温馨提示")
            .setContent(event.msg)
            .setLeftText("取消")
            .setRightText("去聊天")
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {
                    dialogFragment.dismissAllowingStateLoss()
                }

                override fun onRight(dialogFragment: DialogFragment) {
                    dialogFragment.dismissAllowingStateLoss()

                }
            })
            .show(this@MessageVquP2PActivity!!.supportFragmentManager)
    }

    @Subscribe
    fun showDialogWhenUserInCallEvent(event: ShowDialogWhenUserInCallEvent) {
        CommonHintDialog()
            .setTitle("余额不足")
            .setContent("金币余额不足1分钟，请及时充值避免错过缘分！")
            .setLeftText(getString(R.string.agora_vqu_dialog_left_txt))
            .setRightText(getString(R.string.agora_vqu_dialog_right_txt))
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment) {}
                override fun onRight(dialogFragment: DialogFragment) {
//                    EventBus.getDefault().post(ShowDialogHangUpEvent())
                    mPayViewModel.showRechargeDialog(supportFragmentManager)
                }
            })
            .show(supportFragmentManager)


    }

    override fun getCustomization(): SessionCustomization {
        val p2pCustomization =
            SessionCustomization()
        // 定制ActionBar右边的按钮，可以加多个
        val buttons: ArrayList<OptionsButton> = ArrayList()
        val moreButton: OptionsButton = object : OptionsButton() {
            override fun onClick(context: Context?, view: View?, sessionId: String?) {
                sessionId?.let {
                    startARouterActivity(
                        RouteUrl.Message.MessageVquChatSettingActivity,
                        RouteKey.USERID,
                        it.toInt()
                    )
                }
            }
        }
        moreButton.iconId = R.mipmap.resources_vqu_message_more_p2p

        buttons.add(moreButton)
        p2pCustomization.buttons = buttons
        return p2pCustomization
    }
}