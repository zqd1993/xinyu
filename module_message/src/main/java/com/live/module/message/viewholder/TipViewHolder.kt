package com.live.module.message.viewholder

import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.widget.TextView
import com.live.module.message.R
import com.mshy.VInterestSpeed.uikit.business.session.viewholder.MsgViewHolderBase
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter
import com.mshy.VInterestSpeed.uikit.util.HtmlUtils

/**
 * FileName: com.live.module.message.viewholder
 * Date: 2022/4/13 15:51
 * Description:
 * History:
 */
class TipViewHolder(adapter: BaseMultiItemFetchLoadAdapter<*, *>?) : MsgViewHolderBase(adapter) {

    private lateinit var mTvHintCustom: TextView

    override fun getContentResId(): Int {
        return R.layout.message_tanta_tip_viewholder
    }

    override fun inflateContentView() {
        mTvHintCustom = findViewById(R.id.tv_hint_custom)
    }

    override fun bindContentView() {
        if (message != null && !TextUtils.isEmpty(message.content)) {
            mTvHintCustom.text = HtmlUtils.getClickableHtml(message.content)
            mTvHintCustom.movementMethod = LinkMovementMethod.getInstance()
        }
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

}