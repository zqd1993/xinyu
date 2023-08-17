package com.live.module.dynamic.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.dynamic.R
import com.live.module.dynamic.activity.DynamicTantaVideoActivity
import com.live.module.dynamic.bean.DynamicBean
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.setVip
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.view.ShapeLinearLayout
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewBuilder
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.UserViewInfo
import com.mshy.VInterestSpeed.common.utils.UiUtils
import com.mshy.VInterestSpeed.common.utils.UserManager


/**
 * author: Tany
 * date: 2022/3/31
 * description:
 */
class DynamicTantaAdapter(activity: Activity) :
    BaseQuickAdapter<DynamicBean, BaseViewHolder>(R.layout.dynamic_tanta_item_dynamic) {
    var mActivity: Activity? = null

    init {
        mActivity = activity
        addChildClickViewIds(R.id.iv_head,
            R.id.ll_like,
            R.id.ll_chat,
            R.id.ll_beckoning,
            R.id.tv_name)
    }

    override fun convert(holder: BaseViewHolder, item: DynamicBean) {
        holder.getView<ImageView>(R.id.iv_head)
            .vquLoadCircleImage(NetBaseUrlConstant.IMAGE_URL + item.avatar,
                R.mipmap.ic_common_head_circle_def)
        holder.getView<TextView>(R.id.tv_name).text = item.nickname
        holder.getView<TextView>(R.id.tv_city).text = item.city
        holder.getView<TextView>(R.id.tv_time).text = item.createTime
        holder.getView<TextView>(R.id.tv_content).text = item.content
        var llBeckoning =
            holder.getView<ShapeLinearLayout>(R.id.ll_beckoning)
        var tvAudit = holder.getView<TextView>(R.id.tv_audit)
        var tvBottom = holder.getView<TextView>(R.id.tv_bottom)
        if (item.status == 0) {
            tvAudit.visible()
        } else {
            tvAudit.gone()
        }
        holder.setImageResource(
            R.id.iv_beckoning,
            if (item.isBeckon) R.mipmap.resources_tanta_main_home_chat else R.mipmap.resources_tanta_home_like)
        if (item.isBeckon) {
            tvBottom.setTextColor(ContextCompat.getColor(context, R.color.color_FF7AC2))
            llBeckoning.setStartColor(ContextCompat.getColor(context, R.color.color_FFFFFF),
                ContextCompat.getColor(context, R.color.color_FFFFFF))
            holder.setText(R.id.tv_bottom, context.getString(R.string.dynamic_chat))
            llBeckoning.setStrokeWidthAndColor(ContextCompat.getColor(context,
                R.color.color_FF7AC2), 1)

        } else {
            tvBottom.setTextColor(ContextCompat.getColor(context, R.color.color_FFFFFF))
            llBeckoning.setStartColor(ContextCompat.getColor(context, R.color.color_FF7AC2),
                ContextCompat.getColor(context, R.color.color_FF7AC2))
            if (item.gender == 1) {
                holder.setText(R.id.tv_bottom, context.getString(R.string.common_vqu_accost))
            } else {
                holder.setText(R.id.tv_bottom, context.getString(R.string.common_vqu_beckoning))
            }
        }
        holder.getView<TextView>(R.id.tv_name).setVip(item.vip > 0)
        holder.getView<ImageView>(R.id.iv_vip).visibility = if (item.vip > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
        //等级
//        CommonVquAddRankAndGradeViewHelper.addGradeView(context,holder.getView<View>(R.id.cl_root) as ViewGroup,item.info.grade.imgInfo)
        if (item.content.isNullOrEmpty()) {
            holder.getView<TextView>(R.id.tv_content).gone()
        } else {
            holder.getView<TextView>(R.id.tv_content).visible()
        }
        holder.getView<TextView>(R.id.tv_count).text = if (item.likeCount > 0) {
            item.likeCount.toString()
        } else {
            "点赞"
        }
        holder.getView<TextView>(R.id.tv_comment).text = if (item.commentCount > 0) {
            item.commentCount.toString()
        } else {
            "评论"
        }
        holder.getView<ImageView>(R.id.iv_like).isSelected = item.isLike != 0
//        CommonVquAddRankAndGradeViewHelper.addGender(
//            context,
//            holder.getView<LinearLayout>(R.id.cl_root),
//            item.gender,
//            item.age.toString()
//        )
        val builder = (item.age.toString() +
                "岁" + isEmpty(item.height) + isEmpty(item.occupation) + isEmpty(item.weight))
        holder.getView<TextView>(R.id.tv_info).text = builder
        var ivImg = holder.getView<ImageView>(R.id.iv_img)
        var ivPlay = holder.getView<ImageView>(R.id.iv_play)
        var rvImg = holder.getView<RecyclerView>(R.id.rv_img)
        if (!item.fileUrl.isNullOrEmpty() && !item.coverUrl.isNullOrEmpty()) {
            var layoutParams = ivImg.layoutParams
            layoutParams.width = UiUtils.dip2px(context, 160f)
            layoutParams.height = UiUtils.dip2px(context, 213f)
            ivImg.layoutParams = layoutParams
            ivImg.vquLoadRoundImage(item.coverUrl, 24,R.mipmap.video_tanta_def)
            ivImg.visible()
            ivPlay.visible()
            ivImg.setOnClickListener {
                clickVideo(item, ivImg)
            }
        } else {
            ivImg.gone()
            ivPlay.gone()
        }
        if (item.images.isNullOrEmpty()) {
            rvImg.gone()
        } else {
            if (item.images.size == 1) {
                var img = item.images[0]
                if (img.width == 0 || img.height == 0) {
                    var layoutParams = ivImg.layoutParams
                    layoutParams.width =
                        UiUtils.dip2px(context, 160f)
                    layoutParams.height =
                        UiUtils.dip2px(context, 213f)
                    ivImg.layoutParams = layoutParams
                } else {
                    var proportion: Float = img.width * 1.0f / img.height * 1.0f
                    if (proportion >= 5f) {
                        var layoutParams = ivImg.layoutParams
                        layoutParams.width =
                            UiUtils.dip2px(context, 210f)
                        layoutParams.height =
                            UiUtils.dip2px(context, 42f)
                        ivImg.layoutParams = layoutParams
                    } else if (proportion <= 0.2f) {
                        var layoutParams = ivImg.layoutParams
                        layoutParams.width =
                            UiUtils.dip2px(context, 42f)
                        layoutParams.height =
                            UiUtils.dip2px(context, 210f)
                        ivImg.layoutParams = layoutParams
                    } else {
                        if (img.width >= img.height) {
                            var layoutParams = ivImg.layoutParams
                            layoutParams.width =
                                UiUtils.dip2px(context, 210f)
                            layoutParams.height =
                                UiUtils.dip2px(context,
                                    img.height * 1.0f / img.width * 1.0f * 210)
                            ivImg.layoutParams = layoutParams
                        } else {
                            var layoutParams = ivImg.layoutParams
                            layoutParams.width =
                                UiUtils.dip2px(context,
                                    img.width * 1.0f / img.height * 1.0f * 210)
                            layoutParams.height =
                               UiUtils.dip2px(context, 210f)
                            ivImg.layoutParams = layoutParams
                        }
                    }
                }
                rvImg.gone()
                ivImg.vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + item.images[0].url, 24,R.mipmap.img_tanta_def)
                ivImg.visible()
                ivImg.setOnClickListener {
                    clickImage(item.images[0].url, 0, ivImg)
                }

            } else {
                ivImg.gone()
                rvImg.visible()
                var imgAdapter = DynamicTantaImgAdapter(mActivity!!, item.images)
                if (item.images.size == 2 || item.images.size == 4) {
                    rvImg.layoutManager = GridLayoutManager(context, 2)
                } else {
                    rvImg.layoutManager = GridLayoutManager(context, 3)
                }
                rvImg.adapter = imgAdapter
                rvImg.setOnTouchListener { v, event ->
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        holder.itemView.findViewById<ConstraintLayout>(R.id.cl_root)
                            .performClick();  //模拟父控件的点击
                    }
                    false
                }
            }
        }
        var userId = UserManager.userInfo?.userId
        if (userId == item.userId.toString()) {
            llBeckoning.gone()
        } else {
            llBeckoning.visible()
        }
    }

    private fun isEmpty(content: String?): String {
        return if (TextUtils.isEmpty(content)) {
            ""
        } else {
            " | $content"
        }
    }

    private fun clickVideo(item: DynamicBean, ivImg: ImageView) {
        val globalRect = Rect()
        ivImg.getGlobalVisibleRect(globalRect)
        val intent = Intent(mActivity, DynamicTantaVideoActivity::class.java)
        intent.putExtra("video_url", item.fileUrl.toString() + "")
        intent.putExtra("cover_url", item.coverUrl.toString() + "")
        intent.putExtra(
            "region",
            intArrayOf(
                globalRect.left,
                globalRect.top,
                globalRect.right,
                globalRect.bottom,
                ivImg.getWidth(),
                ivImg.getHeight()
            )
        )
        intent.putExtra("position", 0)
        mActivity?.startActivity(intent)
        mActivity?.overridePendingTransition(0, 0)

    }

    private fun clickImage(path: String, innerCount: Int, imageView: ImageView) {
        val list: MutableList<UserViewInfo> =
            ArrayList()
        val bounds = Rect()
        imageView.getGlobalVisibleRect(bounds)
        val userViewInfo =
            UserViewInfo(
                NetBaseUrlConstant.IMAGE_URL + path)
        userViewInfo.bounds = bounds
        list.add(userViewInfo)
//
        if (list.isNullOrEmpty()) {
            return
        }
        GPreviewBuilder.from(mActivity!!)
            .setData<UserViewInfo>(list)
            .setCurrentIndex(innerCount)
            .setType(GPreviewBuilder.IndicatorType.Number)
            .start()

    }
}
