package com.live.module.home.fragment

import android.media.MediaPlayer
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.home.R
import com.live.module.home.adapter.HomeVquRecommendAdapter
import com.live.module.home.bean.HomeDataItemBean
import com.live.module.home.databinding.HomeVquMainRecommendFragmentNewBinding
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.event.ShowToTopEvent
import com.mshy.VInterestSpeed.common.ext.addOnPreloadListener
import com.mshy.VInterestSpeed.common.ext.setNbOnItemClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.UmUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.quyunshuo.module.home.fragment.HomeVquFragmentViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Tany
 * date: 2022/8/8
 * description:
 */
@EventBusRegister
@AndroidEntryPoint
class HomeOnLineFragment :
    BaseLazyFrameFragment<HomeVquMainRecommendFragmentNewBinding, HomeVquFragmentViewModel>(),
    OnRefreshListener, OnLoadMoreListener {
    private var vquRecomendAdapter = HomeVquRecommendAdapter()
    private var vquPage: Int = 1
    private var vquType: Int = 0
    private val mPayViewModel: CommonPayViewModel by viewModels()


    override fun HomeVquMainRecommendFragmentNewBinding.initView() {
        ImmersionBar.with(this@HomeOnLineFragment).statusBarView(R.id.view).init()
        initRecommendList()
        refreshRecommended()
    }


    private fun initRecommendList() {//初始化推荐列表
        mBinding.smart.setOnRefreshListener(this@HomeOnLineFragment)
        mBinding.smart.setOnLoadMoreListener(this@HomeOnLineFragment)
        mBinding.smart.setEnableAutoLoadMore(true)
        mBinding.smart.setFooterTriggerRate(2.toFloat())
        var linearLayoutManager = LinearLayoutManager(context)
        mBinding.rvRecommend.layoutManager = linearLayoutManager
        setLoadSir(mBinding.smart)
        mBinding.rvRecommend.adapter = vquRecomendAdapter
        mBinding.rvRecommend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                var pos = linearLayoutManager.findFirstVisibleItemPosition()
                if (pos > 3) {//显示回到顶部按钮
                    EventBus.getDefault().post(ShowToTopEvent(true))
                } else {
                    EventBus.getDefault().post(ShowToTopEvent(false))
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        )
        mViewModel.vquGetOnLineAnchors(vquPage.toString())
        (mBinding.rvRecommend.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        mBinding.rvRecommend.addOnPreloadListener(20) {
            vquPage += 1
            mViewModel.vquGetOnLineAnchors(vquPage.toString())
//            mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        }
        vquRecomendAdapter.setNbOnItemClickListener { _, _, position ->
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(RouteKey.USERID, vquRecomendAdapter.data[position].id)
                .navigation()
        }
        vquRecomendAdapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.frameLayout_end -> {
                    if (UserManager.userInfo != null) {
                        if (UserManager.userInfo?.gender == 2 || UserManager.userInfo?.isRpAuth == 1) {
                            if (!vquRecomendAdapter.data[position].isBeckon) {
                                UmUtils.setUmEvent(activity, UmUtils.CLICKTOCHAT)
                                vquSetBeckon(
                                    vquRecomendAdapter.data[position].id,
                                    position
                                )
                            } else {
                                UmUtils.setUmEvent(activity, UmUtils.INITIATEPRIVATECHAT)
                                NimUIKit.startP2PSession(
                                    activity,
                                    vquRecomendAdapter.data[position].id.toString()
                                )
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
                                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
                                        .navigation()
                                    return false
                                }
                            })
                            messageDialog.show(childFragmentManager, "")
                        }
                    }

                }
            }

        }
        setLoadSir(mBinding.smart)

    }

    override fun onPause() {
        super.onPause()
    }

    /**
     * 搭讪和心动
     */
    private fun vquSetBeckon(vquUserId: Int, vquPosition: Int) {
        vquRecomendAdapter.data[vquPosition].isBeckon = true
        vquRecomendAdapter.notifyItemChanged(vquPosition)
        mViewModel.vquSendBeckon("[${vquUserId}]", vquPosition,0,0)
    }

    override fun initObserve() {
        //更新心动搭讪
        mViewModel.vquBeckonInt.observe(this) {
            if (it > vquRecomendAdapter.data.size - 1) {
                return@observe
            }
            vquRecomendAdapter.data[it].isBeckon = false
            vquRecomendAdapter.notifyItemChanged(it)
        }
        mViewModel.vquRemoveItem.observe(this) {
            if (vquRecomendAdapter.data.size > it) {
                vquRecomendAdapter.data.removeAt(it)
                vquRecomendAdapter.notifyItemRemoved(it)
                vquRecomendAdapter.notifyItemRangeChanged(it, vquRecomendAdapter.data.size - it)
            }
        }
        mViewModel.vquNoDiamond.observe(this) {
            "余额不足，请先充值".toast()
            mPayViewModel.showRechargeDialog(childFragmentManager)
        }

        mViewModel.vquAccostStrBean.observe(this) {
            var accostDialog = HomeTantaAccostDialog()
            accostDialog.setContent(it.vquAccostStr)
            accostDialog.setOnClickListener(object : HomeTantaAccostDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?, isCheck: Boolean) {
                    if (isCheck) {
                        mViewModel.vquSendBeckon("[${it.vquUserId}]", it.vquPosition, 0, 1)
                    }
                }

                override fun onRight(dialogFragment: DialogFragment?, isCheck: Boolean) {
                    mViewModel.vquSendBeckon("[${it.vquUserId}]", it.vquPosition, 1, 1)
                }

            })
            accostDialog.show(activity!!.supportFragmentManager, "accostDialog")

        }
        mViewModel.vquOnlineData.observe(this) {
            if (vquPage == 1) {
                if (it.list.list.isEmpty()) {
                    showEmpty("暂无数据")
                } else {
                    showContent()
                }
            }
            if (null != it.list) {
                if (null != it.list.list) {
                    if (it.list.list.isNotEmpty()) {
                        if (vquPage == 1) {
                            vquRecomendAdapter.data.clear()
                        }
                        val mNewData: ArrayList<HomeDataItemBean> = it.list.list
                        mNewData.removeAll(vquRecomendAdapter.data)

                        if (vquPage == 1) {
                            vquRecomendAdapter.setList(mNewData)
                        } else {
                            vquRecomendAdapter.addData(mNewData)
                        }

                    }
                }
            }
            finishLoad()
        }
    }

    override fun initRequestData() {
    }

    private var mediaPlayer: MediaPlayer? = null
    override val mViewModel: HomeVquFragmentViewModel by viewModels<HomeVquFragmentViewModel>()

    override fun onRefresh(refreshLayout: RefreshLayout) {
        vquPage = 1
//        mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        mViewModel.vquGetOnLineAnchors(vquPage.toString())
        mBinding.smart.finishRefresh(3000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        vquPage += 1
//        mViewModel.vquGetRecommendAnchors(vquType, vquPage.toString())
        mViewModel.vquGetOnLineAnchors(vquPage.toString())
        mBinding.smart.finishLoadMore(3000)
    }

    private fun finishLoad() {
        mBinding.smart.finishRefresh()
        mBinding.smart.finishLoadMore()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: String) {
        if ("homeDoubleClick" == event) {
            mBinding.rvRecommend.scrollToPosition(0)
            mBinding.smart.autoRefresh()
            mBinding.smart.finishRefresh(3000)
        } else if ("RecommendedSwitch" == event) {//刷新是否隐藏推荐
            refreshRecommended()
        }
    }

    fun refreshRecommended() {
        var state = SpUtils.getBoolean(SpKey.SETTING_RECOMMENDED_OPEN, true)!!
        if (state) {
            mBinding.llOpen.gone()
        } else {
            mBinding.llOpen.visible()
        }
        mBinding.llOpen.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Setting.SettingVquMessageActivity)
                .navigation()
        }
    }


}