package com.live.module.message.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.live.module.message.R
import com.live.module.message.net.MessageVquApiService
import com.live.module.message.net.MsgServiceManage
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.constant.VersionStatus
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusUtils
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.engine.SandboxFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnCallbackIndexListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.SandboxTransformUtils
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean
import com.mshy.VInterestSpeed.common.bean.gift.GiftListBean
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.dialog.*
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.GlideEngine
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.attchment.MessageVquGiftAttachment
import com.mshy.VInterestSpeed.uikit.bean.ChatIntimateBean
import com.mshy.VInterestSpeed.uikit.bean.IMCostBean
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean
import com.mshy.VInterestSpeed.uikit.business.session.fragment.MessageFragment
import com.mshy.VInterestSpeed.uikit.common.adapter.MsgCommonWordAdapter
import com.mshy.VInterestSpeed.uikit.common.http.CommonCallBack
import com.mshy.VInterestSpeed.uikit.event.NotificationIntimateChangeEvent
import com.mshy.VInterestSpeed.uikit.event.NotifyCommonWordEvent
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.opensource.svgaplayer.*
import com.opensource.svgaplayer.SVGAParser.Companion.shareParser
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.net.MalformedURLException
import java.net.URL
import com.mshy.VInterestSpeed.common.BuildConfig
import com.mshy.VInterestSpeed.common.utils.PermissionUtils


/**
 * FileName: com.netease.nim.uikit.business.session.fragment
 * Date: 2022/4/22 10:38
 * Description: 聊天界面
 * History:
 */
@Route(path = RouteUrl.Message.MessageVquMsgFragment)
class MessageVquMsgFragment : MessageFragment() {

    private var isPlaySvg = false

    private val mApi: MessageVquApiService by lazy {
        MsgServiceManage.getMsgService()
    }

    private val mPayViewModel: CommonPayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBusUtils.register(this)
        init()
        if (isShow) {
            initCommonWordModule()
            tvTip.visible()
            messageListView.setPadding(0, dp2px(44f), 0, 0)
        } else {
            tvTip.gone()
        }
        initRequestData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBusUtils.unRegister(this)
    }

    /**
     * 初始化请求数据的接口
     */
    private fun initRequestData() {
        //获取当前的亲密值信息
        getIntimate()
    }

    /**
     * 做一些初始化操作
     */
    private fun init() {
        rootView.findViewById<TextView>(R.id.tv_intimate_value).setViewClickListener {//亲密度权益弹框
            var intimateListDialog = CommonVquIntimateListDialog()
            intimateListDialog.toId = sessionId
            intimateListDialog.show(childFragmentManager)
//            val h5UrlBean = H5UrlBean()
//            h5UrlBean.gravity = "full"
//            H5WebDialog()
//                .setH5UrlBean(h5UrlBean)
//                .setLoadUrl(
//                    CommonVquWebUrlHelper.getInstance().webUrl.chatintimate
//                        .toString() + "&user_id=" + sessionId
//                )
//                .show(childFragmentManager)
        }
    }

    //region--------------------------聊天常用语start------------------------------
    private val mData: MutableList<NIMCommonWordBean> = ArrayList()
    private lateinit var mMsgAdapter: MsgCommonWordAdapter

    /**
     * 初始化常用语模块
     */
    private fun initCommonWordModule() {
        if (SpUtils.getBoolean(sessionId, true) == true) {
            initCommonWordRecyclerView()
            getCommonWordData()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun notifyIntimate(score: String, grade: Int) {
        if (isShow) {
            if (grade > 0) {
                rootView.findViewById<TextView>(R.id.tv_intimate_value).visible()
                rootView.findViewById<TextView>(R.id.tv_intimate_value).text = "$score°C"
            }
        }
    }
    var recyclerView: RecyclerView ?=null
    /**
     * 初始化常用语模块
     */
    private fun initCommonWordRecyclerView() {
        mMsgAdapter =
            MsgCommonWordAdapter(mData)
        recyclerView= rootView.findViewById(R.id.rv_common_word)
        recyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = mMsgAdapter
        recyclerView?.visible()
        mMsgAdapter.setOnItemClickListener { _, _, position ->
            inputPanel?.let {
                inputPanel.onTextMessageSendButtonPressed(mData[position].word)
                SpUtils.putBoolean(sessionId, false)
                recyclerView?.gone()
            }
        }
    }


    /**
     * 获取常用语
     */
    private fun getCommonWordData() {
        mApi.vquGetCommonWord()
            .enqueue(object : CommonCallBack<List<NIMCommonWordBean>>() {
                override fun onSuccess(data: List<NIMCommonWordBean>) {
                    setNewData(data)
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setNewData(result: List<NIMCommonWordBean>) {
        mData.clear()
        if (result.isNotEmpty()) {
            if (result.size > 4) {
                mData.addAll(result.subList(0, 4))
            } else {
                mData.addAll(result)
            }
        }

        mMsgAdapter.notifyDataSetChanged()
    }

    //endregion--------------------------聊天常用语end------------------------------

    //region----------------------------EventBus---------------------------------------
    /**
     * 显示通用语
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: String) {
        if ("NotEnough" == event) {
            "余额不足，请先充值".toast()
            mPayViewModel.showRechargeDialog(childFragmentManager)
//            CommonRechargeDialog().show(childFragmentManager, "充值")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: NotifyCommonWordEvent) {
        if (isShow) {
            setNewData(event.data)
        }
    }

    /**
     * 亲密值变化通知
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: NotificationIntimateChangeEvent) {
        val userInfo = UserSpUtils.getUserBean()
        if (!userInfo?.userId.isNullOrEmpty()) {
            val userId = if (userInfo?.userId?.toInt() == event.data.data.fromUid) {
                event.data.data.toUid
            } else {
                event.data.data.fromUid
            }
            if (userId != sessionId.toInt()) {
                return
            }
            notifyIntimate(event.data.data.score, event.data.data.grade.toInt())
        }
    }

    //endregion----------------------------EventBus---------------------------------------


    private fun getIntimate() {
        mApi.vquQueryIntimate(sessionId)
            .enqueue(object : CommonCallBack<ChatIntimateBean>() {
                override fun onSuccess(data: ChatIntimateBean) {
                    if (data.list.isNotEmpty()) {
                        if (data.list[0].user_id == sessionId.toInt()) {
                            notifyIntimate(data.list[0].score, data.list[0].grade)
                        }
                    }
                }
            })
    }

    /*---------------------------------------实现父类方法------------------------------------------*/

    override fun getGiftData() {
        val bottomGiftFragmentDialog =
            BottomGiftFragmentDialog()
        bottomGiftFragmentDialog.setAutoTouchClose(false)
        val bundle = Bundle()
        bundle.putInt("sendGiftType", 0)
        bottomGiftFragmentDialog.arguments = bundle
        bottomGiftFragmentDialog.setOnGiftItemClickedListener(object :
            BottomGiftFragmentDialog.OnGiftItemClickedListener {
            override fun onGiftClicked(bean: DialogGiftBean?, giftCount: Int) {
                bean?.let {
                    sendGift(Integer.parseInt(sessionId), it, giftCount)
                }
            }

            override fun onWalletClicked() {
                val amountDialogFragment = CommonRechargeDialog()
                amountDialogFragment.show(childFragmentManager, "充值")
            }
        })
        bottomGiftFragmentDialog.show(childFragmentManager)
    }

    override fun onMessageIncomingSvg(messages: MutableList<IMMessage>?) {
        messages?.let {
            it.forEach { msg ->
                if (msg.sessionType == sessionType
                    && !TextUtils.isEmpty(msg.fromAccount)
                    && msg.fromAccount == sessionId
                ) {
                    if (msg.attachment is MessageVquGiftAttachment) {
                        val attachment: MessageVquGiftAttachment =
                            msg.attachment as MessageVquGiftAttachment
                        if (attachment.gift_type == 1) {
                            addSvg(attachment.gift_svga)
                        }
                    }
                }
            }
        }
    }

    override fun addSvg(svgUrl: String) {
        if (svgUrl.isNotEmpty()) {
            val svgaImageView = SVGAImageView(requireContext())
            val flContent: FrameLayout = requireActivity().findViewById(android.R.id.content)
            flContent.addView(svgaImageView, flContent.childCount)
            shareParser().init(requireContext())
            val svgaParser = SVGAParser(context)
            try {
                val url = URL(NetBaseUrlConstant.IMAGE_URL + svgUrl)
                svgaParser.decodeFromURL(url, object : SVGAParser.ParseCompletion {

                    override fun onComplete(videoItem: SVGAVideoEntity) {
                        val drawable = SVGADrawable(videoItem)
                        svgaImageView.loops = 1
                        svgaImageView.callback = object : SVGACallback {
                            override fun onPause() {}
                            override fun onFinished() {
                                flContent.removeView(svgaImageView)
                                isPlaySvg = false
                            }

                            override fun onRepeat() {}
                            override fun onStep(i: Int, v: Double) {
                                isPlaySvg = true
                            }
                        }
                        svgaImageView.setImageDrawable(drawable)
                        //添加空监听避免操作
                        svgaImageView.setOnClickListener {}
                        svgaImageView.startAnimation()
                    }

                    override fun onError() {
                    }

                })
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
        }
    }

    override fun getChatInfo() {
        mApi.vquGetChatInfo(sessionId)
            .enqueue(object : CommonCallBack<JSONObject>() {
                override fun onSuccess(data: JSONObject) {
                    messageListPanel.setAdapterInfo(data)
                }

                override fun showSuccessToast(): Boolean {
                    return false
                }
            })
    }

    override fun getCallInfo() {

        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        var callDialog = CommonVquCallDialog()
        val args = Bundle()
        args.putString("anchor_id", sessionId)
        callDialog.arguments = args
        callDialog.setNoMoneyListener {
            mPayViewModel.showRechargeDialog(childFragmentManager)
        }
        callDialog.show(childFragmentManager, "call")
    }

    override fun callAudio(account: String?) {
        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        vquCall(account!!, 1)
    }

    override fun callVideo(account: String?) {
        if (UserManager.isVideo) {
            toast("正在语音/视频通话中，请稍后再试...")
            return
        }
        vquCall(account!!, 0)
    }

    override fun sendGift(toUid: Int, bean: DialogGiftBean, number: Int) {
        val params = hashMapOf<String, Any>()
        params["type"] = "2"
        params["to_uid"] = toUid
        params["gift_id"] = bean.id
        params["gift_type_id"] = bean.gift_type_id
        params["live_id"] = "0"
        params["num"] = number
        mApi.vquSendGift(params)
            .enqueue(object : CommonCallBack<GiftListBean>() {
                override fun onSuccess(data: GiftListBean) {
                    if (bean.type == 1) {//礼物为svg礼物
                        addSvg(bean.svga)
                    }
//                    toast("赠送成功")//产品说去掉吐司
                }

                override fun onEnoughCoin(msg: String) {
                    super.onEnoughCoin(msg)
//                    Log.d(TAG, "onEnoughCoin: "+"aaaa")
//                    mPayViewModel.showRechargeDialog(childFragmentManager)
//                    CommonRechargeDialog()
//                        .show(childFragmentManager, "充值")
                }
            })
    }

    //发送提示消息
    override fun sendTipMsg() {
        mApi.vquSendTipMsg(sessionId)
            .enqueue(object : CommonCallBack<Any>() {
                override fun onSuccess(data: Any) {
                }
            })
    }

    override fun imCost(type: Int, id: String, content: String?, message: IMMessage) {
        val params = hashMapOf<String, Any>()
        params["type"] = type
        params["to_uid"] = id
        params["content"] = content ?: ""
        params["msgId"] = message.uuid
        /**
         * 测试环境回调
         */
        if (NetBaseUrlConstant.DEBUG_BASE_URL == "http://120.78.160.71:8071/") {
            message.env = "tchat"
        }
        if (NetBaseUrlConstant.BASE_URL == "http://appta.pre.vqu.show/") {
            message.env = "pre"
        }
        SpUtils.putBoolean(sessionId, false)
        recyclerView?.gone()
        mApi.vquSendIMCost(params)
            .enqueue(object : CommonCallBack<IMCostBean>() {
                override fun onSuccess(result: IMCostBean) {
                    val data: MutableMap<String, Any> = HashMap()
                    data["is_cut"] = result.is_cut
                    data["cut_coin"] = result.cut_coin
                    data["coin"] = result.coin
                    data["money"] = result.money
                    data["vip"] = result.vip
                    data["is_free"] = result.is_free
                    message.remoteExtension = data

                    if (result.filter == 0) {
                        sendImMessage(message)
                    } else {
                        if (type == 1) {
                            message.content = result.content;
                            sendImMessage(message)
                        }
                    }
                }
            })
    }

    override fun selectImage() {
        PermissionUtils.videoPermission(
            this,
            "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
            "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
            requestCallback = { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    PictureSelector.create(this)//进页面就进行选择
                        .openGallery(SelectMimeType.ofImage())
                        .isDisplayCamera(!UserManager.isVideo)
                        .setMaxSelectNum(1)
                        .setSandboxFileEngine(object : SandboxFileEngine {
                            override fun onStartSandboxFileTransform(
                                context: Context?,
                                isOriginalImage: Boolean,
                                index: Int,
                                media: LocalMedia?,
                                listener: OnCallbackIndexListener<LocalMedia>?,
                            ) {
                                SandboxTransformUtils.copyPathToSandbox(
                                    context,
                                    media?.realPath,
                                    media?.mimeType
                                )
                            }
                        })
                        .setCompressEngine { _, list, listener ->
                            if (!list.isNullOrEmpty()) {
                                val localMedia = list[0]
                                Luban.with(BaseApplication.application).load(localMedia.realPath)
                                    .ignoreBy(200)
                                    .setCompressListener(object : OnCompressListener {
                                        override fun onStart() {
                                        }

                                        override fun onSuccess(index: Int, compressFile: File?) {
                                            localMedia.compressPath = compressFile?.absolutePath
                                            listener?.onCall(list)
                                        }

                                        override fun onError(index: Int, e: Throwable?) {
                                            e?.printStackTrace()
                                            toast("图片压缩失败")
                                        }
                                    }).launch()
                            }
                        }
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .forResult(object : OnResultCallbackListener<LocalMedia?> {
                            override fun onResult(result: ArrayList<LocalMedia?>?) {
                                if (!result.isNullOrEmpty()) {
                                    val path = result[0]
                                    val file = if (!path?.compressPath.isNullOrEmpty()) {
                                        File(path!!.compressPath)
                                    } else {
                                        File(path!!.realPath)
                                    }

                                    if (sessionType == SessionTypeEnum.P2P) {
                                        sendMessage(
                                            MessageBuilder.createImageMessage(
                                                sessionId,
                                                sessionType,
                                                file,
                                                file.name
                                            )
                                        )
                                    }
                                }
                            }

                            override fun onCancel() {
                            }
                        })
                } else {
                    toast("缺少摄像头权限")
                }
            })
    }


    private fun vquCall(account: String, type: Int) {
        mApi.vquGetCallInfo(account, type)
            .enqueue(object : Callback<BaseResponse<VideoVquCallBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<VideoVquCallBean>>,
                    response: Response<BaseResponse<VideoVquCallBean>>,
                ) {
                    when (response.body()?.code) {
                        0, -9999001 -> {//成功
                            if (type == 0) {
                                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                    .withString(
                                        RouteKey.SOCKET_URL,
                                        response.body()?.data?.socket_url ?: ""
                                    )
                                    .withInt(RouteKey.CODE, response.body()?.code ?: 0)
                                    .withBoolean(RouteKey.IS_CALLER, true)
                                    .withParcelable(RouteKey.CALL_BEAN, response.body()?.data)
                                    .navigation()
                            } else {
                                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                                    .withString(
                                        RouteKey.SOCKET_URL,
                                        response.body()?.data?.socket_url ?: ""
                                    )
                                    .withInt(RouteKey.CODE, response.body()?.code ?: 0)
                                    .withBoolean(RouteKey.IS_CALLER, true)
                                    .withParcelable(RouteKey.CALL_BEAN, response.body()?.data)
                                    .navigation()
                            }

                        }
                        1003, 1002 -> {
                            "余额不足，请先充值".toast()
                            CommonRechargeDialog().show(childFragmentManager, "充值")
                        }
                        1004 -> {
                            CommonHintDialog()
                                .setContentSize(15)
                                .setContentGravity(Gravity.CENTER)
                                .setTitle("温馨提示")
                                .setContent(response.body()?.message)
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
                                .show(childFragmentManager)
                        }
                        else -> {
                            response.body()?.message?.toast()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<VideoVquCallBean>>,
                    t: Throwable,
                ) {

                }
            })
    }

    override fun onBackPressed(): Boolean {
        if (isPlaySvg) {
            return true
        }
        return super.onBackPressed()
    }


}