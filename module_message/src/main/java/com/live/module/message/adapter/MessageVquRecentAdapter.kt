package com.live.module.message.adapter

import android.provider.Settings
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.message.R
import com.live.module.message.recentholder.CommonRecentViewHolder
import com.live.module.message.recentholder.RecentContactsCallback
import com.live.module.message.utils.OnlineMapUtil
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.ui.view.notification.SwipeMessageNotificationManager
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment.RECENT_TAG_STICKY
import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.event.model.Event
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact


/**
 * FileName: com.live.module.message.adapter
 * Date: 2022/4/18 16:10
 * Description:
 * History:
 */
class MessageVquRecentAdapter(
    data: MutableList<RecentContact>? = null,
    var isIntimate: Boolean = false,
) :
    BaseQuickAdapter<RecentContact, BaseViewHolder>(
        R.layout.message_tanta_recent_contact_item,
        data
    ) {
    var mWindowManager: SwipeMessageNotificationManager? = null
    private var dataComparator: Comparator<RecentContact>? = null
    private val viewHolder: CommonRecentViewHolder = CommonRecentViewHolder()

    override fun convert(holder: BaseViewHolder, item: RecentContact) {
        viewHolder.convert(context, holder, item)
        if (isIntimate) {
            if (IntimateUtils.getInstance().isNeedShow(item.contactId.toInt())) {
                holder.itemView.layoutParams.height = context.dp2px(65f)
                Log.e("MessageVquRecentAdapter", "convert: ")
            } else {
                holder.itemView.layoutParams.height = 0
                Log.e("MessageVquRecentAdapter", "convert: ${holder.itemView.layoutParams.height}")
            }
        }
    }

    fun setCallBack(callback: RecentContactsCallback) {
        viewHolder.setCallBack(callback)
    }

    fun update(recentContacts: MutableList<RecentContact>?) {
        recentContacts?.map {
            update(it)
        }
    }

    fun updateOnlineState(events: MutableList<Event>?) {
        events?.map {
            OnlineMapUtil.putOnline(it.publisherAccount, it)
            update(it)
        }
    }

    private fun update(event: Event) {
        for (j in data.indices) {
            if (event.publisherAccount == data[j].contactId) {
                var map = HashMap<String, Any>()
                map.put("online", event.eventValue)
                data[j].extension = map
                notifyDataSetChanged()
                break
            }
        }
    }

    fun update(recentContact: RecentContact?) {
        var des = descOfMsg(recentContact!!)
        if (des?.contains("无法获得该奖励", true) == true || des?.contains(
                "提示消息",
                true
            ) == true || recentContact.msgType == MsgTypeEnum.tip
        ) {

        } else {
            if (Settings.canDrawOverlays(BaseApplication.context)) {
                if (mWindowManager == null) {
                    mWindowManager =
                        SwipeMessageNotificationManager.getInstance(BaseApplication.application)
                }
                mWindowManager?.addNotification(recentContact)
            }
        }


        var removeIndex = -1
        for (j in data.indices) {
            if (recentContact!!.contactId.equals(data[j].contactId)) {
                removeIndex = j
                break
            }
        }
        if (removeIndex > -1) {
            data.removeAt(removeIndex)
            val insertIndex = searchComparatorIndex(recentContact)
            data.add(insertIndex, recentContact!!)
            notifyDataSetChanged()
        } else {
            val insertIndex = searchComparatorIndex(recentContact)
            data.add(insertIndex, recentContact!!)
            notifyDataSetChanged()
        }

    }

    private fun searchComparatorIndex(recentContact: RecentContact?): Int {
        var index: Int = data.size
        // add stick must be insert 0
        if (recentContact?.tag == RECENT_TAG_STICKY) {
            return 0
        }
        for (i in data.indices) {
            if (dataComparator != null && dataComparator!!.compare(
                    recentContact,
                    data.get(i)
                ) < 1
            ) {
                index = i
                break
            }
        }
        return index
    }

    fun descOfMsg(recent: RecentContact): String? {
        if (recent.msgType == MsgTypeEnum.text) {
            return recent.content
        } else if (recent.msgType == MsgTypeEnum.tip) {
            var digest: String? = null
            if (digest == null) {
                digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent)
            }
            return digest
        } else if (recent.attachment != null) {
            var digest: String? = null
            if (digest == null) {
                digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent)
            }
            return digest
        }
        return "[未知]"
    }

    fun setComparator(comparator: Comparator<RecentContact>) {
        dataComparator = comparator
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}