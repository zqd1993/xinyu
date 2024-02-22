package com.live.module.setting.activity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.setting.R
import com.live.module.setting.databinding.SettingTantaActivityAboutBinding
import com.live.module.setting.vm.SettingVquViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.AppUpdateUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Tany
 * date: 2022/4/6
 * description:关于蜜橙
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Setting.SettingVquAboutActivity)
class SettingVquAboutActivity :
    BaseActivity<SettingTantaActivityAboutBinding, SettingVquViewModel>(), View.OnClickListener {
    override val mViewModel: SettingVquViewModel by viewModels()
    var versionCode: Int = 0

    override fun SettingTantaActivityAboutBinding.initView() {

        mBinding.includeTitle.toolbar.initClose(getString(R.string.setting_about)) {
            finish()
        }
        val pm: PackageManager = packageManager
        try {
            val packageInfo: PackageInfo = pm.getPackageInfo(packageName, 0)
            mBinding.tvVersion.text =
                "版本：v" + packageInfo.versionName + if (NetBaseUrlConstant.BASE_URL == "http://appta.pre.vqu.show/") {
                    "(测试环境)"
                } else {
                    ""
                }
            versionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
        }
        mBinding.clAgreement.setOnClickListener(this@SettingVquAboutActivity)
        mBinding.clPrivacy.setOnClickListener(this@SettingVquAboutActivity)
        mBinding.clInfo.setOnClickListener(this@SettingVquAboutActivity)
        mBinding.clPermissions.setOnClickListener(this@SettingVquAboutActivity)
        mBinding.clVersion.setOnClickListener(this@SettingVquAboutActivity)
        mViewModel.vquCheckUpdate()
    }

    override fun initObserve() {
        mViewModel.vquVersionData.observe(this, Observer {
            if (versionCode >= it.data.versioncode) {
                mBinding.viewPoint.gone()
            } else {
                if (it.data.downloadurl.isNullOrEmpty()) {
                    mBinding.viewPoint.gone()
                } else {
                    mBinding.viewPoint.visible()
                }
            }
        })
    }


    override fun initRequestData() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_agreement -> {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                    .withString(
                        RouteKey.URL,
                        NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.AGREEMENT_URL
                    )
                    .navigation()
            }
            R.id.cl_privacy -> {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                    .withString(
                        RouteKey.URL,
                        NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.USER_PRIVACY_URL
                    )
                    .navigation()
            }
            R.id.cl_info -> {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                    .withString(
                        RouteKey.URL,
                        NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.USER_thirdparty_sdk_URL
                    )
                    .navigation()
            }
            R.id.cl_permissions -> {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                    .withString(
                        RouteKey.URL,
                        NetBaseUrlConstant.AGREEMENT_BASE_URL + NetBaseUrlConstant.USER_permission_URL
                    )
                    .navigation()
            }
            R.id.cl_version -> {//版本升级
                AppUpdateUtil.checkUpdate(false, this@SettingVquAboutActivity)
            }
        }
    }

}