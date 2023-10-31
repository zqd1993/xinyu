
package com.live.module.home.fragment

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.live.module.home.R
import com.live.module.home.adapter.HomeVquRecommendAdapter
import com.live.module.home.adapter.TVNumAdapter
import com.live.module.home.databinding.HomeVquMainFragmentNewBinding
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.*
import com.live.vquonline.view.main.bean.HomeVquOnTvBean
import com.mshy.VInterestSpeed.common.bean.notification.NotificationEvent
import com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.PermissionDescribe
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.CommonBackstageEvent
import com.mshy.VInterestSpeed.common.ext.click
import com.mshy.VInterestSpeed.common.helper.MagicIndicatorHelper
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.dialog.CommonRechargeDialog
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import com.mshy.VInterestSpeed.common.utils.UmUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.permissionx.guolindev.callback.RequestCallback
import com.quyunshuo.module.home.fragment.HomeVquFragmentViewModel
import com.youth.banner.Banner
import com.youth.banner.listener.OnPageChangeListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe

/**
 * author: Tany
 * date: 2022/8/8
 * description:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.VquMainFragment.VquMainFragment)
class HomeNewFragment :
    BaseLazyFrameFragment<HomeVquMainFragmentNewBinding, HomeVquFragmentViewModel>() {
    private var vquRecomendAdapter = HomeVquRecommendAdapter()
    private var vquPage: Int = 1
    private var vquType: Int = 0
    private val mPayViewModel: CommonPayViewModel by viewModels()
    private val vquTitles = arrayOf("推荐", "在线")
    private val vquFragments: ArrayList<Fragment> = ArrayList()
    override fun HomeVquMainFragmentNewBinding.initView() {
        ImmersionBar.with(this@HomeNewFragment).statusBarView(R.id.view).init()
        initSearch()
        initTopClick()
        toolbarLayout.post {//改变状态栏颜色
            appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    ImmersionBar.with(this@HomeNewFragment).statusBarDarkFont(true).init()
                }
            })
        }
        initMagicIndicator()
        vquFragments.add(HomeNewRecommendFragment())
        vquFragments.add(HomeOnLineFragment())
//        vquFragments.add(HomeVquRecommendFragment.newInstance(1))

        var vquViewPagerAdapter = CommonVquMainPageAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
        )
        vquViewPagerAdapter?.setData(vquFragments)
        vquViewPager.adapter = vquViewPagerAdapter
        if (NetBaseUrlConstant.IS_MATCH_MAKER) {
            mBinding.tvVideo.text = "即刻恋爱"
        } else {
            mBinding.tvVideo.text = "邂逅有缘人"
        }
    }

    private fun initMagicIndicator() {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return vquTitles.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val simplePagerTitleView: SimplePagerTitleView =
                    MagicIndicatorHelper.getDefaultTitleView(
                        context,
                        vquTitles[index]
                    )
                with(simplePagerTitleView) {
                    setOnClickListener { mBinding.vquViewPager!!.currentItem = index }
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return MagicIndicatorHelper.getDefaultIndicator(context)
            }
        }
        mBinding.vquMagicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mBinding.vquMagicIndicator, mBinding.vquViewPager)
    }

    private fun initTopClick() {
        mBinding.vquSllMatchVideo.click {
            if (UserManager.isVideo) {
                toast("正在语音/视频通话中，请稍后再试...")
                return@click
            }

            UmUtils.setUmEvent(activity, UmUtils.VIDEOSPEEDMATCHING)
            if (!Settings.canDrawOverlays(activity) && UserSpUtils.getUserBean()?.gender == 1) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                startActivity(intent)
            } else {
                PermissionUtils.videoPermission(this@HomeNewFragment,
                    PermissionDescribe.vquVideoPermissionTxt,
                    PermissionDescribe.vquVideoPermissionTxt,
                    object : RequestCallback {
                        override fun onResult(
                            allGranted: Boolean,
                            grantedList: MutableList<String>,
                            deniedList: MutableList<String>,
                        ) {
                            if (allGranted) {
                                if (UserManager.userInfo != null) {
                                    if (UserManager?.userInfo?.gender == 2 || UserManager?.userInfo?.isRpAuth == 1) {
                                        if (UserManager?.userInfo?.gender == 2) {
                                            ARouter.getInstance()
                                                .build(RouteUrl.Agora2.AgoraTantaMatchingActivity)
                                                .withInt("matchType", 0)
                                                .navigation()
                                        } else {
                                            mViewModel.vquAddMatching("video")
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
                                        messageDialog.show(childFragmentManager, "")
                                    }
                                }
                            } else {

                            }
                        }
                    },
                    dismissCallback = {

                        false
                    })
            }


        }

        mBinding.vquSllInvitation.click {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, UserManager?.webUrl?.share ?: "")
                .navigation()
        }

        mBinding.vquSllJobCenter.click {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, UserManager?.webUrl?.taskCenter ?: "")
                .navigation()
            UmUtils.setUmEvent(activity, UmUtils.TASKCENTER)
        }

        mBinding.vquSllMatchVoice.click {

            if (UserManager.isVideo) {
                toast("正在语音/视频通话中，请稍后再试...")
                return@click
            }

            UmUtils.setUmEvent(activity, UmUtils.VOICESPEEDMATCHING)
            if (!Settings.canDrawOverlays(activity) && UserSpUtils.getUserBean()?.gender == 1) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                startActivity(intent)
            } else {
                PermissionUtils.vquVoicePermission(this@HomeNewFragment,
                    PermissionDescribe.vquVoicePermissionTxt,
                    PermissionDescribe.vquVoicePermissionTxt,
                    object : com.permissionx.guolindev.callback.RequestCallback {
                        override fun onResult(
                            allGranted: Boolean,
                            grantedList: MutableList<String>,
                            deniedList: MutableList<String>,
                        ) {
                            if (allGranted) {
                                if (UserManager.userInfo != null) {
                                    if (UserManager?.userInfo?.gender == 2 || UserManager?.userInfo?.isRpAuth == 1) {
                                        if (UserManager?.userInfo?.gender == 2) {
                                            ARouter.getInstance()
                                                .build(RouteUrl.Agora2.AgoraTantaMatchingActivity)
                                                .withInt("matchType", 1)
                                                .navigation()
                                        } else {
                                            mViewModel.vquAddMatching("voice")
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
                                        messageDialog.show(childFragmentManager, "")
                                    }
                                }
                            } else {

                            }
                        }
                    })
            }
        }


    }

    private fun initSearch() {//初始化搜索相关
        if (UserManager.userInfo?.gender == 2) {
            mBinding.ivHomeVquMainSearch.visible()
        } else {
            mBinding.ivHomeVquMainSearch.gone()
        }
        mBinding.ivHomeVquMainSearch.click {//搜索
            ARouter.getInstance().build(RouteUrl.VquMainFragment.HomeVquSearchActivity)
                .navigation()
        }
    }


    override fun onPause() {
        super.onPause()
    }


    override fun initObserve() {
        mViewModel.vquMatch.observe(this) {
            if (it.code == 0) {
                if (it.type.equals("video")) {
                    ARouter.getInstance()
                        .build(RouteUrl.Agora2.AgoraTantaMatchingActivity)
                        .withInt("matchType", 0)
                        .navigation()
                } else {
                    ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaMatchingActivity)
                        .withInt("matchType", 1)
                        .navigation()
                }
            } else if (it.code == 1010) {
                val clickableSpan: ClickableSpan = object : ClickableSpan() {


                    override fun updateDrawState(ds: TextPaint) {
                        ds.setColor(resources.getColor(R.color.color_7C69FE)) //设置颜色
                        ds.setUnderlineText(false) //去掉下划线
                    }

                    override fun onClick(p0: View) {
                        ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                            .withString(RouteKey.URL, UserManager?.webUrl?.anchorStarlevel ?: "")
                            .navigation()
                    }
                }
                val title = "1. ${it.message}\n" +
                        "2. 别灰心，可等待平台随机指派速配单哦~\n" +
                        "3. 点击查看";
                val builder = SpannableStringBuilder()

                builder.append(title)
                builder.append("如何提高星级")
                builder.setSpan(
                    clickableSpan,
                    title.length,
                    6 + title.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                CommonHintDialog()
                    .setTitle("")
                    .setContent(builder)
                    .setSingleButton(true)
                    .setContentSize(15)
                    .setOnSingleClickListener { }.show(childFragmentManager)

            } else if (it.code == 1003 || it.code == 1002) {
                val rechargeDialog = CommonRechargeDialog()
                rechargeDialog.show(childFragmentManager, "")
            } else {
                if (it.type.equals("video")) {
                    ARouter.getInstance()
                        .build(RouteUrl.Agora2.AgoraTantaMatchingActivity)
                        .withInt("matchType", 0)
                        .navigation()
                } else {
                    ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaMatchingActivity)
                        .withInt("matchType", 1)
                        .navigation()
                }
                toast(it.message + "")
            }
        }
        mViewModel.vquOnTvBeanList.observe(this) {//首页电视墙
            vquInitTvList(it)
        }
    }

    override fun initRequestData() {
        mViewModel.vquGetOnTvlList()
    }

    override val mViewModel: HomeVquFragmentViewModel by viewModels<HomeVquFragmentViewModel>()
    var tvListHomeVqu = ArrayList<HomeVquOnTvBean>()
    private fun vquInitTvList(vquTvListHomeVqu: ArrayList<HomeVquOnTvBean>) {
        if (vquTvListHomeVqu != null && vquTvListHomeVqu.size > 0) {
            //添加tv
            tvListHomeVqu.addAll(vquTvListHomeVqu)
            mBinding.includedHeadAd.vquYouthBanner.apply {
                addBannerLifecycleObserver(this@HomeNewFragment)
                setOrientation(Banner.VERTICAL)
                setUserInputEnabled(false)
                // .setIndicator(null, false)
                setLoopTime(3000)
                setScrollTime(400)
                setAdapter(TVNumAdapter(tvListHomeVqu))
                addOnPageChangeListener(object : OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int,
                    ) {

                    }

                    override fun onPageSelected(position: Int) {
                        if ((mBinding.includedHeadAd.vquYouthBanner.adapter.getData(position) as HomeVquOnTvBean).lock_time > 0) {

                            setLoopTime(
                                ((mBinding.includedHeadAd.vquYouthBanner.adapter.getData(
                                    position
                                ) as HomeVquOnTvBean).lock_time * 1000).toLong()
                            )
                            mBinding.includedHeadAd.vquYouthBanner.adapter.notifyItemChanged(
                                position
                            )
                            // (mBinding.includedHeadAd.vquYouthBanner.adapter.getData(position) as HomeVquOnTvBean).lock_time = 0
                        } else {
                            setLoopTime((3 * 1000).toLong())
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })
            }
        }
    }


    @Subscribe
    fun vquOnTVNotification(vquEvent: NotificationEvent) {
        val videoRequestBean: VideoRequestBean = vquEvent.getCustomNotification()
        if (null != videoRequestBean) {
            if (16 == videoRequestBean.id) {
                if (mBinding.includedHeadAd.vquYouthBanner != null) {
                    var vquTvBean = HomeVquOnTvBean(
                        0,
                        videoRequestBean.data.from_avatar ?: "",
                        videoRequestBean.data.from_nickname ?: "",
                        0,
                        0,
                        0,
                        if (videoRequestBean.data.gift_count == null) 0 else videoRequestBean.data.gift_count.toInt(),
                        0,
                        videoRequestBean.data.gift_img ?: "",
                        videoRequestBean.data.gift_name ?: "",
                        videoRequestBean.data.id,
                        videoRequestBean.data.lock_time,
                        videoRequestBean.data.lock_time,
                        videoRequestBean.data.to_avatar ?: "",
                        videoRequestBean.data.to_nickname ?: "",
                        0,
                        videoRequestBean.data.toid
                    )
                    if (mBinding.includedHeadAd.vquYouthBanner!!.currentItem < tvListHomeVqu.size) {
                        tvListHomeVqu.set(
                            mBinding.includedHeadAd.vquYouthBanner.currentItem,
                            vquTvBean
                        )
                    } else {
                        tvListHomeVqu.add(vquTvBean)
                    }
                }
            }
        }
    }


    var vquEvent: CommonBackstageEvent? = null

    @Subscribe
    fun vquMatchStatus(vquEvent: CommonBackstageEvent) {
        this.vquEvent = vquEvent
        if (vquEvent.isMatchCancle == 0) {
            if (vquEvent.type == 1) {
                mBinding.vquTvMatchAudio.text = "语音速配"
            } else {
                mBinding.vquTvMatchVideo.text = "视频速配"
            }
        } else {
            if (vquEvent.type == 1) {
                mBinding.vquTvMatchAudio.text = "速配中..."
            } else {
                mBinding.vquTvMatchVideo.text = "速配中..."
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.vquGetUserInfo()
    }
}