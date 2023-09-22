package com.live.module.dynamic.activity

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.dynamic.R
import com.live.module.dynamic.adapter.DynamicTantaLikeAdapter
import com.live.module.dynamic.databinding.DynamicTantaActivityLikeBinding
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.BaseApplication.Companion.context
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setNbOnItemClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Tany
 * date: 2022/4/9
 * description:动态页面
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquLikeListActivity)
class DynamicTantaLikeListActivity :
    BaseActivity<DynamicTantaActivityLikeBinding, DynamicPublishViewModel>(),
    OnRefreshListener, OnLoadMoreListener {
    var page: Int = 1
    var likeAdapter = DynamicTantaLikeAdapter()
    override val mViewModel: DynamicPublishViewModel by viewModels()
    override fun DynamicTantaActivityLikeBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.dynamic_dynamic_message)) {
            finish()
        }
        setLoadSir(smart)
        mBinding.smart.setOnRefreshListener(this@DynamicTantaLikeListActivity)
        mBinding.smart.setOnLoadMoreListener(this@DynamicTantaLikeListActivity)
        mBinding.rvDynamic.layoutManager = LinearLayoutManager(context)
        (mBinding.rvDynamic.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        mBinding.rvDynamic.adapter = likeAdapter
        likeAdapter.setNbOnItemClickListener { adapter, view, position ->
            if (likeAdapter.data[position].isDelete != 1) {
                ARouter.getInstance()
                    .build(RouteUrl.Dynamic.DynamicVquDynamicDetailActivity)
                    .withString(
                        RouteKey.ID,
                        likeAdapter.data[position].dynamicId.toString()
                    )
                    .navigation()
            }
        }
    }


    override fun initObserve() {
        mViewModel.dynamicLikeLsit.observe(this) {
            if (page == 1) {
                if (it.data.list.isNullOrEmpty()) {
                    showEmpty("暂无动态消息", R.mipmap.dynamic_empty_icon)
                } else {
                    mLoadService?.showSuccess()
                    likeAdapter?.setList(it.data.list)
                }
            } else {
                if (!it.data.list.isNullOrEmpty()) {
                    likeAdapter?.addData(it.data.list)
                }
            }
            likeAdapter?.notifyDataSetChanged()
            finishLoad()
        }
    }

    override fun initRequestData() {
        mViewModel.vquDynamicLikeList(page)
    }

    override fun onRetryBtnClick() {
        page = 1
        mViewModel.vquDynamicLikeList(page)
    }

    fun finishLoad() {
        mBinding.smart.finishRefresh()
        mBinding.smart.finishLoadMore()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.vquDynamicLikeList(page)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page += 1
        mViewModel.vquDynamicLikeList(page)
    }

}