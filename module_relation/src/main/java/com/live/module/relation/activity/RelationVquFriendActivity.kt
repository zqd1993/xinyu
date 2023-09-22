package com.live.module.relation.activity

import android.content.Context
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.relation.R
import com.live.module.relation.databinding.RelationVquActivityFriendBinding
import com.live.module.relation.vm.RelationViewModel
import com.live.vquonline.base.ktx.dp2px
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/5/16
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Relation.RelationVquFriendActivity)
class RelationVquFriendActivity :
    BaseActivity<RelationVquActivityFriendBinding, RelationViewModel>() {

    private val mTabs = mutableListOf<String>()

    private val mFragments = mutableListOf<Fragment>()

    override val mViewModel: RelationViewModel by viewModels()


    @Autowired(name = RouteKey.TYPE)
    @JvmField
    var type: Int = 0


    override fun initObserve() {

    }

    override fun initRequestData() {

    }

    override fun RelationVquActivityFriendBinding.initView() {

        mBinding.tbRelationVquFriendBar.toolbar.initClose("好友") {
            finish()
        }

        initNavigator()

        initFragment()

    }

    private fun initFragment() {
        val focusFragment = ARouter.getInstance().build(RouteUrl.Relation.RelationVquFriendFragment)
            .withInt(RouteKey.TYPE, RouteKey.RelationType.FOCUS)
            .navigation() as Fragment

        val fansFragment = ARouter.getInstance().build(RouteUrl.Relation.RelationVquFriendFragment)
            .withInt(RouteKey.TYPE, RouteKey.RelationType.FANS)
            .navigation() as Fragment

        mFragments.add(focusFragment)
        mFragments.add(fansFragment)

        mBinding.vpVquRelationFriendPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragments[position]
            }
        }

        mBinding.vpVquRelationFriendPage.currentItem = type


    }

    private fun initNavigator() {
        mTabs.add("关注")
        mTabs.add("粉丝")

        val commonNavigator =
            com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator(
                this@RelationVquFriendActivity)
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
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(R.color.color_textMain)
                simplePagerTitleView.setOnClickListener {
                    mBinding.vpVquRelationFriendPage.currentItem = index
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
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(R.color.black),
                    com.mshy.VInterestSpeed.common.utils.ResUtils.getColor(R.color.black)
                )
                return indicator
            }
        }

        mBinding.miVquRelationFriendIndicator.navigator = commonNavigator
        com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper.bind(
            mBinding.miVquRelationFriendIndicator,
            mBinding.vpVquRelationFriendPage
        )
    }
}