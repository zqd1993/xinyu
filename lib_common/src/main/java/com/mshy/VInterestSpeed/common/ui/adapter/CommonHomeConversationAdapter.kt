package com.mshy.VInterestSpeed.common.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.youth.banner.adapter.BannerAdapter

/**
 * author: Tany
 * date: 2022/7/2
 * description:
 */

class CommonHomeConversationAdapter(mDatas: List<RecentContact?>?) :
    BannerAdapter<RecentContact?, CommonHomeConversationAdapter.BannerViewHolder?>(mDatas) {
    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        //注意布局文件，item布局文件要设置为match_parent，这个是viewpager2强制要求的
        //或者调用BannerUtils.getView(parent,R.layout.banner_image_title_num);
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.common_vqu_view_banner_message, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: RecentContact?,
        position: Int,
        size: Int,
    ) {
        val userInfo = com.mshy.VInterestSpeed.uikit.api.NimUIKit.getUserInfoProvider().getUserInfo(
            data!!.contactId)
        holder?.tvContent?.text = descOfMsg(data!!)
        holder?.tvName?.text = if (data!!.fromNick != null) data!!.fromNick else ""
        var head: String? = ""
        if (userInfo == null) {
            com.mshy.VInterestSpeed.uikit.api.NimUIKit.getUserInfoObservable().registerObserver({
                val user = com.mshy.VInterestSpeed.uikit.api.NimUIKit.getUserInfoProvider().getUserInfo(
                    data!!.contactId)
                if (user != null) {
                    val headStr = user.avatar
                    holder?.ivHead?.vquLoadCircleImage(NetBaseUrlConstant.IMAGE_URL + headStr,
                        R.mipmap.ic_head_def_circle)
                    holder?.tvName?.text = user.name

                }
            }, true)
        } else {
            head = userInfo.avatar
            holder?.ivHead?.vquLoadCircleImage(NetBaseUrlConstant.IMAGE_URL + head,
                R.mipmap.ic_head_def_circle)
            holder?.tvName?.text = userInfo.name
        }

    }

    fun descOfMsg(recent: RecentContact): String? {
        if (recent.msgType == MsgTypeEnum.text) {
            return recent.content
        } else if (recent.msgType == MsgTypeEnum.tip) {
            var digest: String? = null
            if (digest == null) {
                digest = com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent)
            }
            return digest
        } else if (recent.attachment != null) {
            var digest: String? = null
            if (digest == null) {
                digest = com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent)
            }
            return digest
        }
        return "[未知]"
    }

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivHead: ImageView
        var tvName: TextView
        var tvContent: TextView

        init {
            ivHead = view.findViewById(R.id.iv_head)
            tvName = view.findViewById(R.id.tv_name)
            tvContent = view.findViewById(R.id.tv_content)
        }
    }


}