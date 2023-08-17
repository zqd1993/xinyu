package com.live.module.auth.activity

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.auth.databinding.AuthVquActivityRealAuthBinding
import com.live.module.auth.vm.AuthVquRealAuthViewModel
import com.live.vquonline.base.utils.StateLayoutEnum
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.addStatusBarHeight
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Auth.AuthVquRealAuthActivity)
class AuthVquRealAuthActivity :
    BaseActivity<AuthVquActivityRealAuthBinding, AuthVquRealAuthViewModel>() {
    override val mViewModel: AuthVquRealAuthViewModel by viewModels()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding.toolbar.addStatusBarHeight(true)
    }

    private val mLoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this)
    }

    override fun AuthVquActivityRealAuthBinding.initView() {
        ImmersionBar.with(this@AuthVquRealAuthActivity)
            .transparentStatusBar()
            .statusBarDarkFont(false)
            .init()

        mBinding.stvAuthVquRealAuthCommit.isEnabled = false


        mBinding.setAuthVquRealAuthId.addTextChangedListener {
            refreshCommit()
        }

        mBinding.setAuthVquRealAuthName.addTextChangedListener {
            refreshCommit()
        }

        mBinding.stvAuthVquRealAuthCommit.setViewClickListener {
            clickCommit()
        }

        mBinding.ivAuthRealAuthFinish.setViewClickListener {
            finish()
        }
    }

    private fun clickCommit() {
        val name = mBinding.setAuthVquRealAuthName.text.toString()
        val id = mBinding.setAuthVquRealAuthId.text.toString()


        mViewModel.vquDescribeVerifyToken(name, id)
    }

    private fun refreshCommit() {
        mBinding.stvAuthVquRealAuthCommit.isEnabled =
            mBinding.setAuthVquRealAuthId.text.isNotEmpty() && mBinding.setAuthVquRealAuthName.text.isNotEmpty()
    }

    override fun initObserve() {
        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> {
                    mLoadingDialog.show()
                }
                StateLayoutEnum.HIDE -> {
                    mLoadingDialog.dismiss()
                }
                else->{
                    mLoadingDialog.show()
                }
            }
        }

        mViewModel.authResult.observe(this) {
            val aRouterBuild = ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
            aRouterBuild.withInt(RouteKey.PAGE_TYPE, 1)
            if (it) {
                UserManager.userInfo?.isAuth = 1
                aRouterBuild.withInt(RouteKey.TYPE, 1)
            } else {
                UserManager.userInfo?.isAuth = 2
                aRouterBuild.withInt(RouteKey.TYPE, 3)
            }
            aRouterBuild.navigation()
            finish()

            /*
            val messageDialog = MessageDialog()
            messageDialog.setIsSingleButton(true)
            messageDialog.setOnButtonClickListener(object :
                MessageDialog.OnSingleButtonClickListener {
                override fun onClick(): Boolean {
                    if (it) {
                        messageDialog.dismiss()

                        if (UserManager.userInfo?.isAnchor == 1) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquFaceIdentifyActivity)
                                .navigation()
                        } else {
                            finish()
                        }
                    }
                    return it
                }
            })
            if (it) {
                UserManager.userInfo?.isAuth = 1
                messageDialog.setTitle("审核通过")
                messageDialog.setContent("您已通过实人认证")
                messageDialog.setSingleText("好的")
            } else {
                UserManager.userInfo?.isAuth = 2
                messageDialog.setTitle("验证失败")
                messageDialog.setContent("请查看身份证号码或姓名填充是否有误？")
                messageDialog.setSingleText("好的")
            }
            messageDialog.show(supportFragmentManager, "")

             */
        }
    }

    override fun initRequestData() {

    }
}