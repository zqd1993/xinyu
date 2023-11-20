package com.live.module.message.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Gravity.*
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.message.R
import com.live.module.message.adapter.MessageVquRecentAdapter
import com.live.module.message.databinding.MessageTantaRecentFragmentBinding
import com.live.module.message.recentholder.RecentContactsCallback
import com.live.module.message.vm.MessageVquRecentFragmentViewModel
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.helper.startARouterActivity
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.api.model.contact.ContactChangedObserver
import com.mshy.VInterestSpeed.uikit.api.model.user.UserInfoObserver
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment.RECENT_TAG_STICKY
import com.mshy.VInterestSpeed.uikit.business.uinfo.UserInfoHelper
import com.mshy.VInterestSpeed.uikit.common.util.sys.TimeUtil
import com.mshy.VInterestSpeed.uikit.event.MessageVquCurrentItemEvent
import com.mshy.VInterestSpeed.uikit.event.NotificationIntimateChangeEvent
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.Observer
import com.netease.nimlib.sdk.RequestCallbackWrapper
import com.netease.nimlib.sdk.ResponseCode
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.msg.model.RecentSessionList
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * FileName: com.live.module.message
 * Author: Reisen
 * Date: 2022/4/9 10:54
 * Description: 亲密fragment
 * History:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquRecentIntimateFragment)
class MessageVquRecentIntimateFragment :
    BaseLazyFrameFragment<MessageTantaRecentFragmentBinding, MessageVquRecentFragmentViewModel>() {

    override val mViewModel: MessageVquRecentFragmentViewModel by viewModels()

    private var items: MutableList<RecentContact> = mutableListOf()

    private var loadedRecent: MutableList<RecentContact> = mutableListOf()

    private lateinit var adapter: MessageVquRecentAdapter

    private var msgLoaded = false

    private var userInfoObserver: UserInfoObserver? = null


    companion object {
        fun newInstance(): MessageVquRecentIntimateFragment {
            val args = Bundle()
            val fragment = MessageVquRecentIntimateFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun MessageTantaRecentFragmentBinding.initView() {
        initMessageList()
        mBinding.smart.setEnableRefresh(true)
        mBinding.smart.setEnableLoadMore(false)
        mBinding.smart.setOnRefreshListener {
            initMessageList()
        }
    }


    private fun initMessageList() {
        adapter = MessageVquRecentAdapter(items)
        adapter.setCallBack(callback)
        adapter.setHasStableIds(true)
        mBinding.recyclerView.itemAnimator = null
        adapter.headerWithEmptyEnable = true
        //添加空布局
        val empty = layoutInflater.inflate(R.layout.message_tanta_include_intimate_empty, null, false)
        empty.findViewById<View>(R.id.ll_empty).visible()
        empty.findViewById<View>(R.id.tv_chat).setViewClickListener {
            //发送跳转事件
            EventBus.getDefault().post(MessageVquCurrentItemEvent(
                0))
        }
        adapter.setEmptyView(empty)
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        mBinding.recyclerView.setHasFixedSize(true)
//        (mBinding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        adapter.setOnItemClickListener { _, _, position ->
            callback.onItemClick(items[position])
        }

        adapter.setOnItemLongClickListener { _, _, position ->
            val isOpenChat = true
            val recent = this.adapter.getItem(position)
            if (isOpenChat) {
                showLongClickMenu(recent)
            }
            true
        }
        mBinding.smart.finishRefresh()
    }


    override fun onDestroy() {
        super.onDestroy()
        registerObservers(false)
    }

    override fun initObserve() {

        mViewModel.isGetIntimate.observe(this) {
            requestMessages(true)
            registerObservers(true)
        }
    }

    override fun initRequestData() {
        mViewModel.vquQueryIntimateList()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
    }


    private val callback: RecentContactsCallback by lazy {
        object : RecentContactsCallback {

            override fun onRecentContactsLoaded() {
            }

            override fun onUnreadCountChange(unreadCount: Int) {
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
                return null
            }

        }
    }


    private fun showLongClickMenu(recent: RecentContact) {
        val topStr = if (SpUtils.getBoolean(recent.contactId, false) == false) {
            resources.getString(R.string.message_vqu_top_chat)
        } else {
            resources.getString(R.string.message_vqu_cancel_top_chat)
        }
        context?.let {
            BottomSelectiveDialog(it)
                .isAddLine(true)
                .addSelectButton(resources.getString(R.string.message_vqu_menu_set_remark)) { _, _ ->
                    //备注
                    showRemarkDialog(recent)
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


    private fun requestMessages(delay: Boolean) {
        if (msgLoaded) {
            return
        }
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            if (msgLoaded) {
                return@Runnable
            }
            NIMClient.getService(MsgService::class.java).queryMySessionList(0, TimeUtil.currentTimeMillis(), 1, 100, 0)
                .setCallback(object : RequestCallbackWrapper<RecentSessionList>() {
                    override fun onResult(
                        code: Int,
                        result: RecentSessionList?,
                        exception: Throwable?
                    ) {
                        Log.e("onRecentContactChanged", "onResult: $code=code -->result=${result}")
                        if (code != ResponseCode.RES_SUCCESS.toInt() || result == null) {
                            return
                        }
                        if(result.sessionList.size == 0){
                            return
                        }
                        result.sessionList.forEach {
                            if (IntimateUtils.getInstance().isNeedShow(it.toRecentContact().contactId.toInt())) {
                                if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
                                    if (it.toRecentContact().contactId.toInt() == 4) {
                                        loadedRecent.add(it.toRecentContact())
                                    }
                                } else {
                                    loadedRecent.add(it.toRecentContact())
                                }

                            }
                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        msgLoaded = true
                        if (isAdded) {
                            onRecentContactsLoaded()
                        }
                    }
                })
            // 查询最近联系人列表数据
//            NIMClient.getService(MsgService::class.java).queryRecentContacts(MsgTypeEnum.tip)
//                .setCallback(object : RequestCallbackWrapper<List<RecentContact>?>() {
//                    override fun onResult(
//                        code: Int,
//                        recents: List<RecentContact>?,
//                        exception: Throwable?,
//                    ) {
//                        Log.e("onRecentContactChanged", "onResult: $code--->${recents?.size}")
//                        if (code != ResponseCode.RES_SUCCESS.toInt() || recents == null) {
//                            return
//                        }
//                        recents.forEach {
//                            if (IntimateUtils.getInstance().isNeedShow(it.contactId.toInt())) {
//                                if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
//                                    if (it.contactId.toInt() == 4) {
//                                        loadedRecent.add(it)
//                                    }
//                                } else {
//                                    loadedRecent.add(it)
//                                }
//
//                            }
//                        }
//                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
//                        msgLoaded = true
//                        if (isAdded) {
//                            onRecentContactsLoaded()
//                        }
//                    }
//                })
        }, if (delay) 250 else 0)
    }


    /**
     * 初次加载
     */
    private fun onRecentContactsLoaded() {
        items.clear()
        items.addAll(loadedRecent)
        loadedRecent.clear()
        refreshMessages()
        callback.onRecentContactsLoaded()
    }

    private fun refreshMessages() {
        sortRecentContacts(items)
        notifyDataSetChanged()
    }


    /**
     * **************************** 排序 ***********************************
     */
    @Synchronized
    private fun sortRecentContacts(list: MutableList<RecentContact>) {
        if (list.size == 0) {
            return
        }
        Collections.sort(list, comp)
    }

    private val comp: Comparator<RecentContact> =
        Comparator { o1, o2 -> // 先比较置顶tag
            val sticky = (o1.tag and RECENT_TAG_STICKY) - (o2.tag and RECENT_TAG_STICKY)
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
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register)
        if (register) {
            registerUserInfoObserver()
        } else {
            unregisterUserInfoObserver()
        }
    }


    private val messageObserver: Observer<List<RecentContact>> = Observer {
        Log.e("onRecentContactChanged", "messageObserver:${it.size}")
        val cacheRecent = mutableListOf<RecentContact>()
        it.forEach { recent ->
            if (IntimateUtils.getInstance().isNeedShow(recent.contactId.toInt())) {
                if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
                    if (recent.contactId.toInt() == 4) {
                        cacheRecent.add(recent)
                    }
                } else {
                    cacheRecent.add(recent)
                }
            }
        }
        onRecentContactChanged(cacheRecent)
    }

    //1 空闲 2刷新界面中 3有等待刷新界面中
    private var refreshStatus = 1;

    private fun onRecentContactChanged(recentContacts: List<RecentContact>) {
        val currentTime = System.currentTimeMillis()
        var index: Int
        for (j in recentContacts.indices) {
            val r = recentContacts[j]
            index = -1
            for (i in items.indices) {
                if (r.contactId == items[i].contactId && r.sessionType == items[i].sessionType) {
                    index = i
                    break
                }
            }
            if (index >= 0) {
                items.removeAt(index)
            }
            items.add(r)
        }
        Log.e("onRecentContactChanged", "循环耗时${System.currentTimeMillis() - currentTime}")
        when (refreshStatus) {
            1 -> {
                //更改状态为刷新界面中
                refreshStatus = 2
                refreshMessages()
                if (refreshStatus == 3) {
                    //先变为空闲然后进行再次刷新
                    refreshStatus = 1
                    //如果处于有等待刷新的状态 再次刷新
                    onRecentContactChanged(recentContacts)
                } else {
                    refreshStatus = 1
                }
            }
            2 -> {
                //处于正在刷新状态将状态码转化为等待刷新
                refreshStatus = 3
                return
            }
            3 -> {
                return
            }
        }

    }


    private val statusObserver: Observer<IMMessage> = Observer {
        val index: Int = getItemIndex(it.uuid)
        if (index >= 0 && index < items.size) {
            val item = items[index]
            item.msgStatus = it.status
            refreshViewHolderByIndex(index)
        }
    }

    private val deleteObserver: Observer<RecentContact> = Observer {
        Log.e("MessageVquRecent", "deleteObserver: ")
        if (it != null) {
            for (i in items.indices) {
                val item = items[i]
                if (TextUtils.equals(item.contactId, it.contactId)
                    && item.sessionType == it.sessionType
                ) {
//                    items.remove(item)
                    adapter.remove(item)
                    refreshMessages()
                    break
                }
            }
        } else {
            items.clear()
        }
        refreshMessages()
    }


    private val friendDataChangedObserver: ContactChangedObserver =
        object :
            ContactChangedObserver {
            override fun onAddedOrUpdatedFriends(accounts: MutableList<String>?) {
                refreshMessages()
            }

            override fun onDeletedFriends(accounts: MutableList<String>?) {
                refreshMessages()
            }

            override fun onAddUserToBlackList(accounts: MutableList<String>?) {
                refreshMessages()
            }

            override fun onRemoveUserFromBlackList(accounts: MutableList<String>?) {
                refreshMessages()
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

    private fun refreshViewHolderByIndex(index: Int) {
        activity?.runOnUiThread {
            adapter.notifyItemChanged(index)
        }
    }

    private fun registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver =
                UserInfoObserver { refreshMessages() }
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true)
    }

    private fun unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false)
        }
    }

    private fun showRemarkDialog(recent: RecentContact) {
        startARouterActivity(
            RouteUrl.Message.MessageVquChatSettingActivity,
            RouteKey.USERID,
            recent.contactId.toInt()
        )
    }


    /**
     * 删除聊天
     */
    private fun deleteChat(recent: RecentContact) {
        CommonHintDialog()
            .setTitle(UserInfoHelper.getUserTitleName(recent.contactId, recent.sessionType))
            .setContent(getString(R.string.message_vqu_delete_dialog_hint))
            .setContentSize(15)
            .setContentGravity(CENTER)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?) {}
                override fun onRight(dialogFragment: DialogFragment?) {
                    // 删除会话，删除后，消息历史被一起删除
                    NIMClient.getService(MsgService::class.java)
                        .deleteRecentContact2(recent.contactId, recent.sessionType)
                    NIMClient.getService(MsgService::class.java)
                        .clearChattingHistory(recent.contactId, recent.sessionType)
                }
            }).show(childFragmentManager)
    }

    /***********************************Event事件****************************************/
    /**
     * 亲密值变化通知
     *
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
            IntimateUtils.getInstance().putData(
                userId,
                event.data.data.score, event.data.data.grade.toInt()
            )
        }
    }

}