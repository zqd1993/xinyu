package com.mshy.VInterestSpeed.common.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ImmersionBar
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.databinding.CommonVquActivityBaseWebviewBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.LoginViewModel
import com.umeng.socialize.UMShareAPI


/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/16 10:07
 *
 */

//@AndroidEntryPoint
//@Route(path = RouteUrl.Login.LoginActivity)
class BaseWebViewActivity : BaseActivity<CommonVquActivityBaseWebviewBinding, LoginViewModel>() {

    override fun setStatusBar() {
        ImmersionBar.with(this)
            .reset()
            .transparentStatusBar()
            .statusBarColor(R.color.color_FFFFFF)
            .fitsSystemWindows(false)
            .statusBarDarkFont(true)
            .init();
    }

    override val mViewModel: LoginViewModel by viewModels()

    private var isDarkStyle = true
    private var isShowTitleBar = true
    private var isShowStatusBar = true
    private var mUrl = ""

    @SuppressLint("JavascriptInterface")
    override fun CommonVquActivityBaseWebviewBinding.initView() {
        mUrl = intent.getStringExtra("ClickUrl")!!
        val titleName = intent.getStringExtra("TitleName")
        isDarkStyle = intent.getBooleanExtra("is_dark", true)
        isShowTitleBar = intent.getBooleanExtra("is_show_title_bar", true)
        isShowStatusBar = intent.getBooleanExtra("is_show_status_bar", true)

        vquWebviewView.setBackgroundColor(ContextCompat.getColor(this@BaseWebViewActivity, R.color.ps_color_transparent))

        if (isDarkStyle) {
            vquWebviewView.setBackgroundResource(R.color.color_1C1D23);
        }

        if (!isShowTitleBar) {
            vquFlTitleBar.setVisibility(View.GONE);
        }

        if (!isShowStatusBar) {
            vquStatusBar.setVisibility(View.GONE);
        }

        vquFlBack.setViewClickListener {
            if(vquWebviewView!=null){
                if (vquWebviewView.canGoBack()) {
                    vquWebviewView.goBack();
                } else {
                    finish()
                }
            }
        }

        vquIvClose.setViewClickListener {
            finish()
        }
        vquTvTitle.text = titleName
        vquWebviewView.addJavascriptInterface(com.mshy.VInterestSpeed.common.ui.js.MyJavascriptInterface_(
            this@BaseWebViewActivity,
            vquWebviewView), "openObject")
        vquWebviewView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                vquWebviewProgress.setProgress(newProgress)
                if (newProgress == 100) {
                    vquWebviewProgress.setVisibility(View.GONE)
                }
            }
        })

        vquWebviewView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                vquTvTitle.setText(vquWebviewView.getTitle())
                if (vquWebviewView != null) {
                    if (vquWebviewView.canGoBack()) {
                        findViewById<View>(R.id.vqu_iv_close).visibility = View.VISIBLE
                    } else {
                        findViewById<View>(R.id.vqu_iv_close).visibility = View.GONE
                    }
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
        })
        vquWebviewView.getSettings().setJavaScriptEnabled(true)
        vquWebviewView.loadUrl(mUrl)
    }

    override fun initObserve() {
    }

    override fun initRequestData() {

    }

    override fun onBackPressed() {
        if (mBinding.vquWebviewView != null) {
            if (mBinding.vquWebviewView.canGoBack()) {
                mBinding.vquWebviewView.goBack();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    private var mIsFirst = true

    override fun onResume() {
        super.onResume()
        if (!mIsFirst && (mBinding.vquWebviewView.getUrl()?:"").contains("task/center")) {
            mBinding?.vquWebviewView.reload()
        }
        mIsFirst = false
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}