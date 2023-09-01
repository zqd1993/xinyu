package com.live.module.home.activity

import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.home.R
import com.live.module.home.adapter.HomeSearchAdapter
import com.live.module.home.databinding.HomeTantaActivitySearchBinding
import com.live.module.home.vm.HomeVquSearchViewModel
import com.live.vquonline.base.utils.AppManager
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initSearch
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.UmUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/7/4
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.VquMainFragment.HomeVquSearchActivity)
class HomeSearchActivity : BaseActivity<HomeTantaActivitySearchBinding, HomeVquSearchViewModel>() {
    override val mViewModel: HomeVquSearchViewModel by viewModels()

    private val mData = mutableListOf<VquRelationBean>()

    private val mAdapter = HomeSearchAdapter()

    private var mKeyWord = ""

    override fun initObserve() {
        mViewModel.resultData.observe(this) {
            mData.clear()
            if (it != null) {
                mData.add(it)
                showContent()
            } else {
                showEmpty("没有搜索到相关用户~")
            }
            mAdapter.notifyDataSetChanged()
        }


        mViewModel.vquBeckonInt.observe(this) {
            val item = mAdapter.getItemOrNull(it)
            if (item != null) {
                item.isBeckon = false
                mAdapter.notifyItemChanged(it)
            }

        }

        mViewModel.vquNoMoneyResult.observe(this) {

            "余额不足，请先充值".toast()
            mPayViewModel.showRechargeDialog(supportFragmentManager)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            mBinding.tbHomeVquSearchBar.tvTitle.isFocusable = true
            mBinding.tbHomeVquSearchBar.tvTitle.requestFocus()
            mBinding.tbHomeVquSearchBar.tvTitle.postDelayed({
                AppManager.getInstance().showSoftKeyBoard(this, mBinding.tbHomeVquSearchBar.tvTitle)
            }, 500)
        }
    }

    override fun initRequestData() {

    }

    override fun HomeTantaActivitySearchBinding.initView() {

        setLoadSir(mBinding.srlHomeVquSearchRefresh)

        mBinding.rvHomeVquSearchList.adapter = mAdapter
        mAdapter.setNewInstance(mData)

        mBinding.tbHomeVquSearchBar.toolbar.initSearch(
            "请输入鹊娘号", onSearch = {
                AppManager.getInstance().hideSoftKeyBoard(this@HomeSearchActivity)
                mKeyWord = it
                search()
            }, onBack = {
                finish()
            }
        )

        mBinding.srlHomeVquSearchRefresh.setOnRefreshListener {
            search()
        }


        mAdapter.setOnItemClickListener { _, _, position ->

            val vquRelationBean = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            if (vquRelationBean.userid.isNullOrEmpty()) {
                return@setOnItemClickListener
            }

            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    vquRelationBean.userid?.toInt() ?: 0
                )
                .navigation()

        }

        mAdapter.setOnItemChildClickListener { _, view, position ->
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.stv_relation_vqu_item_relation_list_like -> {
                    if (UserManager.userInfo != null) {
                        if (UserManager.userInfo?.gender == 2 || UserManager.userInfo?.isRpAuth == 1) {
                            if (item.isBeckon != true) {
                                UmUtils.setUmEvent(
                                    this@HomeSearchActivity,
                                    UmUtils.CLICKTOCHAT
                                )
                                vquSetBeckon(item, position)
                            } else {
                                UmUtils.setUmEvent(
                                    this@HomeSearchActivity,
                                    UmUtils.INITIATEPRIVATECHAT
                                )
                              NimUIKit.startP2PSession(this@HomeSearchActivity, item.userid)
                            }
                        } else {
                            val messageDialog = MessageDialog()
                            messageDialog.setTitle(R.string.common_vqu_go_to_real_auth)
                            messageDialog.setContent(R.string.common_vqu_content_auth)
                            messageDialog.setLeftText(R.string.common_vqu_go_to_no_auth)
                            messageDialog.setRightText(R.string.common_vqu_go_to_auth)
                            messageDialog.setOnButtonClickListener(object :
                                MessageDialog.OnButtonClickListener {
                                override fun onLeftClick(): Boolean {

                                    return false
                                }

                                override fun onRightClick(): Boolean {
                                    ARouter.getInstance()
                                        .build(RouteUrl.Auth.AuthVquCenterActivity)
                                        .navigation()
                                    return false
                                }
                            })
                            messageDialog.show(supportFragmentManager, "")
                        }
                    }
                }
            }
        }
    }

    private fun search() {
        mViewModel.vquGetUserInfoByUserCode(mKeyWord)
    }

    private fun vquSetBeckon(item: VquRelationBean, position: Int) {
        mViewModel.vquSendBeckon("[${item.userid}]", position)
        item.isBeckon = true
        mAdapter.notifyItemChanged(position)
    }

}