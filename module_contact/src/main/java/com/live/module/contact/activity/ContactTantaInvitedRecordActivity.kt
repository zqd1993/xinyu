package com.live.module.contact.activity

import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.contact.adapter.ContactTantaInvitedAdapter
import com.live.module.contact.databinding.ContactVquActivityInvitedRecordBinding
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.view.decoration.VerticalItemDecoration
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/6/22
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Contact.ContactTantaInvitedRecordActivity)
class ContactTantaInvitedRecordActivity :
    BaseActivity<ContactVquActivityInvitedRecordBinding, ContactTantaMyContactViewModel>() {
    override val mViewModel: ContactTantaMyContactViewModel by viewModels()

    private val mAdapter = ContactTantaInvitedAdapter()

    override fun initObserve() {
        mViewModel.isEmptyData.observe(this) {
            if (it) {
                showEmpty()
            } else {
                showContent()
            }
        }

        mViewModel.vquLoadMoreData.observe(this) {
            mBinding.srlContactVquInvitedRecordRefresh.finishLoadMore()
            mBinding.srlContactVquInvitedRecordRefresh.finishRefresh()
            if (it.isNullOrEmpty()) {
                mBinding.srlContactVquInvitedRecordRefresh.setEnableLoadMore(false)
            } else {
                mAdapter.addData(it)
                mBinding.srlContactVquInvitedRecordRefresh.setEnableLoadMore(true)
            }
        }

        mViewModel.vquRefreshData.observe(this) {
            mBinding.srlContactVquInvitedRecordRefresh.finishLoadMore()
            mBinding.srlContactVquInvitedRecordRefresh.finishRefresh()
            if (it.isNullOrEmpty()) {
                mAdapter.setNewInstance(mutableListOf())
                mBinding.srlContactVquInvitedRecordRefresh.setEnableLoadMore(false)
            } else {
                mAdapter.setNewInstance(it)
                mBinding.srlContactVquInvitedRecordRefresh.setEnableLoadMore(true)
            }
        }

        mViewModel.vquInvitedCountData.observe(this) {
            if (it > 0) {
                mBinding.tvContactVquInvitedRecordNum.visibility = View.VISIBLE
            } else {
                mBinding.tvContactVquInvitedRecordNum.visibility = View.GONE
            }

            mBinding.tvContactVquInvitedRecordNum.text = "已邀请${it}人"
        }

    }

    override fun initRequestData() {
        mViewModel.vquInviteList(false, "")
    }

    override fun ContactVquActivityInvitedRecordBinding.initView() {

        setLoadSir(mBinding.srlContactVquInvitedRecordRefresh)

        mBinding.rvContactVquInvitedRecordList.addItemDecoration(
            VerticalItemDecoration(
                this@ContactTantaInvitedRecordActivity, 8f, 12f, 8f
            )
        )

        mBinding.rvContactVquInvitedRecordList.adapter = mAdapter

        mBinding.tbContactVquInvitedRecordBar.toolbar.initClose("邀请记录") {
            finish()
        }

        mBinding.srlContactVquInvitedRecordRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.vquInviteList(false, "")
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.vquInviteList(true, "")
            }

        })

        initEvent()
    }

    private fun initEvent() {
        mAdapter.setOnItemClickListener { _, _, position ->
            val contactVquInvitedData =
                mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            if (!contactVquInvitedData.id.isNullOrEmpty()) {
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                        RouteKey.USERID,
                        contactVquInvitedData.id.toInt()
                    )
                    .navigation()
            }
        }

        mBinding.ivSearch.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaInvitedRecordSearchActivity)
                .navigation()
        }
    }

}