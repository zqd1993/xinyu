package com.live.module.bill.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.bill.R
import com.live.module.bill.adapter.BillVquAccountAdapter
import com.live.module.bill.databinding.BillVquActivityAccountListBinding
import com.live.module.bill.vm.BillVquAccountListViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.view.decoration.VerticalItemDecoration
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaAccountListActivity)
class BillTantaAccountListActivity :
    BaseActivity<BillVquActivityAccountListBinding, BillVquAccountListViewModel>() {
    override val mViewModel: BillVquAccountListViewModel by viewModels()

    private val mAdapter = BillVquAccountAdapter()

    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0

    override fun initObserve() {

        mViewModel.accountListData.observe(this) {
            mBinding.srlBillAccountListRefresh.finishRefresh()
            if (it.isNullOrEmpty()) {
                showEmpty()
            } else {
                mLoadService?.showSuccess()
            }
            mAdapter.setNewInstance(it)
        }

        mViewModel.accountUseResult.observe(this) {
            finish()
        }
    }

    private var isFirst = true

    override fun onResume() {
        super.onResume()
        if (!isFirst) {
            initRequestData()
        }
        isFirst = false
    }

    override fun initRequestData() {
        mViewModel.accountList()
    }

    override fun BillVquActivityAccountListBinding.initView() {

        setLoadSir(mBinding.srlBillAccountListRefresh)

        if (type == 1) {


            mBinding.tbBillVquAccountListBar.toolbar.initClose(
                "提现账号", rightStr = "新增账号", rightColor =
                R.color.color_FF8859,
                onClickRight = {
                    ARouter.getInstance().build(RouteUrl.Bill.BillTantaNewWithdrawalAccountActivity)
                        .withInt(RouteKey.TYPE, 1)
                        .navigation()
                },
                onBack = {
                    finish()
                }
            )
        } else {
            mBinding.tbBillVquAccountListBar.toolbar.initClose("提现账号") {
                finish()
            }
        }

        mBinding.rvBillVquAccountList.addItemDecoration(
            VerticalItemDecoration(
                this@BillTantaAccountListActivity,
                8f
            )
        )

        mBinding.rvBillVquAccountList.adapter = mAdapter

        initEvent()
    }

    private fun initEvent() {
        mAdapter.setOnItemClickListener { _, view, position ->

            val billVquAccountInfo =
                mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            if (billVquAccountInfo.isMonthLimit == 1) {
                toast("此账号本月已提现超额")
                return@setOnItemClickListener
            }

            mViewModel.vquAccountUser(billVquAccountInfo.id)
        }


        mAdapter.setOnItemChildClickListener { adapter, view, position ->

            val billVquAccountInfo =
                mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.stv_bill_vqu_item_account_edit -> {
                    if (billVquAccountInfo.status == 0) {
                        ARouter.getInstance()
                            .build(RouteUrl.Bill.BillTantaNewWithdrawalAccountActivity)
                            .withInt(RouteKey.TYPE, 0)
                            .withString(RouteKey.ID, billVquAccountInfo.id)
                            .withInt(RouteKey.IS_MASTER, billVquAccountInfo.isMaster)
                            .navigation()
                    }
                }
            }
        }

        mBinding.srlBillAccountListRefresh.setOnRefreshListener {
            initRequestData()
        }
    }

}