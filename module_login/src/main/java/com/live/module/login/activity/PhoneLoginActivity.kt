package com.live.module.login.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.login.R
import com.live.module.login.databinding.LoginActivityPhoneLoginBinding
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
@EventBusRegister
@Route(path = RouteUrl.Login.LoginTantaPhoneLoginActivity)
class PhoneLoginActivity : BaseActivity<LoginActivityPhoneLoginBinding, LoginViewModel>() {

    override val mViewModel: LoginViewModel by viewModels()

    private var mSendPhone = ""

    override fun LoginActivityPhoneLoginBinding.initView() {
        mBinding.includeToolBar.toolbar.initClose(backImg = R.mipmap.ic_back) { finish() }

        val phone = SpUtils.getString(SpKey.PHONE, "")

//        mBinding.cetPhoneLoginNumber.editText.addTextChangedListener {
//            mBinding.btnPhoneLoginNext.isEnabled = (it?.length ?: 0) >= 13
//        }

        val phoneTextWatcherSpace =
            com.mshy.VInterestSpeed.common.ui.view.phoneview.PhoneTextWatcher()

        mBinding.cetPhoneLoginNumber.addTextChangedListener(phoneTextWatcherSpace)

        phoneTextWatcherSpace.setTextChangedCallback(object :
            com.mshy.VInterestSpeed.common.ui.view.phoneview.TextChangeCallback() {
            override fun afterTextChanged(unformatted: String?, isPhoneNumberValid: Boolean) {
                mBinding.btnPhoneLoginNext.isEnabled = (unformatted?.length ?: 0) >= 11
            }
        })

        mBinding.cetPhoneLoginNumber.setText(phone)
        mBinding.cetPhoneLoginNumber.setSelection(
            mBinding.cetPhoneLoginNumber.text?.length ?: 0
        )

        mBinding.btnPhoneLoginNext.clickDelay { sendCode(phoneTextWatcherSpace.unformatted(mBinding.cetPhoneLoginNumber.text)) }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            mBinding.cetPhoneLoginNumber.isFocusable = true
            mBinding.cetPhoneLoginNumber.requestFocus()
            mBinding.cetPhoneLoginNumber.postDelayed({
                AppManager.getInstance()
                    .showSoftKeyBoard(this, mBinding.cetPhoneLoginNumber)
            }, 500)
        }
    }

    override fun initObserve() {


        mViewModel.sendCodeResult.observe(this) {
            if (it) {
                SpUtils.putString(SpKey.PHONE, mSendPhone)
                ARouter.getInstance().build(RouteUrl.Login.LoginTantaAuthCodeActivity)
                    .withString(RouteKey.PHONE, mSendPhone).navigation()
            }
        }
    }

    override fun initRequestData() {

    }

    private fun sendCode(phone: String) {
        mSendPhone = phone
        mViewModel.tantaSendCode(phone)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun finishEvent(event: LoginEvent) {
        finish()
    }


}