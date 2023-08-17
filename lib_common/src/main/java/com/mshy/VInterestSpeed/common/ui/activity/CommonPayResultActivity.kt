package com.mshy.VInterestSpeed.common.ui.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonPayResultBinding
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayResultViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Common.CommonPayResultActivity)
class CommonPayResultActivity() : BaseActivity<CommonPayResultBinding, CommonPayResultViewModel>() {
    override val mViewModel: CommonPayResultViewModel by viewModels()

    override fun CommonPayResultBinding.initView() {
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }

}