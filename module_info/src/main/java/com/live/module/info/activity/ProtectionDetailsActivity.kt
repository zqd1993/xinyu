package com.live.module.info.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.info.adapter.ProtectionLevelAdapter
import com.live.module.info.databinding.ProtectionDetailsActivityBinding
import com.live.module.info.vm.InfoVquViewModel
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteUrl.Info.ProtectionDetailsActivity)
class ProtectionDetailsActivity :
    BaseActivity<ProtectionDetailsActivityBinding, InfoVquViewModel>() {
    @Autowired(name = RouteKey.USERID)
    @JvmField
    var userId = 0

    private var myAdapter: ProtectionLevelAdapter? = null

    override val mViewModel: InfoVquViewModel by viewModels()

    override fun ProtectionDetailsActivityBinding.initView() {
        mBinding.includeTitle.toolbar.initClose("守护功能介绍") {
            finish()
        }
    }

    override fun initObserve() {
        mViewModel.vquProtectionOptionBeanList.observe(this) {
            if (it.code == 0) {
                if(it.data.isNotEmpty()){
                    myAdapter = ProtectionLevelAdapter(it.data)
                    mBinding.protectionLevelList.adapter = myAdapter
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.vquGetGuardianOptions()
    }


}