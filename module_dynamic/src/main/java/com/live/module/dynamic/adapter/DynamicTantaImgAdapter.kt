package com.live.module.dynamic.adapter

import android.app.Activity
import android.graphics.Rect
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.dynamic.R
import com.live.module.dynamic.bean.Image
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewBuilder
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.UserViewInfo

/**
 * author: Tany
 * date: 2022/4/11
 * description:
 */
class DynamicTantaImgAdapter(activity: Activity, data: MutableList<Image>) :
    BaseQuickAdapter<Image, BaseViewHolder>(R.layout.dynamic_tanta_item_img, data) {
    var mActivity: Activity? = null

    init {
        mActivity = activity
    }

    override fun convert(holder: BaseViewHolder, item: Image) {
        holder.getView<ImageView>(R.id.iv_img)
            .vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.url, 24,R.mipmap.img_tanta_def)
        holder.getView<ImageView>(R.id.iv_img).setOnClickListener {
            clickImage(data, holder.layoutPosition, holder)
        }
    }

    private fun clickImage(images: MutableList<Image>, innerCount: Int,holder: BaseViewHolder) {
        val list: MutableList<UserViewInfo> = ArrayList()
        images.map {
            val bounds = Rect()
            var imageView=holder.getView<ImageView>(R.id.iv_img)
            imageView.getGlobalVisibleRect(bounds)
            val userViewInfo =
                UserViewInfo(
                    NetBaseUrlConstant.IMAGE_URL + it.url)
            userViewInfo.bounds = bounds
            list.add(userViewInfo)
        }
        if(list.isNullOrEmpty()){
            return
        }
        GPreviewBuilder.from(mActivity!!)
            .setData<UserViewInfo>(list)
            .setCurrentIndex(innerCount)
            .setType(GPreviewBuilder.IndicatorType.Number)
            .start()

    }
}