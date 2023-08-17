package com.live.module.mine.activity

import android.content.Context
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.mine.R
import com.live.module.mine.databinding.MineVquActivityMyBackpackBinding
import com.live.module.mine.vm.MineVquMyBackpackViewModel
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Mine.MineVquMyBackpackActivity)
class MineVquMyBackpackActivity :
    BaseActivity<MineVquActivityMyBackpackBinding, MineVquMyBackpackViewModel>() {
    override val mViewModel: MineVquMyBackpackViewModel by viewModels()

    private val mTabs = mutableListOf<String>()


    private val mFragments = mutableListOf<Fragment>()

    override fun MineVquActivityMyBackpackBinding.initView() {

        mBinding.tbMineVquMyBackpackBar.toolbar.initClose(getString(R.string.mine_vqu_menu_backpack)) { finish() }

        initNavigator()

        initFragment()
    }

    private fun initFragment() {
        val diamondFragment = ARouter.getInstance().build(RouteUrl.Mine.MineVquCouponFragment)
            .withInt(RouteKey.TYPE, 1)
            .navigation() as Fragment

        val earningsFragment =
            ARouter.getInstance().build(RouteUrl.Mine.MineVquTryCardFragment)
                .withInt(RouteKey.TYPE, 2)
                .navigation() as Fragment

        mFragments.add(diamondFragment)
        mFragments.add(earningsFragment)

        mBinding.vpMineVquMyBackpackPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragments[position]
            }
        }
    }

    private fun initNavigator() {

        mTabs.add(getString(R.string.mine_vqu_coupon))
        mTabs.add(getString(R.string.mine_vqu_try_card))

        val commonNavigator =
            com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator(
                this)
        commonNavigator.setMiddle(true)
        commonNavigator.setIsWrapPagerIndicator(true)
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
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.normalColor = ResUtils.getColor(R.color.color_A3AABE)
                simplePagerTitleView.selectedColor = ResUtils.getColor(R.color.color_273145)
                simplePagerTitleView.setOnClickListener {
                    mBinding.vpMineVquMyBackpackPage.currentItem = index
//                    mViewPage.setCurrentItem(index);
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator {
                val indicator =
                    com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator(
                        context)
                indicator.roundRadius = dp2px(1.5f).toFloat()
                indicator.mode = com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_EXACTLY
                indicator.lineWidth = dp2px(10f).toFloat()
                indicator.lineHeight = dp2px(3f).toFloat()
                indicator.yOffset = dp2px(6f).toFloat()
                indicator.setColors(
                    ResUtils.getColor(R.color.color_BFB6FF),
                    ResUtils.getColor(R.color.color_7C69FE)
                )
                return indicator
            }
        }

        mBinding.miMineVquMyBackpackIndicator.navigator = commonNavigator
        com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper.bind(
            mBinding.miMineVquMyBackpackIndicator,
            mBinding.vpMineVquMyBackpackPage
        )
    }

    override fun initObserve() {

    }

    override fun initRequestData() {

    }
}