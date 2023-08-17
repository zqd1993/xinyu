package com.live.module.auth.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.auth.R
import com.live.module.auth.adapter.AuthVquCenterMenuAdapter
import com.live.module.auth.bean.VquAuthEvent
import com.live.module.auth.databinding.AuthVquActivityCenterBinding
import com.live.module.auth.vm.AuthVquCenterViewModel
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.StateLayoutEnum
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.CommonVquMenuBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/5/17
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Auth.AuthVquCenterActivity)
@EventBusRegister
class AuthVquCenterActivity : BaseActivity<AuthVquActivityCenterBinding, AuthVquCenterViewModel>() {
    override val mViewModel: AuthVquCenterViewModel by viewModels()

    private val mVquMenus = mutableListOf<CommonVquMenuBean>()

    private val mVquMenuAdapter = AuthVquCenterMenuAdapter()

    private val mLoadDialog: com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(this)
    }

    override fun initObserve() {
        mViewModel.authData.observe(this) {
            mVquMenus[0].isSelected = it.mobileStatus == 1
            mVquMenus[1].isSelected = it.isAuth == 1
            mVquMenus[2].isSelected = it.isRpAuth == 1
            mVquMenuAdapter.notifyDataSetChanged()
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
        mViewModel.isAuth()
    }

    private var isFirst = true

    override fun onResume() {
        super.onResume()
        if (!isFirst) {
            initRequestData()
        }
        isFirst = false
    }

    override fun AuthVquActivityCenterBinding.initView() {

        mBinding.tbAuthVquCenterBar.toolbar.initClose("认证中心") { finish() }

        mVquMenus.add(
            CommonVquMenuBean(
                title = "手机认证",
                type = 1,
                icon = R.drawable.selector_ic_tanta_auth_center_item_phone,
                desc = "完成官方手机认证标识"
            ).apply {
                isSelected = !UserManager.userInfo?.mobile.isNullOrEmpty()
            })

        mVquMenus.add(CommonVquMenuBean(
            title = "实名认证", type = 2,
            icon = R.drawable.selector_ic_tanta_auth_center_item_name,
            desc = "完成官方实名认证标识"
        ).apply {
            isSelected = UserManager.userInfo?.isAuth == 1
        })

        mVquMenus.add(CommonVquMenuBean(
            title = "真人认证", type = 3,
            icon = R.drawable.selector_ic_tanta_auth_center_item_person,
            desc = "完成官方真人认证标识"
        ).apply {
            isSelected = UserManager.userInfo?.isAuth == 1
        })

        mBinding.rvAuthVquCenterMenus.adapter = mVquMenuAdapter

        mVquMenuAdapter.setNewInstance(mVquMenus)

        initEvent()
    }

    private fun initEvent() {
        mVquMenuAdapter.setOnItemClickListener { _, _, position ->

            val item = mVquMenuAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            if (!item.isSelected) {
                when (item.type) {
                    1 -> {
                        ARouter.getInstance().build(RouteUrl.Setting.SettingVquBindMobileActivity)
                            .navigation()
                    }
                    2 -> {
                        if (UserManager.userInfo?.isAuth == 2) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                                .withInt(RouteKey.TYPE, 3)
                                .withInt(RouteKey.PAGE_TYPE, 1)
                                .navigation()
                        } else {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity)
                                .navigation()
                        }
                    }
                    3 -> {
//                        if (UserManager.userInfo?.gender == 1 && UserManager.userInfo?.isAuth != 1) {
//                            toast("请先进行实名认证")
//                        } else {
                        if (UserManager.userInfo?.isRpAuth == 2) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                                .withInt(RouteKey.TYPE, 3)
                                .withInt(RouteKey.PAGE_TYPE, 1)
                                .navigation()
                        } else {

                            ARouter.getInstance()
                                .build(RouteUrl.Auth.AuthVquAvatarActivity)
                                .navigation()

//                                mViewModel.vquDescribeVerifyToken()
//                                ARouter.getInstance()
//                                    .build(RouteUrl.Auth.AuthVquFaceIdentifyActivity)
//                                    .navigation()
                        }
//                        }
                    }
                }
            } else {
                toast("你已经认证过啦")
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAuthEvent(event: VquAuthEvent) {
        if (event.type == 1) {
            ARouter.getInstance()
                .build(RouteUrl.Auth.AuthVquAvatarActivity)
                .navigation()
//            mViewModel.vquDescribeVerifyToken()
        }
    }

}