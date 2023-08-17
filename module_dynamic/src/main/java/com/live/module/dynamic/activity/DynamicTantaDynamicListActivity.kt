package com.live.module.dynamic.activity

import android.view.Gravity
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.live.module.dynamic.R
import com.live.module.dynamic.adapter.DynamicTantaAdapter
import com.live.module.dynamic.databinding.DynamicTantaActivityDynamicBinding
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.BaseApplication.Companion.context
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.ScrollToPositionEvent
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog.OnButtonSelectListener
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
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
 * description:动态页面
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquDynamicListActivity)
class DynamicTantaDynamicListActivity :
    BaseActivity<DynamicTantaActivityDynamicBinding, DynamicPublishViewModel>(),
    OnRefreshListener, OnLoadMoreListener {
    @Autowired(name = RouteKey.USERID)
    @JvmField
    var userId = 0
    var page: Int = 1
    var isLike: Boolean = false
    var curPos: Int = 0
    var isChat: Boolean = false
    var curUserId: String = ""
    var dynamicVquAdapter: DynamicTantaAdapter? = null
    private var mSelectiveDialog: BottomSelectiveDialog? = null
    override val mViewModel: DynamicPublishViewModel by viewModels()
    override fun DynamicTantaActivityDynamicBinding.initView() {
        mBinding.includeTitle.toolbar.initClose(getString(R.string.dynamic_dynamic)) {
            finish()
        }
        setLoadSir(smart)
        mBinding.smart.setOnRefreshListener(this@DynamicTantaDynamicListActivity)
        mBinding.smart.setOnLoadMoreListener(this@DynamicTantaDynamicListActivity)
        mBinding.rvDynamic.layoutManager = LinearLayoutManager(context)
        dynamicVquAdapter = DynamicTantaAdapter(this@DynamicTantaDynamicListActivity)
        mBinding.rvDynamic.adapter = dynamicVquAdapter
        (mBinding.rvDynamic.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

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
                    if (bean.isLike == 1) {
                        isLike = false
                        mViewModel.vquDynamicLike(1, bean.id)
                        bean.isLike = 0
                        bean.likeCount = bean.likeCount - 1
                        dynamicVquAdapter!!.setData(position, bean)
                        dynamicVquAdapter!!.notifyItemChanged(position)
                    } else {
                        isLike = true
                        mViewModel.vquDynamicLike(0, bean.id)
                        bean.isLike = 1
                        bean.likeCount = bean.likeCount + 1
                        dynamicVquAdapter!!.setData(position, bean)
                        dynamicVquAdapter!!.notifyItemChanged(position)
                    }
                }
                R.id.iv_more -> {//更多
                    var isShow =
                        UserManager.userInfo?.userId == dynamicVquAdapter!!.data[position].userId.toString()
                    mSelectiveDialog =
                        BottomSelectiveDialog(
                            this@DynamicTantaDynamicListActivity,
                            R.style.SelectiveDialog)
                    if (isShow) {
                        mSelectiveDialog?.addSelectButton("删除",
                            OnButtonSelectListener { view, index ->
                                curPos = position
                                mViewModel.vquDeleteDynamic(dynamicVquAdapter!!.data[position].id.toString())
                                mSelectiveDialog?.dismiss()
                            })
//                        mSelectiveDialog?.addSelectButton("删除文字",
//                            OnButtonSelectListener { view, index ->
//                                mViewModel.vquDeleteDynamicTextOrImg(1,dynamicVquAdapter!!.data[position].id.toString())
//                                mSelectiveDialog?.dismiss()
//                            })
                    } else {
                        mSelectiveDialog?.addSelectButton("举报该条动态",
                            OnButtonSelectListener { view, index ->
                                ARouter.getInstance()
                                    .build(RouteUrl.Dynamic.DynamicVquReportActivity)
                                    .withInt(
                                        RouteKey.USERID,
                                        dynamicVquAdapter!!.data[position].userId
                                    )
                                    .withInt(RouteKey.TYPE, 3)
                                    .navigation()
                                mSelectiveDialog?.dismiss()
                            })
                    }
                    mSelectiveDialog?.show()
                }
                R.id.ll_chat -> {//私信
                    UmUtils.setUmEvent(this@DynamicTantaDynamicListActivity, UmUtils.INITIATEPRIVATECHAT )
                    NimUIKit.startP2PSession(
                        this@DynamicTantaDynamicListActivity,
                        dynamicVquAdapter!!.data[position].userId.toString()
                    )
                }
                R.id.ll_beckoning -> {
                    curPos = position
                    curUserId = dynamicVquAdapter!!.data[position].userId.toString()
                    if (dynamicVquAdapter!!.data[position].isBeckon) {
                        isChat = true
                        UmUtils.setUmEvent(this@DynamicTantaDynamicListActivity, UmUtils.INITIATEPRIVATECHAT )
                        if (UserManager.userInfo?.gender == 1) {
                            mViewModel.vquAuth()
                        } else {
                            NimUIKit.startP2PSession(
                                this@DynamicTantaDynamicListActivity,
                                dynamicVquAdapter!!.data[position].userId.toString()
                            )
                        }
                    } else {
                        UmUtils.setUmEvent(this@DynamicTantaDynamicListActivity, UmUtils.CLICKTOCHAT )
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


    override fun initObserve() {
        mViewModel.nyDynamicLsit.observe(this) {
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
            if (isLike) {
//                "点赞成功".toast()
            } else {
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
        mViewModel.vquChatAuthData.observe(this, Observer {
            if (it.data.isRpAuth == 1) {
                if (isChat) {
                    NimUIKit.startP2PSession(
                        this@DynamicTantaDynamicListActivity,
                        curUserId
                    )
                } else {
                    mViewModel.vquSendBeckon("[$curUserId]")
                }

            } else {

                CommonHintDialog()
                    .setTitle("iv_avatar")
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
                    .show(supportFragmentManager)
            }
        })
        mViewModel.vquSendData.observe(this, Observer {
            when (it.code) {
                0 -> {
                    dynamicVquAdapter?.data!![curPos].isBeckon = true
                    dynamicVquAdapter?.notifyItemChanged(curPos)
                }
                1002 -> {
                    "余额不足，请先充值".toast()

                    mPayViewModel.showRechargeDialog(supportFragmentManager)
//                    CommonRechargeDialog().show(supportFragmentManager, "充值")
                }
                else -> {
                    it.message?.toast()
                }
            }

        })
    }

    override fun initRequestData() {
        mViewModel.vquUserDynamicList(userId, page)
    }

    override fun onRetryBtnClick() {
        page = 1
        mViewModel.vquUserDynamicList(userId, page)
    }

    fun finishLoad() {
        mBinding.smart.finishRefresh()
        mBinding.smart.finishLoadMore()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        mViewModel.vquUserDynamicList(userId, page)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        page += 1
        mViewModel.vquUserDynamicList(userId, page)
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
}