package com.live.module.vip.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.live.module.vip.R
import com.live.module.vip.bean.TantaVipInfoBean
import com.live.module.vip.databinding.VipTantaDialogDailyGifBinding
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil


class VipDailyGifDialog : BaseDialogFragment<VipTantaDialogDailyGifBinding>() {
    private var mVquOnClick: ((payType: Any) -> Unit)? = null
    var tantaVipInfoBean: TantaVipInfoBean? = null


    override fun VipTantaDialogDailyGifBinding.initView() {

        if (tantaVipInfoBean?.info?.gift != null) {
            var gift = tantaVipInfoBean?.info?.gift!!
            mBinding.tvName.text = gift.name + " x${gift.num}"
            var imageUrl = NetBaseUrlConstant.IMAGE_URL + gift.img
            LogUtil.e("imageUrl=${imageUrl}")
            Glide.with(BaseApplication.context)
                .load(imageUrl)
                .into(mBinding.ivMineDialogDailyRes)
        }

        if (tantaVipInfoBean?.info?.gift?.is_reward == 0) {
            mBinding.btReceive.text = "立即领取"
            mBinding.btReceive.setStartColor(
                ResUtils.getColor(com.live.module.bill.R.color.color_505050),
                ResUtils.getColor(com.live.module.bill.R.color.color_272727)
            )
        } else {
            mBinding.btReceive.text = "今日已经领取"
            mBinding.btReceive.setStartColor(
                ResUtils.getColor(com.live.module.bill.R.color.color_CCCCCC),
                ResUtils.getColor(com.live.module.bill.R.color.color_CCCCCC)
            )

        }
        mBinding.ivMineDialogDailyClose.setViewClickListener(200) {
            dismiss()
        }
        mBinding.btReceive.setViewClickListener(200) {
            LogUtil.e("is_reward=${tantaVipInfoBean?.info?.gift?.is_reward}")
            LogUtil.e("vip_id=${tantaVipInfoBean?.info?.vip_id!!}")
            if (tantaVipInfoBean?.info?.gift?.is_reward == 0) {
                if (tantaVipInfoBean?.info?.vip_id!! > 0) {
                    mVquOnClick?.invoke(1)
                } else {
                    toast("开通会员即可领取")
                }
            }
            dismiss()
        }
    }

    override fun initWindow() {
        mCancelAble = false
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }


    fun setOnSelectionClickListener(onClick: (payType: Any) -> Unit) {
        mVquOnClick = onClick
    }

}