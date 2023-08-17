package com.live.module.info.adapter

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.module.info.R
import com.live.module.info.viewholder.InfoEditImgViewHolder
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.ext.logI
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.mshy.VInterestSpeed.common.ui.view.dragrecyclerview.MyItemTouchCallback
import com.mshy.VInterestSpeed.common.utils.UiUtils
import java.util.*

/**
 * author: Tany
 * date: 2022/4/6
 * description:
 */
class InfoEditImgAdapter (private val mOnAddPicClickListener: InfoEditImgAdapter.OnAddPicClickListener) :RecyclerView.Adapter<InfoEditImgViewHolder>(), MyItemTouchCallback.ItemTouchAdapter {
    private var list: MutableList<LocalMedia?> = mutableListOf()
    private var selectMax = 6
    val TYPE_CAMERA = 0
    val TYPE_PICTURE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoEditImgViewHolder {
        return InfoEditImgViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.info_tanta_edit_item_img,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int =selectMax

    private fun isShowAddItem(position: Int): Boolean {
        val size = if (list.isEmpty()) 0 else list.size
        return position >= size
    }

    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    fun setList(list: MutableList<LocalMedia?>) {
        this.list = list
    }


    fun getData(): MutableList<LocalMedia?> {
        return list
    }

    override fun getItemViewType(position: Int): Int {

        return if (isShowAddItem(position)) TYPE_CAMERA else TYPE_PICTURE
    }

    override fun onBindViewHolder(viewHolder: InfoEditImgViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.mipmap.ic_upload_add)
            viewHolder.mImg.setOnClickListener { mOnAddPicClickListener.onAddPicClick() }
            viewHolder.tvState.gone()
        } else {
            val media = list[position]
            var path: String
            if (PictureMimeType.isHasHttp(media?.path)){
                path = if (PictureMimeType.isHasVideo(list[position]?.mimeType)){
                    media!!.realPath
                }else{
                    media!!.path
                }
                viewHolder.mImg.vquLoadRoundImage(path, UiUtils.dip2px(BaseApplication.context,12f))
            }
            if (TextUtils.isEmpty(media!!.path)) {
                return
            }

            path = if (media.isCut && !media.isCompressed) {
                // 裁剪过
                media.cutPath
            } else if (media.isCompressed || media.isCut && media.isCompressed) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                media.compressPath
            } else {
                // 原图
                media.path
            }
            if(media.customData=="sh"){
                viewHolder.tvState.visible()
            }else{
                viewHolder.tvState.gone()
            }
            viewHolder.mImg.vquLoadRoundImage(
                if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) Uri.parse(
                    path
                ) else path, UiUtils.dip2px(BaseApplication.context,12f)
            )
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener { v: View? ->
                    val adapterPosition: Int = viewHolder.adapterPosition
                    mItemClickListener?.onItemClick(v, adapterPosition)
                }
            }
        }
    }


    private var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener) {
        mItemClickListener = l
    }


    interface OnAddPicClickListener {
        fun onAddPicClick()
    }
    interface OnItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    override fun onMove(fromPosition: Int, toPosition: Int) {
        list.logI()
        (toPosition.toString()+"----").logI()
        if (fromPosition >= list.size  || toPosition >= list.size ) {
            return
        }
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(getData(), i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(getData(), i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onSwiped(position: Int) {
    }
}