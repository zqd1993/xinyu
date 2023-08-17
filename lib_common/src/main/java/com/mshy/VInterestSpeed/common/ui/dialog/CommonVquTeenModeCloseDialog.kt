package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogTeenmodeCloseBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
/**
 * author: Tany
 * date: 2022/4/18
 * description:
 */
class CommonVquTeenModeCloseDialog : BaseDialogFragment<CommonVquDialogTeenmodeCloseBinding>() {

    override fun CommonVquDialogTeenmodeCloseBinding.initView() {
        mCancelAble=false
        mBinding.tvClose.setViewClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Setting.SettingVquTeenModePwdCloseActivity)
                .navigation()
        }
        mBinding.tvPwd.setViewClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Setting.SettingVquCloseTeenModeActivity)
                .navigation()

        }
    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }
}