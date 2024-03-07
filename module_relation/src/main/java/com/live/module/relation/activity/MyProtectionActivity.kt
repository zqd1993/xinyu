package com.live.module.relation.activity

import android.content.Context
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.relation.R
import com.live.module.relation.databinding.MyProtectionActivityBinding
import com.live.module.relation.vm.MyProtectionViewModel
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ext.initClose
import dagger.hilt.android.AndroidEntryPoint
import com.live.module.relation.databinding.RelationVquActivityFriendBinding
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.constant.RouteKey

@AndroidEntryPoint
@Route(path = RouteUrl.Relation.MyProtectionActivity)
class MyProtectionActivity : BaseActivity<MyProtectionActivityBinding, MyProtectionViewModel>() {

    private val mTabs = mutableListOf<String>()

    private val mFragments = mutableListOf<Fragment>()

    override val mViewModel: MyProtectionViewModel
            by viewModels()

    override fun MyProtectionActivityBinding.initView() {
        mBinding.tbMyProtectionBar.toolbar.initClose("我的守护") {
            finish()
        }
        initNavigator()

        initFragment()
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    private fun initFragment() {
        val focusFragment = ARouter.getInstance().build(RouteUrl.Relation.MyProtectionFragment)
            .withInt(RouteKey.TYPE, RouteKey.RelationType.PROTECTION_ME)
            .navigation() as Fragment

        val fansFragment = ARouter.getInstance().build(RouteUrl.Relation.MyProtectionFragment)
            .withInt(RouteKey.TYPE, RouteKey.RelationType.MY_PROTECTION)
            .navigation() as Fragment

        mFragments.add(focusFragment)
        mFragments.add(fansFragment)

        mBinding.vpMyProtectionPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragments[position]
            }
        }

    }

    private fun initNavigator() {
        mTabs.add("守护我的")
        mTabs.add("我守护的")

        val commonNavigator =
            com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator(
                this@MyProtectionActivity)
        commonNavigator.setMiddle(true)
        commonNavigator.setIsWrapPagerIndicator(true)
        commonNavigator.isAdjustMode = true
        commonNavigator.setSpacePx(dp2px(28f).toFloat())
        commonNavigator.adapter = object : com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTabs.size
            }

            override fun getTitleView(context: Context?, index: Int): com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView {
                val simplePagerTitleView: com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView =
                    com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView(
                        context)
                simplePagerTitleView.text = mTabs[index]
                simplePagerTitleView.textSize = 16f
                simplePagerTitleView.normalColor =
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(
                        R.color.color_textDesc
                    )
                simplePagerTitleView.selectedColor =
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(R.color.color_B4A3FD)
                simplePagerTitleView.setOnClickListener {
                    mBinding.vpMyProtectionPage.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator {
                val indicator =
                    com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator(
                        context)
                indicator.roundRadius = dp2px(1.5f).toFloat()
                indicator.mode =
                    com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_EXACTLY
                indicator.lineWidth = dp2px(8f).toFloat()
                indicator.lineHeight = dp2px(3f).toFloat()
                indicator.yOffset = dp2px(6f).toFloat()
                indicator.setColors(
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(R.color.color_B4A3FD),
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(R.color.color_B4A3FD)
                )
                return indicator
            }
        }

        mBinding.miMyProtectionIndicator.navigator = commonNavigator
        com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper.bind(
            mBinding.miMyProtectionIndicator,
            mBinding.vpMyProtectionPage
        )
    }

}