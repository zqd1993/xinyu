package com.live.module.dynamic.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.dynamic.databinding.DynamicTantaDialogPublishBinding
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment


class DynamicTantaPublishDialog : BaseDialogFragment<DynamicTantaDialogPublishBinding>(),
    View.OnClickListener {


    override fun onClick(v: View?) {
    }

    override fun DynamicTantaDialogPublishBinding.initView() {

        mBinding.ivImg.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Dynamic.DynamicVquPublishImgActivity)
                .navigation()
            dismiss()
        }
        mBinding.ivVideo.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Dynamic.DynamicVquPublishVideoActivity)
                .navigation()
            dismiss()
        }
    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBinding.ivClose.setOnClickListener {
            dismiss()
        }
    }


}