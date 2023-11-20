package com.live.module.setting.activity

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityLogoffBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/4/2
 * description:注销账号页面
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquLogoffActivity)
class SettingVquLogoffActivity :
    BaseActivity<SettingTantaActivityLogoffBinding, SettingVquViewModel>() {
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityLogoffBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_logoff)) {
            finish()
        }
        mBinding.ivAgree.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mBinding.cancelButton.isEnabled = true
                mBinding.cancelButton.isClickable = true
                mBinding.cancelButton.setStartColor(
                    resources.getColor(R.color.color_B4A3FD),
                    resources.getColor(R.color.color_B4A3FD)
                )
            } else {
                mBinding.cancelButton.isEnabled = false
                mBinding.cancelButton.isClickable = false
                mBinding.cancelButton.setStartColor(
                    resources.getColor(R.color.color_cccccc),
                    resources.getColor(R.color.color_cccccc)
                )
            }
        }
        mBinding.tvAgree.setOnClickListener {
            mBinding.ivAgree.isChecked=!mBinding.ivAgree.isChecked
        }
        mBinding.cancelButton.setViewClickListener {
            cancelAccount()
        }
    }

    private fun cancelAccount() {
        val messageDialog = MessageDialog()
        messageDialog.setTitle(R.string.setting_tip)
        messageDialog.setContent(R.string.setting_cancel_tip)
        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                messageDialog.dismiss()
                return true
            }

            override fun onRightClick(): Boolean {
                mViewModel.vquCloseAccount()
                return false
            }

        })
        messageDialog.show(supportFragmentManager, "")
    }

    override fun initObserve() {
        mViewModel.vquCloseAccountData.observe(this, Observer {
            "注销成功".toast()
            com.mshy.VInterestSpeed.uikit.api.NimUIKit.logout()
            NIMClient.getService(AuthService::class.java).logout()
            UserSpUtils.clear()
            EventBus.getDefault().post("mainFinish")
            ARouter.getInstance()
                .build(RouteUrl.Login.LoginTantaLoginActivity)
                .navigation()
            finish()

        })
    }

    override fun initRequestData() {
    }
}