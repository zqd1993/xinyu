package com.live.module.info.adapter

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.info.R
import com.live.module.info.bean.Album
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.utils.UiUtils

/**
 * author: Tany
 * date: 2022/4/26
 * description:
 */
class InfoVquSmallImgAdapter(data: MutableList<Album>) :
    BaseQuickAdapter<Album, BaseViewHolder>(R.layout.info_tanta_img_small_item, data) {
    var position: Int = 0
    var screenshotUrl:String="?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto"
    override fun convert(holder: BaseViewHolder, item: Album) {
        var img = holder.getView<ImageView>(R.id.iv_img)
        var ivPlay = holder.getView<ImageView>(R.id.iv_play)
        var clRoot = holder.getView<ConstraintLayout>(R.id.cl_root)
        if (item.isVideo == 1) {
            img.vquLoadRoundImage( NetBaseUrlConstant.IMAGE_URL+item.url+screenshotUrl, UiUtils.dip2px(context, 10f))
        } else {
            img.vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.url, UiUtils.dip2px(context, 10f))
        }

        if (position == holder.layoutPosition) {//选中显示边框
            if (item.isVideo == 1) {
                ivPlay.visible()
                ivPlay.setImageResource(R.mipmap.ic_info_video_playing)

            } else {
                ivPlay.gone()
            }
            clRoot.setBackgroundResource(R.drawable.vqu_shape_border_white)
        } else {
            if (item.isVideo == 1) {
                ivPlay.visible()
                ivPlay.setImageResource(R.mipmap.ic_info_video_play)
            } else {
                ivPlay.gone()
            }
            clRoot.setBackgroundResource(R.drawable.vqu_shape_border_trans)
        }
    }

    fun setSelect(sel: Int) {
        position = sel
        notifyDataSetChanged()
    }
}