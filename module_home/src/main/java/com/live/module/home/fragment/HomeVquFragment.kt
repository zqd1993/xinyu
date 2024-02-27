package com.live.module.home.fragment


import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.github.penfeizhou.animation.apng.APNGDrawable
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import com.live.module.home.R
import com.live.module.home.databinding.HomeTantaMainFragmentBinding
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.SpUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.*
import com.mshy.VInterestSpeed.common.ext.click
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadRoundImage
import com.mshy.VInterestSpeed.common.ui.BaseLazyFrameFragment
import com.mshy.VInterestSpeed.common.ui.dialog.CommonRechargeDialog
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.*
import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean
import com.mshy.VInterestSpeed.common.bean.notification.NotificationEvent
import com.mshy.VInterestSpeed.common.bean.video.VideoRequestBean
import com.mshy.VInterestSpeed.common.event.CommonBackstageEvent
import com.mshy.VInterestSpeed.common.event.HomeVquShowBottomEvent
import com.mshy.VInterestSpeed.common.event.ShowAdDialogEvent
import com.mshy.VInterestSpeed.common.helper.MagicIndicatorHelper
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.permissionx.guolindev.callback.RequestCallback
import com.quyunshuo.module.home.fragment.HomeVquFragmentViewModel
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.listener.OnPageChangeListener
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/4/11 11:07
 *
 */
class HomeVquFragment :
    BaseLazyFrameFragment<HomeTantaMainFragmentBinding, HomeVquFragmentViewModel>() {

    private val vquTitles = arrayOf("推荐")
    private val vquFragments: ArrayList<Fragment> = ArrayList()
    private var vquViewPagerAdapter: CommonVquMainPageAdapter? = null

//    var vquVoiceApngDrawable: APNGDrawable? = null
    var vquVideoApngDrawable: APNGDrawable? = null

    @TargetApi(Build.VERSION_CODES.M)
    override fun HomeTantaMainFragmentBinding.initView() {

        ImmersionBar.with(this@HomeVquFragment).statusBarView(R.id.view).init()
//        val path = "file:///android_asset/vqu_voice_speed.png"

//        // 从asset file中加载
//        val vquVoiceAssetLoader = AssetStreamLoader(BaseApplication.context, "vqu_voice_speed.png")
//        // 创建APNG Drawable
//        vquVoiceApngDrawable = APNGDrawable(vquVoiceAssetLoader)
//        //自动播放
//        vquVoiceApng.setImageDrawable(vquVoiceApngDrawable)
//        //可覆盖动画中设置的播放次数
//        vquVoiceApngDrawable?.setLoopLimit(0)


//        // 从asset file中加载
//        val vquVideoAssetLoader = AssetStreamLoader(BaseApplication.context, "vqu_video_speed.png")
//        // 创建APNG Drawable
//        vquVideoApngDrawable = APNGDrawable(vquVideoAssetLoader)
//        //自动播放
//        vquVideoApng.setImageDrawable(vquVideoApngDrawable)
//        //可覆盖动画中设置的播放次数
//        vquVideoApngDrawable?.setLoopLimit(0)
        if (UserManager.userInfo?.gender == 2) {
            ivHomeVquMainSearch.visible()
        } else {
            ivHomeVquMainSearch.gone()
        }

        ivHomeVquMainSearch.click {
            ARouter.getInstance().build(RouteUrl.VquMainFragment.HomeVquSearchActivity)
                .navigation()
        }

        vquBtnEdit.setViewClickListener {
            ARouter.getInstance().build(RouteUrl.Info.InfoVquEditActivity).navigation()
        }
        vquIncludeAd.findViewById<ImageView>(R.id.vqu_iv_close).setViewClickListener {
            vquIncludeAd.visibility = View.GONE
        }

        initMagicIndicator()

        vquFragments.add(HomeVquRecommendFragment.newInstance(0))
//        vquFragments.add(HomeVquRecommendFragment.newInstance(1))

        vquViewPagerAdapter =
            CommonVquMainPageAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
            )
        vquViewPagerAdapter?.setData(vquFragments)
        vquViewPager.adapter = vquViewPagerAdapter

        vquIvDelete.setViewClickListener {
            if (mBinding.vquLlBottom != null) {
                mBinding.vquLlBottom.visibility = View.GONE
                mViewModel.vquCloseInfoFinishTip()
            }
        }

        if (UserSpUtils.getUserBean()?.gender == 1) {
            vquGetRedPakeage.setText("领红包")
            vquGetDiamond.setText("红包")
        } else {
            vquGetRedPakeage.setText("领金币")
            vquGetDiamond.setText("金币")
        }
        refreshRecommended()
        toolbarLayout.post {
            appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    ImmersionBar.with(this@HomeVquFragment).statusBarDarkFont(true).init()
                }
            })
        }

        vquSllMatchVideo.click {
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
                PermissionUtils.videoPermission(this@HomeVquFragment,
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

        vquSllInvitation.click {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, UserManager?.webUrl?.share ?: "")
                .navigation()
        }

        vquSllJobCenter.click {
            ARouter.getInstance().build(RouteUrl.Common.WebViewActivity)
                .withString(RouteKey.URL, UserManager?.webUrl?.taskCenter ?: "")
                .navigation()
            UmUtils.setUmEvent(activity, UmUtils.TASKCENTER)
        }

        vquSllMatchVoice.click {

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
                PermissionUtils.vquVoicePermission(this@HomeVquFragment,
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


    private fun initMagicIndicator() {
        val commonNavigator =
            CommonNavigator(
                activity)
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
                simplePagerTitleView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        mBinding.vquViewPager!!.currentItem = index
                    }
                })
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return MagicIndicatorHelper.getDefaultIndicator(context)
            }
        }
        mBinding.vquMagicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mBinding.vquMagicIndicator, mBinding.vquViewPager)
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
                        ds.setColor(resources.getColor(R.color.color_5FBCFE)) //设置颜色
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
                    .setTitle("温馨提示")
                    .setContent(builder)
                    .setSingleButton(true)
                    .setContentSize(15)
                    .setOnSingleClickListener(object : CommonHintDialog.OnSingleClickListener {
                        override fun onClick(dialogFragment: DialogFragment?) {

                        }
                    }).show(childFragmentManager)

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

        mViewModel.vquAdBean.observe(this) {
            vquNotifyAdBanner(it)
        }
        mViewModel.vquAdBean6.observe(this) {
            if (it.getIs_open() === 1) {
                for (i in 0 until it.getList().size) {
                    GlobalScope.launch {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            EventBus.getDefault().post(ShowAdDialogEvent(
                                it.getList()[i]))
                        }
                    }
                }
            } else {
                GlobalScope.launch {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        EventBus.getDefault().post(ShowAdDialogEvent(
                            null))
                    }
                }
            }
        }
    }

    var vqu_bt_know: ShapeTextView? = null

    //加载圆形网络图片
    fun vquSetCircleImageUrl(vquUrl: String?, vquImageView: ImageView?) {
        if (vquUrl == null) {
            return
        }
        context?.let {
            Glide.with(it).load(vquUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(vquImageView!!)
        }
    }

    //加载网络图片
    fun vquSetImageUrl(vquUrl: String?, vquImageView: ImageView?) {
        if (vquUrl == null) {
            return
        }
        context?.let {
            val options = RequestOptions()
                .transform(CenterCrop())
            Glide.with(it)
                .load(vquUrl)
                .apply(options)
                .into(vquImageView!!)
        }
    }


    private fun vquNotifyAdBanner(vquAdBean: CommonVquAdBean) {
        if (vquAdBean != null && vquAdBean.getIs_open() === 1) {
            mBinding.vquIncludeAd.findViewById<Banner<String, BannerImageAdapter<String>>>(R.id.vqu_banner_ad).visibility =
                View.VISIBLE
        } else {
            mBinding.vquIncludeAd.visibility = View.GONE
            return
        }
        if (!vquAdBean.getList().isEmpty()) {
            val images: MutableList<String> = ArrayList()
            for (i in 0 until vquAdBean.getList().size) {
                images.add(NetBaseUrlConstant.IMAGE_URL + vquAdBean?.getList()?.get(i)?.getImage())
            }

            mBinding.vquIncludeAd.findViewById<Banner<String, BannerImageAdapter<String>>>(R.id.vqu_banner_ad)
                .addBannerLifecycleObserver(this)
                .setOrientation(Banner.HORIZONTAL)
                // .setIndicator(null, false)
                .setLoopTime(3000)
                .setAdapter(object : BannerImageAdapter<String>(images) {
                    override fun onBindView(
                        holder: BannerImageHolder?,
                        data: String,
                        position: Int,
                        size: Int,
                    ) {
                        holder?.imageView?.vquLoadRoundImage(data, dp2px(6f))
                    }

                }).setOnBannerListener { data, position ->
                    if (!vquAdBean?.getList().isEmpty()) {
                        JumpUtils.jump(
                            vquAdBean.getList().get(position).getLink_type(),
                            vquAdBean.getList().get(position).getLink_url(),
                            activity
                        )
                    }
                }

        } else {
            mBinding.vquIncludeAd.visibility = View.GONE
        }
    }

    @Subscribe
    fun vquShowMainThread(vquEvent: HomeVquShowBottomEvent) {
        if (vquEvent.isShow) {
            mBinding.vquLlBottom.visibility = View.VISIBLE
        } else {
            mBinding.vquLlBottom.visibility = View.GONE
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

    //加载圆形网络图片
    fun setCircleImageUrl(url: String?, imageView: ImageView?) {
        if (url == null) {
            return
        }
        if (activity != null) {
            Glide.with(requireActivity()).load(url)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imageView!!)
        }
    }

    //加载网络图片
    fun setImageUrl(url: String?, imageView: ImageView?) {
        if (url == null) {
            return
        }
        if (activity != null) {
            val options = RequestOptions()
                .transform(CenterCrop())
            Glide.with(requireActivity())
                .load(url)
                .apply(options)
                .into(imageView!!)
        }
    }


    override fun initRequestData() {
//        mViewModel.vquGetAdvert("2")
//        mViewModel.vquGetAdvert("6")
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: String) {
        if ("RecommendedSwitch" == event) {//刷新是否隐藏推荐
            refreshRecommended()
        }
    }

    override fun onFragmentResume() {
        mBinding.vquVoiceApng.startAnimation()
        mBinding.vquVideoApng.startAnimation()
        super.onFragmentResume()
    }

    override fun onFragmentPause() {
        mBinding.vquVoiceApng?.stopAnimation()
        mBinding.vquVideoApng?.stopAnimation()
        super.onFragmentPause()
    }


    override val mViewModel: HomeVquFragmentViewModel by viewModels<HomeVquFragmentViewModel>()
}