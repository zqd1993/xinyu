package com.live.module.setting.activity

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityTeenModePwdBinding
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
 * description:开启青少年模式
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquTeenModePwdActivity)
class SettingVquTeenModePwdActivity :
    BaseActivity<SettingTantaActivityTeenModePwdBinding, SettingVquViewModel>() {
    var pwd: String = ""
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityTeenModePwdBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_teen_mode)) {
            finish()
        }
        pwd = intent.getStringExtra("pwd")!!

        mBinding.tvBind.setViewClickListener {
            vquOpenTeenMode()
        }
    }

    private fun vquOpenTeenMode() {
        var vquNewPwd = mBinding.civPwd.text.toString().trim()
        if (!vquNewPwd.isNullOrEmpty() && pwd == vquNewPwd) {
            vquSetPwd()
        } else {
            "密码不正确".toast()
        }

    }

    fun vquSetPwd() {
        mViewModel.vquSetTeenMode(pwd)
    }

    override fun initObserve() {
        mViewModel.vquSetPwdData.observe(this, Observer {
            "设置成功".toast()
            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.KidEvent(-1));
            finish();
        })
    }

    override fun initRequestData() {
    }
}