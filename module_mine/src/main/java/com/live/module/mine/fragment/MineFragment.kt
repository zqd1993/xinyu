package com.live.module.mine.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.live.module.mine.R
import com.live.module.mine.adapter.MineVquSignDayAdapter
import com.live.module.mine.databinding.MineFragmentMineBinding
import com.live.module.mine.dialog.SignDayDialog
import com.live.module.mine.dialog.SignDaySuccessDialog
import com.live.module.mine.vm.MineSignDayViewModel
import com.live.module.mine.vm.MineViewModel
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.CommonApplication
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.utils.*
import com.qiyukf.unicorn.api.UICustomization
import com.qiyukf.unicorn.api.Unicorn
import com.qiyukf.unicorn.api.YSFUserInfo
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author: Lau
 * date: 2022/4/1
 * description:我的页面
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Mine.MineVquFragment)
@EventBusRegister
class MineFragment : BaseLazyFrameFragment<MineFragmentMineBinding, MineViewModel>() {

    override val mViewModel: MineViewModel by viewModels()

    val mSignDayViewModel: MineSignDayViewModel by viewModels()

    /**
     * 网格菜单数据
     * 在[initVquGridMenuData]中初始化数据
     * 并且在[MineViewModel.userInfoData]的回调中判断是否赋值女神设置菜单，详情
     * 查看[parsingUserInfo]方法
     */
//    private val mVquGirdMenuData = mutableListOf<CommonVquMenuBean>()

    /**
     * 列表菜单数据
     * 在[initVquListMenuData]中初始化数据
     */
    private val mVquListMenuData = mutableListOf<CommonVquMenuBean>()

    /**
     * 网格菜单的适配器
     */
//    private val mVquGridMenuAdapter = GridMenuAdapter()

    /**
     * 列表菜单的适配器
     */
    private val mVquListMenuAdapter = ListMenuAdapter()

    private val mVquSignDayAdapter = MineVquSignDayAdapter()

    private var mVquUserHomeBean: VquUserHomeBean? = null

    private val mBannerList =
        mutableListOf<CommonVquBannerBean>()

    private var isTaskRewards: Int = 0    //默认任务中心无红点

    var isVip = false
    override fun onResume() {
        super.onResume()
        refreshVipValue()
    }

    //模拟数据vip
    private fun refreshVipValue() {
        if (UserManager.userInfo?.vip == 0) {
            mBinding.tvVipTip.text = "尊贵会员，享超级权益"
            mBinding.btnVip.text = "开通VIP"
            mBinding.ivVipLabel.visibility = View.GONE
        } else {
            mBinding.tvVipTip.text = UserManager.userInfo?.vipDes
            mBinding.btnVip.text = "续费"
            mBinding.ivVipLabel.visibility = View.VISIBLE
        }
    }

    override fun MineFragmentMineBinding.initView() {

        refreshVipValue()
//        mBinding.srlMineRefresh.setOnRefreshListener {
//            mViewModel.vquGetUserInfo()
//            mViewModel.vquWalletIndex()
//
//            mBinding.srlMineRefresh.finishRefresh()
//        }
        val typeface = Typeface.createFromAsset(
            BaseApplication.application.assets,
            "fonts/DINAlternateBold.ttf"
        ) //根据路径得到Typeface
        mBinding.tvMineMyEarningsNum.typeface = typeface

        mBinding.tvMineMyRechargeNum.typeface = typeface

        mBinding.clMineUserInfoParent.setViewClickListener(1) {
            ARouter.getInstance()
                .build(RouteUrl.Info.InfoVquPersonalInfoActivity)
                .withInt(
                    RouteKey.USERID,
                    (UserManager.userInfo?.userId ?: "0").toInt()
                )
                .navigation()
        }

        mBinding.tvMineVquUserVquId.setViewClickListener(1) {
            val cm: ClipboardManager =
                BaseApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(
                ClipData.newPlainText(
                    null,
                    UserManager.userInfo?.usercode ?: ""
                )
            )

            toast(R.string.vqu_bill_copy_success)
        }

        mBinding.btnVip.setViewClickListener(1) {

            ARouter.getInstance().build(RouteUrl.Vip.VipTantaCenterActivity)
                .withBoolean(RouteKey.USER_VIP, UserManager.userInfo?.vip!! > 0)
                .navigation()
        }

//        mBinding.llMineGradeParent.setViewClickListener {
//
//            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
//                .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.myGrade ?: "")
//                .navigation()
//        }

        mBinding.tvMineMyRechargeNum.typeface = FontsFamily.tfDDinExpBold
        mBinding.tvMineMyEarningsNum.typeface = FontsFamily.tfDDinExpBold
    }

    /**
     * 初始化列表菜单适配器
     */
    private fun initVquListMenuAdapter() {


        mVquListMenuAdapter.setOnItemClickListener { _, _, position ->
            val item = mVquListMenuAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            when (item.type) {
                1 -> {    //美颜设置
                    PermissionUtils.cameraPermission(
                        this@MineFragment,
                        requestCallback = { allGranted, _, _ ->
                            if (allGranted) {
                                ARouter.getInstance()
                                    .build(RouteUrl.Agora2.AgoraTantaBeautySettingActivity)
                                    .navigation()
                            } else {
                                toast("美颜功能缺少摄像头相关权限")
                            }
                        })
                }

                2 -> {    //在线客服
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.help ?: "")
                        .navigation()
                }

                3 -> {    //违规公布
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.publish ?: "")
                        .navigation()
                }

                4 -> {    //系统设置
                    ARouter.getInstance()
                        .build(RouteUrl.Setting.SettingVquActivity)
                        .navigation()
                }

                5 -> {  //邀请有奖
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.share ?: "")
                        .navigation()
                }

                6 -> {  //女神设置
                    ARouter.getInstance().build(RouteUrl.Anchor.AnchorVquSettingsActivity)
                        .navigation()
                }

                7 -> {  //任务中心
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.taskCenter ?: "")
                        .navigation()
                    com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
                        activity,
                        com.mshy.VInterestSpeed.common.utils.UmUtils.TASKCENTER
                    )

//                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquCameraActivity)
//                        .navigation()
                }

                8 -> {
                    clickMyAuth()
                }

                9 -> {

//                    PermissionUtils.videoPermission(this, requestCallback =object :RequestCallback{
//                        override fun onResult(
//                            allGranted: Boolean,
//                            grantedList: MutableList<String>,
//                            deniedList: MutableList<String>
//                        ) {
//                            ARouter.getInstance().build(RouteUrl.Agora2.AgoraVquVideoActivity).navigation()
//                        }
//
//                    })


                    ARouter.getInstance().build(RouteUrl.Contact.ContactTantaMyContactActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.share ?: "")
                        .navigation()
                }

                10 -> {
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(
                            RouteKey.URL,
                            "http://asset.zhenban.top/h5_clear/jingwang.html"
                        )//此处有H5
                        .navigation()
                }
            }
        }
    }

    override fun onFragmentFirstVisible() {
        initView()
        mViewModel.vquIndexBanner()
    }

    override fun dispatchUserVisibleHint(isVisible: Boolean) {
        super.dispatchUserVisibleHint(isVisible)
        if (isVisible) {
            mViewModel.vquGetUserInfo()
            mViewModel.vquWalletIndex()
            mSignDayViewModel.vquTodayActivityIndex()
        }
    }

    private fun initView() {


        /**
         * 网格菜单数据初始化
         */
//        initVquGridMenuData()

        /**
         * 列表菜单数据初始化
         */
        initVquListMenuData()

        /**
         * 初始化网格菜单适配器
         */
//        initVquGridMenuAdapter()

        /**
         * 初始化列表菜单适配器
         */
        initVquListMenuAdapter()


        initVquSignDay()

        mBinding.rvMineVquUserListMenu.adapter = mVquListMenuAdapter
        mVquListMenuAdapter.setNewInstance(mVquListMenuData)

        mBinding.clMineVquFocusParent.setViewClickListener(200) {
//            startRelationActivity(RouteKey.RelationType.FOCUS, R.string.mine_vqu_my_focus)

            ARouter.getInstance().build(RouteUrl.Relation.RelationVquFriendActivity)
                .withInt(RouteKey.TYPE, 0).navigation()

        }

        mBinding.clMineVquFansParent.setViewClickListener(200) {
//            startRelationActivity(RouteKey.RelationType.FANS, R.string.mine_vqu_my_fans)

            ARouter.getInstance().build(RouteUrl.Relation.RelationVquFriendActivity)
                .withInt(RouteKey.TYPE, 1).navigation()

        }

        mBinding.clMineVquVisitorParent.setViewClickListener(200) {
            mBinding.stvMineVquVisitorNewNum.text = "0"
            mBinding.stvMineVquVisitorNewNum.visibility = View.GONE
            startRelationActivity(RouteKey.RelationType.VISTOR, R.string.mine_vqu_my_visitor)
        }

        mBinding.clMineVquTrackParent.setViewClickListener(200) {
            startRelationActivity(RouteKey.RelationType.TRACK, R.string.mine_vqu_my_track)

            /*
            if (UserManager.userInfo?.isAnchor == 1) {
//                startRelationActivity(RouteKey.RelationType.PRAISE, R.string.mine_vqu_my_praise)
                ARouter.getInstance().build(RouteUrl.Relation.RelationVquPraiseListActivity)
                    .navigation()
            } else {
                startRelationActivity(RouteKey.RelationType.TRACK, R.string.mine_vqu_my_track)
            }

             */
        }

        mBinding.cdMineMyRechargeParent.setViewClickListener(200) {
            ARouter.getInstance().build(RouteUrl.Bill.BillTantaRechargeActivity).navigation()
        }

        mBinding.cdMineMyEarningsParent.setViewClickListener(200) {
            ARouter.getInstance().build(RouteUrl.Bill.BillTantaEarningsActivity).navigation()
        }
        mBinding.clMineToEditInfoParent.setViewClickListener(200) {
            ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
        }
        mBinding.ivMineVquUserImproveData.setViewClickListener(200) {
            ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
        }
    }

    private fun initVquSignDay() {
        mBinding.rvMineVquMineSignDay.adapter = mVquSignDayAdapter

        mVquSignDayAdapter.setOnItemClickListener { _, _, position ->

//            val item =
//                mVquSignDayAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

//            if (item.nowDay && !item.status) {
//                mViewModel.vquTodayActivity()
//            } else if (position >= mVquSignDayAdapter.todayCount) {
//                toast("今天已经签到过，明天才能签到了哦")
//            }

            val signDayDialog = SignDayDialog()
            signDayDialog.show(childFragmentManager, "")
        }
    }

    /**
     * 初始化网格菜单适配器
     */
    /*
    private fun initVquGridMenuAdapter() {
        mVquGridMenuAdapter.setOnItemClickListener { _, _, position ->

            val item = mVquGridMenuAdapter.getItemOrNull(position) ?: return@setOnItemClickListener

            when (item.type) {
                1 -> {    //邀请好友
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.share ?: "")
                        .navigation()
                }
                2 -> {    //任务中心
                    ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.taskCenter ?: "")
                        .navigation()
                }
                3 -> {    //我的背包
                    ARouter.getInstance().build(RouteUrl.Mine.MineVquMyBackpackActivity)
                        .navigation()
                }
                4 -> {    //我的人脉
                    ARouter.getInstance().build(RouteUrl.Contact.ContactVquMyContactActivity)
                        .withString(RouteKey.URL, mVquUserHomeBean?.webUrl?.share ?: "")
                        .navigation()
                }
                5 -> {    //我的认证

                    clickMyAuth()

                }
                6 -> {    //主播设置
                    ARouter.getInstance().build(RouteUrl.Anchor.AnchorVquSettingsActivity)
                        .navigation()
                }
            }
        }
    }

     */

    private fun clickMyAuth() {

        ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity)
            .navigation()

        /*
        val bean = mVquUserHomeBean ?: return

        when (bean.userinfo.isAuth) {
            1 -> {
                //审核成功
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                    .withInt(RouteKey.TYPE, 1)
                    .navigation()
            }
            2 -> {
                //审核失败
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquResultActivity)
                    .withInt(RouteKey.TYPE, 3)
                    .navigation()
            }
            else -> {

                if (UserManager.userInfo?.gender == 1) {
                    //女认证
                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquFaceIdentifyActivity)
                        .navigation()
                } else {
                    //男认证
                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity)
                        .navigation()
                }

//                val messageDialog = MessageDialog()
//                messageDialog.setTitle("真人认证")
//                messageDialog.setContent("心语提倡真实交友，真人认证通过后即可心动交友啦~")
//                messageDialog.setRightText("去认证")
//                messageDialog.setOnButtonClickListener(object :
//                    MessageDialog.OnButtonClickListener {
//                    override fun onLeftClick(): Boolean {
//                        return false
//                    }
//
//                    override fun onRightClick(): Boolean {
//
//
//                        return false
//                    }
//                })
//                messageDialog.show(childFragmentManager, "")
            }
        }

         */

    }

    private fun startRelationActivity(type: Int, @StringRes titleId: Int) {

        val title = getString(titleId)

        ARouter.getInstance().build(RouteUrl.Relation.RelationVquActivity)
            .withInt(RouteKey.TYPE, type).withString(RouteKey.TITLE, title).navigation()
    }

    /**
     * 列表菜单数据初始化
     */
    private fun initVquListMenuData() {

        mVquListMenuData.clear()

        mVquListMenuData.add(//暂时去掉
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_invite,
                getString(R.string.mine_vqu_menu_invite),
                type = 5,
                desc = "邀请好友，最多奖励8888金币",
                descIcon = R.mipmap.ic_mine_list_menu_desc_red_pack
            )
        )

        mVquListMenuData.add(//暂时去掉
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_task,
                getString(R.string.mine_vqu_menu_task),
                type = 7,
                desc = "做任务，免费赚金币",
                descIcon = R.mipmap.ic_mine_list_menu_desc_diamond,
                showRedPoint = isTaskRewards
            )
        )

        mVquListMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_auth,
                getString(R.string.mine_vqu_menu_auth),
                type = 8
            )
        )
        mVquListMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_jw,
                "净网行动",
                type = 10
            )
        )

        mVquListMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_contacts,
                getString(R.string.mine_vqu_menu_contacts),
                type = 9
            )
        )


//        mVquListMenuData.add(
//            CommonVquMenuBean(
//                R.mipmap.ic_mine_list_menu_face,
//                getString(R.string.mine_vqu_menu_face),
//                type = 1
//            )
//        )
        mVquListMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_service,
                ResUtils.getString(R.string.mine_vqu_menu_service),
                type = 2
            )
        )
//        mVquListMenuData.add(
//            CommonVquMenuBean(
//                R.mipmap.ic_mine_list_menu_report,
//                ResUtils.getString(R.string.mine_vqu_menu_report),
//                type = 3
//            )
//        )
        mVquListMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_system,
                ResUtils.getString(R.string.mine_vqu_menu_system),
                type = 4
            )
        )
    }

    /**
     * 网格菜单数据初始化
     */
    /*
    private fun initVquGridMenuData() {

        mVquGirdMenuData.clear()

        //邀请好友
        mVquGirdMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_grid_menu_invite,
                ResUtils.getString(R.string.mine_vqu_menu_invite),
                ResUtils.getString(R.string.mine_vqu_menu_invite_desc),
                1
            )
        )

        //任务中心
        mVquGirdMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_grid_menu_task,
                ResUtils.getString(R.string.mine_vqu_menu_task),
                ResUtils.getString(R.string.mine_vqu_menu_task_desc),
                2
            )
        )

        //我的背包
        mVquGirdMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_grid_menu_backpack,
                ResUtils.getString(R.string.mine_vqu_menu_backpack),
                type = 3
            )
        )

        //我的人脉
        mVquGirdMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_grid_menu_contacts,
                ResUtils.getString(R.string.mine_vqu_menu_contacts),
                type = 4
            )
        )

        //我的认证
        mVquGirdMenuData.add(
            CommonVquMenuBean(
                R.mipmap.ic_mine_grid_menu_auth,
                ResUtils.getString(R.string.mine_vqu_menu_auth),
                type = 5
            )
        )

    }

     */

    override fun initObserve() {
        mViewModel.userInfoData.observe(this) {
            parsingUserInfo(it)
        }

        mViewModel.walletData.observe(this) {
            parsingWallet(it)
        }

        mViewModel.bannerListData.observe(this) {
            initBanner(it)
        }

        mSignDayViewModel.todayLiveData.observe(this) {
            mVquSignDayAdapter.todayCount = it.todayCount
            mVquSignDayAdapter.setNewInstance(it.today)
        }
    }


    private fun initBanner(mutableList: MutableList<CommonVquBannerBean>?) {
        if (!mutableList.isNullOrEmpty()) {
            mBinding.brMineVquAd.visibility = View.VISIBLE
//            mBinding.brMineVquAd.setIndicatorSelectedWidth(dp2px(3.5f))
//            mBinding.brMineVquAd.indicatorConfig.indicatorSize = dp2px(3.5f)
//            mBinding.brMineVquAd.indicatorConfig.normalColor =
//                ResUtils.getColor(R.color.color_26FFFFFF)
//            mBinding.brMineVquAd.indicatorConfig.selectedColor =
//                ResUtils.getColor(R.color.color_FFFFFF)

            mBinding.brMineVquAd.indicator = CircleIndicator(requireActivity())
//            mBinding.brMineVquAd.setIndicatorNormalWidth(dp2px(3.5f))
//            mBinding.brMineVquAd.setIndicatorSelectedWidth(dp2px(3.5f))
//            mBinding.brMineVquAd.setIndicatorRadius(dp2px(3.5f))
//            mBinding.brMineVquAd.setIndicatorNormalColor(ResUtils.getColor(R.color.color_26FFFFFF))
//            mBinding.brMineVquAd.setIndicatorSelectedColor(ResUtils.getColor(R.color.color_FFFFFF))

//            mBinding.brMineVquAd.indicatorConfig.setIndicatorSize(dp2px(3.5f))

            mBannerList.clear()
            mBannerList.addAll(mutableList)
            mBinding.brMineVquAd.addBannerLifecycleObserver(this)
                .setOrientation(Banner.HORIZONTAL)
                .setLoopTime(3000)
                .setAdapter(object :
                    BannerImageAdapter<CommonVquBannerBean>(
                        mBannerList
                    ) {
                    override fun onBindView(
                        holder: BannerImageHolder?,
                        data: CommonVquBannerBean?,
                        position: Int,
                        size: Int,
                    ) {
                        holder?.imageView?.vquLoadRoundImage(
                            NetBaseUrlConstant.IMAGE_URL + data?.image,
                            dp2px(12f)
                        )
                    }
                }).setOnBannerListener { data, position ->
                    JumpKtUtils.jump(
                        requireActivity(),
                        mBannerList[position].link_type,
                        mBannerList[position].link_url
                    )
                }

        } else {
            mBinding.brMineVquAd.visibility = View.GONE
        }
    }

    /**
     * 解析钱包数据，并且改变UI
     */
    private fun parsingWallet(it: TantaWalletBean?) {
        if (it == null) {
            return
        }

        mBinding.tvMineMyRechargeNum.text = it.account.coin
        mBinding.tvMineMyEarningsNum.text = it.account.incomeCoinMoney
    }

    /**
     * 解析用户信息数据，并且改变UI
     */
    private fun parsingUserInfo(it: VquUserHomeBean?) {

        if (it == null) {
            return
        }

        mVquUserHomeBean = it


        //昵称
        mBinding.tvMineVquUserName.text = it.userinfo.nickname
        //Vqu号
        mBinding.tvMineVquUserVquId.text = getString(R.string.mine_vqu_id, it.userinfo.usercode)

        //头像
        mBinding.cafvMineVquUserAvatar.loadAvatar(it.userinfo.avatar)
        mBinding.cafvMineVquUserAvatar.loadAvatarFrame(it.userinfo.avatarFrame)
        mBinding.cafvMineVquUserAvatar.showAnchorView(it.userinfo.isLive == 0)


        //关注
        mBinding.tvMineVquFocusNum.text = " " + it.usercount.followCount.toString() + " "
        //粉丝
        mBinding.tvMineVquFansNum.text = " " + it.usercount.fansCount.toString() + " "
        //访客
        mBinding.tvMineVquVisitorNum.text = " " + it.usercount.visitorCount.toString() + " "
        //足迹
        mBinding.tvMineVquTrackNum.text = " " + it.usercount.viewerCount.toString() + " "

        var pos = 1
        if (mVquListMenuData[0].type == 5) {
            if (mVquUserHomeBean?.userinfo?.gender != 1) {
                if (mVquUserHomeBean?.userinfo?.isStarScout == 1) {
                    pos = 1
                } else {
                    pos = 0
                    mVquListMenuData.removeAt(0)
                    mVquListMenuAdapter.notifyDataSetChanged()
                }
            }
        } else {
            pos = 0
        }
        //有无新任务//暂时去掉
        isTaskRewards = it.isTaskRewards
        if (mVquListMenuData.size > 0) {
            mVquListMenuData[pos] = CommonVquMenuBean(
                R.mipmap.ic_mine_list_menu_task,
                getString(R.string.mine_vqu_menu_task),
                type = 7,
                desc = "做任务，免费赚金币",
                descIcon = R.mipmap.ic_mine_list_menu_desc_diamond,
                showRedPoint = isTaskRewards
            )
            mVquListMenuAdapter.notifyItemChanged(pos)
        }


        refreshVipValue()

        //如果是主播
        /*
        if (it.userinfo.isAnchor == 1) {
//            val commonVquMenuBean = CommonVquMenuBean(
//                R.mipmap.ic_mine_list_menu_anchor,
//                ResUtils.getString(R.string.mine_vqu_menu_anchor),
//                type = 6
//            )
//
//            if (!mVquListMenuData.contains(commonVquMenuBean)) {
//                //网格菜单增加主播设置
//                mVquListMenuData.add(
//                    4,
//                    commonVquMenuBean
//                )
//
//                mVquListMenuAdapter.notifyDataSetChanged()
//            }


            //如果是主播，则将足迹替换成获赞
            mBinding.tvMineVquTrackTxt.setText(R.string.mine_vqu_praise)
            mBinding.tvMineVquTrackNum.text = it.usercount.videoTrendsLikeCount.toString()
        } else {
            //如果不是主播，则维持足迹
            mBinding.tvMineVquTrackTxt.setText(R.string.mine_vqu_track)
            mBinding.tvMineVquTrackNum.text = it.usercount.viewerCount.toString()
        }

         */

//        mVquGridMenuAdapter.setNewInstance(mVquGirdMenuData)


        //如果有新增访客，显示访客红点数量
        if (it.usercount.newVisitorCount > 0) {
            mBinding.stvMineVquVisitorNewNum.visibility = View.VISIBLE
            mBinding.stvMineVquVisitorNewNum.text = it.usercount.newVisitorCount.toString()
        }


        //是否完善信息
        if (it.isCompleteInfo == 1) {

            //获取上次提示的时间
            val time = UserSpUtils.getLong(SpKey.improveUserDataTime) ?: 0
            //获取当前时间
            val currentTimeMillis = System.currentTimeMillis()

            //时间差
            val diffTime = currentTimeMillis - time
            val day = diffTime / (24 * 60 * 60 * 1000)

            //判断是否小于3天，是则隐藏提示，否则则显示，并且在3秒后消失
            if (day < 3) {
                mBinding.ivMineVquUserImproveData.visibility = View.INVISIBLE
            } else {
                mBinding.ivMineVquUserImproveData.visibility = View.VISIBLE
                UserSpUtils.put(SpKey.improveUserDataTime, System.currentTimeMillis())
                handler.sendEmptyMessageDelayed(100, 3000)
            }
        }

        setCustomerInfo(it)
    }

    /**
     * 设置客服信息
     */
    private fun setCustomerInfo(it: VquUserHomeBean) {
        //获取版本号
        if (activity == null) return
        val pm: PackageManager = activity!!.packageManager
        var version = ""
        try {
            val packageInfo: PackageInfo = pm.getPackageInfo(activity!!.packageName, 0)
            version = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
        }
        //设置客户端头像
        var uiCustomization = UICustomization()
        uiCustomization.rightAvatar = NetBaseUrlConstant.IMAGE_URL + it.userinfo.avatar
        if (CommonApplication.ysfOptions != null) {
            CommonApplication.ysfOptions!!.uiCustomization = uiCustomization
        }
        var userInfo = YSFUserInfo()
        // App 的用户 ID
        userInfo.userId = it.userinfo.usercode
        // 当且仅当开发者在管理后台开启了 authToken 校验功能时，该字段才有效
        userInfo.authToken = "auth-token-from-user-server";
        var gender = if (it.userinfo.gender == 2) "男" else "女"
        var name = "{\"key\":\"real_name\", \"value\":\"${it.userinfo.nickname}\"}"
        var phone =
            "{\"key\":\"mobile_phone\", \"hidden\":true, \"value\":\"${it.userinfo.mobile}\"}"
        var avatar =
            "{\"key\":\"avatar\", \"value\": \"${NetBaseUrlConstant.IMAGE_URL + it.userinfo.avatar}\"}"
        var index0 =
            "{\"index\":0, \"key\":\"account\", \"label\":\"心语号\", \"value\":${it.userinfo.usercode}}"
        var index2 = "{\"index\":2,\"key\":\"desc\",\"label\":\"来源\",\"value\":\"$version\"}"
        var index1 =
            "{\"index\":1, \"key\":\"sex\", \"label\":\"性别\", \"value\": \"${gender}\"}"
        var index3 =
            "{\"index\":3, \"key\":\"avatar\", \"label\":\"头像\", \"value\":\"${NetBaseUrlConstant.IMAGE_URL + it.userinfo.avatar}\"}"
        userInfo.data = listOf(name, index0, index1, index2, index3).toString()
        Unicorn.setUserInfo(userInfo)
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //这里开始，是自定义对消息的处理，是主进程处理
            mBinding.ivMineVquUserImproveData.visibility = View.INVISIBLE
        }
    }

    override fun initRequestData() {

    }


    /**
     * 网格菜单的适配器
     * 绑定[mVquGirdMenuData]
     */
    private inner class GridMenuAdapter :
        BaseQuickAdapter<CommonVquMenuBean, BaseViewHolder>(R.layout.mine_item_mine_user_menu_list) {

        override fun convert(holder: BaseViewHolder, item: CommonVquMenuBean) {
            holder.setText(R.id.tv_mine_vqu_item_user_menu_name, item.title)
            holder.setImageResource(R.id.iv_mine_item_mine_user_menu_list_icon, item.icon)

            val stvDesc =
                holder.getView<com.mshy.VInterestSpeed.common.ui.view.ShapeTextView>(R.id.stv_mine_vqu_item_user_menu_desc)

            if (item.desc.isNotEmpty()) {
                stvDesc.visibility = View.VISIBLE
                stvDesc.text = item.desc
            } else {
                stvDesc.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 列表菜单的适配器
     * 绑定[mVquListMenuData]
     */
    private inner class ListMenuAdapter :
        BaseQuickAdapter<CommonVquMenuBean, BaseViewHolder>(R.layout.mine_item_mine_system_menu_list) {
        override fun convert(holder: BaseViewHolder, item: CommonVquMenuBean) {
            holder.setImageResource(R.id.iv_mine_item_mine_system_menu_list_icon, item.icon)
            holder.setText(R.id.tv_mine_item_mine_system_menu_list_title, item.title)
            holder.setText(R.id.tv_mine_item_mine_system_menu_list_desc, item.desc)
            holder.setGone(R.id.stv_latest, item.showRedPoint != 1)
            if (item.descIcon != 0) {
                holder.setImageResource(
                    R.id.iv_mine_item_mine_system_menu_list_desc_icon,
                    item.descIcon
                )
                holder.setGone(R.id.iv_mine_item_mine_system_menu_list_desc_icon, false)
            } else {
                holder.setGone(R.id.iv_mine_item_mine_system_menu_list_desc_icon, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSignDayEvent(successData: SignDaySuccessData) {
        mSignDayViewModel.vquTodayActivityIndex()
        mViewModel.vquWalletIndex()
        val dialog = SignDaySuccessDialog()

        dialog.setData(successData)
        dialog.show(childFragmentManager, "")
    }
}