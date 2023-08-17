package com.live.module.mine.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.mine.adapter.MineVquCouponAdapter
import com.live.module.mine.databinding.MineVquFragmentBackpackCardBinding
import com.live.module.mine.vm.MineVquMyBackpackViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Mine.MineVquCouponFragment)
class MineVquCouponFragment :
    BaseFragment<MineVquFragmentBackpackCardBinding, MineVquMyBackpackViewModel>() {
    override val mViewModel: MineVquMyBackpackViewModel by viewModels()

    private val mAdapter = MineVquCouponAdapter()

    override fun MineVquFragmentBackpackCardBinding.initView() {

        setLoadSir(mBinding.srlMineBackpackCardRefresh)

        initAdapter()

        mBinding.srlMineBackpackCardRefresh.setOnRefreshListener {
            mViewModel.getPackage("video_coupon")
        }
    }

    private fun initAdapter() {
        mBinding.rvMineVquBackpackCardList.adapter = mAdapter
    }

    override fun initObserve() {
        mViewModel.listData.observe(this) {
            mBinding.srlMineBackpackCardRefresh.finishRefresh()
            if (it.isNullOrEmpty()) {
                showEmpty()
            }else{
                mLoadService?.showSuccess()
            }
            mAdapter.setNewInstance(it)

        }

        mViewModel.useResult.observe(this) {
            if (it) {
                mViewModel.getPackage("video_coupon")
            }
        }
    }

    override fun initRequestData() {
        mViewModel.getPackage("video_coupon")
    }
}