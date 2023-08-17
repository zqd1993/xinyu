package com.live.module.vip.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.vip.databinding.VipTantaActivityMemberOpenSucceedBinding
import com.live.module.vip.vm.VipTantaMemberOpenSucceedModel
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Vip.VipTantaMemberOpenSucceedActivity)
//@EventBusRegister
class VipTantaMemberOpenSucceedActivity :
    BaseActivity<VipTantaActivityMemberOpenSucceedBinding, VipTantaMemberOpenSucceedModel>() {
    override val mViewModel: VipTantaMemberOpenSucceedModel by viewModels()

    @Autowired(name = RouteKey.TYPE_OPEN_SUCCEED)
    @JvmField
    var isVip = false

    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun VipTantaActivityMemberOpenSucceedBinding.initView() {
        mBinding.tbVipVquSucceedBar.toolbar.initClose("开通成功") { finish() }
        if (isVip) {
            mBinding.tvTip.text = "续费VIP会员成功！"
        } else {
            mBinding.tvTip.text = "恭喜你开通VIP会员成功！"
        }
        initEvent()
    }

    private fun initEvent() {

        mBinding.btJump.setViewClickListener(1) {
            finish()
        }
    }


}