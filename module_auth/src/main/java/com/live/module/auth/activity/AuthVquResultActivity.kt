package com.live.module.auth.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.auth.R
import com.live.module.auth.bean.VquAuthEvent
import com.live.module.auth.databinding.AuthVquActivityResultBinding
import com.live.module.auth.vm.AuthVquFaceIdentifyViewModel
import com.live.vquonline.base.utils.StateLayoutEnum
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

/**
 * author: Lau
 * date: 2022/4/27
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Auth.AuthVquResultActivity)
class AuthVquResultActivity :
    BaseActivity<AuthVquActivityResultBinding, AuthVquFaceIdentifyViewModel>() {
    override val mViewModel: AuthVquFaceIdentifyViewModel by viewModels()

    /**
     * 审核状态
     * type == 1 审核成功
     * type == 2 审核中
     * type == 3 审核失败
     */
    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0

    /**
     * 页面类型
     * pageType == 1 实名认证页面
     * pageType != 1 真人认证页面
     */
    @Autowired(name = RouteKey.PAGE_TYPE)
    @JvmField
    var pageType = 0

    private val mLoadDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this)
    }

    override fun AuthVquActivityResultBinding.initView() {

        mBinding.tbAuthVquResultBar.toolbar.initClose { finish() }

        when (type) {
            1 -> {
                mBinding.ivAuthVquResultImg.setImageResource(R.mipmap.ic_auth_success)
                mBinding.tvAuthenticationStatus.text = "恭喜您认证成功"
                if (pageType == 1) {
                    mBinding.tvAuthVquResultTips.text = "恭喜你通过了实名认证！"
                } else {
                    mBinding.tvAuthVquResultTips.text = "恭喜你通过了真人认证！"
                }
                mBinding.tbAuthVquResultBar.tvTitle.text = "审核通过"
                mBinding.stvAuthVquResultBtn.text = "好的"
            }
            2 -> {
                mBinding.ivAuthVquResultImg.setImageResource(R.mipmap.ic_auth_cheking)
                mBinding.tvAuthVquResultTips.text = "认证审核中，1小时内完成，认证结果会通过"
                mBinding.tvAuthenticationStatus.text = "认证审核中"
                mBinding.tbAuthVquResultBar.tvTitle.text = "审核中"
                mBinding.stvAuthVquResultBtn.text = "好的"
            }
            3 -> {
                mBinding.ivAuthVquResultImg.setImageResource(R.mipmap.ic_auth_failed)
                mBinding.tvAuthenticationStatus.text = "认证失败"
                if (pageType == 1) {
                    if (UserManager.userInfo?.gender == 1 || UserManager.userInfo?.isAnchor != 1) {
                        mBinding.tvAuthVquResultTips.text = "抱歉，你的实名资料审核失败，请重新提交"
                    } else {
                        mBinding.tvAuthVquResultTips.text =
                            "抱歉，你的实名资料审核失败。你已进行真人认证，为保证用户真实性，请使用同一个人的身份证资料"
                    }
                } else {
                    mBinding.tvAuthVquResultTips.text = "抱歉，你的人脸识别与头像不匹配。为保证用户真实性，请上传本人头像"
//                    if (UserManager.userInfo?.gender == 1 || UserManager.userInfo?.isAuth == 1) {
//                        mBinding.tvAuthVquResultTips.text = "抱歉，你的人脸识别与实名认证资料不匹配。为保证用户真实性，请同一个人进行认证"
//                    } else {
//                        mBinding.tvAuthVquResultTips.text = "抱歉，人脸识别失败，请重新认证"
//                    }
                }

                mBinding.tbAuthVquResultBar.tvTitle.text = "审核失败"
                mBinding.stvAuthVquResultBtn.text = "再试一次"
            }
        }

        mBinding.stvAuthVquResultBtn.setViewClickListener {
            if (type == 3) {
                if (pageType == 1) {
                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity).navigation()
                } else {
                    EventBus.getDefault().post(VquAuthEvent(1))
                }
            }
            finish()
        }
    }

    override fun initObserve() {
        mViewModel.verifyData.observe(this) {
            if (it != null) {
                mViewModel.rpVerify(this, it)
            }
        }

        mViewModel.verifyResultData.observe(this) {
            ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                .withInt(RouteKey.TYPE, 2)
                .navigation()

            finish()
        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> {
                    mLoadDialog.show()
                }
                StateLayoutEnum.HIDE -> {
                    mLoadDialog.dismiss()
                }
                else -> {
                    mLoadDialog.dismiss()
                }
            }
        }
    }

    override fun initRequestData() {

    }
}