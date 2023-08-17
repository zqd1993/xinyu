package com.live.module.bill.fragment

import android.content.res.AssetManager
import android.graphics.Typeface
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.bill.adapter.BillVquDetailAdapter
import com.live.module.bill.adapter.BillVquDetailItemDecoration
import com.live.module.bill.bean.BillVquTimeSelectEvent
import com.live.module.bill.databinding.BillVquFragmentBillDetailBinding
import com.live.module.bill.vm.BillVquDetailViewModel
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/4/7
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaDetailFragment)
@EventBusRegister
class BillTantaDetailFragment :
    BaseFragment<BillVquFragmentBillDetailBinding, BillVquDetailViewModel>() {

    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 1

    private val mAdapter by lazy {
        BillVquDetailAdapter(type)
    }

    private var mVquDate = ""

    override val mViewModel: BillVquDetailViewModel by viewModels()

    override fun BillVquFragmentBillDetailBinding.initView() {

        setLoadSir(mBinding.srfBillVquDetailRefresh)

        val assetsManager: AssetManager = BaseApplication.application.assets //得到AssetManager

        val tf: Typeface =
            Typeface.createFromAsset(assetsManager, "fonts/DINAlternateBold.ttf") //根据路径得到Typeface

        mAdapter.setTypeface(tf)

        mBinding.rvBillVquDetailList.addItemDecoration(BillVquDetailItemDecoration(requireContext()))

        mBinding.rvBillVquDetailList.adapter = mAdapter

        initRefresh()
        initEvent()
    }

    private fun initEvent() {

        mAdapter.setOnItemClickListener { _, _, position ->
            val bill =
                mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            if (bill.tracUserId.isNullOrEmpty()) {
                return@setOnItemClickListener
            }

            bill.tracUserId?.toInt()?.let {

                if (it == 0) {
                    return@setOnItemClickListener
                }

                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                        RouteKey.USERID,
                        it
                    )
                    .navigation()
            }

        }
    }

    private fun initRefresh() {
        mBinding.srfBillVquDetailRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                loadData(false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                loadData(true)
            }
        })
    }

    override fun initObserve() {
        mViewModel.listData.observe(this) {

            if (mViewModel.page == 1) {

                if (it.isNullOrEmpty()) {
//                    mLoadService?.
                    showEmpty()
                } else {
                    mLoadService?.showSuccess()
                }

                mAdapter.setNewInstance(it)
                mBinding.srfBillVquDetailRefresh.finishRefresh()
            } else {
                mAdapter.addData(it)
                mBinding.srfBillVquDetailRefresh.finishLoadMore()
            }
        }
    }

    override fun initRequestData() {
        loadData(false)
    }

    private fun loadData(isLoadMore: Boolean) {
        mViewModel.vquWalletRecord(0, 0, mVquDate, type, type, isLoadMore)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBillVquDateTimeEvent(event: BillVquTimeSelectEvent) {
        if (event.type == type) {
            mVquDate = event.date
            loadData(false)
        }
    }
}