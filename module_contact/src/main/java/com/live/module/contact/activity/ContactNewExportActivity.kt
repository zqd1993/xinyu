package com.live.module.contact.activity

import android.widget.TextView
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.live.module.contact.R
import com.live.module.contact.databinding.ContactVquActivityExportNewBinding
import com.live.module.contact.dialog.ContactVquTimeDialog
import com.live.module.contact.vm.ContactTantaMyContactViewModel
import com.mshy.VInterestSpeed.common.constant.RouteKey
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
class ContactNewExportActivity :
    BaseActivity<ContactVquActivityExportNewBinding, ContactTantaMyContactViewModel>() {
    @Autowired(name =  RouteKey.START_TIME)
    @JvmField
    var myStartTime: String = ""

    @Autowired(name = RouteKey.END_TIME)
    @JvmField
    var myEndTime: String = ""
    var sex: Int = 1
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

    override fun ContactVquActivityExportNewBinding.initView() {
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
        mBinding.tvSexAll.setOnClickListener {
            sexSelect(1)
        }
        mBinding.tvSexMan.setOnClickListener {
            sexSelect(2)
        }
        mBinding.tvSexWoman.setOnClickListener {
            sexSelect(3)
        }
    }

    private fun sexSelect(sexIndex: Int) {
        sex = sexIndex
        setTextConfig(mBinding.tvSexAll, sex == 1)
        setTextConfig(mBinding.tvSexMan, sex == 2)
        setTextConfig(mBinding.tvSexWoman, sex == 3)
    }

    private fun setTextConfig(textView: TextView, isSelect: Boolean) {
        textView.apply {
            if (isSelect) {
                context?.resources?.getColor(R.color.color_FF7AC2)?.let { setTextColor(it) }
                background = context?.getDrawable(R.drawable.bg_stroke_ff7ac2)
            } else {
                context?.resources?.getColor(R.color.color_black_273145)?.let { setTextColor(it) }
                background = context?.getDrawable(R.drawable.bg_gray_stroke_d3d1d7)
            }
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