package com.mshy.VInterestSpeed.common.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonActivityWebViewBinding
import com.mshy.VInterestSpeed.common.ext.init
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.js.MyJavascriptInterface
import com.mshy.VInterestSpeed.common.ui.vm.WebViewViewModel

@Route(path = RouteUrl.Common.WebViewActivity)
class CommonVquWebViewActivity : BaseActivity<CommonActivityWebViewBinding, WebViewViewModel>() {

    override val mViewModel: WebViewViewModel by viewModels()

    @Autowired(name = RouteKey.URL)
    @JvmField
    var mUrl: String = ""

    private var mWebView: WebView? = null

    private var mIsFirst = true


    override fun CommonActivityWebViewBinding.initView() {

        mBinding.tbCommonVquWebViewBar.toolbar.initClose {
            onBackPressed()
        }

        initWebView()
    }


    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun initWebView() {

        mWebView = WebView(this@CommonVquWebViewActivity).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        mBinding.flCommonWebViewRoot.addView(mWebView)

        val myJavascriptInterface = MyJavascriptInterface(this, mWebView!!)

        myJavascriptInterface.setPayListener { id, type ->

        }

        mWebView?.addJavascriptInterface(myJavascriptInterface, "openObject")



        mWebView?.settings?.javaScriptEnabled = true


        mWebView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
            }
        }

        mWebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mBinding.tbCommonVquWebViewBar.toolbar.init(view?.title ?: "")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed() // 兼容https
            }
        }

        mWebView?.loadUrl(mUrl)
    }

    override fun initObserve() {
    }

    override fun initRequestData() {

    }

    override fun onDestroy() {
        mWebView?.loadUrl("")
        mWebView = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        if (!mIsFirst && (mWebView?.url ?: "").contains("task/center")) {
            mWebView?.reload()
        }
        mIsFirst = false
    }

    override fun onBackPressed() {
        if (mWebView?.canGoBack() == true) {
            mWebView?.goBack()
        } else {
            super.onBackPressed()
        }
    }
}