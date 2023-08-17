package com.live.module.mine.fragment

import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.mine.adapter.MineVquTryCardAdapter
import com.live.module.mine.bean.MineVquCouponBean
import com.live.module.mine.databinding.MineVquFragmentBackpackCardBinding
import com.live.module.mine.vm.MineVquMyBackpackViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Mine.MineVquTryCardFragment)
class MineVquTryCardFragment :
    BaseFragment<MineVquFragmentBackpackCardBinding, MineVquMyBackpackViewModel>() {

    private val mAdapter = MineVquTryCardAdapter()

    override val mViewModel: MineVquMyBackpackViewModel by viewModels()

    override fun MineVquFragmentBackpackCardBinding.initView() {
        setLoadSir(mBinding.srlMineBackpackCardRefresh)
        initAdapter()
        mBinding.srlMineBackpackCardRefresh.setOnRefreshListener {
            mViewModel.getPackage("noble_experience_card")
        }
    }

    private fun initAdapter() {
        mBinding.rvMineVquBackpackCardList.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->
            val bean =
                mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            showUseDialog(bean)
        }
    }

    private fun showUseDialog(bean: MineVquCouponBean) {
        val dialog = MessageDialog()
        dialog.setTitle("提示").setContent("确定使用？")
            .setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
                override fun onLeftClick(): Boolean {
                    return false
                }

                override fun onRightClick(): Boolean {
                    vquUseCard(bean)
                    return false
                }

            })
        dialog.show(childFragmentManager, "")
    }

    private fun vquUseCard(bean: MineVquCouponBean) {
        mViewModel.vquUseNobleCard(bean.id)
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
                mViewModel.getPackage("noble_experience_card")
            }
        }
    }

    override fun initRequestData() {
        mViewModel.getPackage("noble_experience_card")
    }
}