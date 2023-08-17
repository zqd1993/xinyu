package com.live.module.bill.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.bill.adapter.BillVquWithdrawalListAdapter
import com.live.module.bill.databinding.BillVquFragmentWithdrawalListBinding
import com.live.module.bill.vm.BillVquWithdrawalListViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/6/21
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaWithdrawalListFragment)
class BillVquWithdrawalListFragment :
    BaseFragment<BillVquFragmentWithdrawalListBinding, BillVquWithdrawalListViewModel>() {
    override val mViewModel: BillVquWithdrawalListViewModel by viewModels()

    private val mAdapter = BillVquWithdrawalListAdapter()

    override fun initObserve() {

        mViewModel.emptyData.observe(this) {
            mBinding.srlBillVquWithdrawalListRefresh.setEnableLoadMore(it)
        }

        mViewModel.emptyData.observe(this) {
            if (it) {
                showEmpty()
            } else {
                showContent()
            }
        }

        mViewModel.refreshWithdrawalListData.observe(this) {
            mBinding.srlBillVquWithdrawalListRefresh.finishRefresh()
            mAdapter.setNewInstance(it)
        }

        mViewModel.loadMoreWithdrawalListData.observe(this) {
            mBinding.srlBillVquWithdrawalListRefresh.finishLoadMore()
            if (!it.isNullOrEmpty()) {
                mAdapter.addData(it)
            }
        }
    }

    override fun initRequestData() {
        mViewModel.cashoutRecord(false)
    }

    override fun BillVquFragmentWithdrawalListBinding.initView() {
        setLoadSir(mBinding.srlBillVquWithdrawalListRefresh)

        mBinding.rvBillVquWithdrawalList.adapter = mAdapter

        mBinding.srlBillVquWithdrawalListRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.cashoutRecord(false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.cashoutRecord(true)
            }
        })
    }
}