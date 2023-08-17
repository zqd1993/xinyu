package com.live.module.auth.activity

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.auth.databinding.AuthVquActivityFaceIdentifyBinding
import com.live.module.auth.vm.AuthVquFaceIdentifyViewModel
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.init
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Auth.AuthVquFaceIdentifyActivity)
@Deprecated(message = "产品说不要女神协议了")
class AuthVquFaceIdentifyActivity :
    BaseActivity<AuthVquActivityFaceIdentifyBinding, AuthVquFaceIdentifyViewModel>() {
    override val mViewModel: AuthVquFaceIdentifyViewModel by viewModels()

    @Autowired(name = "agree")
    @JvmField
    var agree = 0

    override fun initObserve() {
        mViewModel.verifyData.observe(this) {
            if (it == null) {
                return@observe
            }

            mViewModel.rpVerify(this, it)
        }

        mViewModel.verifyResultData.observe(this) {

            if (it == 1) {
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquAvatarActivity)
                    .navigation()
            } else {
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                    .withInt(RouteKey.TYPE, 3)
                    .navigation()
            }

//            ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
//                .withInt(RouteKey.TYPE, 2)
//                .navigation()

            finish()
        }
    }

    override fun initRequestData() {

    }

    override fun AuthVquActivityFaceIdentifyBinding.initView() {
        mBinding.tbAuthVquFaceIdentifyBar.toolbar.initClose("实人认证") {
            finish()
        }


        mBinding.anchorContractWebview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mBinding.tbAuthVquFaceIdentifyBar.toolbar.init(
                    titleStr = mBinding.anchorContractWebview.title ?: "实人认证"
                )
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

        }
        mBinding.anchorContractWebview.loadUrl(NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.CONTRACT_URL)

        if (1 == agree) {
            mBinding.anchorContractAgreeButton.visibility = View.GONE
        } else {
            mBinding.anchorContractAgreeButton.visibility = View.VISIBLE
        }

        mBinding.anchorContractAgreeButton.setViewClickListener {
            vquAgreeContract()
        }
    }

    private fun vquAgreeContract() {
        mViewModel.vquAgreeProtocol()
    }
}