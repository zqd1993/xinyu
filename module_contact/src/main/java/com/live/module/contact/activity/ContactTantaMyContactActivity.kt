package com.live.module.contact.activity

import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.contact.R
import com.live.module.contact.adapter.ContactTantaOverviewAdapter
import com.live.module.contact.databinding.ContactVquActivityMyContactBinding
import com.live.module.contact.dialog.ContactVquSortDialog
import com.live.module.contact.dialog.ContactVquTimeDialog
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.DateUtils
import com.live.vquonline.base.utils.copyString
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.addStatusBarHeight
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.view.decoration.VerticalItemDecoration
import com.mshy.VInterestSpeed.common.utils.FontsFamily
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Contact.ContactTantaMyContactActivity)
class ContactTantaMyContactActivity :
    BaseActivity<ContactVquActivityMyContactBinding, ContactTantaMyContactViewModel>() {
    override val mViewModel: ContactTantaMyContactViewModel by viewModels()

    private var mVquAfterKey = 0

    private var mVquBeginTime = ""
    private var mVquEndTime = ""

    private var mSortType = ContactVquSortDialog.USER_ID

    private val mAdapter = ContactTantaOverviewAdapter()

    private val mVquTimeDialog = ContactVquTimeDialog().apply {
        setRange(31)
        setOnSelectListener(object :
            ContactVquTimeDialog.OnSelectListener {
            override fun setSelect(startTime: String, endTime: String) {

                mBinding.stvContactVquMyContactDate.text = "${startTime}~${endTime}"

                mVquAfterKey = 0
                mVquBeginTime = startTime
                mVquEndTime = endTime
                mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
            }
        })
    }

    @Autowired(name = RouteKey.URL)
    lateinit var url: String

    override fun ContactVquActivityMyContactBinding.initView() {
        mBinding.tvContactVquMyContactTotalMoney.typeface = FontsFamily.tfDDinExpBold
//        setLoadSir(mBinding.flEmptyView)

        (mBinding.rvContactVquMyContactList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        mBinding.rvContactVquMyContactList.addItemDecoration(
            VerticalItemDecoration(
                this@ContactTantaMyContactActivity, 8f
            )
        )
        mBinding.rvContactVquMyContactList.adapter = mAdapter

        mBinding.tvContactVquMyContactBack.setViewClickListener {
            finish()
        }

        mBinding.tvContactVquMyContactInvitedRecord.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaInvitedRecordActivity)
                .navigation()
        }

        mBinding.tvContactVquMyContactInvitedRecordBottom.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaInvitedRecordActivity)
                .navigation()
        }

        mBinding.stvContactVquMyContactMore.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, url).navigation()
        }

        mBinding.stvContactVquMyContactMoreBottom.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, url).navigation()
        }

        mBinding.tvExport.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaExportActivity)
                .withString("startTime", mVquBeginTime)
                .withString("endTime", mVquEndTime).navigation()
        }

        mBinding.stvContactVquMyContactDate.setViewClickListener {
            mVquTimeDialog.show(supportFragmentManager, "")
        }

        mBinding.stvContactVquMyContactSort.setViewClickListener {
            clickSort()
        }

        mBinding.srlContactVquMyContactRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mVquAfterKey = 0
                requestData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                requestData()
            }
        })

        initAdapter()

    }

    private fun clickSort() {
        val dialog = ContactVquSortDialog()
        dialog.vquSetOnSortTypeClickListener {
            mSortType = it
            mVquAfterKey = 0
            requestData()
        }
        dialog.show(supportFragmentManager, "")
    }

    fun requestData() {
        mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
    }

    override fun setStatusBar() {

        ImmersionBar.with(this@ContactTantaMyContactActivity)
            .transparentStatusBar().statusBarDarkFont(false).init()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.toolbar.addStatusBarHeight(true)
    }

    private fun initAdapter() {
        mAdapter.setOnItemChildClickListener { _, view, position ->

            val item =
                mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.tv_contact_vqu_item_my_contact_id -> {
                    copyString(item.usercode)
                    toast(R.string.vqu_bill_copy_success)
                }
                R.id.tv_contact_vqu_item_my_contact_more -> {
                    item.isSelected = !item.isSelected
                    mAdapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun initObserve() {
        mViewModel.vquOverviewData.observe(this) {
//            mVquAfterKey = it.afterKey
            mBinding.tvContactVquMyContactTotalMoney.text = it.totalIncome
            mBinding.srlContactVquMyContactRefresh.finishRefresh()
            mBinding.srlContactVquMyContactRefresh.finishLoadMore()

            if (mVquAfterKey == 0) {
                mAdapter.setNewInstance(it.list)
            } else {
                mAdapter.addData(it.list)
            }

            mBinding.srlContactVquMyContactRefresh.setEnableLoadMore(it.list.isNotEmpty())

            if (mAdapter.itemCount > 0) {
                mBinding.clContactVquMyContactInvitedNumParent.visibility = View.VISIBLE
                mBinding.tvContactVquMyContactInvitedNum.text =
                    getString(R.string.contact_vqu_my_invited, it.totalPeople)
                mBinding.stvContactVquMyContactMoreBottom.visibility = View.GONE
                mBinding.tvContactVquMyContactInvitedRecordBottom.visibility = View.GONE
//                showContent()
                mBinding.flEmptyView.gone()
                //工会模式或者星推官，可导出
                if ( UserManager.userInfo?.scoutModel == 1) {
                    mBinding.tvExport.visible()
                } else {
                    mBinding.tvExport.gone()
                }

            } else {
                mBinding.clContactVquMyContactInvitedNumParent.visibility = View.GONE
//                mBinding.clContactVquMyContactInvitedNumParent.visibility = View.VISIBLE
                mBinding.stvContactVquMyContactMoreBottom.visibility = View.VISIBLE
                mBinding.tvContactVquMyContactInvitedRecordBottom.visibility = View.VISIBLE
                mBinding.flEmptyView.visible()
//                showEmpty("邀请好友，即享高额提成")
            }

            mVquAfterKey = it.afterKey

        }
    }

    override fun initRequestData() {

        mVquBeginTime = DateUtils.getFirstDayOfThisMonth() ?: ""
        mVquEndTime = DateUtils.getDateFormatString(System.currentTimeMillis(), "yyyy-MM-dd")
        mBinding.stvContactVquMyContactDate.text = "${mVquBeginTime}至${mVquEndTime}"
        com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("initRequestData")
        mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
    }

}