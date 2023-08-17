package com.live.module.anchor.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.anchor.R
import com.live.module.anchor.adapter.AnchorVquSettingsMenuAdapter
import com.live.module.anchor.databinding.AnchorVquActivitySettingsMenuBinding
import com.live.module.anchor.dialog.FateLinkDialog
import com.live.module.anchor.vm.AnchorVquSettingsMenuViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.CommonVquMenuBean
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Anchor.AnchorVquSettingsActivity)
class AnchorVquSettingsMenuActivity :
    BaseActivity<AnchorVquActivitySettingsMenuBinding, AnchorVquSettingsMenuViewModel>() {
    override val mViewModel: AnchorVquSettingsMenuViewModel by viewModels()

    private val mMenuAdapter = AnchorVquSettingsMenuAdapter()

    private val mVquMenuList = mutableListOf<CommonVquMenuBean>()

    override fun AnchorVquActivitySettingsMenuBinding.initView() {

        mBinding.tbAnchorVquSettingsBar.toolbar.initClose(getString(R.string.mine_vqu_menu_anchor)) { finish() }

        initAdapter()
    }

    private fun initAdapter() {

        mBinding.rvAnchorVquSettingsList.adapter = mMenuAdapter

        mVquMenuList.add(
            CommonVquMenuBean(
                R.mipmap.ic_goddess_vqu_settings_rate,
                getString(R.string.contact_vqu_setting_rate),
                type = 1
            )
        )

        mVquMenuList.add(
            CommonVquMenuBean(
                R.mipmap.ic_goddess_vqu_settings_face,
                getString(R.string.mine_vqu_menu_face),
                type = 5
            )
        )

        mVquMenuList.add(
            CommonVquMenuBean(
                R.mipmap.ic_goddess_vqu_settings_hello,
                getString(R.string.contact_vqu_setting_hello),
                type = 2
            )
        )

//        mVquMenuList.add(
//            CommonVquMenuBean(
//                R.mipmap.ic_goddess_vqu_settings_profit,
//                getString(R.string.contact_vqu_setting_profit),
//                type = 3
//            )
//        )

//        mVquMenuList.add(
//            CommonVquMenuBean(
//                R.mipmap.ic_goddess_vqu_settings_link,
//                getString(R.string.contact_vqu_setting_link),
//                type = 4
//            )
//        )

        mMenuAdapter.setNewInstance(mVquMenuList)


        mMenuAdapter.setOnItemClickListener { _, _, position ->
            val commonVquMenuBean =
                mMenuAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            when (commonVquMenuBean.type) {
                1 -> {      //收费设置
                    ARouter.getInstance().build(RouteUrl.Anchor.AnchorTantaRateSettingActivity)
                        .navigation()
                }
                2 -> {      //打招呼设置

                    ARouter.getInstance().build(RouteUrl.Anchor.AnchorVquHelloSettingActivity)
                        .navigation()

                }
                3 -> {      //分成详情
                    //暂时不做
                }
                4 -> {      //
                    val dialog = FateLinkDialog()
                    dialog.show(supportFragmentManager, "")
                }
                5 -> {
                    PermissionUtils.cameraPermission(
                        this,
                        requestCallback = { allGranted, grantedList, deniedList ->
                            if (allGranted) {
                                ARouter.getInstance()
                                    .build(RouteUrl.Agora2.AgoraTantaBeautySettingActivity)
                                    .navigation()
                            } else {
                                toast("缺少摄像头权限")
                            }
                        })
                }
            }
        }
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }
}