package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogTeenmodeBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
/**
 * author: Tany
 * date: 2022/4/18
 * description:
 */
class CommonVquTeenModeDialog : BaseDialogFragment<CommonVquDialogTeenmodeBinding>() {

    override fun CommonVquDialogTeenmodeBinding.initView() {

        mBinding.tvRemind.setViewClickListener { dismiss() }
        mBinding.tvEnter.setViewClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Setting.SettingVquTeenModeActivity)
                .navigation()
            dismiss()
        }
    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}