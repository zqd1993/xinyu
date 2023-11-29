package com.live.module.contact.activity

import android.widget.EditText
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.contact.R
import com.live.module.contact.adapter.ContactTantaInvitedAdapter
import com.live.module.contact.databinding.ContactVquActivityInvitedRecordSearchBinding
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initSearch
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
@Route(path = RouteUrl.Contact.ContactTantaInvitedRecordSearchActivity)
class ContactTantaInvitedRecordSearchActivity :
    BaseActivity<ContactVquActivityInvitedRecordSearchBinding, ContactTantaMyContactViewModel>() {
    override val mViewModel: ContactTantaMyContactViewModel by viewModels()

    private val mAdapter = ContactTantaInvitedAdapter()

    private var mVquKeyword = ""


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

    }

    override fun initRequestData() {
//        mViewModel.vquInviteList(false, "")
    }

    override fun ContactVquActivityInvitedRecordSearchBinding.initView() {


        setLoadSir(mBinding.srlContactVquInvitedRecordRefresh)

        mBinding.rvContactVquInvitedRecordList.addItemDecoration(
            VerticalItemDecoration(
                this@ContactTantaInvitedRecordSearchActivity, 8f, 12f, 8f
            )
        )

        mBinding.rvContactVquInvitedRecordList.adapter = mAdapter

        mBinding.srlContactVquInvitedRecordRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.vquInviteList(false, mVquKeyword)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.vquInviteList(true, mVquKeyword)
            }

        })

        mBinding.tbContactVquInvitedRecordSearchBar.toolbar.initSearch("搜索甜缘号或昵称", onSearch = {
            val keyword = it

            if (keyword.isEmpty()) {
                toast("请输入用户甜缘号或昵称")
                return@initSearch
            }

            mVquKeyword = keyword
            mViewModel.vquInviteList(false, mVquKeyword)

//            if (keyword.isEmpty() && mAdapter.data.isNullOrEmpty()) {
//                finish()
//            } else {
//                mVquKeyword = ""
//                mBinding.setContactVquInvitedRecordSearchKeyword.setText("")
//                mAdapter.setNewInstance(mutableListOf())
//            }
        }, onBack = {
            finish()
        })

        initEvent()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val tvTitle =
                mBinding.tbContactVquInvitedRecordSearchBar.toolbar.findViewById<EditText>(R.id.tv_title)
            tvTitle.isFocusable = true
            tvTitle.requestFocus()
            tvTitle.postDelayed({
                AppManager.getInstance()
                    .showSoftKeyBoard(this, tvTitle)
            }, 200)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

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

//        mBinding.setContactVquInvitedRecordSearchKeyword.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
//
//
//                return@setOnEditorActionListener true
//            }
//            return@setOnEditorActionListener false
//        }
    }


}