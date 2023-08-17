package com.live.module.setting.activity

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityTeenModePwdCloseBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/4/2
 * description:关闭青少年模式
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquTeenModePwdCloseActivity)
class SettingVquTeenModePwdCloseActivity :
    BaseActivity<SettingTantaActivityTeenModePwdCloseBinding, SettingVquViewModel>() {
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityTeenModePwdCloseBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_teen_mode)) {
            finish()
        }

        mBinding.tvBind.setViewClickListener {
            vquCloseTeenMode()
        }
    }

    private fun vquCloseTeenMode() {
        var vquNewPwd = mBinding.civPwd.text.toString().trim()
        if (!vquNewPwd.isNullOrEmpty()) {
            vquSetPwd(vquNewPwd)
        } else {
            "密码不正确".toast()
        }

    }

    fun vquSetPwd(pwd: String) {
        mViewModel.vquCloseTeenMode(pwd)
    }

    override fun initObserve() {
        mViewModel.vquCloseModeData.observe(this, Observer {
            "关闭成功".toast()
            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.KidEvent(1))
            finish()
        })
    }

    override fun initRequestData() {
    }
}