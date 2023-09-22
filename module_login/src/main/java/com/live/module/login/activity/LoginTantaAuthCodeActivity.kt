package com.live.module.login.activity

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.login.R
import com.live.module.login.databinding.LoginActivityAuthCodeBinding
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.StateLayoutEnum
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.RiskControlUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/4/1
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Login.LoginTantaAuthCodeActivity)
@EventBusRegister
class LoginTantaAuthCodeActivity :
    BaseActivity<LoginActivityAuthCodeBinding, LoginViewModel>() {

    @Autowired(name = RouteKey.PHONE)
    @JvmField
    var mPhone: String = ""

    private val mTantaLoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this, "请求中...")
    }

    override val mViewModel: LoginViewModel by viewModels()

    override fun LoginActivityAuthCodeBinding.initView() {
        mBinding.includeToolBar.toolbar.initClose(backImg = R.mipmap.ic_back) {
            finish()
        }
        mBinding.btnTantaLoginAuthCodeSendCode.isEnabled = false
        mBinding.btnTantaLoginAuthCodeSendCode.clickDelay {
            mBinding.btnTantaLoginAuthCodeSendCode.isEnabled = false
            mViewModel.tantaSendCode(mPhone)
        }

        mBinding.tvTantaAuthCodeTipsPhone.text = mPhone

        mBinding.civTantaAuthCodeNumber.addTextChangedListener {
            if ((it?.length ?: 0) >= 4) {
                tantaLogin()
            }
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            mBinding.civTantaAuthCodeNumber.isFocusable = true
            mBinding.civTantaAuthCodeNumber.requestFocus()
            mBinding.civTantaAuthCodeNumber.postDelayed({
                AppManager.getInstance().showSoftKeyBoard(this, mBinding.civTantaAuthCodeNumber)
            }, 500)
        }
    }

    private fun tantaLogin() {
        RiskControlUtil.getToken(this@LoginTantaAuthCodeActivity,4)
        mViewModel.tantaCodeLogin(mPhone, mBinding.civTantaAuthCodeNumber.text.toString())
    }

    override fun initObserve() {
        mViewModel.time.observe(this) {
            mBinding.btnTantaLoginAuthCodeSendCode.text = it
            mBinding.btnTantaLoginAuthCodeSendCode.isEnabled =
                it.equals(ResUtils.getString(R.string.login_send_auth_code))
        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> mTantaLoadingDialog.show()
                else -> {
                    mTantaLoadingDialog.dismiss()
                }
            }
        }
    }

    override fun initRequestData() {
//        mViewModel.vquSendCode(mPhone)

        mViewModel.tantaStartDownTime()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: LoginEvent) {
        finish()
    }
}