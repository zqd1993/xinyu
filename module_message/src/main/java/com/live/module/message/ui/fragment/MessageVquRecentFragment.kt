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
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.message.R
import com.live.module.message.adapter.MessageVquRecentAdapter
import com.live.module.message.databinding.MessageTantaRecentFragmentBinding
import com.live.module.message.recentholder.RecentContactsCallback
import com.live.module.message.vm.MessageVquRecentFragmentViewModel
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.UnReadCountEvent
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.utils.JumpUtils
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.api.model.contact.ContactChangedObserver
import com.mshy.VInterestSpeed.uikit.api.model.user.UserInfoObserver
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment.RECENT_TAG_STICKY
import com.mshy.VInterestSpeed.uikit.business.uinfo.UserInfoHelper
import com.mshy.VInterestSpeed.uikit.common.badger.BadgerNum
import com.mshy.VInterestSpeed.uikit.event.DeleteRecentContactEvent
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
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.youth.banner.Banner

import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * FileName: com.live.module.message
 * Author: Reisen
 * Date: 2022/4/9 10:54
 * History:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquRecentFragment)
@Deprecated(message = "效率过低废弃")
class MessageVquRecentFragment :
    BaseLazyFrameFragment<MessageTantaRecentFragmentBinding, MessageVquRecentFragmentViewModel>() {

    override val mViewModel: MessageVquRecentFragmentViewModel by viewModels()

    private var isIntimate = false

    private var items: MutableList<RecentContact> = mutableListOf()

    private var loadedRecent: MutableList<RecentContact> = mutableListOf()

    private lateinit var adapter: MessageVquRecentAdapter

    private var msgLoaded = false

    private var userInfoObserver: UserInfoObserver? = null

    private var banner: Banner<String, BannerImageAdapter<String>>? = null

    private val images: MutableList<String> = mutableListOf()

    private var headView: View? = null


    companion object {
        fun newInstance(intimate: Boolean = false): MessageVquRecentFragment {
            val args = Bundle()
            args.putBoolean("intimate", intimate)
            val fragment = MessageVquRecentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun MessageTantaRecentFragmentBinding.initView() {
        isIntimate = arguments?.getBoolean("intimate") == true
        initMessageList()
        mBinding.includeIntimateEmpty.tvChat.setViewClickListener {
            //发送跳转事件
            EventBus.getDefault().post(MessageVquCurrentItemEvent(
                0))
        }
    }


    private fun initMessageList() {
        adapter = MessageVquRecentAdapter(items, isIntimate)
        adapter.setCallBack(callback)
        if (!isIntimate) {
            adapter.headerWithEmptyEnable = true
            //添加空布局
            val empty = layoutInflater.inflate(R.layout.message_tanta_msg_empty, null, false)
            adapter.setEmptyView(empty)
            headView = layoutInflater.inflate(R.layout.message_tanta_include_banner, null, false)
            banner = headView?.findViewById(R.id.vqu_banner_ad)
            headView?.let {
                adapter.addHeaderView(it)
            }
        }
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        mBinding.recyclerView.setHasFixedSize(true)
//        mBinding.recyclerView.itemAnimator = null //去除item动画
        (mBinding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
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

    }


    override fun onDestroy() {
        super.onDestroy()
        registerObservers(false)
    }

    override fun initObserve() {
        mViewModel.banners.observe(this) {
            images.clear()
            if (it.data.banner.isNotEmpty()) {
                it.data.banner.forEach { bean ->
                    images.add(NetBaseUrlConstant.IMAGE_URL + bean.image)
                }
                Log.e("MessageVqu", "initObserve: ${images.size}")
                Log.e("MessageVqu", "initObserve: $banner")

                banner!!.addBannerLifecycleObserver(this)
                    .setOrientation(Banner.HORIZONTAL)
                    //  .setIndicator(null, false)
                    .setLoopTime(3000)
                    .setAdapter(object : BannerImageAdapter<String>(images) {
                        override fun onBindView(
                            holder: BannerImageHolder?,
                            data: String,
                            position: Int,
                            size: Int
                        ) {
                            holder?.imageView?.vquLoadRoundImage(data, dp2px(6f))
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
                headView?.let { head ->
                    adapter.removeHeaderView(head)
                }
            }
        }

        mViewModel.isGetIntimate.observe(this){
            requestMessages(true)
            registerObservers(true)
        }
    }

    override fun initRequestData() {
        if (!isIntimate) {
            mViewModel.getBannerData()
        }
        mViewModel.vquQueryIntimateList()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun notifyDataSetChanged() {
        showIntimateListEmpty()
        adapter.notifyDataSetChanged()
    }

    /**
     * 判断是否显示亲密度空布局
     */
    private fun showIntimateListEmpty() {
        if (isIntimate) {
            var count = 0
            items.forEach {
                if (IntimateUtils.getInstance().isNeedShow(it.contactId.toInt())) {
                    count++
                    return@forEach
                }
            }
            mBinding.includeIntimateEmpty.llEmpty.visibility =
                (if (count == 0 && msgLoaded) View.VISIBLE else View.GONE)
        }
    }


    private val callback: RecentContactsCallback by lazy {
        object : RecentContactsCallback {

            override fun onRecentContactsLoaded() {
            }

            override fun onUnreadCountChange(unreadCount: Int) {
                EventBus.getDefault().post(UnReadCountEvent(
                    unreadCount))
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
                attachment: MsgAttachment?
            ): String? {
                return null
            }

            override fun getDigestOfTipMsg(recent: RecentContact?): String? {
                return null
            }

        }
    }


    private fun showLongClickMenu(recent: RecentContact) {
        context?.let {
            BottomSelectiveDialog(it)
                .isAddLine(true)
                .addSelectButton(resources.getString(R.string.message_vqu_delete_chat)) { _, _ ->
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
            // 查询最近联系人列表数据
            NIMClient.getService(MsgService::class.java).queryRecentContacts()
                .setCallback(object : RequestCallbackWrapper<List<RecentContact>?>() {
                    override fun onResult(
                        code: Int,
                        recents: List<RecentContact>?,
                        exception: Throwable?
                    ) {
                        Log.e("onRecentContactChanged", "onResult: $code--->${recents?.size}")
                        if (code != ResponseCode.RES_SUCCESS.toInt() || recents == null) {
                            return
                        }
                        var count = 0
                        loadedRecent.addAll(recents)
                        loadedRecent.forEach {
                            if (IntimateUtils.getInstance().isNeedShow(it.contactId.toInt())) {
                                count++
                            }
                        }
                        if (isIntimate) {
                            mBinding.includeIntimateEmpty.llEmpty.visibility =
                                (if (count == 0) View.VISIBLE else View.GONE)
                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        msgLoaded = true
                        if (isAdded) {
                            onRecentContactsLoaded()
                        }
                    }
                })
        }, if (delay) 250 else 0)
    }


    /**
     * 初次加载
     */
    private fun onRecentContactsLoaded() {
        items.clear()
        items.addAll(loadedRecent)
        loadedRecent.clear()
        refreshMessages(
            true,
            mBinding.includeIntimateEmpty.llEmpty.visibility == View.GONE
        )
        callback.onRecentContactsLoaded()
    }

    private fun refreshMessages(
        unreadChanged: Boolean,
        isRefreshIntimate: Boolean = true,
        isDelete: Boolean = false
    ) {
        if (!isDelete) {
            sortRecentContacts(items)
            if (!isIntimate || isRefreshIntimate) {
                notifyDataSetChanged()
            }
        }
        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）
            var unreadNum = 0
            items.forEach {
                unreadNum += it.unreadCount
            }

            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            callback.onUnreadCountChange(unreadNum)
            BadgerNum.setBadgerNum(BaseApplication.context,unreadNum)
//            Badger.updateBadgerCount(unreadNum)
        }
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
        onRecentContactChanged(it)
    }

    //1 空闲 2刷新界面中 3有等待刷新界面中
    private var refreshStatus = 1;

    private fun onRecentContactChanged(recentContacts: List<RecentContact>) {
        val currentTime = System.currentTimeMillis()
        var index: Int
        //是否需要刷新亲密列表
        var isRefreshIntimate = false
        for (j in recentContacts.indices) {
            val r = recentContacts[j]
            index = -1
            for (i in items.indices) {
                if (isIntimate) {
                    if (IntimateUtils.getInstance()
                            .isNeedShow(r.contactId.toInt())
                    ) {
                        isRefreshIntimate = true
                    }
                }
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
                Log.e("onRecentContactChanged", "正在处理${isIntimate}")
                refreshMessages(true, isRefreshIntimate)
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
                Log.e("onRecentContactChanged", "刷新中${isIntimate}")
                return
            }
            3 -> {
                Log.e("onRecentContactChanged", "等待刷新${isIntimate}")
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
                    refreshMessages(
                        true,
                        isRefreshIntimate = true,
                        isDelete = true
                    )
                    break
                }
            }
            showIntimateListEmpty()
        } else {
            items.clear()
            refreshMessages(true)
        }
    }


    private val friendDataChangedObserver: ContactChangedObserver =
        object :
            ContactChangedObserver {
            override fun onAddedOrUpdatedFriends(accounts: MutableList<String>?) {
                refreshMessages(false)
            }

            override fun onDeletedFriends(accounts: MutableList<String>?) {
                refreshMessages(false)
            }

            override fun onAddUserToBlackList(accounts: MutableList<String>?) {
                refreshMessages(false)
            }

            override fun onRemoveUserFromBlackList(accounts: MutableList<String>?) {
                refreshMessages(false)
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
            if (!isIntimate) {
                adapter.notifyItemChanged(index + 1)
            } else {
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver =
                UserInfoObserver {
                    refreshMessages(false)
                }
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true)
    }

    private fun unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
           NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false)
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


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEventMainThread(event: IsShowGuideEvent) {
//        val msgGuideIsShowed = SpUtils.getBoolean(SpKey.msg_guide_is_showed, false)
//        if (!isIntimate && msgGuideIsShowed == false && UserManager.userInfo?.gender == 1) {
//            mBinding.recyclerView.postDelayed({
//                val location = IntArray(2)
//                mBinding.recyclerView.getLocationOnScreen(location) //获取在整个屏幕内的绝对坐标，含statusBar
//                val event = GuideEvent()
//                event.type = GuideEvent.TYPE_MESSAGE
//                event.params = HashMap()
//                event.params["y"] = location[1]
//                EventBus.getDefault().post(event)
//            }, 200)
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: DeleteRecentContactEvent) {
        items.forEach {
            if (!(IntimateUtils.getInstance().findData(it.contactId.toInt()) != null
                        && IntimateUtils.getInstance().findData(it.contactId.toInt()).grade > 1)
            ) {
                NIMClient.getService(MsgService::class.java)
                    .deleteRecentContact2(it.contactId, it.sessionType)
                NIMClient.getService(MsgService::class.java)
                    .clearChattingHistory(it.contactId, it.sessionType)
            }
        }
    }
}