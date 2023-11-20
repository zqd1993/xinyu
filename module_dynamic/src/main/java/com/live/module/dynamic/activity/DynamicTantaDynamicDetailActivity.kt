package com.live.module.dynamic.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ktx.immersionBar
import com.live.module.dynamic.R
import com.live.module.dynamic.adapter.*
import com.live.module.dynamic.bean.*
import com.live.module.dynamic.databinding.DynamicTantaActivityDynamicDetailBinding
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.BaseApplication.Companion.context
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.BarUtils.getStatusBarHeight
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.*
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog.OnButtonSelectListener
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.view.ShapeLinearLayout
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewBuilder
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.UserViewInfo
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UiUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.util.KeyBroadUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe


/**
 * author: Tany
 * date: 2022/4/9
 * description:动态详情界面
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquDynamicDetailActivity)
class DynamicTantaDynamicDetailActivity :
    BaseActivity<DynamicTantaActivityDynamicDetailBinding, DynamicPublishViewModel>(),
    OnRefreshListener, OnLoadMoreListener {

    @Autowired(name = RouteKey.ID)
    @JvmField
    var dynamicId = "0"

    private var dynamicDetailView: View? = null

    private lateinit var myAdapter: DynamicTantaCommentAdapter
    private var layoutManager: LinearLayoutManager? = null
    private var selectCircleItemH = 0
    private var commentConfig: CommentConfig? = null
    var isShow = false
    private var peopleId = ""
    private var userId: String = "0"

    var isLike: Boolean = false
    var ivHead: ImageView? = null
    var tvName: TextView? = null
    var tvTime: TextView? = null
    var tvComment: TextView? = null
    var tvInfo: TextView? = null
    var tvCommentCount: TextView? = null
    var tvBottom: TextView? = null
    var tvContent: TextView? = null
    var tvCount: TextView? = null
    var ivLike: ImageView? = null
    var ivImg: ImageView? = null
    var ivPlay: ImageView? = null
    var ivBeckoning: ImageView? = null
    var ivVip: ImageView? = null
    var rvImg: RecyclerView? = null
    var rvHead: RecyclerView? = null
    var llLike: LinearLayout? = null
    var llBeckoning: ShapeLinearLayout? = null
    var llCommentCount: LinearLayout? = null
    var llComment: LinearLayout? = null
    var clLike: View? = null
    var commentTopSll: View? = null
    var page: Int = 1
    var commentId: Int = 0
    var curPos: Int = 0
    var commentCount: Int = 0
    var isBeckon: Int = 0
    var isReply: Boolean = false
    override val mViewModel: DynamicPublishViewModel by viewModels()
    private var mSelectiveDialog: BottomSelectiveDialog? =
        null

    @SuppressLint("ClickableViewAccessibility")
    override fun DynamicTantaActivityDynamicDetailBinding.initView() {
        immersionBar {
            statusBarColor(R.color.white)
            statusBarDarkFont(true)
            fitsSystemWindows(true)
            keyboardEnable(true)
        }
        mBinding.includeTitle.toolbar.initClose(
            getString(R.string.dynamic_dynamic_detail),
            onClickRight = {
                showRight()
            },
            onBack = {
                finish()
            }
        )
        mBinding.smart.setEnableRefresh(true)
        mBinding.smart.setEnableLoadMore(true)
        mBinding.smart.setOnRefreshListener(this@DynamicTantaDynamicDetailActivity)
        mBinding.smart.setOnLoadMoreListener(this@DynamicTantaDynamicDetailActivity)
        myAdapter = DynamicTantaCommentAdapter()
        layoutManager = LinearLayoutManager(this@DynamicTantaDynamicDetailActivity)
        mBinding.rvComment.layoutManager = layoutManager
        mBinding.rvComment.adapter = myAdapter
        myAdapter.headerWithEmptyEnable = true
        dynamicDetailView =
            layoutInflater.inflate(R.layout.dynamic_vqu_layout_dynamic_detail, null, false)
        ivHead = dynamicDetailView?.findViewById(R.id.iv_head)
        tvName = dynamicDetailView?.findViewById(R.id.tv_name)
        tvTime = dynamicDetailView?.findViewById(R.id.tv_time)
        tvComment = dynamicDetailView?.findViewById(R.id.tv_comment)
        tvInfo = dynamicDetailView?.findViewById(R.id.tv_info)
        tvCommentCount = dynamicDetailView?.findViewById(R.id.tv_comment_count)
        tvContent = dynamicDetailView?.findViewById(R.id.tv_content)
        tvCount = dynamicDetailView?.findViewById(R.id.tv_count)
        ivLike = dynamicDetailView?.findViewById(R.id.iv_like)
        ivImg = dynamicDetailView?.findViewById(R.id.iv_img)
        ivVip = dynamicDetailView?.findViewById(R.id.iv_vip)
        ivPlay = dynamicDetailView?.findViewById(R.id.iv_play)
        ivBeckoning = dynamicDetailView?.findViewById(R.id.iv_beckoning)
        tvBottom = dynamicDetailView?.findViewById(R.id.tv_bottom)
        rvImg = dynamicDetailView?.findViewById(R.id.rv_img)
        llLike = dynamicDetailView?.findViewById(R.id.ll_like)
        llBeckoning = dynamicDetailView?.findViewById(R.id.ll_beckoning)
        rvHead = dynamicDetailView?.findViewById(R.id.rv_head)
        llCommentCount = dynamicDetailView?.findViewById(R.id.ll_comment_count)
        llComment = dynamicDetailView?.findViewById(R.id.ll_comment)
        clLike = dynamicDetailView?.findViewById(R.id.cl_like)
        commentTopSll = dynamicDetailView?.findViewById(R.id.comment_top_sll)
        if (!myAdapter.hasHeaderLayout()) {
            myAdapter.addHeaderView(dynamicDetailView!!)
        }
        setViewTreeObserver()
        setLoadSir(smart)
        mBinding.tvSend.setOnClickListener {
            var comment: String? = mBinding.etComment.text.toString().trim()
            if (comment.isNullOrEmpty()) {
                "请输入要发布的内容".toast()
            } else {
                vquComment(comment, commentId)
            }
        }
        myAdapter.setOnItemLongClickListener { adapter, view, position ->
            showMenu(UserManager.userInfo?.userId == myAdapter.data[position].userId.toString(),
                position)
            true
        }
        myAdapter.setNbOnItemClickListener { adapter, view, position ->
            if (UserManager.userInfo?.userId != myAdapter.data[position].userId.toString()) {
                if (isShow) {
                    updateEditTextBodyVisible(View.GONE, null)
                } else {
                    val config = CommentConfig()
                    config.circlePosition = position
                    config.commentPosition = 0
                    config.commentType = CommentConfig.Type.PUBLIC
                    config.replyUser = myAdapter.data.get(position).nickname
                    config.commentId = myAdapter.data.get(position).id
                    config.userId = myAdapter.data.get(position).userId.toString()
                    updateEditTextBodyVisible(View.VISIBLE, config)
                }
            }
        }
        mBinding.rvComment.setOnTouchListener { v, event ->
            if (isShow) {
                updateEditTextBodyVisible(View.GONE, null)
                true
            }
            false
        }
        llComment?.setOnClickListener {
            updateEditTextBodyVisible(View.VISIBLE, null)
        }
    }

    //评论
    @Subscribe
    fun onEventMainThread(event: CommentPositionEvent?) {
        if (event?.isLongClick == true) {
            showMenu(UserManager.userInfo?.userId == event.commentBean?.userId.toString(),
                event.position)
        } else {
            if (UserManager.userInfo?.userId != event?.commentBean?.userId.toString()) {
                if (isShow) {
                    updateEditTextBodyVisible(View.GONE, null)
                } else {
                    val config = CommentConfig()
                    config.circlePosition = event?.position!!
                    config.commentPosition = 0
                    config.commentType = CommentConfig.Type.PUBLIC
                    config.replyUser = event?.commentBean?.nickname
                    config.commentId = event?.commentBean?.id!!
                    config.userId = event?.commentBean?.userId.toString()
                    updateEditTextBodyVisible(View.VISIBLE, config)
                }
            }
        }
    }

    private fun showRight() {
        var isShow =
            UserManager.userInfo?.userId == userId
        mSelectiveDialog =
            BottomSelectiveDialog(this@DynamicTantaDynamicDetailActivity,
                R.style.SelectiveDialog)
        if (isShow) {
            mSelectiveDialog?.addSelectButton("删除",
                OnButtonSelectListener { view, index ->
                    CommonHintDialog()
                        .setContent(
                            "是否确定删除这条动态?"
                        )
                        .setLeftText("取消")
                        .setRightText("删除")
                        .setContentSize(15)
                        .setContentGravity(Gravity.CENTER)
                        .setOnClickListener(object :
                            CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {
                                dialogFragment.dismissAllowingStateLoss()
                            }

                            override fun onRight(dialogFragment: DialogFragment) {
                                mViewModel.vquDeleteDynamic(dynamicId)
                                mSelectiveDialog?.dismiss()
                            }
                        })
                        .show(supportFragmentManager)

                })
        } else {
            mSelectiveDialog?.addSelectButton("举报该条动态",
                OnButtonSelectListener { view, index ->
                    ARouter.getInstance()
                        .build(RouteUrl.Dynamic.DynamicVquReportActivity)
                        .withInt(
                            RouteKey.USERID,
                            dynamicId.toInt()
                        )
                        .withInt(RouteKey.TYPE, 3)
                        .navigation()
                    mSelectiveDialog?.dismiss()
                })
        }
        mSelectiveDialog?.show()
    }

    private fun getCommentList(isFresh: Boolean) {
        if (isFresh) {
            page = 1
        } else {
            page += 1
        }
        mViewModel.vquGetCommentList(dynamicId, page)
    }

    private fun vquComment(content: String, commentId: Int) {
        isReply = commentId > 0
        mViewModel.vquComment(dynamicId, content, commentId)
    }

    private fun showMenu(isMine: Boolean, position: Int) {
        var mSelectiveDialog =
            BottomSelectiveDialog(this@DynamicTantaDynamicDetailActivity,
                R.style.SelectiveDialog)
        if (isMine) {
            mSelectiveDialog.addSelectButton("删除",
                OnButtonSelectListener { view, index ->
                    CommonHintDialog()
                        .setContent(
                            "是否确定删除这条评论?"
                        )
                        .setLeftText("取消")
                        .setRightText("删除")
                        .setContentSize(15)
                        .setContentGravity(Gravity.CENTER)
                        .setOnClickListener(object :
                            CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {
                                dialogFragment.dismissAllowingStateLoss()
                            }

                            override fun onRight(dialogFragment: DialogFragment) {
                                curPos = position
                                mViewModel.vquDeleteComment(dynamicId, myAdapter.data[position].id)
                            }
                        })
                        .show(supportFragmentManager)

                })
        } else {
            mSelectiveDialog.addSelectButton("回复",
                OnButtonSelectListener { view, index ->
                    Handler().postDelayed({
                        if (isShow) {
                            updateEditTextBodyVisible(View.GONE, null)
                        } else {
                            val config = CommentConfig()
                            config.circlePosition = position
                            config.commentPosition = 0
                            config.commentType = CommentConfig.Type.PUBLIC
                            config.replyUser = myAdapter.data.get(position).nickname
                            config.commentId = myAdapter.data.get(position).id
                            config.userId = myAdapter.data.get(position).userId.toString()
                            updateEditTextBodyVisible(View.VISIBLE, config)
                        }
                    }, 100)
                })
            mSelectiveDialog.addSelectButton("举报",
                OnButtonSelectListener { view, index ->
                    CommonHintDialog()
                        .setContent(
                            "是否举报这条评论?"
                        )
                        .setLeftText("取消")
                        .setRightText("举报")
                        .setContentSize(15)
                        .setContentGravity(Gravity.CENTER)
                        .setOnClickListener(object :
                            CommonHintDialog.OnClickListener {
                            override fun onLeft(dialogFragment: DialogFragment) {
                                dialogFragment.dismissAllowingStateLoss()
                            }

                            override fun onRight(dialogFragment: DialogFragment) {
                                mViewModel.vquReportComment(myAdapter.data[position].id)
                            }
                        })
                        .show(supportFragmentManager)
                })

        }
        mSelectiveDialog?.show()

    }

    override fun initObserve() {
        mViewModel.dynamicDetail.observe(this, Observer {
            if (it.code == 0) {
                initDetail(it.data)
                getCommentList(true)
                mLoadService?.showSuccess()
            } else if (it.code == 60001) {
                showEmpty("该动态已被删除~")
                mBinding.clComment.gone()
                mBinding.includeTitle.ivRight.gone()
            }

        })
        mViewModel.dynamicLike.observe(this) {
            if (isLike) {
//                "点赞成功".toast()
            } else {
            }
            mViewModel.vquDynamicInfo(dynamicId)
        }
        mViewModel.dynamicDetailCommentListData.observe(this, Observer {
            finishRefresh()
            if (page == 1) {
                if (it.data == null) {
                    llCommentCount?.gone()
                } else {
                    if (it.data.total > 0) {
                        commentCount = it.data.total
//                        llCommentCount?.visible()
                        tvCommentCount?.text = "评论(" + it.data.total.toString() + ")"
                    } else {
                        llCommentCount?.gone()
                    }
                }
            }
            if (it.data != null && it.data.list != null) {
                initCommentList(it.data.list)
            }


        })
        mViewModel.dynamicCommentData.observe(this, Observer {
            if (isReply) {
                "回复评论成功".toast()
            } else {
                "评论成功".toast()
            }

            mBinding.etComment.setText("")
            mViewModel.vquDynamicInfo(dynamicId)
            KeyBroadUtils.hideKeyboard(this@DynamicTantaDynamicDetailActivity)
//            layoutManager!!.scrollToPosition(myAdapter.data.size - 1)
        })
        mViewModel.dynamicDelCommentData.observe(this, Observer {
            "删除成功".toast()
            myAdapter.removeAt(curPos)
            commentCount -= 1
            if (commentCount == 0) {
                llCommentCount?.gone()
            } else {
                tvCommentCount?.text = "评论($commentCount)"
            }
            tvComment?.text = if (commentCount > 0) {
                commentCount.toString()
            } else {
                "评论"
            }
        })
        mViewModel.vquDelDynamicData.observe(
            this, Observer {
                "删除成功".toast()
                finish()
            }
        )
        mViewModel.vquChatAuthDetailData.observe(this, Observer {
            if (it.data.isRpAuth == 1) {
                mViewModel.vquSendBeckonByDetail("[$userId]")
            } else {
                CommonHintDialog()
                    .setTitle("真人认证")
                    .setContent(resources.getString(R.string.common_vqu_auth))
                    .setLeftText("暂不认证")
                    .setRightText("去认证")
                    .setContentSize(15)
                    .setContentGravity(Gravity.CENTER)
                    .setOnClickListener(object :
                        CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {}
                        override fun onRight(dialogFragment: DialogFragment) {
                            ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
                                .navigation()
                        }
                    })
                    .show(supportFragmentManager)
            }
        })
        mViewModel.vquSendBeckoningData.observe(this, Observer {
            when (it.code) {
                0 -> {
                    isBeckon = 1
                    tvBottom?.setTextColor(ContextCompat.getColor(context, R.color.white))
                    tvBottom?.text = context.getString(R.string.dynamic_chat)
                    ivBeckoning?.setImageResource(R.mipmap.resources_tanta_main_home_chat)
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
        mViewModel.dynamicReportCommentData.observe(this, Observer {
            "举报成功".toast()
        })
    }

    var commentList = mutableListOf<DynamicTantaCommentBean>()
    private fun initCommentList(list: MutableList<DynamicTantaCommentBean>) {
        if (page == 1) {
            commentList.clear()
        }
        commentList.addAll(list)
        if(commentList.size > 0){
            commentTopSll?.visibility = View.VISIBLE
        } else {
            commentTopSll?.visibility = View.GONE
        }
        myAdapter.setList(commentList)
    }

    override fun initRequestData() {
        mViewModel.vquDynamicInfo(dynamicId)
    }

    fun initDetail(bean: DynamicVquDetailBean) {
        userId = bean.user.userId.toString()
        if (UserManager.userInfo?.userId == userId) {
            llBeckoning?.gone()
        }
        ivHead?.vquLoadCircleImage(NetBaseUrlConstant.IMAGE_URL + bean.user.avatar,
            R.mipmap.ic_common_head_circle_def)
        tvName?.text = bean.user.nickname
        tvTime?.text = bean.createTime
        tvContent?.text = bean.content
        tvName?.setVip(bean.isVip > 0)
        if (bean.isVip > 0) {
            ivVip?.visible()
        } else {
            ivVip?.gone()
        }
        val builder = (bean.user.age +
                "岁" + isEmpty(bean.user.height) + isEmpty(bean.user.occupation) + isEmpty(bean.user.weight))
        tvInfo?.text = builder
        if (bean.content.isNullOrEmpty()) {
            tvContent?.gone()
        } else {
            tvContent?.visible()
        }
        tvComment?.text = if (bean.commentCount > 0) {
            bean.commentCount.toString()
        } else {
            "评论"
        }
        tvCount?.text = if (bean.likeCount > 0) {
            bean.likeCount.toString()
        } else {
            "点赞"
        }
        ivLike?.isSelected = bean.isLike != 0
        if (bean.video == null) {
            ivImg?.gone()
            ivPlay?.gone()
        } else {
            var layoutParams = ivImg?.layoutParams
            layoutParams?.width = UiUtils.dip2px(context, 160f)
            layoutParams?.height =
                UiUtils.dip2px(context, 213f)
            ivImg?.layoutParams = layoutParams
            ivImg?.vquLoadRoundImage(bean.video.coverUrl, 24)
            ivImg?.visible()
            ivPlay?.visible()
            ivImg?.setOnClickListener {
                clickVideo(bean)
            }
        }
        if (bean.images.isNullOrEmpty()) {
            rvImg?.gone()
        } else {
            if (bean.images.size == 1) {
                var img = bean.images[0]
                if (img.width == 0 || img.height == 0) {
                    var layoutParams = ivImg?.layoutParams
                    layoutParams?.width =
                        UiUtils.dip2px(context, 160f)
                    layoutParams?.height =
                        UiUtils.dip2px(context, 213f)
                    ivImg?.layoutParams = layoutParams
                } else {
                    var proportion: Float = img.width * 1.0f / img.height * 1.0f
                    if (proportion >= 5f) {
                        var layoutParams = ivImg?.layoutParams
                        layoutParams?.width =
                            UiUtils.dip2px(context, 210f)
                        layoutParams?.height =
                            UiUtils.dip2px(context, 42f)
                        ivImg?.layoutParams = layoutParams
                    } else if (proportion <= 0.2f) {
                        var layoutParams = ivImg?.layoutParams
                        layoutParams?.width =
                            UiUtils.dip2px(context, 42f)
                        layoutParams?.height =
                            UiUtils.dip2px(context, 210f)
                        ivImg?.layoutParams = layoutParams
                    } else {
                        if (img.width >= img.height) {
                            var layoutParams = ivImg?.layoutParams
                            layoutParams?.width =
                                UiUtils.dip2px(context, 210f)
                            layoutParams?.height =
                                UiUtils.dip2px(context,
                                    img.height * 1.0f / img.width * 1.0f * 210)
                            ivImg?.layoutParams = layoutParams
                        } else {
                            var layoutParams = ivImg?.layoutParams
                            layoutParams?.width =
                                UiUtils.dip2px(context,
                                    img.width * 1.0f / img.height * 1.0f * 210)
                            layoutParams?.height =
                                UiUtils.dip2px(context, 210f)
                            ivImg?.layoutParams = layoutParams
                        }
                    }
                }

                rvImg?.gone()
                ivImg?.vquLoadRoundImage(NetBaseUrlConstant.IMAGE_URL + bean.images[0].url,
                    24)
                ivImg?.visible()
                ivImg?.setOnClickListener {
                    clickImage(bean.images[0].url, 0, ivImg!!)
                }

            } else {
                ivImg?.gone()
                rvImg?.visible()
                var imgAdapter =
                    DynamicTantaImgAdapter(this@DynamicTantaDynamicDetailActivity, bean.images)
                if (bean.images.size == 2 || bean.images.size == 4) {
                    rvImg?.layoutManager = GridLayoutManager(context, 2)
                } else {
                    rvImg?.layoutManager = GridLayoutManager(context, 3)
                }
                rvImg?.adapter = imgAdapter
            }
        }
        llLike?.setOnClickListener {
            if (bean.isLike == 1) {
                isLike = false
                mViewModel.vquDynamicLike(1, bean.id)
                bean.isLike = 0
                bean.likeCount = bean.likeCount - 1
                ivLike?.isSelected = false
                tvCount?.text = bean.likeCount.toString()
            } else {
                isLike = true
                mViewModel.vquDynamicLike(0, bean.id)
                bean.isLike = 1
                bean.likeCount = bean.likeCount + 1
                ivLike?.isSelected = true
                tvCount?.text = bean.likeCount.toString()
            }
        }
        if (UserManager.userInfo?.userId != bean.user.userId.toString()) {
            clLike?.gone()
        } else {
            if (bean.likeRecord == null) {
                clLike?.gone()
                return
            }
            if (bean.likeRecord?.isNullOrEmpty()) {
                clLike?.gone()
                return
            }
            clLike?.visible()
            rvHead?.layoutManager = GridLayoutManager(this@DynamicTantaDynamicDetailActivity, 9)
            var headAdapter = DynamicTantaLikeHeadAdapter(bean.likeRecord)
            rvHead?.adapter = headAdapter
            headAdapter.setNbOnItemClickListener { adapter, view, position ->
                ARouter.getInstance()
                    .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                    .withInt(
                        RouteKey.USERID,
                        headAdapter!!.data[position].id
                    )
                    .navigation()
            }
        }
        ivBeckoning?.setImageResource(if (bean.isBeckon == 1) R.mipmap.resources_tanta_main_home_chat else R.mipmap.resources_tanta_home_like)
        if (bean.isBeckon == 1) {
            tvBottom?.text = ResUtils.getString(R.string.dynamic_chat)
            tvBottom?.setTextColor(ResUtils.getColor(R.color.white))
        } else {
            if (bean.user.gender == 1) {
                tvBottom?.text = context.getString(R.string.common_vqu_accost)
            } else {
                tvBottom?.text = context.getString(R.string.common_vqu_beckoning)
            }
            tvBottom?.setTextColor(ContextCompat.getColor(context, R.color.color_B4A3FD))

        }




        isBeckon = bean.isBeckon
        llBeckoning?.setOnClickListener {
            if (isBeckon == 1) {//私聊
                NimUIKit.startP2PSession(
                    this@DynamicTantaDynamicDetailActivity,
                    bean.user.userId.toString()
                )
            } else {//心动
                if (UserManager.userInfo?.gender == 1) {
                    mViewModel.vquAuthByDetail()
                } else {
                    mViewModel.vquSendBeckonByDetail("[$userId]")
                }
            }
        }
    }

    private fun isEmpty(content: String?): String {
        return if (TextUtils.isEmpty(content)) {
            ""
        } else {
            " | $content"
        }
    }

    private fun clickImage(path: String, innerCount: Int, imageView: ImageView) {
        val list: MutableList<UserViewInfo> =
            ArrayList()
        val bounds = Rect()
        imageView.getGlobalVisibleRect(bounds)
        val userViewInfo =
            UserViewInfo(
                NetBaseUrlConstant.IMAGE_URL + path)
        userViewInfo.bounds = bounds
        list.add(userViewInfo)
        if (list.isNullOrEmpty()) {
            return
        }
        GPreviewBuilder.from(this@DynamicTantaDynamicDetailActivity)
            .setData<UserViewInfo>(list)
            .setCurrentIndex(innerCount)
            .setType(GPreviewBuilder.IndicatorType.Number)
            .start()

    }

    private fun clickVideo(item: DynamicVquDetailBean) {
        val globalRect = Rect()
        ivImg?.getGlobalVisibleRect(globalRect)
        val intent =
            Intent(this@DynamicTantaDynamicDetailActivity, DynamicTantaVideoActivity::class.java)
        intent.putExtra("video_url", item.video.fileUrl + "")
        intent.putExtra("cover_url", item.video.coverUrl + "")
        intent.putExtra(
            "region",
            intArrayOf(
                globalRect.left,
                globalRect.top,
                globalRect.right,
                globalRect.bottom,
                ivImg!!.width,
                ivImg!!.height
            )
        )
        intent.putExtra("position", 0)
        startActivity(intent)
        overridePendingTransition(0, 0)

    }

    private var screenHeight = 0
    private var currentKeyboardH = 0
    private var editTextBodyHeight = 0
    private var selectCommentItemOffset = 0

    private fun setViewTreeObserver() {
        val swipeRefreshLayoutVTO: ViewTreeObserver = mBinding.smart.getViewTreeObserver()
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener {
            val r = Rect()
            mBinding.smart.getWindowVisibleDisplayFrame(r)
            val statusBarH = getStatusBarHeight() //状态栏高度
            val screenH: Int = mBinding.smart.getRootView().getHeight()
            if (r.top != statusBarH) {
                //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                r.top = statusBarH
            }
            val keyboardH = screenH - (r.bottom - r.top)
            if (keyboardH == currentKeyboardH) { //有变化时才处理，否则会陷入死循环
                return@addOnGlobalLayoutListener
            }
            currentKeyboardH = keyboardH
            screenHeight = screenH //应用屏幕的高度

            editTextBodyHeight = mBinding.clComment.getHeight()
            if (keyboardH < 150) { //说明是隐藏键盘的情况
                updateEditTextBodyVisible(View.GONE, null)
                return@addOnGlobalLayoutListener
            } else {
                isShow = true
            }
            //偏移listview
            //偏移listview
            if (layoutManager != null && commentConfig != null) {
                layoutManager!!.scrollToPositionWithOffset(
                    commentConfig!!.circlePosition,
                    getListviewOffset(commentConfig)
                )
            }
        }
    }


    /**
     * 测量偏移量
     * @param commentConfig
     * @return
     */
    private fun getListviewOffset(commentConfig: CommentConfig?): Int {
        if (commentConfig == null) return 0
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        var listviewOffset: Int =
            screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - mBinding.includeTitle.toolbar.height - mBinding.clComment.height
        if (commentConfig.commentType === CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset
        }
        return listviewOffset
    }


    fun updateEditTextBodyVisible(visibility: Int, commentConfig: CommentConfig?) {
        this.commentConfig = commentConfig
        measureCircleItemHighAndCommentItemOffset(commentConfig)
        if (View.VISIBLE == visibility) {
            mBinding.etComment.requestFocus()
            isShow = true
            //弹出键盘
            KeyBroadUtils.showSoftInput(CircleCommentActivity@ this, mBinding.etComment)
            if (commentConfig != null) {
                mBinding.etComment.hint = "回复" + commentConfig.replyUser
                peopleId = commentConfig.userId
                commentId = commentConfig.commentId

            }
//            CommonUtils.showSoftInput(editText.getContext(), editText)
        } else if (View.GONE == visibility) {
            //隐藏键盘
            isShow = false
            KeyBroadUtils.hideKeyboard(CircleCommentActivity@ this)
            peopleId = ""
            commentId = 0
            mBinding.etComment.hint = "评论一下"
        }

    }

    private fun measureCircleItemHighAndCommentItemOffset(commentConfig: CommentConfig?) {
        if (commentConfig == null) return
        val firstPosition: Int = layoutManager!!.findFirstVisibleItemPosition()
        //只能返回当前可见区域（列表可滚动）的子项
        val selectCircleItem =
            layoutManager!!.getChildAt(commentConfig.circlePosition - firstPosition)
        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.height
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.vquDynamicInfo(dynamicId)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        getCommentList(false)
    }

    fun finishRefresh() {
        mBinding.smart.finishRefresh()
        mBinding.smart.finishLoadMore()
    }
}