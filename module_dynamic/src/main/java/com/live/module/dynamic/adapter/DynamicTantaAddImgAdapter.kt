package com.live.module.dynamic.adapter

import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.module.dynamic.R
import com.live.module.dynamic.viewholder.DynamicAddImgViewHolder
import com.live.vquonline.base.BaseApplication
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil

/**
 * author: Tany
 * date: 2022/4/6
 * description:
 */
class DynamicTantaAddImgAdapter(private val mOnAddPicClickListener: DynamicTantaAddImgAdapter.OnAddPicClickListener) :
    RecyclerView.Adapter<DynamicAddImgViewHolder>() {
    private var list: MutableList<LocalMedia?> = mutableListOf()
    private var selectMax = 9
    val TYPE_CAMERA = 0
    val TYPE_PICTURE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicAddImgViewHolder {
        return DynamicAddImgViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dynamic_tanta_item_add_img,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = if (list.size < selectMax) {
        list.size + 1
    } else {
        list.size
    }

    private fun isShowAddItem(position: Int): Boolean {
        val size = if (list.isEmpty()) 0 else list.size
        return position == size
    }

    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    /**
     * 删除
     */
    fun delete(position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION && list.size > position) {
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, list.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    override fun onBindViewHolder(viewHolder: DynamicAddImgViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_CAMERA) {
            viewHolder.mImg.setImageResource(R.mipmap.ic_upload_add)
            viewHolder.mImg.setOnClickListener { mOnAddPicClickListener.onAddPicClick() }
            viewHolder.mIvDel.visibility = View.INVISIBLE
        } else {
            val media = list[position]
            var path: String
            if (PictureMimeType.isHasHttp(media!!.path)) {
                path = if (PictureMimeType.isHasVideo(list[position]!!.mimeType)) {
                    media.realPath
                } else {
                    media.path
                }
                viewHolder.mImg.vquLoadRoundImage(path, UIUtil.dip2px(BaseApplication.context,12.0))
            }
            if (TextUtils.isEmpty(media.path)) {
                return
            }
            viewHolder.mIvDel.setOnClickListener {
                val index: Int = viewHolder.adapterPosition
                delete(index)
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
            viewHolder.mImg.vquLoadRoundImage(
                if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) Uri.parse(
                    path
                ) else path, UIUtil.dip2px(BaseApplication.context,12.0)
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
}