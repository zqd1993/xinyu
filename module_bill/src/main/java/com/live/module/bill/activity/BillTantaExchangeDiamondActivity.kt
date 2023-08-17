package com.live.module.bill.activity

import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.live.module.bill.R
import com.live.module.bill.databinding.BillVquActivityExchangeDiamondBinding
import com.live.module.bill.vm.BillTantaExchangeDiamondViewModel
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.initClose
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import dagger.hilt.android.AndroidEntryPoint


/**
 * author: Lau
 * date: 2022/4/19
 * description:
 */
@AndroidEntryPoint
@Route(path = RouteUrl.Bill.BillTantaExchangeDiamondActivity)
class BillTantaExchangeDiamondActivity :
    BaseActivity<BillVquActivityExchangeDiamondBinding, BillTantaExchangeDiamondViewModel>() {
    override val mViewModel: BillTantaExchangeDiamondViewModel by viewModels()

    //兑换比例
    private val mTantaRatio = 100

    private var mTantaEarnings = 0.0

    override fun BillVquActivityExchangeDiamondBinding.initView() {
        mBinding.tbBillVquExchangeDiamond.toolbar.initClose(
            getString(R.string.vqu_bill_exchange_diamond)
        ) {
            finish()
        }

        mBinding.etBillVquExchangeDiamondUseMoney.addTextChangedListener {
            var money: Int
            try {
                money = if (it.isNullOrEmpty()) {
                    0
                } else {
                    it.toString().toInt()
                }
            } catch (e: Exception) {
                money = 0
                toast("请输入正确金额")
            }
            val earningsMoney = mTantaEarnings.toInt()
            if (money > earningsMoney) {
                money = earningsMoney
                mBinding.etBillVquExchangeDiamondUseMoney.setText(earningsMoney.toString())
                mBinding.etBillVquExchangeDiamondUseMoney.setSelection(earningsMoney.toString().length)
            }

            val diamondNum = (money * mTantaRatio).toString()

            mBinding.tvBillVquExchangeDiamondGetNum.text =
                getString(R.string.vqu_bill_get_diamond_num, diamondNum)

        }

        mBinding.stvBillVquExchangeDiamondNow.clickDelay {
            vquClickExchangeNow()
        }

    }

    private fun vquClickExchangeNow() {
        try {
            val useMoney = mBinding.etBillVquExchangeDiamondUseMoney.text.toString()

            if (useMoney.isEmpty()) {
                toast("请输入要使用的金额")
                return
            }

            val money = useMoney.toInt()

            if (money <= 0) {
                toast("请输入正确的金额")
                return
            }

            if (mTantaEarnings < money) {
                toast("余额不足")
                return
            }

            showExchangeDialog(money)

        } catch (e: Exception) {
            toast("请输入正确的金额")
        }
    }

    private fun showExchangeDialog(money: Int) {
        val messageDialog = MessageDialog()
        messageDialog.setTitle(R.string.vqu_bill_exchange_diamond)
        messageDialog.setIsHtml(true)

        val diamond = money * mTantaRatio

        val content = getString(R.string.vqu_bill_exchange_text, money.toString(), diamond.toString())
        val html = com.mshy.VInterestSpeed.common.utils.TextUtils.getSpannedHtml(content)
        messageDialog.setContent(html)

        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {
                exchangeNow(money)
                return false
            }
        })

        messageDialog.show(supportFragmentManager, "")


    }

    private fun exchangeNow(money: Int) {
        mViewModel.vquChangeCoin(money)
    }

    override fun initObserve() {
        mViewModel.earningData.observe(this) {
            mBinding.tvBillVquExchangeEarnings.text =
                getString(R.string.vqu_bill_earning_money_num, it.incomeMoney)
            mTantaEarnings = it.incomeMoney.toDouble()
        }
    }

    override fun initRequestData() {
    }

    override fun onResume() {
        super.onResume()
        mViewModel.vquWithdraw()
    }

}