package com.live.module.dynamic.adapter

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.dynamic.R
import com.live.module.dynamic.bean.DynamicLikeBean
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage

/**
 * author: Tany
 * date: 2022/6/9
 * description:
 */
class DynamicTantaLikeAdapter :
    BaseQuickAdapter<DynamicLikeBean, BaseViewHolder>(R.layout.dynamic_tanta_item_like) {
    override fun convert(holder: BaseViewHolder, item: DynamicLikeBean) {
        holder.getView<ImageView>(R.id.iv_head)
            .vquLoadRoundImage(
                NetBaseUrlConstant.IMAGE_URL + item.fromUserAvatar,
                50,
                R.mipmap.ic_common_head_def
            )
        holder.getView<TextView>(R.id.tv_time).text = item.createTime
        holder.getView<TextView>(R.id.tv_head).text = item.fromUserNickname
        var tvLike = holder.getView<TextView>(R.id.tv_like)
        if (item.isDelete == 1) {
            holder.getView<TextView>(R.id.tv_like).text = "评论已被删除"
        } else {
            when (item.isType) {
                1 -> {
                    tvLike.text = item.content
                }

                2 -> {
                    val content = "回复了你: " + item.content.replace(" ", "&#160")
                    tvLike.text = Html.fromHtml(content)

                }

                3 -> {
                    val content = "评论了你: " + item.content.replace(" ", "&#160")
                    tvLike.text = Html.fromHtml(content)
                }

                else -> ""
            }

        }
//            tvLike.movementMethod = LinkMovementMethod.getInstance()
//            tvLike.setText(MyTextSpan()
//                .append(item.fromUserNickname,
//                    ForegroundColorSpan(
//                        context.resources.getColor(R.color.color_7C69FE)),
//                    object : ClickableSpan() {
//                        override fun onClick(widget: View) {
//                            ARouter.getInstance()
//                                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
//                                .withInt(
//                                    RouteKey.USERID,
//                                    item.fromUserId
//                                )
//                                .navigation()
//                        }
//
//                        override fun updateDrawState(ds: TextPaint) {
//                            ds.setColor(context.resources.getColor(R.color.color_7C69FE))
//                        }
//                    })
//                .append(when (item.isType) {
//                    1 -> {
//                        ""
//                    }
//                    2 -> {
//                        "回复了你: "
//                    }
//                    3 -> {
//                        "评论了你: "
//                    }
//                    else -> ""
//                }, ForegroundColorSpan(
//                    context.resources.getColor(R.color.color_273145)),
//                    object : ClickableSpan() {
//                        override fun onClick(widget: View) {
//                            ARouter.getInstance()
//                                .build(RouteUrl.Dynamic.DynamicVquDynamicDetailActivity)
//                                .withString(
//                                    RouteKey.ID,
//                                    item.dynamicId.toString()
//                                )
//                                .navigation()
//                        }
//                        override fun updateDrawState(ds: TextPaint) {
//                            ds.setColor(context.resources.getColor(R.color.color_273145))
//                        }
//                    })
//                .append(item.content, ForegroundColorSpan(
//                    context.resources.getColor(R.color.color_273145)),
//                    object : ClickableSpan() {
//                        override fun onClick(widget: View) {
//                            ARouter.getInstance()
//                                .build(RouteUrl.Dynamic.DynamicVquDynamicDetailActivity)
//                                .withString(
//                                    RouteKey.ID,
//                                    item.dynamicId.toString()
//                                )
//                                .navigation()
//                        }
//
//                        override fun updateDrawState(ds: TextPaint) {
//                            ds.setColor(context.resources.getColor(R.color.color_273145))
//                        }
//                    })
//                .ssb)
//        }
        holder.getView<ImageView>(R.id.iv_head).setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    item.fromUserId
                )
                .navigation()

        }

        if (item.dynamicType == 1) {
            holder.getView<ImageView>(R.id.iv_img).vquLoadRoundImage(item.videoUrl, 13)
            holder.getView<ImageView>(R.id.iv_video).visible()
        } else {
            holder.getView<ImageView>(R.id.iv_img)
                .vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.imageUrl, 13)
            holder.getView<ImageView>(R.id.iv_video).gone()
        }

    }
}