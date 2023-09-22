package com.live.module.dynamic.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.dynamic.R
import com.live.module.dynamic.bean.CommentPositionEvent
import com.live.module.dynamic.bean.DynamicTantaCommentBean
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant.IMAGE_URL
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.utils.SpanUtils
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/7/20
 * description:
 */
class DynamicTantaCommentAdapter :
    BaseQuickAdapter<DynamicTantaCommentBean, BaseViewHolder>(R.layout.dynamic_tanta_layout_dynamic_comment_item) {
    override fun convert(holder: BaseViewHolder, item: DynamicTantaCommentBean) {
        var isLongClick: Boolean = false
        holder.getView<ImageView>(R.id.iv_head).vquLoadCircleImage(
            IMAGE_URL + item.avatar,
            R.mipmap.ic_common_head_circle_def
        )
        holder.getView<TextView>(R.id.tv_name).text = item.nickname
        holder.getView<TextView>(R.id.tv_time).text = item.createTime
        val llRoot = holder.getView<LinearLayout>(R.id.ll_root)
        if (holder.layoutPosition == data.size) {
            llRoot.setBackgroundResource(R.drawable.vqu_shape_f5f5f5_bottom_r12)
        } else {
            llRoot.setBackgroundColor(BaseApplication.context.resources.getColor(R.color.color_F5F5F5))
        }
        var tvContent: TextView = holder.getView<TextView>(R.id.tv_content)
        if (item.commentNickname.isNullOrEmpty()) {
            tvContent.text = item.content
            tvContent.setOnClickListener {
                EventBus.getDefault()
                    .post(CommentPositionEvent(item, false, holder.layoutPosition - 1))
            }
        } else {
//            val content = "回复 <font color='#7C69FE'>" + item.commentNickname + "</font> "+": "+item.content
//            tvContent.text = Html.fromHtml(content)
            val spanUtils = SpanUtils.with(tvContent)
            spanUtils.append("回复").append(item.commentNickname)
                .setClickSpan(
                    BaseApplication.context.resources.getColor(R.color.color_576990),
                    false
                ) {
                    if (!isLongClick) {
                        ARouter.getInstance()
                            .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                            .withInt(
                                RouteKey.USERID,
                                item.commentUserId
                            )
                            .navigation()
                    }
                    isLongClick = false
                }.append(":" + item.content)
                .setClickSpan(
                    BaseApplication.context.resources.getColor(R.color.black),
                    false
                ) {
                    EventBus.getDefault()
                        .post(CommentPositionEvent(item, false, holder.layoutPosition - 1))
                }
            spanUtils.create()


//            tvContent.movementMethod = LinkMovementMethod.getInstance()
//            tvContent.setText(MyTextSpan().append("回复")
//                .append(item.commentNickname,
//                    ForegroundColorSpan(
//                        context.resources.getColor(R.color.color_7C69FE))).append(": "+item.content)
//                .ssb)

        }
//        , object : ClickableSpan() {
//            override fun onClick(widget: View) {
//                ARouter.getInstance()
//                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
//                    .withInt(
//                        RouteKey.USERID,
//                        item.commentUserId
//                    )
//                    .navigation()
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                ds.setColor(context.resources.getColor(R.color.color_7C69FE))
//            }
//        }
        tvContent.setOnLongClickListener {
            isLongClick = true
            EventBus.getDefault().post(CommentPositionEvent(item, true, holder.layoutPosition - 1))
            true
        }

        if (item.isVip > 0) {
            holder.getView<ImageView>(R.id.iv_vip).visible()
            holder.getView<TextView>(R.id.tv_name)
                .setTextColor(context.resources.getColor(R.color.color_934800))
        } else {
            holder.getView<ImageView>(R.id.iv_vip).gone()
            holder.getView<TextView>(R.id.tv_name)
                .setTextColor(context.resources.getColor(R.color.color_576990))
        }
        holder.getView<ImageView>(R.id.iv_head).setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    item.userId
                )
                .navigation()
        }
        holder.getView<TextView>(R.id.tv_name).setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    item.userId
                )
                .navigation()
        }
    }
}