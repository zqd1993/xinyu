package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonTantaDialogBindPhoneBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import org.greenrobot.eventbus.EventBus
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.UserSpUtils

/**
 * author: Lau
 * date: 2022/5/24
 * description:
 */
class CommonTantaBindPhoneDialog : BaseDialogFragment<CommonTantaDialogBindPhoneBinding>() {
    override fun CommonTantaDialogBindPhoneBinding.initView() {
        mBinding.stvCommonDialogMessageBtnSingle.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Setting.SettingVquBindMobileActivity).navigation()
        }

        mBinding.tvCommonTantaBindPhoneChange.setViewClickListener {
            dismiss()
            com.mshy.VInterestSpeed.uikit.api.NimUIKit.logout()
            NIMClient.getService(AuthService::class.java).logout()
            UserSpUtils.clear()
            EventBus.getDefault().post("mainFinish")
            com.mshy.VInterestSpeed.uikit.util.IntimateUtils.getInstance().clearData()
            ARouter.getInstance()
                .build(RouteUrl.Login.LoginTantaLoginActivity)
                .navigation()
        }
    }

    override fun initWindow() {
//        super.initWindow()
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }

}