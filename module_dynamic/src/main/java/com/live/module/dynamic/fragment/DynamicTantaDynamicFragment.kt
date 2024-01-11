package com.live.module.dynamic.fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.dynamic.databinding.DynamicTantaFragmentDynamicBinding
import com.live.module.dynamic.utils.MagicIndicatorHelper
import com.live.module.dynamic.vm.DynamicPublishViewModel
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.event.DynamicLikeEvent
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.mshy.VInterestSpeed.common.utils.GlideEngine
import com.mshy.VInterestSpeed.common.utils.PermissionUtils
import java.io.File


/**
 * author: Tany
 * date: 2022/4/9
 * description:
 */
@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteUrl.Dynamic.DynamicVquDynamicFragment)
class DynamicTantaDynamicFragment :
    BaseFragment<DynamicTantaFragmentDynamicBinding, DynamicPublishViewModel>() {
    private val mTitles = arrayOf("推荐", "关注")
    private var mViewPagerAdapter: CommonVquMainPageAdapter? = null
    private val mFragments: ArrayList<Fragment> = ArrayList()

    override val mViewModel: DynamicPublishViewModel by viewModels()

    private var selectPicList: ArrayList<LocalMedia?>? = null

    override fun DynamicTantaFragmentDynamicBinding.initView() {
        ImmersionBar.with(this@DynamicTantaDynamicFragment).titleBar(clTop).init()
        mFragments.add(DynamicTantaDynamicChildFragment.newInstance(1))
        mFragments.add(DynamicTantaDynamicChildFragment.newInstance(2))
        mViewPagerAdapter =
            CommonVquMainPageAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
            )
        mViewPagerAdapter?.setData(mFragments)
        initMagicIndicator()
        mBinding.viewPager.adapter = mViewPagerAdapter
        mBinding.ivPublish.setOnClickListener {
            if (UserManager.userInfo?.gender == 1) {
                mViewModel.vquIsAuth()
            } else {
//                var dialog = DynamicVquPublishDialog()
//                dialog.show(parentFragmentManager, "Dynamic")
                PermissionUtils.storagePermission(
                    this@DynamicTantaDynamicFragment,
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    "需要开启储存权限以便使用聊天、动态、投诉、相册、等功能中图片、视频的选择和保存功能。",
                    requestCallback = { allGranted, grantedList, deniedList ->
                        if (allGranted) {
                            publishDynamic()
                        } else {
                            toast("用户拒绝授权")
                        }
                    })
            }

        }
        mBinding.rlMessage.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Dynamic.DynamicVquLikeListActivity)
                .navigation()
        }
    }

    private val mCompressDialog: LoadingDialog by lazy {
        LoadingDialog(activity!!, "压缩中")
    }

    private fun publishDynamic() {
        PictureSelector.create(this@DynamicTantaDynamicFragment)//进页面就进行选择
            .openGallery(SelectMimeType.ofAll())
            .setImageEngine(GlideEngine.createGlideEngine())
            .isDisplayCamera(!UserManager.isVideo)
//            .setSandboxFileEngine(object : SandboxFileEngine {
//                override fun onStartSandboxFileTransform(
//                    context: Context?,
//                    isOriginalImage: Boolean,
//                    index: Int,
//                    media: LocalMedia?,
//                    listener: OnCallbackIndexListener<LocalMedia>?
//                ) {
//                    SandboxTransformUtils.copyPathToSandbox(context,media?.realPath,media?.mimeType)
//                }
//            })
            .setCompressEngine { _, list, listener ->
                if (!list.isNullOrEmpty()) {
                    list.map {
                        Luban.with(activity).load(it.realPath)
                            .ignoreBy(200)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {
                                    mCompressDialog.show()
                                }

                                override fun onSuccess(index: Int, compressFile: File?) {
                                    mCompressDialog.dismiss()
                                    it.compressPath = compressFile?.absolutePath
                                    listener?.onCall(list)
                                }

                                override fun onError(index: Int, e: Throwable?) {
                                    mCompressDialog.dismiss()
                                }
                            }).launch()
                    }

                }
            }
            .setMaxSelectNum(9)
            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(result: ArrayList<LocalMedia?>?) {
                    selectPicList = result
                    if (selectPicList.isNullOrEmpty()) {
                        return
                    }
                    var typeStr = selectPicList?.get(0)?.mimeType
                    if (typeStr!!.startsWith("video")) {
                        ARouter.getInstance()
                            .build(RouteUrl.Dynamic.DynamicVquPublishVideoActivity)
                            .withSerializable("list", selectPicList)
                            .navigation()
                    } else if (typeStr!!.startsWith("image")) {
                        ARouter.getInstance()
                            .build(RouteUrl.Dynamic.DynamicVquPublishImgActivity)
                            .withSerializable("list", selectPicList)
                            .navigation()
                    }

                }

                override fun onCancel() {
                }
            })
    }

    private fun initMagicIndicator() {
        val commonNavigator =
            CommonNavigator(
                activity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val simplePagerTitleView: SimplePagerTitleView =
                    MagicIndicatorHelper.getDefaultTitleView(
                        context,
                        mTitles[index]
                    )
                simplePagerTitleView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        mBinding.viewPager!!.currentItem = index
                    }
                })
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return MagicIndicatorHelper.getDefaultIndicator(context)
            }
        }
        mBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.viewPager)
    }

    override fun initObserve() {
        mViewModel.vquAuthData.observe(this, Observer {
            publishDynamic()
//            if (it.data.isAuth == 1) {
//            var dialog = DynamicVquPublishDialog()
//            dialog.show(parentFragmentManager, "Dynamic")
//            } else {
//                CommonHintDialog()
//                    .setTitle("真人认证")
//                    .setContent(resources.getString(R.string.common_vqu_auth))
//                    .setLeftText("暂不认证")
//                    .setRightText("去认证")
//                    .setContentSize(15)
//                    .setContentGravity(Gravity.CENTER)
//                    .setOnClickListener(object : CommonHintDialog.OnClickListener {
//                        override fun onLeft(dialogFragment: DialogFragment) {}
//                        override fun onRight(dialogFragment: DialogFragment) {
//                            if (UserManager.userInfo?.gender == 1) {
//                                //女认证
//                                ARouter.getInstance().build(RouteUrl.Auth.AuthVquFaceIdentifyActivity).navigation()
//                            } else {
//                                //男认证
//                                ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity).navigation()
//                            }
//                        }
//                    })
//                    .show(parentFragmentManager)
//            }
        })
        mViewModel.dynamicLikeCount.observe(this, Observer {
            if (it.data in 1..98) {
                mBinding.tvCount.visible()
                mBinding.tvCount.text = it.data.toString()
            } else if (it.data == 0) {
                mBinding.tvCount.gone()
            } else {
                mBinding.tvCount.visible()
                mBinding.tvCount.text = "99"
            }

        })
    }

    override fun onResume() {
        super.onResume()
        mViewModel.vquDynamicLikeCount()
    }

    override fun initRequestData() {
        mViewModel.vquDynamicLikeCount()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(event: DynamicLikeEvent) {
        mViewModel.vquDynamicLikeCount()
    }
}