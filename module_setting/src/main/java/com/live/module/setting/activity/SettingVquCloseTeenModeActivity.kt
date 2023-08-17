package com.live.module.setting.activity

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityCloseTeenModeBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


/**
 * author: Tany
 * date: 2022/4/2
 * description:关闭青少年模式
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquCloseTeenModeActivity)
class SettingVquCloseTeenModeActivity :
    BaseActivity<SettingTantaActivityCloseTeenModeBinding, SettingVquViewModel>() {
    var vquMobile: String? = ""
    var vquCode: String? = ""
    override val mViewModel: SettingVquViewModel by viewModels()
    override fun SettingTantaActivityCloseTeenModeBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_teen_mode_close)) {
            finish()
        }
        mBinding.tvSend.setViewClickListener { vquSendCode() }
        mBinding.tvBind.setViewClickListener { bindMobile() }
        mBinding.etCode.addTextChangedListener(textWatcher)
    }

    private fun bindMobile() {
        mViewModel.vquCloseModeByCode(vquMobile!!, vquCode!!)

    }

    var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            vquMobile = mBinding.etMobile.text.toString().trim()
            vquCode = mBinding.etCode.text.toString().trim()
            if (!vquMobile.isNullOrEmpty() && vquMobile?.length == 11 && !vquCode.isNullOrEmpty() && vquCode?.length == 4) {
                mBinding.tvBind.isEnabled = true
                mBinding.tvBind.isClickable = true
                mBinding.tvBind.setStartColor(
                    resources.getColor(R.color.color_BFB6FF),
                    resources.getColor(R.color.color_7C69FE)
                )
            } else {
                mBinding.tvBind.isEnabled = false
                mBinding.tvBind.isClickable = false
                mBinding.tvBind.setStartColor(
                    resources.getColor(R.color.color_D3D1D7),
                    resources.getColor(R.color.color_D3D1D7)
                )
            }

        }

    }

    /**
     * 发送验证码
     */
    private fun vquSendCode() {
        vquMobile = mBinding.etMobile.text.toString().trim()
        if (!vquMobile?.isNullOrEmpty()!! && vquMobile?.length == 11) {
            mViewModel.vquSendCloseCode(vquMobile!!)
        } else {
            "请输入正确的手机号".toast()
        }
    }

    override fun initObserve() {
        mViewModel.vquSendCodeData.observe(this, Observer {
            mTimer?.start()
            mBinding.tvSend.isEnabled = false
            mBinding.tvSend.isClickable = false
        })
        mViewModel.vquBindData.observe(this, Observer {
            "关闭成功".toast()
            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.KidEvent(1));
            finish()
        })
    }

    override fun initRequestData() {
    }

    private val mTimer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        @SuppressLint("SetTextI18n")
        override fun onTick(l: Long) {
            mBinding.tvSend.text = (l / 1000).toString() + "s"
        }

        override fun onFinish() {
            mBinding.tvSend.isEnabled = true
            mBinding.tvSend.isClickable = true
            mBinding.tvSend.text = resources.getString(R.string.setting_code_get)
        }
    }
}