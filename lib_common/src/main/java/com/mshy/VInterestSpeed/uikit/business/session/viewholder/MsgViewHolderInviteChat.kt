package com.mshy.VInterestSpeed.uikit.business.session.viewholder

import android.widget.TextView
import androidx.annotation.DimenRes
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.InviteNoBalanceEvent
import com.mshy.VInterestSpeed.common.event.InviteTipEvent
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.utils.UserManager
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author: Tany
 * date: 2022/8/15
 * description:
 */
class MsgViewHolderInviteChat(adapter: com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter<*, *>?) :
    com.mshy.VInterestSpeed.uikit.business.session.viewholder.MsgViewHolderBase(adapter) {
    private var tvTitle: TextView? = null
    private var tvMsg: TextView? = null
    private var tvCall: TextView? = null
    private var userId:String="0"
    private var callType:Int=0//0视频 1音频
    override fun getContentResId(): Int {
        return R.layout.common_tanta_nim_invite_chat_custom_message
    }

    override fun inflateContentView() {
        tvTitle = view.findViewById(R.id.tv_title)
        tvMsg = view.findViewById(R.id.tv_msg)
        tvCall = view.findViewById(R.id.tv_call)
    }

    override fun bindContentView() {
        if (message.attachment == null) {
            return
        }
        val msg = message.attachment as com.mshy.VInterestSpeed.uikit.attchment.MessageVquInviteChatAttachment
        tvTitle!!.text = msg.title
        tvMsg!!.text = msg.msg
        userId=msg.from_uid
        callType=msg.calltype
        if(callType==0){
            tvCall?.text="拨打视频"
        }else{
            tvCall?.text="拨打音频"
        }
    }

    private fun getDimen(@DimenRes dimen: Int): Float {
        return context.resources.getDimension(dimen)
    }

    override fun isMiddleItem(): Boolean {
        return true
    }

    override fun isShowBubble(): Boolean {
        return false
    }

    override fun isShowHeadImage(): Boolean {
        return false
    }

    override fun shouldDisplayReceipt(): Boolean {
        return false
    }

    override fun leftBackground(): Int {
        return 0
    }

    override fun rightBackground(): Int {
        return 0
    }

    override fun shouldDisplayStatus(): Boolean {
        return false
    }

    override fun onItemClick() {
        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        var globalApiService =
            GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
        globalApiService.vquGetCallInfo(userId!!, callType)
            .enqueue(object : Callback<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>>,
                    response: Response<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>>,
                ) {
                    when (response.body()?.code) {
                        0, -9999001 -> {//成功
                                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                    .withString(
                                        RouteKey.SOCKET_URL,
                                        response.body()?.data?.socket_url ?: ""
                                    )
                                    .withBoolean(RouteKey.IS_CALLER, true)
                                    .withInt(RouteKey.CODE, response.body()?.code ?: 0)
                                    .withParcelable(RouteKey.CALL_BEAN, response.body()?.data)
                                    .navigation()

                        }
                        1003, 1002 -> {
                            "余额不足，请先充值".toast()
                            EventBus.getDefault().post(InviteNoBalanceEvent(userId))
                        }
                        1004 -> {
                            EventBus.getDefault().post(InviteTipEvent(userId,response.body()?.message))
                        }
                        else -> {
                            response.body()?.message?.toast()
                        }

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>>,
                    t: Throwable,
                ) {

                }
            })

    }
}