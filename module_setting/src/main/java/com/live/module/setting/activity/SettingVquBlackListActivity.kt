package com.live.module.setting.activity

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.setting.R
import com.live.module.setting.adapter.SettingVquBlackListAdapter
import com.live.module.setting.databinding.SettingTantaActivityBlacklistBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setNbOnItemChildClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Tany
 * date: 2022/4/2
 * description:黑名单页面
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquBlackListActivity)
class SettingVquBlackListActivity :
    BaseActivity<SettingTantaActivityBlacklistBinding, SettingVquViewModel>(), OnRefreshListener,
    OnLoadMoreListener {
    var page: Int = 1
    var curPos = 0
    var vquAdapter = SettingVquBlackListAdapter()
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityBlacklistBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_blacklist)) {
            finish()
        }
        setLoadSir(smart)
        smart.setOnRefreshListener(this@SettingVquBlackListActivity)
        smart.setOnLoadMoreListener(this@SettingVquBlackListActivity)
        rvBlacklist.layoutManager = LinearLayoutManager(
            this@SettingVquBlackListActivity,
            RecyclerView.VERTICAL, false
        )
        rvBlacklist.adapter = vquAdapter
        vquAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            curPos = position
            mViewModel.vquDoBlack(vquAdapter.data[position].userid.toString())
        }
    }

    override fun onRetryBtnClick() {
        page = 1
        mViewModel.vquGetBlackList(page)
    }

    override fun initObserve() {
        mViewModel.vquBlackListData.observe(this, Observer {
            if (page == 1) {
                if (it.data.list.isNullOrEmpty()) {
                    showEmpty("暂无数据")
                } else {
                    vquAdapter.setList(it.data.list)
                    mLoadService?.showSuccess()
                }
            } else {
                if (!it.data.list.isNullOrEmpty()) {
                    vquAdapter.addData(it.data.list)
                }
                vquAdapter.notifyDataSetChanged()
            }
            vquFinishLoad()
        })
        mViewModel.vquDoBlackData.observe(this, Observer {
            if (!vquAdapter.data.isNullOrEmpty() && vquAdapter.data.size >= (curPos + 1)) {
                vquAdapter.data.removeAt(curPos)
            }
            if (vquAdapter.data.isNullOrEmpty()) {
                showEmpty("暂无数据")
            } else {
                vquAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun initRequestData() {
        mViewModel.vquGetBlackList(page)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.vquGetBlackList(page)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page += 1
        mViewModel.vquGetBlackList(page)
    }

    fun vquFinishLoad() {
        mBinding.smart.finishRefresh()
        mBinding.smart.finishLoadMore()
    }
}