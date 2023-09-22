package com.live.module.message.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.message.R
import com.live.module.message.databinding.MessageTantaMainFragmentBinding
import com.live.module.message.vm.MessageVquMainFragmentViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.SpUtils
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.constant.SpKey
import com.mshy.VInterestSpeed.common.ext.logI
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquBewareDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonVquNoticeDialog
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.uikit.util.UIKitUtils
import com.michael.easydialog.EasyDialog
import com.mshy.VInterestSpeed.common.event.IsShowGuideEvent
import com.mshy.VInterestSpeed.common.helper.MagicIndicatorHelper
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.BottomSelectiveDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.mshy.VInterestSpeed.common.utils.NotificationsUpUtils
import com.mshy.VInterestSpeed.common.utils.NotificationsUtils
import com.mshy.VInterestSpeed.uikit.event.DeleteRecentContactEvent
import com.mshy.VInterestSpeed.uikit.event.MessageVquCurrentItemEvent
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.msg.MsgService
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * FileName: com.live.module.message.ui.fragment
 * Date: 2022/4/14 11:26
 * Description:
 * History:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Message.MessageVquMainFragment)
class MessageVquMainFragment :
    BaseLazyFrameFragment<MessageTantaMainFragmentBinding, MessageVquMainFragmentViewModel>() {
    private lateinit var vquAdapter: CommonVquMainPageAdapter
    private val vquTitles = arrayListOf<String>()
    private var easyDialog: EasyDialog? = null
    private var isOverlays: Boolean = false
    private var mSelectiveDialog: BottomSelectiveDialog? = null

    var vquFragments: MutableList<Fragment> = mutableListOf()
    private var isImFail: Boolean = false
    override val mViewModel: MessageVquMainFragmentViewModel by viewModels()

    override fun MessageTantaMainFragmentBinding.initView() {
        ImmersionBar.with(this@MessageVquMainFragment).titleBar(vquLlPaddingTop).init()
        vquInitMagicIndicator()
        addOnClickListener()
        isImFail = arguments?.getBoolean(RouteKey.IM_LOGIN_FAIL, false) == true
        vquFragments.add(MessageVquConversationFragment())
        vquFragments.add(MessageVquRecentIntimateFragment.newInstance())
        vquFragments.add(MessageVquCallRecordFragment.newInstance())
        vquAdapter =
            CommonVquMainPageAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )
        vquAdapter.setData(vquFragments)
        mBinding.vquViewPager.offscreenPageLimit = 3
        mBinding.vquViewPager.adapter = vquAdapter
        mBinding.vquViewPager.currentItem = 0
        mBinding.clNotice.setOnClickListener {
            if (isOverlays) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                startActivity(intent)
            } else {
                val localIntent = Intent()
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (Build.VERSION.SDK_INT >= 9) {
                    localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    localIntent.data = Uri.fromParts(
                        "package",
                        activity?.packageName,
                        null
                    )
                } else if (Build.VERSION.SDK_INT <= 8) {
                    localIntent.action = Intent.ACTION_VIEW
                    localIntent.setClassName(
                        "com.android.settings",
                        "com.android.settings.InstalledAppDetails"
                    )
                    localIntent.putExtra(
                        "com.android.settings.ApplicationPkgName",
                        activity?.packageName
                    )
                }
                startActivity(localIntent)
            }
        }
        mBinding.ivClose.setOnClickListener { mBinding.clNotice.gone() }
//        checkImLogin()
    }

    private fun checkImLogin() {
        if (isImFail) {
            var userinfo = UserManager.userInfo ?: return
            UIKitUtils.loginNIM(userinfo.userId, userinfo.imToken) {
                object : RequestCallback<LoginInfo> {
                    override fun onSuccess(param: LoginInfo?) {
                        Log.d("LoginNIM", "onSuccess: " + param?.account)
                        if (param != null) {
                            UserSpUtils.put(SpKey.isLogin, true)
                            UserSpUtils.putString(SpKey.im_account, param.account)
                            UserSpUtils.putString(SpKey.im_token, param.token)
                            UserManager.userInfo = userinfo
                            UserSpUtils.putBean(SpKey.userInfo, userinfo)
                        }
                        EventBus.getDefault().post("ImLoginSuccess")
                    }

                    override fun onFailed(code: Int) {
                        ("登录失败$code").logI()
                        checkImLogin()
                    }

                    override fun onException(exception: Throwable?) {
                        ("登录失败${exception.toString()}").logI()
                        checkImLogin()
                    }
                }
            }
        }

    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    private fun notificationRun() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!NotificationsUpUtils.isEnableV26(activity)) {//显示通知弹框
                var noticeDialog = CommonVquNoticeDialog()
                noticeDialog.show(childFragmentManager, "notice")
            } else {//不显示通知弹框
            }
        } else {
            if (!NotificationsUtils.isNotificationEnabled(activity)) {//显示通知弹框
                var noticeDialog = CommonVquNoticeDialog()
                noticeDialog.show(childFragmentManager, "notice")
            } else {//不显示通知弹框
            }
        }
    }

    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!NotificationsUpUtils.isEnableV26(activity)) {//显示通知开启横条
                isOverlays = false
                mBinding.clNotice.visible()
            } else {//不显示通知开启横条
                if (Settings.canDrawOverlays(activity)) {
                    mBinding.clNotice.gone()
                    isOverlays = false
                } else {//没开悬浮窗权限
                    isOverlays = true
                    mBinding.clNotice.visible()
                    mBinding.tvNotice.text = "开启悬浮窗权限，随时回复消息"
                }

            }
        } else {
            if (!NotificationsUtils.isNotificationEnabled(activity)) {//显示通知开启横条
                mBinding.clNotice.visible()
                isOverlays = false
            } else {//不显示通知开启横条
                if (Settings.canDrawOverlays(activity)) {
                    isOverlays = false
                    mBinding.clNotice.gone()
                } else {//没开悬浮窗权限
                    isOverlays = true
                    mBinding.clNotice.visible()
                    mBinding.tvNotice.text = "开启悬浮窗权限，随时回复消息"
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val vquUserInfo = UserSpUtils.getUserBean()
        if (vquUserInfo?.gender == 1 && vquUserInfo.isRpAuth != 1) {
            mBinding.vquIvPrivilege.gone()
        } else {
            mBinding.vquIvPrivilege.gone()
        }
        showNotification()
    }

    private fun vquInitMoreDialog() {
        val popView = layoutInflater.inflate(R.layout.message_tanta_main_more_pop, null)
        val tvRead: TextView = popView.findViewById(R.id.tv_read)
        val tvClear: TextView = popView.findViewById(R.id.tv_clear)
        tvRead.setViewClickListener {
            easyDialog?.dismiss()
            readAllMsg()
        }
        tvClear.setViewClickListener {
            easyDialog?.dismiss()
            deleteAllMsg()
        }
        easyDialog = EasyDialog(activity)
            .setLayout(popView)
            .setGravity(EasyDialog.GRAVITY_BOTTOM)
            .setBackgroundColor(Color.parseColor("#EDF7FF"))
            .setLocationByAttachedView(mBinding.vquIvMore)
            .setAnimationTranslationShow(EasyDialog.DIRECTION_X, 350, 400f, 0f)
            .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, 350, 0f, 400f)
            .setTouchOutsideDismiss(true)
            .setMatchParent(false)
            .setOutsideColor(resources.getColor(R.color.transparent))
    }

    private fun initMoreDialog() {
        mSelectiveDialog =
            BottomSelectiveDialog(
                activity!!,
                R.style.SelectiveDialog
            )
        mSelectiveDialog?.addSelectButton(
            "一键已读"
        ) { _, _ ->
            readAllMsg()
        }
        mSelectiveDialog?.addSelectButton(
            "清除消息"
        ) { _, _ ->
            deleteAllMsg()
        }
        mSelectiveDialog?.show()
    }

    /**
     * 初始化MagicIndicator
     */
    private fun vquInitMagicIndicator() {
        vquTitles.add(getString(R.string.message_vqu_main_fragment_msg))
        vquTitles.add(getString(R.string.message_vqu_main_fragment_intimate))
        vquTitles.add(getString(R.string.message_vqu_main_fragment_call_record))
        val commonNavigator =
            CommonNavigator(
                activity
            )
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return vquTitles.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    MagicIndicatorHelper.getDefaultTitleView(context, vquTitles[index])
                simplePagerTitleView.setOnClickListener {
                    mBinding.vquViewPager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return MagicIndicatorHelper.getDefaultIndicator(context)
            }
        }
        mBinding.vquMagicIndicator.navigator = commonNavigator
        mBinding.vquViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    EventBus.getDefault().post("CallRecordRefresh")
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        ViewPagerHelper.bind(mBinding.vquMagicIndicator, mBinding.vquViewPager)
    }

    /**
     * 添加监听
     */
    private fun addOnClickListener() {
        mBinding.vquIvMore.setViewClickListener {
            if (easyDialog == null) {
//                vquInitMoreDialog()
                initMoreDialog()
            }
            easyDialog?.show()
        }
        mBinding.vquIvFriend.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Relation.RelationVquFriendActivity).navigation()
        }
        mBinding.vquIvPrivilege.setViewClickListener {
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
                    ARouter.getInstance().build(RouteUrl.Auth.AuthVquCenterActivity).navigation()
//                    if (UserManager.userInfo?.gender == 1) {
//                        //女认证
//                        ARouter.getInstance().build(RouteUrl.Auth.AuthVquFaceIdentifyActivity)
//                            .navigation()
//                    } else {
//                        //男认证
//                        ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity)
//                            .navigation()
//                    }
                    return false
                }
            })
            messageDialog.show(childFragmentManager, "")
        }
    }

    /**
     * 一键已读
     */
    private fun readAllMsg() {
        CommonHintDialog()
            .setTitle(getString(R.string.message_vqu_read_all))
            .setContent(getString(R.string.message_vqu_read_all_hint))
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setClickCancelable(false)
            .setBackCanceledOnTouchOutside(false)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?) {}
                override fun onRight(dialogFragment: DialogFragment?) {
                    NIMClient.getService(MsgService::class.java).clearAllUnreadCount()
                }
            }).show(childFragmentManager)
    }

    /**
     * 删除所有消息
     */
    private fun deleteAllMsg() {
        CommonHintDialog()
            .setTitle(getString(R.string.message_vqu_clear_all_title))
            .setContent(getString(R.string.message_vqu_clear_all_hint))
            .setLeftText(getString(R.string.message_vqu_clear_all_left_btn))
            .setRightText(getString(R.string.message_vqu_clear_all_right_btn))
            .setContentSize(15)
            .setContentGravity(Gravity.CENTER)
            .setClickCancelable(false)
            .setBackCanceledOnTouchOutside(false)
            .setOnClickListener(object : CommonHintDialog.OnClickListener {
                override fun onLeft(dialogFragment: DialogFragment?) {}
                override fun onRight(dialogFragment: DialogFragment?) {
                    EventBus.getDefault().post(DeleteRecentContactEvent())
//                    NIMClient.getService(MsgService::class.java).clearMsgDatabase(true)
                }
            }).show(childFragmentManager)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MessageVquCurrentItemEvent) {
        val pos = event.currentItemPos
        mBinding.vquViewPager.currentItem = pos

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: IsShowGuideEvent) {
        showNotification()
        if (SpUtils.getBoolean(SpKey.KEY_NOTICE, false) != true) {
            notificationRun()
        }
        if (SpUtils.getBoolean(SpKey.KEY_BEWARE, false) != true) {
            var bewareDialog = CommonVquBewareDialog()
            bewareDialog.show(childFragmentManager, "bewareDialog")
            SpUtils.putBoolean(SpKey.KEY_BEWARE, true)
        }
    }

}