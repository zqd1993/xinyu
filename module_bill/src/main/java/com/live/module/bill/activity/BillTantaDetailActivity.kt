package com.live.module.bill.activity

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.live.module.bill.R
import com.live.module.bill.bean.BillVquTimeSelectEvent
import com.live.module.bill.databinding.BillVquActivityBillDetailBinding
import com.live.module.bill.vm.TantaRechargeViewModel
import com.live.vquonline.base.ktx.dp2px
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * author: Lau
 * date: 2022/4/6
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaDetailActivity)
class BillTantaDetailActivity :
    BaseActivity<BillVquActivityBillDetailBinding, TantaRechargeViewModel>() {
    override val mViewModel: TantaRechargeViewModel by viewModels()

    private val mTabs = mutableListOf<String>()


    private val mFragments = mutableListOf<Fragment>()

    private lateinit var mDatePicker: TimePickerView

    private val mMyOnPageChangeCallback = MyOnPageChangeCallback()

    override fun BillVquActivityBillDetailBinding.initView() {
        mBinding.tbTantaBillBar.toolbar.initClose(getString(R.string.vqu_bill_detail),
            getString(R.string.vqu_bill_selector),
            onBack = {
                finish()
            },
            onClickRight = {
                mDatePicker.show()
            })

        initNavigator()

        initFragment()

        initAgeView()

        initEvent()
    }


    private fun initEvent() {

        mBinding.vpVquBillDetailPage.registerOnPageChangeCallback(mMyOnPageChangeCallback)


    }

    private fun initFragment() {
        val diamondFragment = ARouter.getInstance().build(RouteUrl.Bill.BillTantaDetailFragment)
            .withInt(RouteKey.TYPE, 1)
            .navigation() as Fragment

        val earningsFragment = ARouter.getInstance().build(RouteUrl.Bill.BillTantaDetailFragment)
            .withInt(RouteKey.TYPE, 2)
            .navigation() as Fragment

        val withdrawalFragment =
            ARouter.getInstance().build(RouteUrl.Bill.BillTantaWithdrawalListFragment)
                .navigation() as Fragment

        mFragments.add(diamondFragment)
        mFragments.add(earningsFragment)
        mFragments.add(withdrawalFragment)

        mBinding.vpVquBillDetailPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return mFragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return mFragments[position]
            }
        }
    }

    private fun initAgeView() {
        val endCalendar: Calendar = Calendar.getInstance()
        endCalendar.set(
            endCalendar.get(Calendar.YEAR),
            endCalendar.get(Calendar.MONTH),
            endCalendar.get(Calendar.DAY_OF_MONTH)
        )

        val startCalendar: Calendar = Calendar.getInstance()
        startCalendar.set(1940, 1, 1)

        mDatePicker = TimePickerBuilder(
            this
        ) { date, _ ->
            val calendar = Calendar.getInstance()
            calendar.time = date
            val month = calendar[Calendar.MONTH] + 1
            val year = calendar[Calendar.YEAR]

            val dateTime = "${year}-${month}"

            EventBus.getDefault().post(
                BillVquTimeSelectEvent(
                    mBinding.vpVquBillDetailPage.currentItem + 1, dateTime
                )
            )

        }.setBgColor(android.R.color.transparent)
            .setType(booleanArrayOf(true, true, false, false, false, false))
            .setTitleBgColor(android.R.color.transparent)
            .setDividerColor(ResUtils.getColor(android.R.color.transparent))
            .setCancelColor(ResUtils.getColor(R.color.color_A3AABE))
            .setSubmitColor(ResUtils.getColor(R.color.color_273145))
            .setTextColorCenter(ResUtils.getColor(R.color.color_273145))
            .setRangDate(startCalendar, endCalendar)
            .build()

        val group: ViewGroup = mDatePicker.dialogContainerLayout
        group.setBackgroundResource(R.drawable.shape_bg_picker_view)
        mDatePicker.setDate(endCalendar)
    }

    private fun initNavigator() {

        mTabs.add(getString(R.string.vqu_bill_tab_diamond))
        mTabs.add(getString(R.string.vqu_bill_tab_earnings))
        mTabs.add(getString(R.string.vqu_bill_tab_withdrawal))

        val commonNavigator =
            CommonNavigator(
                this@BillTantaDetailActivity
            )
        commonNavigator.setMiddle(true)
        commonNavigator.setIsWrapPagerIndicator(true)
        commonNavigator.isAdjustMode = true
        commonNavigator.setSpacePx(dp2px(28f).toFloat())
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTabs.size
            }

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(
                        context
                    )
                simplePagerTitleView.text = mTabs[index]
                simplePagerTitleView.textSize = 16f
                simplePagerTitleView.normalColor = ResUtils.getColor(R.color.color_textDesc)
                simplePagerTitleView.selectedColor = ResUtils.getColor(R.color.color_textMain)
                simplePagerTitleView.setOnClickListener {
                    mBinding.vpVquBillDetailPage.currentItem = index
//                    mViewPage.setCurrentItem(index);
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator {
                val indicator =
                    LinePagerIndicator(
                        context
                    )
                indicator.roundRadius = dp2px(1.5f).toFloat()
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineWidth = dp2px(10f).toFloat()
                indicator.lineHeight = dp2px(3f).toFloat()
                indicator.yOffset = dp2px(6f).toFloat()
                indicator.setColors(
                    ResUtils.getColor(R.color.color_FF7AC2),
                    ResUtils.getColor(R.color.color_FF7AC2)
                )
                return indicator
            }
        }

        mBinding.miVquBillDetailIndicator.navigator = commonNavigator
        com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper.bind(
            mBinding.miVquBillDetailIndicator,
            mBinding.vpVquBillDetailPage
        )
    }

    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun onDestroy() {
        mBinding.vpVquBillDetailPage.unregisterOnPageChangeCallback(mMyOnPageChangeCallback)
        super.onDestroy()
    }

    private inner class MyOnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
//            super.onPageSelected(position)

            if (position == 2) {
                mBinding.tbTantaBillBar.toolbar.findViewById<TextView>(R.id.tv_right).gone()
            } else {
                mBinding.tbTantaBillBar.toolbar.findViewById<TextView>(R.id.tv_right).visible()
            }
        }
    }
}

