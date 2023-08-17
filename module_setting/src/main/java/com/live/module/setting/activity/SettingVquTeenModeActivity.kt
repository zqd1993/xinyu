package com.live.module.setting.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityTeenModeBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.KidEvent
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author: Tany
 * date: 2022/4/2
 * description:青少年模式已关闭
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquTeenModeActivity)
class SettingVquTeenModeActivity :
    BaseActivity<SettingTantaActivityTeenModeBinding, SettingVquViewModel>() {
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityTeenModeBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_teen_mode)) {
            finish()
        }
        mBinding.tvBind.setViewClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Setting.SettingVquTeenModeOpenActivity)
                .navigation()
        }
    }

    //监听青少年模式
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: KidEvent?) {
        finish()
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }
}