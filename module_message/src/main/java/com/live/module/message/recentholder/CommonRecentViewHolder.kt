package com.live.module.message.recentholder

import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact


/**
 * FileName: com.live.module.message.recentholder
 * Date: 2022/4/19 10:44
 * Description:
 * History:
 */
class CommonRecentViewHolder : RecentViewHolder() {

    override fun getContent(recent: RecentContact): String {
        return descOfMsg(recent) ?: ""
    }

    override fun getOnlineStateContent(recent: RecentContact?): String {
        return if (recent?.sessionType == SessionTypeEnum.P2P && NimUIKitImpl.enableOnlineState()) {
            NimUIKitImpl.getOnlineStateContentProvider().getSimpleDisplay(recent.contactId)
        } else {
            super.getOnlineStateContent(recent)
        }
    }

    private fun descOfMsg(recent: RecentContact): String? {
        when {
            recent.msgType == MsgTypeEnum.text -> {
                return recent.content
            }
            recent.msgType == MsgTypeEnum.tip -> {
                var digest: String?
                digest = callback?.getDigestOfTipMsg(recent)
                if (digest == null) {
                    digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent)
                }
                return digest
            }
            recent.attachment != null -> {
                var digest: String?
                digest = callback?.getDigestOfAttachment(recent, recent.attachment)
                if (digest == null) {
                    digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent)
                }
                return digest
            }
            else -> return "[未知]"
        }
    }
}