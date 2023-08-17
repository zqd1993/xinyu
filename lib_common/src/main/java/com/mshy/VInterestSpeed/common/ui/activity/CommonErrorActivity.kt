package com.mshy.VInterestSpeed.common.ui.activity

import androidx.activity.viewModels
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.databinding.CommonActivityErrorBinding
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.vm.SplashViewModel
import com.mshy.VInterestSpeed.uikit.common.http.CommonCallBack

/**
 * author: Tany
 * date: 2022/7/6
 * description:
 */
class CommonErrorActivity : BaseActivity<CommonActivityErrorBinding, SplashViewModel>() {
    override val mViewModel: SplashViewModel by viewModels()
    var config: CaocConfig? = null
    var errStr: String = ""
    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun CommonActivityErrorBinding.initView() {
        mBinding.includeTitle.toolbar.initClose("出错了")
        config = CustomActivityOnCrash.getConfigFromIntent(intent)
        CustomActivityOnCrash.getStackTraceFromIntent(intent)?.let {
            android.util.Log.i("err", it)
            errStr = it
        }
        mBinding.tvError.setOnClickListener {
            config?.run {
                postErr(config)
            }
        }
    }

    override fun onBackPressed() {
        config?.run {
            postErr(config)
        }
    }

    fun postErr(config: CaocConfig?) {
        GlobalServiceManage.getRetrofit()
            .create(GlobalApiService::class.java)
            .vquSystemReport(errStr)
            .enqueue(object :
                CommonCallBack<Any>() {
                override fun onSuccess(data: Any) {
                    CustomActivityOnCrash.restartApplication(this@CommonErrorActivity, config!!)
                }

                override fun onFailed(t: BaseResponse<Any>) {
                    super.onFailed(t)
                    CustomActivityOnCrash.restartApplication(this@CommonErrorActivity, config!!)
                }
            })
    }
}