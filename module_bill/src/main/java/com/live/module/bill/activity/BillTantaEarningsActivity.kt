package com.live.module.bill.activity

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gyf.immersionbar.ImmersionBar
import com.live.module.bill.R
import com.live.module.bill.bean.BillTantaWithdrawOptions
import com.live.module.bill.bean.UpdateExchangeSucceedEvent
import com.live.module.bill.bean.UpdateVquEarningEvent
import com.live.module.bill.bean.UpdateVquWalletEvent
import com.live.module.bill.databinding.BillVquActivityEarningsBinding
import com.live.module.bill.fragment.BillVquExchangeFragment
import com.live.module.bill.fragment.BillVquWithdrawFragment
import com.live.module.bill.vm.TantaEarningsViewModel
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.BarUtils
import com.live.vquonline.base.utils.EventBusRegister
import com.mshy.VInterestSpeed.common.bean.TantaWalletBean
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ext.setVisible
import com.mshy.VInterestSpeed.common.helper.MagicIndicatorHelper
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.mshy.VInterestSpeed.common.utils.FontsFamily
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/4/6
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaEarningsActivity)
@EventBusRegister
class BillTantaEarningsActivity :
    BaseActivity<BillVquActivityEarningsBinding, TantaEarningsViewModel>() {
    override val mViewModel: TantaEarningsViewModel by viewModels()

    private var mTantaWalletBean: TantaWalletBean? = null

    private var mTantaCurrentOptions: BillTantaWithdrawOptions? = null

    private var mAlipayAccount = ""
    private val mTitles = arrayOf("收益提现", "兑换金币")
//    private val mTitles = arrayOf( "兑换金币")
    private var mViewPagerAdapter: CommonVquMainPageAdapter? =
        null
    private val mFragments: ArrayList<Fragment> = ArrayList()

    override fun BillVquActivityEarningsBinding.initView() {
        //"今日收益"面板，只针对女用户开放
        mBinding.sclRecentEarnings.setVisible(UserManager.userInfo?.gender == 1)

        mBinding.tvVquBillEarningsBalanceNum.typeface = FontsFamily.tfDDinExpBold

//        mBinding.tbTantaBillBar.toolbar.initClose(
//            getString(R.string.mine_my_earnings),
//            onBack = {
//                finish()
//            },
//            backImg = R.mipmap.ic_back_white,
//            onClickRight = {
//                ARouter.getInstance().build(RouteUrl.Bill.BillTantaDetailActivity).navigation()
//            },
//            rightStr = getString(R.string.vqu_bill_detail),
//            backgroundColor = R.color.transparent,
//            rightColor = R.color.white
//        )
//        mBinding.tbTantaBillBar.tvTitle.setTextColor(ResUtils.getColor(R.color.white))
//        val height = BarUtils.getStatusBarHeight()
//        val layoutParams = mBinding.tbTantaBillBar.toolbar.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.topMargin += height
        mBinding.ivBack.clickDelay {
            finish()
        }
        mBinding.tvRight.clickDelay{
            ARouter.getInstance().build(RouteUrl.Bill.BillTantaDetailActivity).navigation()
        }

        mFragments.add(BillVquWithdrawFragment.newInstance())
        mFragments.add(BillVquExchangeFragment.newInstance(2))
        mViewPagerAdapter =
            com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter(
                supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT
            )
        mViewPagerAdapter?.setData(mFragments)
        initMagicIndicator()
        mBinding.viewPager.adapter = mViewPagerAdapter
    }

    override fun setStatusBar() {
//        super.setStatusBar()
        ImmersionBar.with(this).transparentStatusBar().init()
    }


    private fun initMagicIndicator() {
        val commonNavigator =
            CommonNavigator(
                this@BillTantaEarningsActivity
            )
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object :
            CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getTitleView(
                context: Context?,
                index: Int
            ): IPagerTitleView? {
                val simplePagerTitleView = MagicIndicatorHelper.getDefaultTitleView(
                    context,
                    mTitles[index]
                )
//                val simplePagerTitleView = SimplePagerTitleView(context)
//                simplePagerTitleView.selectedColor = ResUtils.getColor(R.color.black)
//                simplePagerTitleView.normalColor = ResUtils.getColor(R.color.color_999999)
//                simplePagerTitleView.textSize = 23f
//                simplePagerTitleView.typeface = Typeface.defaultFromStyle(Typeface.BOLD) //加粗
//                simplePagerTitleView.text = mTitles[index]
////                val simplePagerTitleView: SimplePagerTitleView =
////                    MagicIndicatorHelper.getDefaultTitleView(
////                        context,
////                        mTitles[index]
////                    )
                simplePagerTitleView.setOnClickListener { mBinding.viewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator? {
                return MagicIndicatorHelper.getDefaultIndicator(context, R.color.color_B4A3FD)
            }
        }
        mBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mBinding.magicIndicator, mBinding.viewPager)
    }


//    private fun bindAccount() {
//        val name = mBinding.etBillVquEarningsAlipayName.text.toString()
//        val account = mBinding.etBillVquEarningsAlipayAccount.text.toString()
//
//        val options = mVquCurrentOptions
//        if (options == null) {
//            toast(R.string.mine_vqu_earning_withdrawal_tips)
//            return
//        }
//
//        mViewModel.vquBindAlipay(name, account, options.money)
//    }


    override fun initObserve() {
        mViewModel.earningsData.observe(this) {
            var updateVquEarningEvent = UpdateVquEarningEvent()
            updateVquEarningEvent.mVquBillVquEarningData = it
            EventBus.getDefault().post(updateVquEarningEvent)
//            observeEarningData(it)
        }


        mViewModel.walletIndexResultData.observe(this) {
            observeWalletData(it)
        }
    }


    private fun observeWalletData(it: TantaWalletBean) {
        mTantaWalletBean = it
        //传值到子fragment
        var updateVquWalletEvent = UpdateVquWalletEvent()
        updateVquWalletEvent.mTantaWalletBean = it
        EventBus.getDefault().post(updateVquWalletEvent)

        mBinding.tvVquBillEarningsBalanceNum.text = it.account.incomeCoinMoney

        //兑换按钮
        mBinding.stvWithdrawIng.setVisible(it.cash > 0)
        if (it.cash > 0) {
            mBinding.stvWithdrawIng.text =
                getString(R.string.common_vqu_withdrawaling, it.cash.toString())
        }

        mBinding.tvTodayEarnings.text = it.account.todayIncome
        mBinding.tvSevenEarnings.text = it.account.hebdoIncome

    }


    override fun initRequestData() {
//        mViewModel.vquWithdraw()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getWalletIndexData()
//        mViewModel.vquWalletAlipay()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateExchangeSucceedEvent(event: UpdateExchangeSucceedEvent) {
        mViewModel.getWalletIndexData()
    }

}