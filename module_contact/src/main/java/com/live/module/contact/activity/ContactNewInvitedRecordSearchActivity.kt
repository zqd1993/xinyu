package com.live.module.contact.activity

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.contact.adapter.ContactTantaInvitedAdapter
import com.live.module.contact.databinding.ContactVquActivityInvitedRecordSearchNewBinding
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/6/22
 * description:
 */
@AndroidEntryPoint
class ContactNewInvitedRecordSearchActivity :
    BaseActivity<ContactVquActivityInvitedRecordSearchNewBinding, ContactTantaMyContactViewModel>() {
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

    override fun ContactVquActivityInvitedRecordSearchNewBinding.initView() {


        setLoadSir(mBinding.srlContactVquInvitedRecordRefresh)

        mBinding.rvContactVquInvitedRecordList.adapter = mAdapter
//        mAdapter.hideTotalValue = UserManager.userInfo?.scoutModel != 1

        mBinding.srlContactVquInvitedRecordRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.vquInviteList(false, mVquKeyword)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.vquInviteList(true, mVquKeyword)
            }

        })
        mBinding.ivBack.setOnClickListener {
            finish()
        }
        mBinding.ivSearchClear.setOnClickListener {
            mVquKeyword = ""
            mBinding.setContactVquInvitedRecordSearchKeyword.setText("")
            mAdapter.setNewInstance(mutableListOf())
        }
        initEvent()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            mBinding.setContactVquInvitedRecordSearchKeyword.isFocusable = true
            mBinding.setContactVquInvitedRecordSearchKeyword.requestFocus()
            mBinding.setContactVquInvitedRecordSearchKeyword.postDelayed({
                AppManager.getInstance()
                    .showSoftKeyBoard(this, mBinding.setContactVquInvitedRecordSearchKeyword)
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
        mBinding.tvSearch.setOnClickListener {
            val keyword = mBinding.setContactVquInvitedRecordSearchKeyword.text.toString()

            if (keyword.isEmpty()) {
                toast("请输入用户山海甜缘号或昵称")
                return@setOnClickListener
            }

            mVquKeyword = keyword
            mViewModel.vquInviteList(false, mVquKeyword)
        }
        mBinding.setContactVquInvitedRecordSearchKeyword.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                val imm: InputMethodManager =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    mBinding.setContactVquInvitedRecordSearchKeyword.windowToken,
                    0
                )

                val keyword = mBinding.setContactVquInvitedRecordSearchKeyword.text.toString()

                if (keyword.isEmpty()) {
                    toast("请输入用户山海甜缘号或昵称")
                    return@setOnEditorActionListener false
                }

                mVquKeyword = keyword
                mViewModel.vquInviteList(false, mVquKeyword)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }


}