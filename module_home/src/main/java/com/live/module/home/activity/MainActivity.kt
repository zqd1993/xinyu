package com.live.module.home.activity

import android.graphics.Color
import androidx.activity.viewModels
import com.live.module.home.databinding.HomeActivityMainBinding
import com.live.vquonline.base.ktx.observeLiveData
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.live.vquonline.base.utils.ToastUtils
import com.live.vquonline.base.utils.getPackageManagerName
import dagger.hilt.android.AndroidEntryPoint

/**
 * 首页
 *
 * ""
 * @since 5/22/21 2:26 PM
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<HomeActivityMainBinding, HomeViewModel>() {

    /**
     * 通过 viewModels() + Hilt 获取 ViewModel 实例
     */
    override val mViewModel by viewModels<HomeViewModel>()

    override fun HomeActivityMainBinding.initView() {}

    override fun initObserve() {
        observeLiveData(mViewModel.data, ::processData)
    }

    private fun processData(data: String) {
        mBinding.vTvHello.text = data
        mBinding.vTvHello.setTextColor(Color.BLUE)
        ToastUtils.showToast(getPackageManagerName(),1000)
    }

    override fun initRequestData() {
        // 模拟获取数据
        mViewModel.getData()
    }
}