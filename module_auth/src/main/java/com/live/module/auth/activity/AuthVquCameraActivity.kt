package com.live.module.auth.activity

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.auth.R
import com.live.module.auth.databinding.AuthVquActivityCameraBinding
import com.live.module.auth.vm.AuthVquCameraViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Auth.AuthVquCameraActivity)
class AuthVquCameraActivity : BaseActivity<AuthVquActivityCameraBinding, AuthVquCameraViewModel>() {
    override val mViewModel: AuthVquCameraViewModel by viewModels()

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    override fun AuthVquActivityCameraBinding.initView() {

        val fragment = ARouter.getInstance().build(RouteUrl.Auth.AuthVquCameraFragment)
            .navigation() as Fragment

        supportFragmentManager.beginTransaction().replace(R.id.fl_auth_vqu_container, fragment)
            .commit()
    }

}