package com.live.module.dynamic.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.live.module.dynamic.R
import com.live.module.dynamic.adapter.DynamicTantaAdapter
import com.live.module.dynamic.databinding.DynamicTantaFragmentDynamicChildBinding
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.event.ScrollToPositionEvent
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.UmUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author: Tany
 * date: 2022/4/9
 * description:动态子页面
 */
@EventBusRegister
@AndroidEntryPoint
class DynamicTantaDynamicChildFragment :
    BaseFragment<DynamicTantaFragmentDynamicChildBinding, DynamicPublishViewModel>(),
    OnRefreshListener, OnLoadMoreListener {
    var type: Int = 0//0同城  1热门 2关注
    var page: Int = 1
    var isLike: Boolean = false
    var curPos: Int = 0
    var dynamicVquAdapter: DynamicTantaAdapter? = null
    var curUserId: String = ""
    private var mSelectiveDialog: BottomSelectiveDialog? = null
    override val mViewModel: DynamicPublishViewModel by viewModels()
    private val mPayViewModel: CommonPayViewModel by viewModels()
    var isChat: Boolean = false
    override fun DynamicTantaFragmentDynamicChildBinding.initView() {
        type = arguments?.getInt("type")!!
        setLoadSir(smart)
        mBinding.smart.setOnRefreshListener(this@DynamicTantaDynamicChildFragment)
        mBinding.smart.setOnLoadMoreListener(this@DynamicTantaDynamicChildFragment)
        mBinding.rvDynamic.layoutManager = LinearLayoutManager(context)
        dynamicVquAdapter = DynamicTantaAdapter(requireActivity())
        mBinding.rvDynamic.adapter = dynamicVquAdapter
        (mBinding.rvDynamic.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        refreshRecommended()
        dynamicVquAdapter?.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.iv_head, R.id.tv_name -> {
                    ARouter.getInstance()
                        .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                        .withInt(
                            RouteKey.USERID,
                            dynamicVquAdapter!!.data[position].userId
                        )
                        .navigation()
                }
                R.id.ll_like -> {
                    var bean = dynamicVquAdapter!!.data[position]
                    curPos = position
                    if (bean.isLike == 1) {
                        isLike = false
                        mViewModel.vquDynamicLike(1, bean.id)
                    } else {
                        isLike = true
                        mViewModel.vquDynamicLike(0, bean.id)

                    }
                }
//                R.id.iv_more -> {//更多
//                    var isShow =
//                        UserManager.userInfo?.userId == dynamicVquAdapter!!.data[position].userId.toString()
//                    mSelectiveDialog =
//                        BottomSelectiveDialog(requireContext(), R.style.SelectiveDialog)
//                    if (isShow) {
//                        mSelectiveDialog?.addSelectButton("删除",
//                            OnButtonSelectListener { view, index ->
//                                curPos = position
//                                mViewModel.vquDeleteDynamic(dynamicVquAdapter!!.data[position].id.toString())
//                                mSelectiveDialog?.dismiss()
//                            })
////                        mSelectiveDialog?.addSelectButton("删除文字",
////                            OnButtonSelectListener { view, index ->
////                                mViewModel.vquDeleteDynamicTextOrImg(1,dynamicVquAdapter!!.data[position].id.toString())
////                                mSelectiveDialog?.dismiss()
////                            })
//                    } else {
//                        mSelectiveDialog?.addSelectButton("举报该条动态",
//                            OnButtonSelectListener { view, index ->
//                                ARouter.getInstance()
//                                    .build(RouteUrl.Dynamic.DynamicVquReportActivity)
//                                    .withInt(
//                                        RouteKey.USERID,
//                                        dynamicVquAdapter!!.data[position].userId
//                                    )
//                                    .withInt(RouteKey.TYPE, 3)
//                                    .navigation()
//                                mSelectiveDialog?.dismiss()
//                            })
//                    }
//                    mSelectiveDialog?.show()
//                }
                R.id.ll_chat -> {//私信
                    curUserId = dynamicVquAdapter!!.data[position].userId.toString()
                    isChat = true
                    if (UserManager.userInfo?.gender == 1) {
                        mViewModel.vquAuth()
                    } else {
                        NimUIKit.startP2PSession(
                            activity,
                            dynamicVquAdapter!!.data[position].userId.toString()
                        )
                    }
                }
                R.id.ll_beckoning -> {
                    curPos = position
                    curUserId = dynamicVquAdapter!!.data[position].userId.toString()
                    if (dynamicVquAdapter!!.data[position].isBeckon) {
                        isChat = true
                        UmUtils.setUmEvent(activity, UmUtils.INITIATEPRIVATECHAT)
                        if (UserManager.userInfo?.gender == 1) {
                            mViewModel.vquAuth()
                        } else {
                            NimUIKit.startP2PSession(
                                activity,
                                dynamicVquAdapter!!.data[position].userId.toString()
                            )
                        }
                    } else {
                        UmUtils.setUmEvent(activity, UmUtils.CLICKTOCHAT)
                        isChat = false
                        if (UserManager.userInfo?.gender == 1) {
                            mViewModel.vquAuth()
                        } else {
                            mViewModel.vquSendBeckon("[$curUserId]")
                        }

                    }

                }
            }

        }
        dynamicVquAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                ARouter.getInstance()
                    .build(RouteUrl.Dynamic.DynamicVquDynamicDetailActivity)
                    .withString(
                        RouteKey.ID,
                        dynamicVquAdapter!!.data[position].id.toString()
                    )
                    .navigation()
            }

        })


    }

    companion object {
        fun newInstance(type: Int): DynamicTantaDynamicChildFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = DynamicTantaDynamicChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initObserve() {
        mViewModel.dynamicLsit.observe(this) {
            if (page == 1) {
                if (it.data.list.isNullOrEmpty()) {
                    showEmpty("暂无动态")
                } else {
                    mLoadService?.showSuccess()
                    dynamicVquAdapter?.setList(it.data.list)
                }
            } else {
                if (!it.data.list.isNullOrEmpty()) {
                    dynamicVquAdapter?.addData(it.data.list)
                }
            }
            dynamicVquAdapter?.notifyDataSetChanged()
            finishLoad()
        }
        mViewModel.dynamicLike.observe(this) {
            var bean = dynamicVquAdapter!!.data[curPos]
            if (isLike) {
//                "点赞成功".toast()
                bean.isLike = 1
                bean.likeCount = bean.likeCount + 1
                dynamicVquAdapter!!.setData(curPos, bean)
                dynamicVquAdapter!!.notifyItemChanged(curPos)
            } else {
                bean.isLike = 0
                bean.likeCount = bean.likeCount - 1
                dynamicVquAdapter!!.setData(curPos, bean)
                dynamicVquAdapter!!.notifyItemChanged(curPos)
//                "已取消点赞".toast()
            }
        }
        mViewModel.vquDelDynamicData.observe(this, Observer {
            "删除成功".toast()
            dynamicVquAdapter?.data?.removeAt(curPos)
            dynamicVquAdapter?.notifyDataSetChanged()
            if (dynamicVquAdapter?.data.isNullOrEmpty()) {
                showEmpty("暂无动态")
            }
        })
        mViewModel.vquDelTextOrImgData.observe(this, Observer {
            "删除成功".toast()
            dynamicVquAdapter?.data!![curPos].content = ""
            dynamicVquAdapter?.notifyItemChanged(curPos)
        })
        mViewModel.vquSendData.observe(this, Observer {
            when (it.code) {
                0 -> {
                    dynamicVquAdapter?.data!![curPos].isBeckon = true
                    dynamicVquAdapter?.notifyItemChanged(curPos)
                }
                1002 -> {
                    "余额不足，请先充值".toast()
                    mPayViewModel.showRechargeDialog(childFragmentManager)
//                    CommonRechargeDialog().show(childFragmentManager, "充值")
                }
                7480 -> {
                    dynamicVquAdapter?.data!![curPos].isBeckon = true
                    dynamicVquAdapter?.notifyItemChanged(curPos)
                }
                else -> {
                    it.message?.toast()
                }
            }

        })
        mViewModel.vquChatAuthData.observe(this, Observer {
            if (it.data.isRpAuth == 1) {
                if (isChat) {
                    NimUIKit.startP2PSession(
                        activity,
                        curUserId
                    )
                } else {
                    mViewModel.vquSendBeckon("[$curUserId]")
                }

            } else {
                CommonHintDialog()
                    .setTitle("认证提醒")
                    .setContent(resources.getString(R.string.common_vqu_auth))
                    .setLeftText("暂不认证")
                    .setRightText("去认证")
                    .setContentSize(15)
                    .setContentGravity(Gravity.CENTER)
                    .setOnClickListener(object : CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {}
                        override fun onRight(dialogFragment: DialogFragment) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
                                .navigation()
//                            if (UserManager.userInfo?.gender == 1) {
//                                //女认证
//                                ARouter.getInstance()
//                                    .build(RouteUrl.Auth.AuthVquFaceIdentifyActivity).navigation()
//                            } else {
//                                //男认证
//                                ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity)
//                                    .navigation()
//                            }
                        }
                    })
                    .show(parentFragmentManager)
            }
        })
    }

    override fun initRequestData() {
        mViewModel.vquDynamicList(type, page)
    }

    override fun onRetryBtnClick() {
        page = 1
        mViewModel.vquDynamicList(type, page)
    }

    fun finishLoad() {
        mBinding.smart.finishRefresh()
        mBinding.smart.finishLoadMore()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.vquDynamicList(type, page)
        mBinding.smart.finishRefresh(3000)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page += 1
        mViewModel.vquDynamicList(type, page)
        mBinding.smart.finishLoadMore(3000)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: String) {
        if ("refreshDynamic" == event) {
            page = 1
            mViewModel.vquDynamicList(type, page)
        } else if ("doubleClick" == event) {
            mBinding.rvDynamic.scrollToPosition(0)
            mBinding.smart.autoRefresh()
            mBinding.smart.finishRefresh(3000)
        } else if ("RecommendedSwitch" == event) {//刷新是否隐藏推荐
            refreshRecommended()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: ScrollToPositionEvent) {
        mBinding.rvDynamic.postDelayed(Runnable { //  moveToPosition(event.position);
            if (event.arr != null && event.listener != null) {
                event.listener.onRegion(
                    event.arr[0],
                    event.arr[1], event.arr[2], event.arr[3], event.arr[4], event.arr[5]
                )
            }
        }, (if (event.delayEnable) event.delayDuration else 0).toLong())
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