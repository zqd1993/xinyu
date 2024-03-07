package com.live.module.relation.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.relation.R
import com.live.module.relation.adapter.MyProtectionListAdapter
import com.live.module.relation.bean.MyProtectionListBean
import com.live.module.relation.databinding.MyProtectionFragmentBinding
import com.live.module.relation.vm.MyProtectionViewModel
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteUrl.Relation.MyProtectionFragment)
class MyProtectionFragment : BaseFragment<MyProtectionFragmentBinding, MyProtectionViewModel>() {

    private val mAdapter = MyProtectionListAdapter()

    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0

    override val mViewModel: MyProtectionViewModel
            by viewModels()

    override fun initObserve() {
        mViewModel.protectionList.observe(this) {
            if (mViewModel.mPage == 1 && it.isNullOrEmpty()) {
                mBinding.srlProtectionRefresh.finishRefresh()
                showEmpty("你还没有守护的人哦", R.mipmap.resources_protection_empty)
            } else {
                mLoadService?.showSuccess()
                if (mViewModel.mPage == 1) {
                    mBinding.srlProtectionRefresh.finishRefresh()
                    mAdapter.setNewInstance(it)
                } else {
                    mBinding.srlProtectionRefresh.finishLoadMore()
                    mAdapter.addData(it)
                }
            }
        }
    }

    override fun initRequestData() {
        mViewModel.vquGetList(type, false)
    }

    override fun MyProtectionFragmentBinding.initView() {
        setLoadSir(mBinding.srlProtectionRefresh)

        mBinding.rvProtectionList.adapter = mAdapter
        mBinding.rvProtectionList.animation = null
        var recycledViewPool = RecyclerView.RecycledViewPool()
        recycledViewPool.setMaxRecycledViews(0, 10)
        mBinding.rvProtectionList.setRecycledViewPool(recycledViewPool)

        mAdapter.setOnItemClickListener { _, _, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            var protectionId = 0
            when (type) {
                RouteKey.RelationType.PROTECTION_ME -> {
                    protectionId = item.guardianUserId.toInt()
                }

                RouteKey.RelationType.MY_PROTECTION -> {
                    protectionId = item.userId.toInt()
                }
            }
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    protectionId
                )
                .navigation()
        }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.parent_cl -> {

                }
            }
        }

        mBinding.srlProtectionRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.vquGetList(type, false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.vquGetList(type, true)
            }
        })
    }
}