package com.live.module.bill.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.live.module.bill.R
import com.live.module.bill.bean.UpdateExchangeSucceedEvent
import com.live.module.bill.databinding.BillVquFragmentExchangeBinding
import com.live.module.bill.vm.BillTantaExchangeDiamondViewModel
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus


/**
 * author: Tany
 * date: 2022/4/9
 * description:兑换金币
 */
@AndroidEntryPoint
//@EventBusRegister
class BillVquExchangeFragment :
    BaseFragment<BillVquFragmentExchangeBinding, BillTantaExchangeDiamondViewModel>() {
    var type: Int = 0//0同城  1热门 2关注
    override val mViewModel: BillTantaExchangeDiamondViewModel by viewModels()
    var isExchange: Boolean = false

    //兑换比例
    private val mVquRatio = 100

    private var mVquEarnings = 0.0


    override fun BillVquFragmentExchangeBinding.initView() {
        type = arguments?.getInt("type")!!

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
            val earningsMoney = mVquEarnings.toInt()
            if (money > earningsMoney) {
                money = earningsMoney
                mBinding.etBillVquExchangeDiamondUseMoney.setText(earningsMoney.toString())
                mBinding.etBillVquExchangeDiamondUseMoney.setSelection(earningsMoney.toString().length)
            }

            if (money > 0) {
                mBinding.stvExchange.setStartColor(
                    ResUtils.getColor(R.color.color_FF7AC2),
                    ResUtils.getColor(R.color.color_FF7AC2)
                )
            } else {
                mBinding.stvExchange.setStartColor(
                    ResUtils.getColor(R.color.color_CCCCCC),
                    ResUtils.getColor(R.color.color_CCCCCC)
                )
            }

            val diamondNum = (money * mVquRatio).toString()

            mBinding.tvBillVquExchangeDiamondGetNum.text =
                getString(R.string.vqu_bill_get_diamond_num, diamondNum)

        }

        mBinding.stvExchange.clickDelay {
            val useMoney = mBinding.etBillVquExchangeDiamondUseMoney.text.toString()
            if (useMoney.isEmpty()) {
                return@clickDelay
            }
            val money = useMoney.toInt()

            if (money <= 0) {
                return@clickDelay
            }
            vquClickExchangeNow()
        }
    }

    private fun vquClickExchangeNow() {
        try {
            val useMoney = mBinding.etBillVquExchangeDiamondUseMoney.text.toString()
            com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("mVquEarnings=$mVquEarnings...useMoney=$useMoney")
            if (useMoney.isEmpty()) {
                toast("请输入要使用的金额")
                return
            }

            val money = useMoney.toInt()

            if (money <= 0) {
                toast("请输入正确的金额")
                return
            }

            if (mVquEarnings < money) {
                toast("余额不足")
                return
            }

            showExchangeDialog(money)

        } catch (e: Exception) {
            com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("Exception=$e")
            toast("请输入正确的金额")
        }
    }

    companion object {
        fun newInstance(type: Int): BillVquExchangeFragment {
            val args = Bundle()
            args.putInt("type", type)
            val fragment = BillVquExchangeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun showExchangeDialog(money: Int) {
        val messageDialog = MessageDialog()
        messageDialog.setIsHtml(true)
        messageDialog.setTitle("兑换金币")

        val diamond = money * mVquRatio
        val content = getString(
            R.string.vqu_bill_exchange_text,
            money.toString(), diamond.toString()
        )
        messageDialog.setContent(com.mshy.VInterestSpeed.common.utils.TextUtils.getSpannedHtml(content))
        messageDialog.setContentGravity(Gravity.CENTER)

        messageDialog.setRightButtonColor(
            ResUtils.getColor(R.color.color_FFB5DB),
            ResUtils.getColor(R.color.color_FE66A4)
        )

        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {
                exchangeNow(money)
                return false
            }

        })
        activity?.supportFragmentManager?.let { messageDialog.show(it, "") }

    }


    private fun exchangeNow(money: Int) {
        isExchange = true
        mViewModel.vquChangeCoin(money)
    }

    override fun initObserve() {
        mViewModel.earningData.observe(this) {
            if (isExchange) {
                EventBus.getDefault().post(UpdateExchangeSucceedEvent())
                mBinding.etBillVquExchangeDiamondUseMoney.setText("")
                mBinding.tvBillVquExchangeDiamondGetNum.text =
                    getString(R.string.vqu_bill_get_diamond_num, "0")
                mBinding.etBillVquExchangeDiamondUseMoney.clearFocus()
                isExchange = false
            }
            mVquEarnings = it.incomeMoney.toDouble()
        }
    }

    override fun initRequestData() {
    }

    override fun onResume() {
        super.onResume()
        mViewModel.vquWithdraw()
    }

}