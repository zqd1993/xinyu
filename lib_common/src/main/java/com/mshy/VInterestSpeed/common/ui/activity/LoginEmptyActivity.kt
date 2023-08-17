package com.mshy.VInterestSpeed.common.ui.activity

import android.content.Intent
import android.view.Gravity
import android.view.WindowManager
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.ActivityLoginEmptyBinding
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import com.umeng.socialize.UMShareAPI
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/5/30
 * description:
 */
@AndroidEntryPoint
@EventBusRegister
@Route(path = RouteUrl.Common.LoginEmptyActivity)
class LoginEmptyActivity : BaseActivity<ActivityLoginEmptyBinding, LoginViewModel>() {
    override val mViewModel: LoginViewModel by viewModels()
    override fun initObserve() {
    }

    override fun initRequestData() {
        mViewModel.loginThird(this, com.mshy.VInterestSpeed.common.utils.ShareManager.TYPE_QQ)
    }

    override fun ActivityLoginEmptyBinding.initView() {
        //这一步很关键（如果不设置背景是黑色或者白色）
        window.setBackgroundDrawable(getDrawable(R.color.transparent))
        val params = window.attributes
//        ScreenUtils.getScreenSize()
        com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil.screenWidth
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.CENTER
        //透明度
        params.dimAmount = 0.0f
//        params.alpha = 0.6f
        window.attributes = params
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFinishEvent(onFinishEvent: com.mshy.VInterestSpeed.common.bean.OnFinishEvent) {
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(loginEvent: LoginEvent) {
        finish()
    }
}