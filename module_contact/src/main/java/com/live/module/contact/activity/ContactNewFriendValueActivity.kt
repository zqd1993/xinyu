package com.live.module.contact.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.gyf.immersionbar.ImmersionBar
import com.live.module.contact.R
import com.live.module.contact.databinding.ContactVquActivityFriendValueBinding
import com.live.module.contact.dialog.ContactVquSortDialog
import com.live.module.contact.dialog.ContactVquTimeDialog
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.DateUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.ext.addStatusBarHeight
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/22
 * description:
 */
@AndroidEntryPoint
class ContactNewFriendValueActivity :
    BaseActivity<ContactVquActivityFriendValueBinding, ContactTantaMyContactViewModel>() {

    @Autowired(name = RouteKey.START_TIME)
    @JvmField
    var startTime: String = ""

    @Autowired(name = RouteKey.END_TIME)
    @JvmField
    var endTime: String = ""

    override val mViewModel: ContactTantaMyContactViewModel by viewModels()

    private var mVquAfterKey = 0

    private var mVquBeginTime = ""
    private var mVquEndTime = ""

    private var mSortType = ContactVquSortDialog.USER_ID


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


    override fun ContactVquActivityFriendValueBinding.initView() {
        if (startTime.isNullOrEmpty()) {
            mVquBeginTime = startTime
            mVquEndTime = endTime
        } else {
            mVquBeginTime = DateUtils.getFirstDayOfThisMonth() ?: ""
            mVquEndTime = DateUtils.getDateFormatString(System.currentTimeMillis(), "yyyy-MM-dd")
        }
        if (UserManager.userInfo?.scoutModel == 1) {
            mBinding.llcScoutTotalValue.visible()
            mBinding.llcScoutTotalContribute.visible()
        } else {
            mBinding.llcNormalTotalValue.visible()
            mBinding.llcScoutTotalValue.gone()
            mBinding.llcScoutTotalContribute.gone()
        }



        mBinding.stvContactVquMyContactDate.text = "${mVquBeginTime}至${mVquEndTime}"

        mBinding.tvContactVquMyContactBack.setViewClickListener {
            finish()
        }

        mBinding.stvContactVquMyContactDate.setViewClickListener {
            mVquTimeDialog.show(supportFragmentManager, "")
        }

        mBinding.tvUserId.setViewClickListener(1) {
            val cm: ClipboardManager =
                BaseApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(
                ClipData.newPlainText(
                    null,
                    UserManager.userInfo?.usercode ?: ""
                )
            )

            toast(R.string.vqu_bill_copy_success)
        }

        initAdapter()

    }

    override fun setStatusBar() {

        ImmersionBar.with(this@ContactNewFriendValueActivity)
            .transparentStatusBar().statusBarDarkFont(false).init()
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.toolbar.addStatusBarHeight(true)
    }

    private fun initAdapter() {
    }

    override fun initObserve() {
        mViewModel.vquOverviewData.observe(this) {

        }
    }

    override fun initRequestData() {
        mViewModel.vquOverview(mVquAfterKey, mVquBeginTime, mVquEndTime, mSortType)
    }

}