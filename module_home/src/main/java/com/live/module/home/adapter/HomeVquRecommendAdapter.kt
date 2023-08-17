package com.live.module.home.adapter

import android.os.CountDownTimer
import android.text.TextUtils
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.home.R
import com.live.module.home.bean.HomeDataItemBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.view.ShapeLinearLayout
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil
import com.mshy.VInterestSpeed.common.utils.UserManager

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/4/12 19:40
 *
 */
class HomeVquRecommendAdapter :
    BaseQuickAdapter<HomeDataItemBean, BaseViewHolder>(R.layout.home_tanta_item_main_home_recommend) {

    //退出activity时关闭所有定时器，避免造成资源泄漏。
    private val countDownMap = SparseArray<CountDownTimer?>()

    var gender = 0

    fun gender(gender: Int) {
        this.gender = gender
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }

    init {
        addChildClickViewIds(R.id.frameLayout_end)
    }

    override fun convert(helper: BaseViewHolder, item: HomeDataItemBean) {
         gender= UserManager.userInfo!!.gender
        val sdvHead: ImageView = helper.getView(R.id.sdv_head)
        sdvHead.vquLoadRoundImage(
            NetBaseUrlConstant.IMAGE_URL + item.avatar
                    + "?x-oss-process=image/resize,w_150",
            UIUtil.dip2px(context, 10.00), R.mipmap.ic_common_head_def
        )

        //贵族标识
        //CommonVquAddRankAndGradeViewHelper.addNobleView(context,helper.getView<View>(R.id.ll_nick_name) as ViewGroup,item.vip_icon)
        val spiltStr = " | "
        val builder = (item.age +
                "岁" + isEmpty(item.height) + isEmpty(item.occupation) + isEmpty(item.weight))
        if (item.isVip == 1) {
            helper.setGone(R.id.iv_vip, false)
            helper.setTextColor(R.id.tv_nick_name, context.resources.getColor(R.color.color_934800))
        } else {
            helper.setGone(R.id.iv_vip, true)
            helper.setTextColor(R.id.tv_nick_name, context.resources.getColor(R.color.color_273145))
        }
        if (item.isRpAuth == 1) {//真人
            helper.setGone(R.id.iv_real, false)
            helper.setGone(R.id.iv_real_person, true)
        } else {//实名
            if (item.isAuth == 1) {
                helper.setGone(R.id.iv_real_person, false)
                helper.setGone(R.id.iv_real, true)
            } else {
                helper.setGone(R.id.iv_real, true)
                helper.setGone(R.id.iv_real_person, true)
            }
        }
        helper.setText(R.id.tv_nick_name, item.nickname)
            .setText(R.id.tv_des, builder)
            .setText(R.id.tv_sign, item.sign)
            .setImageResource(
                R.id.iv_chat_type,
                if (item.isBeckon) R.mipmap.resources_tanta_main_home_chat else R.mipmap.resources_tanta_home_like
            )
        var flEnd = helper.getView<ShapeLinearLayout>(R.id.fl_end)
        if (item.isBeckon) {
            helper.setText(R.id.tv_chat, context.getString(R.string.dynamic_chat))
            helper.setTextColor(R.id.tv_chat, ContextCompat.getColor(context, R.color.color_FF7AC2))
            flEnd.setStartColor(
                ContextCompat.getColor(context, R.color.color_FFFFFF),
                ContextCompat.getColor(context, R.color.color_FFFFFF)
            )
            flEnd.setStrokeWidthAndColor(ContextCompat.getColor(context, R.color.color_FF7AC2),UIUtil.dip2px(context,1.0))
        } else {
            if (gender == 1) {
                helper.setText(R.id.tv_chat, context.getString(R.string.common_vqu_beckoning))
            } else {
                helper.setText(R.id.tv_chat, context.getString(R.string.common_vqu_accost))
            }
            flEnd.setStartColor(
                ContextCompat.getColor(context, R.color.color_FF7AC2),
                ContextCompat.getColor(context, R.color.color_FF7AC2)
            )
            helper.setTextColor(R.id.tv_chat, ContextCompat.getColor(context, R.color.color_FFFFFF))
        }
        helper.setVisible(R.id.view_online, false)
        val online = item.isOnline
        if (online == 1) {
            helper.setVisible(R.id.view_online, true)
        } else {
            helper.setVisible(R.id.view_online, false)
        }


    }

    private fun isEmpty(content: String?): String {
        return if (TextUtils.isEmpty(content)) {
            ""
        } else {
            " | $content"
        }
    }

    private fun isEmptyContent(content: String?): String {
        return if (TextUtils.isEmpty(content)) {
            ""
        } else {
            " | "
        }
    }
}