package com.live.module.contact.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.contact.R
import com.live.module.contact.databinding.ContactVquActivityExportBinding
import com.live.module.contact.dialog.ContactVquTimeDialog
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Tany
 * date: 2022/5/26
 * description:我的人脉导出
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Contact.ContactTantaExportActivity)
class ContactTantaExportActivity :
    BaseActivity<ContactVquActivityExportBinding, ContactTantaMyContactViewModel>() {
    @Autowired(name = "startTime")
    @JvmField
    var myStartTime: String = ""

    @Autowired(name = "endTime")
    @JvmField
    var myEndTime: String = ""
    override val mViewModel: ContactTantaMyContactViewModel by viewModels()

    private val mTimeDialog by lazy {
        val dialog = ContactVquTimeDialog()
        dialog.setOnSelectListener(object : ContactVquTimeDialog.OnSelectListener {
            override fun setSelect(startTime: String, endTime: String) {
                mBinding.tvTime.text = "$startTime~$endTime"
                myStartTime = startTime
                myEndTime = endTime
            }

        })
        dialog
    }

    override fun ContactVquActivityExportBinding.initView() {
        mBinding.tvTime.text = "$myStartTime~$myEndTime"

        mBinding.includeTitle.toolbar.initClose(getString(R.string.mine_vqu_menu_contacts)) {
            finish()
        }
        mBinding.llTime.setOnClickListener {
            mTimeDialog.setStartDate(myStartTime)
            mTimeDialog.setEndDate(myEndTime)
            mTimeDialog.show(supportFragmentManager, "选择时间")
        }
        mBinding.tvExport.setOnClickListener {
            var email = mBinding.etEmail.text.toString()

            if (email.isEmpty()) {
                "请输入邮箱".toast()
                return@setOnClickListener
            }
            if (!email.contains("@")) {
                "邮箱格式不正确".toast()
                return@setOnClickListener
            }
            if (myStartTime.isEmpty()) {
                "请选择时间".toast()
                return@setOnClickListener
            }
            mViewModel.vquExcel2email(email, myStartTime, myEndTime)
        }
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
        mViewModel.vquExcelViewData.observe(this, androidx.lifecycle.Observer {
            it.message.toast()
            finish()
        })
    }


}