package com.mshy.VInterestSpeed.common.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.vquonline.base.bean.AgoraFloatViewEvent
import com.live.vquonline.base.bean.AgoraServiceEvent
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.AppManager
import com.live.vquonline.base.utils.DeviceManager
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.BuildConfig
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.LoginEvent
import com.mshy.VInterestSpeed.common.bean.StartCameraEvent
import com.mshy.VInterestSpeed.common.bean.UserInCallEvent
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.databinding.CommonVquActivityMainBinding
import com.mshy.VInterestSpeed.common.event.*
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.helper.startARouterActivity
import com.mshy.VInterestSpeed.common.service.VquForegroundService
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.adapter.CommonHomeConversationAdapter
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.*
import com.mshy.VInterestSpeed.common.ui.view.MyBaseTabItem
import com.mshy.VInterestSpeed.common.ui.view.floating.FloatWindow
import com.mshy.VInterestSpeed.common.ui.vm.CommonVquMainViewModel
import com.mshy.VInterestSpeed.common.utils.*
import com.mshy.VInterestSpeed.uikit.api.NimUIKit
import com.mshy.VInterestSpeed.uikit.common.util.sys.ScreenUtil
import com.mshy.VInterestSpeed.uikit.event.MessageVquCurrentItemEvent
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.qiyukf.unicorn.api.Unicorn
import com.youth.banner.Banner
import dagger.hilt.android.AndroidEntryPoint
import me.majiajie.pagerbottomtabstrip.NavigationController
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 *
 * @Description: 项目主页
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:11
 *
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Main.CommonVquMainAcitvity)
class CommonVquMainActivity : BaseActivity<CommonVquActivityMainBinding, CommonVquMainViewModel>() {

    override val mViewModel: CommonVquMainViewModel by viewModels()
    var teenModeCloseDialog: CommonVquTeenModeCloseDialog? = null
    var teenModeDialog: CommonVquTeenModeDialog? = null
    private lateinit var vqu_fragments: ArrayList<Fragment>
    private var vqu_adapter: CommonVquMainPageAdapter? = null
    private var vqu_NavigationController: NavigationController? = null
    var type: Int = 0
    var ViewClickTime: Long = 0L
    var HomeClickTime: Long = 0L
    var lastRefreshTime: Long = 0L
    private var isImFail: Boolean = false
    private var isHome: Boolean = true
    private var isTopShow: Boolean = false
    private var unreadCount: Int = 0
    private var isPerfectShow: Boolean = false

    override fun setStatusBar() {
        //super.setStatusBar()
        ImmersionBar.with(this@CommonVquMainActivity).reset()
            .transparentStatusBar()
            .statusBarDarkFont(true, 0.5f)
            .fitsSystemWindows(false)
            .init()
    }

    override fun CommonVquActivityMainBinding.initView() {
        Unicorn.initSdk();
        if (!BuildConfig.VERSION_TYPE.equals("VERSION_STATUS_RELEASE")) {
//            toast("测试包${NetBaseUrlConstant.BASE_URL}")
        }
//        ImmersionBar.with(this@CommonVquMainActivity).statusBarColor(R.color.color_FFFFFF)
//            .fitsSystemWindows(true).init()
        isImFail = intent.getBooleanExtra(RouteKey.IM_LOGIN_FAIL, false)
        mBinding.ivTop.setOnClickListener {
            EventBus.getDefault().post("homeDoubleClick")
        }
        EventBus.getDefault().post(LoginEvent())
        mViewModel.getOnlineUpdate()
        if (UserManager.userInfo == null) {
            UserManager.userInfo = UserSpUtils.getUserBean()
            DeviceManager.getInstance().token = UserManager.userInfo?.token
        }
        AppUpdateUtil.checkUpdate(true, this@CommonVquMainActivity)
        vqu_NavigationController = commonVquBottomTab.custom()
            .addItem(
                vquNewItem(
                    R.mipmap.resources_tanta_fate_un_selelct,
                    R.mipmap.resources_tanta_fate_selelct,
                    getString(R.string.common_vqu_main_fate)
                )
            )
            .addItem(
                vquNewItem(
                    R.mipmap.resources_tanta_dynamic_un_selelct,
                    R.mipmap.resources_tanta_dynamic_selelct,
                    getString(R.string.common_vqu_main_dynamic)
                )
            )
            .addItem(
                vquNewItem(
                    R.mipmap.resources_tanta_msg_un_selelct,
                    R.mipmap.resources_tanta_msg_selelct,
                    getString(R.string.common_vqu_main_msg)
                )
            )
            .addItem(
                vquNewItem(
                    R.mipmap.resources_tanta_my_un_selelct,
                    R.mipmap.resources_tanta_my_selelct,
                    getString(R.string.common_vqu_main_my)
                )
            )
            .enableAnimateLayoutChanges()
            .build()
        vqu_adapter = CommonVquMainPageAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
        )
        mBinding.commonVquMainViewPager.offscreenPageLimit = 4
        mBinding.commonVquMainViewPager.adapter = vqu_adapter
        vqu_NavigationController?.setupWithViewPager(mBinding.commonVquMainViewPager)
        vqu_fragments = ArrayList<Fragment>()
        //通过ARouter 获取其他组件提供的fragment
        vqu_fragments.add(
            ARouter.getInstance().build(RouteUrl.VquMainFragment.VquMainFragment)
                .navigation() as Fragment
        )
        vqu_fragments.add(
            ARouter.getInstance().build(RouteUrl.Dynamic.DynamicVquDynamicFragment)
                .navigation() as Fragment
        )
        vqu_fragments.add(
            ARouter.getInstance().build(RouteUrl.Message.MessageVquMainFragment)
                .withBoolean(RouteKey.IM_LOGIN_FAIL, isImFail)
                .navigation() as Fragment
        )
        vqu_fragments.add(
            ARouter.getInstance().build(RouteUrl.Mine.MineVquFragment).navigation() as Fragment
        )
        vqu_adapter!!.setData(vqu_fragments)

        mBinding.commonVquMainViewPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        isHome = true
                        HomeClickTime = System.currentTimeMillis()
                        if (unreadCount > 0) {
                            mBinding.bannerConversation.visible()
                        }
                        if (isPerfectShow) {
                            mBinding.vquLlBottom.visible()
                        } else {
                            mBinding.vquLlBottom.gone()
                        }
                        if (isTopShow) {
                            mBinding.ivTop.visible()
                        } else {
                            mBinding.ivTop.gone()
                        }
                        UmUtils.setUmEvent(this@CommonVquMainActivity, UmUtils.FRONTPAGE)
                    }

                    1 -> {
                        isHome = false
                        mBinding.bannerConversation.gone()
                        ViewClickTime = System.currentTimeMillis()
                        UmUtils.setUmEvent(this@CommonVquMainActivity, UmUtils.ENTERDYNAMICPAGE)
                        mBinding.vquLlBottom.gone()
                        mBinding.ivTop.gone()
                    }

                    2 -> {
                        isHome = false
                        mBinding.bannerConversation.gone()
                        EventBus.getDefault()
                            .post(com.mshy.VInterestSpeed.common.event.IsShowGuideEvent())
                        EventBus.getDefault().post("CallRecordRefresh")
                        EventBus.getDefault().post("MsgSelect")
                        UmUtils.setUmEvent(this@CommonVquMainActivity, UmUtils.ENTERMESSAGELIST)
                        mBinding.vquLlBottom.gone()
                        mBinding.ivTop.gone()
                    }

                    3 -> {
                        isHome = false
                        mBinding.bannerConversation.gone()
                        UmUtils.setUmEvent(this@CommonVquMainActivity, UmUtils.ENTERMYPAGE)
                        mBinding.vquLlBottom.gone()
                        mBinding.ivTop.gone()
                    }

                    else -> {
//                        ImmersionBar.with(this@CommonVquMainActivity)
//                            .statusBarColor(R.color.color_FFFFFF).fitsSystemWindows(true).init()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        val unReadNum = NIMClient.getService(MsgService::class.java).totalUnreadCount
        val newVisitorCount = SpUtils.getInt(SpKey.NEW_VISITOR_COUNT, 0)
        if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
            vqu_NavigationController?.setMessageNumber(2, 0)
        } else {
            vqu_NavigationController?.setMessageNumber(2, unReadNum + newVisitorCount!!)
        }
        mBinding.guideLayout.llGuideMsgItem.setViewClickListener {
            startARouterActivity(RouteUrl.Message.MessageVquGuideActivity)
            mBinding.guideLayout.llGuide.gone()
        }
        mViewModel.isAuth()
        //用户正在通话中，初始化false
        SpUtils.putBean(
            SpKey.user_in_call,
            UserInCallEvent(false, false)
        )

        vquIvDelete.setViewClickListener {
            if (mBinding.vquLlBottom != null) {
                mBinding.vquLlBottom.visibility = View.GONE
                isPerfectShow = false
                mViewModel.vquCloseInfoFinishTip()
            }
        }
        vquBtnEdit.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
        }

        Handler().postDelayed({ mViewModel.initSm() }, 20000)
    }

    //监听是否回到顶部按钮
    @Subscribe
    fun onEventMainThread(event: ShowToTopEvent) {
        isTopShow = event.show
        if (event.show) {
            if (isHome) {
                mBinding.ivTop.visible()
            }
        } else {
            mBinding.ivTop.gone()
        }
    }

    private fun startOnLineTask(onlineTime: Long?) {
        if (onlineTime == null) {
            return
        }
        task = object : TimerTask() {
            override fun run() {
                val message = Message()
                message.what = 1
                handler.sendMessage(message)
            }
        }
        timer?.schedule(task, 100, 1000 * onlineTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel();
    }

    override fun onResume() {
        super.onResume()
        //setStatusBar()
        EventBus.getDefault().post(IsShowGuideEvent())
        mViewModel.getSystemIndex()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startService(
//                Intent(
//                    this@CommonVquMainActivity,
//                    UpdateOnlineService::class.java
//                )
//            )
//        } else {
//            startService(Intent(this@CommonVquMainActivity, UpdateOnlineService::class.java))
//        }

    }

    private val timer: Timer? = Timer()
    private var task: TimerTask? = null
    var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            mViewModel.getOnlineUpdateNotResponse()
            super.handleMessage(msg)
        }
    }

    //创建一个Item
    private fun vquNewItem(drawable: Int, checkedDrawable: Int, text: String): MyBaseTabItem? {
        val normalItemView = MyBaseTabItem(this)
        normalItemView.initialize(drawable, checkedDrawable, text)
        normalItemView.setTextDefaultColor(ContextCompat.getColor(this, R.color.color_BBBBBB))
        normalItemView.setTextCheckedColor(ContextCompat.getColor(this, R.color.color_B4A3FD))
        normalItemView.onRepeat()
        return normalItemView
    }

    override fun initObserve() {
        mViewModel.isAuthData.observe(this) {
            if (it.mobileStatus != 1) {
                val dialog = CommonTantaBindPhoneDialog()
                dialog.show(supportFragmentManager, "")
            }
        }
        mViewModel.vquOnlineUpdateData.observe(this, Observer {
            if (it.code == 0) {
                if (it.data != null) {
                    if (it.data.user_online_open == 1) {
                        startOnLineTask(it.data.user_online_time)
                    }
                }
            }
        })
        mViewModel.vquGetTeenModeData.observe(this, Observer {
            if (it.data.isOpen == 1) {
                if (teenModeCloseDialog == null) {
                    teenModeCloseDialog = CommonVquTeenModeCloseDialog()
                    teenModeCloseDialog!!.show(supportFragmentManager, "")
                } else {
                    teenModeCloseDialog!!.show(supportFragmentManager, "")
                }
            } else {
                if (type == 0) {
                    val lastTime = SpUtils.getBean(
                        SpKey.KID_MODEL,
                        Long::class.java
                    )
                    val currentTime = System.currentTimeMillis()

                    if (null == lastTime || currentTime - lastTime > 7 * 24 * 60 * 60 * 1000) {
                        if (teenModeDialog == null) {
                            teenModeDialog = CommonVquTeenModeDialog()
                            teenModeDialog!!.show(supportFragmentManager, "")
                        } else {
                            teenModeDialog!!.show(supportFragmentManager, "")
                        }
                        SpUtils.putBean(SpKey.KID_MODEL, currentTime)
                    }

                } else {
                    teenModeCloseDialog?.dismiss()
                }
            }
        })
//        mViewModel.videoVquCallBean.observe(this) {
//            EventBus.getDefault().post( CallNotification(false,it,it?.socket_url ?: ""))
//        }

        appViewModel.unreadConversationList.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                unreadCount = it.size
                if (isHome) {
                    mBinding.bannerConversation.visible()
                } else {
                    mBinding.bannerConversation.gone()
                }
                var currentTime = System.currentTimeMillis()
                var time = currentTime - lastRefreshTime
                if (time > 3000) {//如果时间大于3秒再更新
                    initConversationBannerView(it)
                }
            } else {
                unreadCount = 0
                mBinding.bannerConversation.gone()
            }
        })

    }

    private fun initConversationBannerView(it: MutableList<RecentContact>) {
        lastRefreshTime = System.currentTimeMillis()
        mBinding.bannerConversation.apply {
            addBannerLifecycleObserver(this@CommonVquMainActivity)
            setOrientation(Banner.VERTICAL)
            setUserInputEnabled(false)
            // .setIndicator(null, false)
            setLoopTime(3000)
            setScrollTime(400)
            setAdapter(CommonHomeConversationAdapter(it))
            setOnBannerListener { data, position ->
                NimUIKit.startP2PSession(
                    this@CommonVquMainActivity,
                    it[position].contactId
                )


            }
        }

    }

    override fun initRequestData() {
        mViewModel.vquGetGlobalConfig()
//        mViewModel.vquGetBadgeConfig()
        mViewModel.vquGetTeenMode()
        mViewModel.vquQueryCities()
//        mViewModel.vquQueryIntimateList()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        var userInfo = UserManager.userInfo
        if (userInfo == null) {
            toHome()
        }
        //如果是返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            //重写返回键
            var unreadConversationList = appViewModel.unreadConversationList.value
            if (unreadConversationList.isNullOrEmpty()) {
                CommonHintDialog()
                    .setContentSize(15)
                    .setContentGravity(Gravity.CENTER)
                    .setContent(
                        if (userInfo?.gender == 1) {
                            "小姐姐要离开了吗，再逛逛~"
                        } else {
                            "小哥哥要离开了吗，再逛逛~"
                        }
                    )
                    .setLeftText("忍痛离开")
                    .setRightText("我再想想")
                    .setOnClickListener(object : CommonHintDialog.OnClickListener {
                        override fun onLeft(dialogFragment: DialogFragment) {
                            dialogFragment.dismissAllowingStateLoss()
                            Handler().postDelayed({
                                toHome()
                            }, 100)
                        }

                        override fun onRight(dialogFragment: DialogFragment) {
                            dialogFragment.dismissAllowingStateLoss()
                        }
                    })
                    .show(supportFragmentManager)
            } else {
                var recentContact = unreadConversationList[0]
                var myDialog = CommonVquDetainmentDialog()
                myDialog.setData(recentContact)
                myDialog.setOnClickListener(object : CommonVquDetainmentDialog.OnClickListener {
                    override fun onLeft(dialogFragment: DialogFragment?) {
                        dialogFragment?.dismissAllowingStateLoss()
                        Handler().postDelayed({
                            toHome()
                        }, 100)
                    }

                    override fun onRight(dialogFragment: DialogFragment?) {
                        dialogFragment?.dismissAllowingStateLoss()
                        NimUIKit.startP2PSession(
                            this@CommonVquMainActivity,
                            recentContact.contactId
                        )
                    }

                })
                myDialog.show(supportFragmentManager, "Detainment")
            }

        }
        return false
    }

    fun toHome(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val isIntimateUp = intent!!.getBooleanExtra(RouteKey.IS_INTIMATE_UP, false)
        val pos = intent.getIntExtra(RouteKey.POS, -1)
        if (isIntimateUp) {
            vqu_NavigationController?.setSelect(2)
            EventBus.getDefault().post(MessageVquCurrentItemEvent(1))
        }
        if (pos != -1) {
            vqu_NavigationController?.setSelect(pos)
        }
    }

    //监听青少年模式
    @Subscribe
    fun onEventMainThread(event: KidEvent?) {
        type = event!!.type
        mViewModel.vquGetTeenMode()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: String?) {
        if ("mainFinish" == event) {
            finish()
        } else if ("onRepeat" == event) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - ViewClickTime < 200) {//双击
                EventBus.getDefault().post("doubleClick")
            }
            ViewClickTime = System.currentTimeMillis()
        } else if ("onHomeRepeat" == event) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - HomeClickTime < 200) {//双击
                EventBus.getDefault().post("homeDoubleClick")
            }

            HomeClickTime = System.currentTimeMillis()
        } else if ("logout" == event) {
            NimUIKit.logout()
            NIMClient.getService(AuthService::class.java).logout()
            UserSpUtils.clear()
            EventBus.getDefault().post("mainFinish")
            IntimateUtils.getInstance().clearData()
            ARouter.getInstance().build(RouteUrl.Login.LoginTantaLoginActivity)
                .navigation()
        } else if ("goFront" == event) {
            if (UserManager.videoRequestBean != null) {
                CallUtil.call(UserManager.videoRequestBean!!, false)
            } else {
                if (UserManager.isVideo) {
                    if (AgoraUtils.contactType == AgoraUtils.CONTACT_TYPE_VIDEO) {
                        EventBus.getDefault().post(StartCameraEvent())
                    }
                }
            }
        } else if ("agoraDestroy" == event) {
            Log.d("AudioCallManager", "onEventMainThread : agoraDestroy")
//            if (!AppManager.isMiniWindow) {
//                UserManager.isVideo = false
//            }
        }
    }


    @Subscribe
    fun onEventMainThread(event: GuideEvent) {
        if (event.type == GuideEvent.TYPE_MESSAGE) {
            showMsgGuide()
            if (event.params != null) {
                val y = event.params["y"]
                if (y != null) {
                    mBinding.guideLayout.llGuideMsgItem.visible()
                    val iy = y as Int
                    Log.e("TAG", "onEventMainThread:$iy")
                    val statusBarHeight: Int = ScreenUtil.getStatusBarHeight(this)
                    mBinding.guideLayout.llGuideMsgItem.translationY =
                        (iy - statusBarHeight + dp2px(82f)).toFloat()
                }
            }
        }
    }

    private fun showMsgGuide() {
        mBinding.guideLayout.llGuide.visible()
        val statusBarHeight: Int = ScreenUtil.getStatusBarHeight(this)
        val layoutParams: ViewGroup.LayoutParams = mBinding.guideLayout.vStatusBar.layoutParams
        layoutParams.height = statusBarHeight
        mBinding.guideLayout.vStatusBar.layoutParams = layoutParams
    }


    @Subscribe
    fun onEventMainThread(event: UnReadCountEvent) {
        if (SpUtils.getInt(SpKey.openGreen, 0) == 1) {
            vqu_NavigationController?.setMessageNumber(2, 0)
        } else {
            val newVisitorCount = SpUtils.getInt(SpKey.NEW_VISITOR_COUNT, 0)
            vqu_NavigationController?.setMessageNumber(2, event.count + newVisitorCount!!)
        }

    }

    private var notStopService = false  //用于判断是否不能停止服务

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFloatingEvent(event: AgoraServiceEvent) {
        /**
         * 如果是视频中，并且没有活跃的Activity，则会启动服务
         */
        if (event.isShow && UserManager.isVideo) {
            notStopService = true

            /**
             * 如果服务不是运行中，则运行服务
             */
            if (!UserManager.isStartForegroundService) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //适配8.0机制
                    startForegroundService(
                        Intent(this, VquForegroundService::class.java)
                    )
                } else {
                    startService(Intent(this, VquForegroundService::class.java))
                }
            }

        } else {
            notStopService = false
            if (UserManager.isStartForegroundService) {
                mBinding.commonVquMainViewPager.postDelayed(
                    {
                        if (!notStopService && UserManager.isStartForegroundService) {
                            stopService(Intent(this, VquForegroundService::class.java))
                        }
                    }, 7 * 1000
                )
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFloatViewEvent(event: AgoraFloatViewEvent) {
        if (UserManager.isVideo && AppManager.isMiniWindow) {
            FloatWindow.get().show()
        }
    }

    @Subscribe
    fun vquShowMainThread(vquEvent: HomeVquShowBottomEvent) {
        if (vquEvent.isShow) {
            isPerfectShow = true
            if (mBinding.commonVquMainViewPager.currentItem == 0) {
                mBinding.vquLlBottom.visibility = View.VISIBLE
            }
        } else {
            mBinding.vquLlBottom.visibility = View.GONE
            isPerfectShow = false
        }
    }
//    override fun onPause() {
//        super.onPause()
//        startForegroundService(Intent(this, VquForegroundService::class.java))
//    }
}
