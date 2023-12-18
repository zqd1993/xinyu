package com.live.module.message.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.live.module.message.R
import com.live.module.message.adapter.MessageVquRecentAdapter
import com.live.module.message.databinding.MessageTantaRecentFragmentBinding
import com.live.module.message.recentholder.RecentContactsCallback
import com.live.module.message.vm.MessageVquRecentFragmentViewModel
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant.IMAGE_URL
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.event.IsShowGuideEvent
import com.mshy.VInterestSpeed.common.event.UnReadCountEvent
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.helper.startARouterActivity
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog
import com.mshy.VInterestSpeed.common.utils.JumpUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.api.model.user.UserInfoObserver
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment
import com.mshy.VInterestSpeed.uikit.business.uinfo.UserInfoHelper
import com.mshy.VInterestSpeed.uikit.common.CommonUtil
import com.mshy.VInterestSpeed.uikit.common.badger.BadgerNum
import com.mshy.VInterestSpeed.uikit.common.ui.drop.DropFake
import com.mshy.VInterestSpeed.uikit.event.DeleteRecentContactEvent
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.RequestCallbackWrapper
import com.netease.nimlib.sdk.ResponseCode
import com.netease.nimlib.sdk.event.EventSubscribeService
import com.netease.nimlib.sdk.event.EventSubscribeServiceObserver
import com.netease.nimlib.sdk.event.model.EventSubscribeRequest
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * author: Tany
 * date: 2022/6/20
 * description:新的会话列表页面
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquRecentBackupFragment)
class MessageVquConversationFragment :
    BaseLazyFrameFragment<MessageTantaRecentFragmentBinding, MessageVquRecentFragmentViewModel>(),
    OnRefreshListener, OnLoadMoreListener {
    private var items: MutableList<RecentContact> = mutableListOf()
    private lateinit var adapter: MessageVquRecentAdapter
    private var headView: View? = null
    private var visitorsHeadView: View? = null
    private var ivHead: ImageView? = null
    private var tvContent: TextView? = null
    private var tvCount: DropFake? = null
    private var banner: Banner<String, BannerImageAdapter<String>>? = null
    private val images: MutableList<String> = mutableListOf()
    private var lastRecentContact: RecentContact? = null
    var isClearAll: Boolean = false
    var delCount: Int = 0
    var imCount: Int = 0
    var loadingDialog: LoadingDialog? = null
    var time: Long = 6 * 60 * 60 * 1000//上线6小时
    override val mViewModel: MessageVquRecentFragmentViewModel by viewModels()
    private var userInfoObserver: UserInfoObserver? = null
    private var hasHeader: Boolean = false
    private var lookAtMeRl: RelativeLayout? = null
    private var readOnceMsgRl: RelativeLayout? = null
    private var ivMsgHead: ImageView? = null
    private var tvMsg: TextView? = null
    private var tvMsgContent: TextView? = null
    override fun MessageTantaRecentFragmentBinding.initView() {
        loadingDialog = LoadingDialog(
            requireContext(),
            "删除中请稍后"
        )
        initMessageList()
        registerObservers(true)
    }

    private fun initMessageList() {
        mBinding.smart.setEnableRefresh(true)
        mBinding.smart.setEnableLoadMore(false)
        mBinding.smart.setOnRefreshListener(this)
        mBinding.smart.setOnLoadMoreListener(this)
        mBinding.recyclerView.itemAnimator = null
        adapter = MessageVquRecentAdapter(items, false)
        adapter.headerWithEmptyEnable = true
        //添加空布局
        val empty = layoutInflater.inflate(R.layout.message_tanta_msg_empty, null, false)
        empty.findViewById<ImageView>(R.id.iv_empty)
            .setImageResource(R.mipmap.resources_tanta_message_empty)
        adapter.setEmptyView(empty)
//        headView = layoutInflater.inflate(R.layout.message_tanta_include_banner, null, false)
//        banner = headView?.findViewById(R.id.vqu_banner_ad)
//        headView?.let {
//            adapter.addHeaderView(it)
//        }
        visitorsHeadView =
            layoutInflater.inflate(R.layout.message_tanta_include_visitors, null, false)
        ivHead = visitorsHeadView?.findViewById(R.id.iv_head)
        tvCount = visitorsHeadView?.findViewById(R.id.tv_count)
        tvContent = visitorsHeadView?.findViewById(R.id.tv_content)
        lookAtMeRl = visitorsHeadView?.findViewById(R.id.look_at_me_rl)
        readOnceMsgRl = visitorsHeadView?.findViewById(R.id.read_once_msg_rl)
        ivMsgHead = visitorsHeadView?.findViewById(R.id.iv_msg_head)
        tvMsg = visitorsHeadView?.findViewById(R.id.tv_msg)
        tvMsgContent = visitorsHeadView?.findViewById(R.id.tv_msg_content)
        banner = visitorsHeadView?.findViewById(R.id.vqu_banner_ad)
        visitorsHeadView?.let {
            adapter.addHeaderView(it)
        }
        lookAtMeRl?.setOnClickListener {
            ARouter.getInstance().build(RouteUrl.Relation.RelationVquActivity)
                .withInt(RouteKey.TYPE, RouteKey.RelationType.VISTOR)
                .withString(RouteKey.TITLE, getString(R.string.mine_vqu_my_visitor)).navigation()
        }
        readOnceMsgRl?.setOnClickListener {
            if(tvMsgContent?.text!!.startsWith("http")){
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val targetUrl = Uri.parse(tvMsgContent?.text.toString())
                intent.data = targetUrl
                startActivity(intent)
            }
        }
        var linearLayoutManager = LinearLayoutManager(activity)
        mBinding.recyclerView.layoutManager = linearLayoutManager
        mBinding.recyclerView.setHasFixedSize(true)
        adapter.setComparator(conversationComparator)
        adapter.setHasStableIds(true)
        adapter.setCallBack(callback)
        mBinding.recyclerView.adapter = adapter
        adapter.setOnItemLongClickListener { _, _, position ->
            val isOpenChat = true
            val recent = this.adapter.getItem(position)
            if (isOpenChat) {
                showLongClickMenu(recent)
            }
            true
        }
        adapter.setOnItemClickListener { _, _, position ->
            callback.onItemClick(items[position])
        }
        mBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var pos = linearLayoutManager.findFirstVisibleItemPosition()
                if (pos > 3) {//显示回到顶部按钮
                    mBinding.ivTop.visible()
                } else {
                    mBinding.ivTop.gone()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        )
        mBinding.ivTop.setOnClickListener {
            mBinding.recyclerView.scrollToPosition(0)
        }
    }

    private fun showLongClickMenu(recent: RecentContact) {
        val topStr = if (CommonUtil.isTagSet(recent, RecentContactsFragment.RECENT_TAG_STICKY)) {
            resources.getString(R.string.message_vqu_cancel_top_chat)
        } else {
            resources.getString(R.string.message_vqu_top_chat)
        }
        context?.let {
            BottomSelectiveDialog(it)
                .isAddLine(true)
                .addSelectButton(resources.getString(R.string.message_vqu_menu_set_remark)) { _, _ ->
                    //备注
                    startARouterActivity(
                        RouteUrl.Message.MessageVquChatSettingActivity,
                        RouteKey.USERID,
                        recent.contactId.toInt()
                    )
                }
                .addSelectButton(topStr) { _, _ ->
                    //置顶
                    mViewModel.topChat(recent)
                }
                .addSelectButton(resources.getString(R.string.message_vqu_delete_chat)) { _, _ ->
                    //删除聊天
                    deleteChat(recent)
                }
                .show()
        }
    }

    /**
     * 删除聊天
     */
    private fun deleteChat(recent: RecentContact) {
        CommonHintDialog()
            .setTitle(UserInfoHelper.getUserTitleName(recent.contactId, recent.sessionType))
            .setContent(getString(R.string.message_vqu_delete_dialog_hint))
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?) {}
                override fun onRight(dialogFragment: DialogFragment?) {
                    isClearAll = false
                    // 删除会话，删除后，消息历史被一起删除
                    NIMClient.getService(MsgService::class.java)
                        .deleteRecentContact2(recent.contactId, recent.sessionType)
                    NIMClient.getService(MsgService::class.java)
                        .clearChattingHistory(recent.contactId, recent.sessionType)
                }
            }).show(childFragmentManager)
    }


    override fun initObserve() {
        mViewModel.banners.observe(this) {
            images.clear()
            if (it.data.banner.isNotEmpty()) {
                banner?.visibility = View.VISIBLE
                it.data.banner.forEach { bean ->
                    images.add(NetBaseUrlConstant.IMAGE_URL + bean.image)
                }

                banner!!.addBannerLifecycleObserver(this)
                    .setOrientation(Banner.HORIZONTAL)
                    //  .setIndicator(null, false)
                    .setLoopTime(3000)
                    .setAdapter(object : BannerImageAdapter<String>(images) {
                        override fun onBindView(
                            holder: BannerImageHolder?,
                            data: String,
                            position: Int,
                            size: Int,
                        ) {
                            holder?.imageView?.vquLoadRoundImage(data, dp2px(12f))
                            // ImageExtKt.vquLoadRoundImage(imageView, path, SizeUnitKtxKt.dp2px(context,6f));
                        }

                    }).setOnBannerListener { data, position ->
                        JumpUtils.jump(
                            it.data.banner[position].link_type,
                            it.data.banner[position].link_url,
                            activity
                        )
                    }
            } else {
//                headView?.let { head ->
//                    adapter.removeHeaderView(head)
//                }
                banner?.visibility = View.GONE
            }
        }
        mViewModel.visitorsData.observe(this, androidx.lifecycle.Observer {
            if (it.data.lookMeInfo != null) {
                var lookMeInfo = it.data.lookMeInfo
                if (lookMeInfo.newVisitorCount > 0) {
//                    if (!hasHeader) {
//                        visitorsHeadView?.let {
//                            if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
//                            } else {
//                                adapter.addHeaderView(it)
//                                hasHeader = true
//                            }
//                        }
//                    }
                    lookAtMeRl?.visibility = View.VISIBLE
                    ivHead?.vquLoadCircleImage(
                        IMAGE_URL + lookMeInfo.avatar + "?x-oss-process=image/blur,r_50,s_30",
                        R.mipmap.ic_common_head_circle_def
                    )
                    tvContent?.text = if (lookMeInfo.descriptiveCopy.isNullOrEmpty()) {
                        ""
                    } else {
                        lookMeInfo.descriptiveCopy
                    }
                    tvCount?.text = lookMeInfo.newVisitorCount.toString()
                    SpUtils.putInt(SpKey.NEW_VISITOR_COUNT, lookMeInfo.newVisitorCount)
                } else {
                    SpUtils.putInt(SpKey.NEW_VISITOR_COUNT, 0)
//                    if (hasHeader) {
//                        visitorsHeadView?.let {
//                            adapter.removeHeaderView(it)
//                            hasHeader = false
//                        }
//                    }
                    lookAtMeRl?.visibility = View.GONE
                }
            } else {
//                if (hasHeader) {
//                    visitorsHeadView?.let {
//                        SpUtils.putInt(SpKey.NEW_VISITOR_COUNT, 0)
//                        adapter.removeHeaderView(it)
//                        hasHeader = false
//                    }
//                }
                SpUtils.putInt(SpKey.NEW_VISITOR_COUNT, 0)
                lookAtMeRl?.visibility = View.GONE
            }
            val unreadNum = NIMClient.getService(MsgService::class.java).totalUnreadCount
            EventBus.getDefault().post(
                UnReadCountEvent(
                    unreadNum
                )
            )
        })
        NIMClient.getService(EventSubscribeServiceObserver::class.java)
            .observeEventChanged(Observer {
                adapter.updateOnlineState(it)
            }, true)
        mViewModel.isClearMsg.observe(this) {
            if (it) {
                mViewModel.resetClearStatue()
                mViewModel.getVisitorsData()
                mViewModel.getNotifyMsg()
                SpUtils.putInt(SpKey.NEW_VISITOR_COUNT, 0)
                EventBus.getDefault().post(
                    UnReadCountEvent(
                        0
                    )
                )
            }
        }
        mViewModel.notifyMsgData.observe(this){
            if (it.data != null) {
                readOnceMsgRl?.visibility = View.VISIBLE
                ivMsgHead?.vquLoadCircleImage(
                    IMAGE_URL + it.data.avatar + "?x-oss-process=image/blur,r_50,s_30",
                    R.mipmap.ic_common_head_circle_def
                )
                tvMsg?.text = it.data.nickname
                tvMsgContent?.text = it.data.content
            } else {
                readOnceMsgRl?.visibility = View.GONE
            }
        }
    }

    override fun initRequestData() {
        mViewModel.getBannerData()
        mViewModel.getVisitorsData()
        mViewModel.getNotifyMsg()
        getConversationList()
    }

    override fun onDestroy() {
        super.onDestroy()
        registerObservers(false)
    }

    private fun getConversationList() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            // 查询最近联系人列表数据
            NIMClient.getService(MsgService::class.java)
                .queryRecentContacts(MsgTypeEnum.tip)
                .setCallback(object : RequestCallbackWrapper<List<RecentContact>?>() {
                    override fun onResult(
                        code: Int,
                        recents: List<RecentContact>?,
                        exception: Throwable?,
                    ) {
                        finishLoad()
                        if (code != ResponseCode.RES_SUCCESS.toInt() || recents == null) {
                            return
                        }
                        if (lastRecentContact == null) {
                            items.clear()
                        }
                        if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
                            recents.forEach {
                                if (it.contactId.toInt() == 5) {
                                    items.add(it)
                                }
                            }
                        } else {
                            items.addAll(recents)
                        }
                        Collections.sort(items, conversationComparator)
                        adapter.notifyDataSetChanged()
                        registerStatusObservers()
                        setUnReadCount()
                    }
                })
        }, 250)
    }

    var isAutoClear: Boolean = false
    private fun autoClear() {
        if (isAutoClear) {
            return
        }
        var clearList = mutableListOf<RecentContact>()
        var userId = UserManager.userInfo?.userId
        if (userId.isNullOrEmpty()) {
            return
        }
        var lastTime = SpUtils.getLong(userId + "clearTime", 0)
        if (System.currentTimeMillis() - lastTime!! >= time) {//如果大于24小时 就清除24小时以前的消息

            items.map {
                if (it.time <= (System.currentTimeMillis() - time)) {//判断是不是24小时之前的消息
                    if (IntimateUtils.getInstance().findData(it.contactId.toInt()) != null
                        && IntimateUtils.getInstance()
                            .findData(it.contactId.toInt()).score != "0"
                    ) {//亲密度大于0的
                    } else {
                        if (it.contactId.toInt() != 100091 && it.contactId.toInt() != 2) {//100091是客服消息
                            NIMClient.getService(MsgService::class.java)
                                .deleteRecentContact(it)
                            NIMClient.getService(MsgService::class.java)
                                .clearChattingHistory(it.contactId, it.sessionType)
                            clearList.add(it)
                        }
                    }

                }

            }
        }
        if (!clearList.isNullOrEmpty()) {
            "已为您自动清除无效消息".toast()
            isAutoClear = true
            SpUtils.putLong(userId + "clearTime", System.currentTimeMillis())
            clearLikeList(clearList)
        }
    }

    private fun clearLikeList(clearList: MutableList<RecentContact>) {
        var idsList = mutableListOf<String>()
        clearList.map {
            idsList.add(it.contactId)
        }
        val spliceArrays = spliceArrays(idsList, 2000)
        spliceArrays?.map {
            mViewModel.vquDelBeckons(Gson().toJson(it))
        }
        var eventSubscribeRequest = EventSubscribeRequest()
        eventSubscribeRequest.eventType = 1
        eventSubscribeRequest.publishers = idsList
        mViewModel.vquVisitorList()
        NIMClient.getService(EventSubscribeService::class.java)
            .unSubscribeEvent(eventSubscribeRequest)
    }

    private fun <String> spliceArrays(datas: List<String>?, splitSize: Int): List<List<String>>? {
        if (datas == null || splitSize < 1) {
            return null
        }
        val totalSize = datas.size
        val count =
            if (totalSize % splitSize == 0) totalSize / splitSize else totalSize / splitSize + 1
        val rows: MutableList<List<String>> = ArrayList()
        for (i in 0 until count) {
            val cols = datas.subList(
                i * splitSize,
                if (i == count - 1) totalSize else splitSize * (i + 1)
            )
            rows.add(cols)
        }
        return rows
    }

    private val conversationComparator: java.util.Comparator<RecentContact> =
        Comparator { o1, o2 -> // 先比较置顶tag
            val sticky =
                (o1.tag and RecentContactsFragment.RECENT_TAG_STICKY) - (o2.tag and RecentContactsFragment.RECENT_TAG_STICKY)
            if (sticky != 0L) {
                if (sticky > 0) -1 else 1
            } else {
                val time = o1.time - o2.time
                if (time == 0L) 0 else if (time > 0) -1 else 1
            }
        }

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private fun registerObservers(register: Boolean) {
        val service = NIMClient.getService(MsgServiceObserve::class.java)
        service.observeRecentContact(messageObserver, register)
        service.observeMsgStatus(statusObserver, register)
        service.observeRecentContactDeleted(deleteObserver, register)
//        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register)
        if (register) {
            registerUserInfoObserver()
        } else {
            unregisterUserInfoObserver()
        }
    }

    private fun registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver =
                UserInfoObserver { getConversationList() }
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true)
    }

    private fun unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false)
        }
    }

    private val statusObserver: Observer<IMMessage> = Observer {
        val index: Int = getItemIndex(it.uuid)
        if (index >= 0 && index < items.size) {
            val item = items[index]
            item.msgStatus = it.status
            refreshViewHolderByIndex(index)
        }
        setUnReadCount()
    }

    private fun refreshViewHolderByIndex(index: Int) {
        activity?.runOnUiThread {
            adapter.notifyItemChanged(index + 1)
        }
    }

    private fun getItemIndex(uuid: String): Int {
        for (i in items.indices) {
            val item = items[i]
            if (TextUtils.equals(item.recentMessageId, uuid)) {
                return i
            }
        }
        return -1
    }

    private val deleteObserver: Observer<RecentContact> = Observer { delItem ->
        if (delItem != null) {
            if (isClearAll) {
                delCount += 1
                if (imCount == delCount) {
                    loadingDialog?.dismiss()
                    getConversationList()
                }
            } else {
                for (i in items.indices) {
                    if (TextUtils.equals(delItem.contactId, items[i].contactId)
                        && delItem.sessionType == items[i].sessionType
                    ) {
                        adapter.remove(i)
                        break
                    }
                }
            }
        }
    }

    private fun registerStatusObservers() {
        var idList: MutableList<String> = mutableListOf()
        items.map {
            idList.add(it.contactId)
        }
        var eventSubscribeRequest = EventSubscribeRequest()
        eventSubscribeRequest.eventType = 1
        eventSubscribeRequest.expiry = 60 * 60 * 24
        eventSubscribeRequest.isSyncCurrentValue = true
        eventSubscribeRequest.publishers = idList

        NIMClient.getService(EventSubscribeService::class.java)
            .subscribeEvent(eventSubscribeRequest)
    }


    private val messageObserver: Observer<MutableList<RecentContact>> = Observer {
        Log.e("onRecentContactChanged", "messageObserver:${it.size}")
        if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
            var list = mutableListOf<RecentContact>()
            list.clear()
            it.forEach {
                if (it.contactId.toInt() == 5) {
                    list.add(it)
                }
            }
            adapter.update(list)
        } else {
            adapter.update(it)
        }
        registerStatusObservers()
        setUnReadCount()
    }

    private fun setUnReadCount() {
        var myUnreadConversationList = mutableListOf<RecentContact>()
        if (items.isNullOrEmpty()) {
            BadgerNum.setBadgerNum(BaseApplication.context, 0)
            return
        }
        var unreadNum = 0
        items.forEach {
            unreadNum += it.unreadCount
            if (it.unreadCount > 0) {
                if (it.contactId != "2" && it.fromAccount != UserManager.userInfo?.userId) {
                    myUnreadConversationList.add(it)
                }

            }
        }
        appViewModel.unreadConversationList.value = myUnreadConversationList
        callback.onUnreadCountChange(unreadNum)
        BadgerNum.setBadgerNum(BaseApplication.context, unreadNum)
    }

    private val callback: RecentContactsCallback by lazy {
        object : RecentContactsCallback {

            override fun onRecentContactsLoaded() {
            }

            override fun onUnreadCountChange(unreadCount: Int) {
                EventBus.getDefault().post(
                    UnReadCountEvent(
                        unreadCount
                    )
                )
            }

            override fun onItemClick(recent: RecentContact?) {
                if (recent?.sessionType == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(activity, recent.contactId)
                } else if (recent?.sessionType == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(activity, recent.contactId)
                }
            }

            override fun getDigestOfAttachment(
                recent: RecentContact?,
                attachment: MsgAttachment?,
            ): String? {
                return null
            }

            override fun getDigestOfTipMsg(recent: RecentContact?): String? {
//                recent.content
                return null
            }

        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        lastRecentContact = null
        mViewModel.getBannerData()
        mViewModel.getVisitorsData()
        mViewModel.getNotifyMsg()
        getConversationList()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        getConversationList()
    }

    fun finishLoad() {
        try {
            if (mBinding != null) {
                mBinding.smart.finishRefresh()
            }
        } catch (e: Exception) {
            Log.i("err", e.message.toString())
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: String) {
        if ("ImLoginSuccess" == event) {//IM登录成功
            getConversationList()
        } else if ("MsgSelect" == event) {
            autoClear()//自动清除消息
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: IsShowGuideEvent) {
        mViewModel.getVisitorsData()
        mViewModel.getNotifyMsg()
        getConversationList()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: DeleteRecentContactEvent) {
        isClearAll = true
        delCount = 0
        imCount = 0
        var clearList = mutableListOf<RecentContact>()
        var delItems = mutableListOf<RecentContact>()
        delItems.addAll(items)
        delItems.forEach {
            if (!(IntimateUtils.getInstance().findData(it.contactId.toInt()) != null
                        && IntimateUtils.getInstance().findData(it.contactId.toInt()).grade > 1)
            ) {
                imCount += 1
                if (loadingDialog?.isShowing == false) {
                    loadingDialog?.show()
                }
                NIMClient.getService(MsgService::class.java)
                    .deleteRecentContact2(it.contactId, it.sessionType)
                NIMClient.getService(MsgService::class.java)
                    .clearChattingHistory(it.contactId, it.sessionType)
                clearList.add(it)
            }
        }
        clearLikeList(clearList)
    }
}