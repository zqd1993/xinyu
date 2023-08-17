package com.live.module.relation.activity

import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.relation.R
import com.live.module.relation.adapter.RelationVquAdapter
import com.live.module.relation.databinding.RelationActivityRelationBinding
import com.live.module.relation.vm.RelationViewModel
import com.mshy.VInterestSpeed.common.bean.VquRelationBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/2
 * description:关注、粉丝、足迹、访客
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Relation.RelationVquActivity)
class RelationVquActivity : BaseActivity<RelationActivityRelationBinding, RelationViewModel>() {
    override val mViewModel: RelationViewModel by viewModels()

    @Autowired(name = RouteKey.TITLE)
    @JvmField
    var title: String = ""

    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type = 0
    var isVip = false

    private val mAdapter = RelationVquAdapter()

    override fun RelationActivityRelationBinding.initView() {
        mAdapter.type = type
        //最近来访，男用户，不是VIP && UserManager.userInfo?.gender == 0

        refreshVipValue()

        setLoadSir(mBinding.srlRelationRefresh)

        mBinding.rvRelationVquList.adapter = mAdapter
        mBinding.rvRelationVquList.animation = null

        var itemAnimator = mBinding.rvRelationVquList.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
        itemAnimator?.changeDuration = 0
//        mAdapter.setHasStableIds(true)

        mBinding.tbRelationBar.toolbar.initClose(title) {
            finish()
        }

        initEvent()

    }


    override fun onResume() {
        super.onResume()
        refreshVipValue()
    }

    private fun refreshVipValue() {
        LogUtil.e("type=$type" + "...userInfo=${UserManager.userInfo.toString()}")
        if (RouteKey.RelationType.VISTOR == type
            && UserManager.userInfo?.gender == 2
            && UserManager.userInfo?.vip == 0
        ) {
            mBinding.blurrViewBlack.setBlurRadius(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8f,
                    resources.displayMetrics
                )
            )
            mBinding.rlNotVipLock.visibility = View.VISIBLE
            mBinding.srlRelationRefresh.setEnableLoadMore(false)
            mBinding.srlRelationRefresh.setEnableRefresh(false)
        } else {
            mBinding.rlNotVipLock.visibility = View.GONE
            mBinding.srlRelationRefresh.setEnableLoadMore(true)
            mBinding.srlRelationRefresh.setEnableRefresh(true)
        }
    }

    private fun initEvent() {

        mBinding.srlRelationRefresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                mViewModel.vquGetList(type, false)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mViewModel.vquGetList(type, true)
            }
        })

        mAdapter.setOnItemClickListener { _, _, position ->
//            if (!isVip) {
//                return@setOnItemClickListener
//            }
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            item.userid?.toInt()?.let {
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                        RouteKey.USERID,
                        it
                    )
                    .navigation()
            }
        }

        mAdapter.setOnItemChildClickListener { _, view, position ->
//            if (!isVip) {
//                return@setOnItemChildClickListener
//            }
            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemChildClickListener

            when (view.id) {
                R.id.stv_relation_vqu_item_relation_list_focus -> {
                    vquFocus(item)
                }
                R.id.stv_relation_vqu_item_relation_list_like -> {
                    if (UserManager.userInfo != null) {
                        if (UserManager.userInfo?.gender == 2 || UserManager.userInfo?.isRpAuth == 1) {
                            if (item.isBeckon != true) {
                                com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
                                    this@RelationVquActivity,
                                    com.mshy.VInterestSpeed.common.utils.UmUtils.CLICKTOCHAT
                                )
                                vquSetBeckon(item, position)
                            } else {
                                com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
                                    this@RelationVquActivity,
                                    com.mshy.VInterestSpeed.common.utils.UmUtils.INITIATEPRIVATECHAT
                                )
                                com.mshy.VInterestSpeed.uikit.api.NimUIKit.startP2PSession(this, item.userid)
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
        mBinding.stvOpen.setViewClickListener(1) {
            ARouter.getInstance().build(RouteUrl.Vip.VipTantaCenterActivity)
                .navigation()
        }
        mBinding.stvOpenAll.setViewClickListener(1) {
            ARouter.getInstance().build(RouteUrl.Vip.VipTantaCenterActivity)
                .navigation()
        }
        mBinding.tvThink.setViewClickListener(1) {
            mBinding.sllVipOpen.visibility = View.GONE
            mBinding.llcOpenAll.visibility = View.VISIBLE

            mBinding.blurrViewWhite.visibility = View.VISIBLE
            mBinding.blurrViewBlack.visibility = View.GONE

        }
        mBinding.rlNotVipLock.setViewClickListener(1) {
            mBinding.sllVipOpen.visibility = View.VISIBLE
            mBinding.llcOpenAll.visibility = View.GONE

            mBinding.blurrViewBlack.visibility = View.VISIBLE
            mBinding.blurrViewWhite.visibility = View.GONE
        }
    }

    private fun vquSetBeckon(item: VquRelationBean, position: Int) {
        mViewModel.vquSendBeckon("[${item.userid}]", position)
        item.isBeckon = true
        mAdapter.notifyItemChanged(position)
    }

    private fun vquFocus(item: VquRelationBean) {
        mViewModel.vquFocus(item)
    }

    override fun initObserve() {
        mViewModel.relationList.observe(this) {
            if (mViewModel.mPage == 1 && it.isNullOrEmpty()) {
                showEmpty()
            } else {
                val iteratorServerAll: MutableIterator<VquRelationBean> = it.iterator()
                while (iteratorServerAll.hasNext()) {
                    val vquRelationBean = iteratorServerAll.next() as VquRelationBean
                    if (RouteKey.RelationType.VISTOR == type) {
                        vquRelationBean.itemType = RouteKey.RelationType.VISTOR
                    } else {
                        vquRelationBean.itemType = RouteKey.RelationType.TRACK
                    }
                }
                mLoadService?.showSuccess()
                if (mViewModel.mPage == 1) {
                    mBinding.srlRelationRefresh.finishRefresh()
                    mAdapter.setNewInstance(it)
                } else {
                    mBinding.srlRelationRefresh.finishLoadMore()
                    mAdapter.addData(it)
                }
            }

        }

        mViewModel.followResult.observe(this) {
            val index = mAdapter.data.indexOf(it)
            if (index >= 0) {
                mAdapter.notifyItemChanged(index)
            }
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

    override fun initRequestData() {
        mViewModel.vquGetList(type, false)
    }
}