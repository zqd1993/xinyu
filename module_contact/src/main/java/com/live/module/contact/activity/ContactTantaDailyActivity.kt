package com.live.module.contact.activity

import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.contact.adapter.ContactTantaDailyAdapter
import com.live.module.contact.databinding.ContactVquActivityDailyBinding
import com.live.module.contact.dialog.ContactVquSortDialog
import com.live.module.contact.dialog.ContactVquTimeDialog
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.DateUtils
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
 * description: 废弃了
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Contact.ContactTantaDailyActivity)
class ContactTantaDailyActivity :
    BaseActivity<ContactVquActivityDailyBinding, ContactTantaMyContactViewModel>() {

    @Autowired(name = RouteKey.URL)
    lateinit var url: String

    override val mViewModel: ContactTantaMyContactViewModel by viewModels()

    private var mVquAfterKey = 0
    private var mVquBeginTime = ""
    private var mVquEndTime = ""

    private var mSortType = ContactVquSortDialog.USER_ID

    private val mAdapter = ContactTantaDailyAdapter()

    private val mVquTimeDialog = ContactVquTimeDialog().apply {
        setRange(31)
        setOnSelectListener(object :
            ContactVquTimeDialog.OnSelectListener {
            override fun setSelect(startTime: String, endTime: String) {

                mBinding.stvContactVquMyContactDate.text = "${startTime}至${endTime}"

                mVquAfterKey = 0
                mVquBeginTime = startTime
                mVquEndTime = endTime
                mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
            }
        })
    }


    override fun ContactVquActivityDailyBinding.initView() {
        mBinding.tvContactVquMyContactTotalMoney.typeface = FontsFamily.tfDDinExpBold

        mVquBeginTime = DateUtils.getFirstDayOfThisMonth() ?: ""
        mVquEndTime = DateUtils.getDateFormatString(System.currentTimeMillis(), "yyyy-MM-dd")
        mBinding.stvContactVquMyContactDate.text = "${mVquBeginTime}至${mVquEndTime}"

        if (UserManager.userInfo?.scoutModel == 1) {
            mBinding.sllDailyScoutModelTotal.visible()
            mBinding.llNormalMyConnection.gone()
        } else {
            mBinding.sllDailyScoutModelTotal.gone()
            mBinding.llNormalMyConnection.visible()
        }

        initAdapter()
        setClickListener()
    }

    private fun setClickListener() {
        //关闭
        mBinding.tvContactVquMyContactBack.setViewClickListener {
            finish()
        }
        //我的人脉(普通模式)
        mBinding.llcMyConnection.setViewClickListener {
            jumpToMyConnection()
        }
        //我的人脉(公会模式)
        mBinding.llcMyConnection.setViewClickListener {
            jumpToMyConnection()
        }
        //邀请记录
        mBinding.tvContactVquMyContactInvitedRecord.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaInvitedRecordActivity)
                .navigation()
        }
        //邀请记录
        mBinding.tvContactVquMyContactInvitedRecordBottom.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaInvitedRecordActivity)
                .navigation()
        }

        //邀请页面
        mBinding.stvContactVquMyContactMore.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, url).navigation()
        }

        //邀请页面
        mBinding.stvContactVquMyContactMoreBottom.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, url).navigation()
        }

        //日期筛选
        mBinding.stvContactVquMyContactDate.setViewClickListener {
            mVquTimeDialog.show(supportFragmentManager, "")
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
    }

    fun jumpToMyConnection() {
        ARouter.getInstance().build(RouteUrl.Contact.ContactTantaMyContactActivity)
            .withString(RouteKey.URL, url)
            .withString(RouteKey.START_TIME, mVquBeginTime)
            .withString(RouteKey.END_TIME, mVquEndTime)
            .navigation()
    }

    fun requestData() {
        mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
    }

    override fun setStatusBar() {

        ImmersionBar.with(this@ContactTantaDailyActivity)
            .transparentStatusBar().statusBarDarkFont(false).init()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.toolbar.addStatusBarHeight(true)
    }

    private fun initAdapter() {
        (mBinding.rvContactVquMyContactList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        mBinding.rvContactVquMyContactList.addItemDecoration(
            VerticalItemDecoration(
                this@ContactTantaDailyActivity, 8f
            )
        )
        mBinding.rvContactVquMyContactList.adapter = mAdapter



        mAdapter.setOnItemClickListener { _, _, position ->
            val item =
                mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            ARouter.getInstance().build(RouteUrl.Contact.ContactTantaMyContactActivity)
                .withString(RouteKey.URL, url)
                .withString(RouteKey.START_TIME, item.date)
                .withString(RouteKey.END_TIME, item.date)
                .navigation()

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

            if(it.isAuditChannel == 1){
                mBinding.stvContactVquMyContactMoreBottom.visibility = View.GONE
                mBinding.tvContactVquMyContactInvitedRecordBottom.visibility = View.GONE
            } else {
                mBinding.stvContactVquMyContactMoreBottom.visibility = View.VISIBLE
                mBinding.tvContactVquMyContactInvitedRecordBottom.visibility = View.VISIBLE
            }

            if (mAdapter.itemCount > 0) {
                mBinding.clContactVquMyContactInvitedNumParent.visibility = View.VISIBLE
//                showContent()
                mBinding.flEmptyView.gone()

            } else {
                mBinding.clContactVquMyContactInvitedNumParent.visibility = View.GONE
//                mBinding.clContactVquMyContactInvitedNumParent.visibility = View.VISIBLE
                mBinding.flEmptyView.visible()
//                showEmpty("邀请好友，即享高额提成")
            }

            mVquAfterKey = it.afterKey

        }
    }

    override fun initRequestData() {
        mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
    }

}