package com.live.module.anchor.adapter

import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.anchor.R
import com.live.module.anchor.bean.AnchorVquHelloTemplateBean
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ext.vquLoadVideoFirstFrame
import com.mshy.VInterestSpeed.common.ui.view.ShapeLinearLayout
import java.lang.String
import kotlin.Int
import kotlin.Long

/**
 * author: Lau
 * date: 2022/4/25
 * description:
 */
class AnchorVquHelloSettingAdapter :
    BaseQuickAdapter<AnchorVquHelloTemplateBean, BaseViewHolder>(R.layout.anchor_vqu_item_hello_setting_list) {

    init {
        addChildClickViewIds(
            R.id.iv_hello_template_delete, R.id.iv_hello_template_edit,
            R.id.fl_hello_template_voice_parent, R.id.cb_hello_template_use,
            R.id.fl_hello_template_video_parent, R.id.riv_hello_template_img
        )
    }

    override fun convert(holder: BaseViewHolder, item: AnchorVquHelloTemplateBean) {
        holder.setText(
            R.id.tv_hello_template_name,
            if (item.name.isNullOrEmpty()) {
                "我的模板"
            } else {
                item.name
            }
        )

        holder.setText(R.id.tv_hello_template_desc, item.title)

        val ivImg = holder.getView<ImageView>(R.id.riv_hello_template_img)

        if (!item.file.isNullOrEmpty()) {
            ivImg.visibility = View.VISIBLE
            ivImg.vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.file, context.dp2px(10f))
        } else {
            ivImg.visibility = View.GONE
        }

        val ivVideo = holder.getView<ImageView>(R.id.riv_hello_template_video)
        val flVideoParent = holder.getView<com.mshy.VInterestSpeed.common.ui.view.ShapeRelativeLayout>(R.id.fl_hello_template_video_parent)

        if (!item.videoFile.isNullOrEmpty()) {
            flVideoParent.visibility = View.VISIBLE

            ivVideo.vquLoadVideoFirstFrame(
                NetBaseUrlConstant.IMAGE_URL + item.videoFile
            )
        } else {
            flVideoParent.visibility = View.GONE
        }


        val flVoiceParent: ShapeLinearLayout = holder.getView(R.id.fl_hello_template_voice_parent)
        if (!item.voiceFile.isNullOrEmpty()) {
            flVoiceParent.visibility = View.VISIBLE
            holder.setText(R.id.tv_hello_template_voice_time, String.valueOf(item.len))
        } else {
            flVoiceParent.visibility = View.GONE
        }

        val ivStatus: ImageView = holder.getView(R.id.iv_hello_template_status)
        val ivEdit: ImageView = holder.getView(R.id.iv_hello_template_edit)
        val cbUse: TextView = holder.getView(R.id.cb_hello_template_use)
        val ivDelete: ImageView = holder.getView(R.id.iv_hello_template_delete)

        if (item.status != 1) {
            ivStatus.visibility = View.VISIBLE
            ivEdit.visibility = View.GONE
            cbUse.visibility = View.GONE
            if (item.status == 0) {
                ivDelete.visibility = View.GONE
                ivStatus.setImageResource(R.mipmap.ic_vqu_anchor_hello_status_checking)
            } else {
                ivDelete.visibility = View.VISIBLE
                ivStatus.setImageResource(R.mipmap.ic_vqu_anchor_hello_status_unqualified)
            }
        } else {
            ivStatus.visibility = View.GONE
            ivEdit.visibility = View.VISIBLE
            cbUse.visibility = View.VISIBLE
            ivDelete.visibility = View.VISIBLE
            cbUse.isSelected = item.isDefault == 1
        }
    }

    fun startDownTime(viewHolder: BaseViewHolder, position: Int) {
        val item = getItemOrNull(position) ?: return

        if (item.countDownTimer == null) {
            item.countDownTimer = object : CountDownTimer(item.len * 1000L, 1 * 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    if (item.playType == 1) {
                        viewHolder.setText(
                            R.id.tv_hello_template_voice_time,
                            String.valueOf(millisUntilFinished / 1000)
                        )
                    }
                }

                override fun onFinish() {
                    viewHolder.setText(R.id.tv_hello_template_voice_time, String.valueOf(item.len))
                }
            }
        }
        item.countDownTimer?.start()
    }

    fun stopDownTime(viewHolder: BaseViewHolder, position: Int) {
        val item = getItemOrNull(position) ?: return
        item.countDownTimer?.cancel()

        viewHolder.setText(R.id.tv_hello_template_voice_time, String.valueOf(item.len))
    }
}