package com.live.module.bill.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.bill.R
import com.live.module.bill.adapter.BillVquEarningsAdapter
import com.live.module.bill.bean.*
import com.live.module.bill.databinding.BillVquFragmentWithdrawBinding
import com.live.module.bill.vm.TantaEarningsViewModel
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.EventBusRegister
import com.live.vquonline.base.utils.ToastUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.TantaWalletBean
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.BaseFragment
import com.mshy.VInterestSpeed.common.ui.dialog.MessageDialog
import com.mshy.VInterestSpeed.common.ui.dialog.TopDownButtonMessageDialog
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.RiskControlUtil
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * author: Tany
 * date: 2022/4/9
 * description:收益提现
 */
@AndroidEntryPoint
@EventBusRegister
class BillVquWithdrawFragment :
    BaseFragment<BillVquFragmentWithdrawBinding, TantaEarningsViewModel>() {
    private var mAlipayAccount = ""
    private val mAdapter = BillVquEarningsAdapter()
    private var mTantaWalletBean: TantaWalletBean? = null
    private var mVquBillVquEarningData: BillVquEarningData? = null
    private var mCurrentPosition = -1
    private var mVquCurrentOptions: BillTantaWithdrawOptions? = null
    private var mAlipayType: Int? = null
    override val mViewModel: TantaEarningsViewModel by viewModels()
    override fun BillVquFragmentWithdrawBinding.initView() {

        initRecyclerView()

        mBinding.stvWithdrawBtn.clickDelay {
            postWithdraw()
        }
        mBinding.sclBindAccount.clickDelay {
            jumpBindAccountPage()
        }
    }

    companion object {
        fun newInstance(): BillVquWithdrawFragment {
            val args = Bundle()
            val fragment = BillVquWithdrawFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initObserve() {
        mViewModel.withdrawPriceData.observe(this) {
            observeWithdrawPriceData(it)
        }

        mViewModel.withdrawResultData.observe(this) {
            if (it == -1) {
                showMothLimitDialog()
            } else {
                EventBus.getDefault().post(UpdateExchangeSucceedEvent())
            }
        }
    }

    override fun initRequestData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateVquEarningEvent(event: UpdateVquEarningEvent) {
        event.mVquBillVquEarningData?.let { setEarningData(it) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateVquWalletEvent(event: UpdateVquWalletEvent) {
        mTantaWalletBean = event.mTantaWalletBean
        mTantaWalletBean?.account?.incomeCoinMoney?.let {
            mAdapter.setVquIncomeCoinMoney(it.toDouble())
        }

    }

    private fun initRecyclerView() {
        (mBinding.rvWithdrawOptions.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false

        mBinding.rvWithdrawOptions.adapter = mAdapter

        mAdapter.setOnItemClickListener { _, _, position ->

            if (position == mCurrentPosition) {
                return@setOnItemClickListener
            }


            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener


            if ((mTantaWalletBean?.account?.incomeCoinMoney?.toDouble()
                    ?: 0.0) < item.money.toInt()
            ) {
                toast("可提现余额不足")
                return@setOnItemClickListener
            }

            if (mCurrentPosition == position) {
                return@setOnItemClickListener
            }


            item.selected = true
            mAdapter.notifyItemChanged(position)

            val lastItem = mAdapter.getItemOrNull(mCurrentPosition)
            if (lastItem != null) {
                lastItem.selected = false
                mAdapter.notifyItemChanged(mCurrentPosition)
            }
            mCurrentPosition = position

            mVquCurrentOptions = item
        }
    }

    private fun setEarningData(it: BillVquEarningData) {
        mVquBillVquEarningData = it
        //提现说明
        mBinding.tvWithdrawDesc.text = it.des
        mAdapter.setNewInstance(it.options)
        if (!it.options.isNullOrEmpty() && it.options[0].selected) {
            mCurrentPosition = 0
            mVquCurrentOptions = it.options[0]
            mBinding.stvWithdrawBtn.setStartColor(
                ResUtils.getColor(R.color.color_6BBFFD),
                ResUtils.getColor(R.color.color_4CB6FF)
            )
        }

        mAlipayAccount = it.alipayAccount ?: ""
        mAlipayType = it.cardType
        if (mAlipayAccount.isNotEmpty()) {
            mBinding.tvPersonalAccount.text = it.alipayName + "(" + mAlipayAccount + ")"
        }

        if (!it.bank.isNullOrEmpty()) {
            mBinding.ivBankIcon.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + it.icon)
            mBinding.tvBankName.text = it.bank + "账号"
        }

    }

    /**
     * 请求提现
     */
    private fun postWithdraw() {

        if (mAlipayAccount.isEmpty()) {
            toast("请绑定提现账号")
            return
        }

        if (UserManager.userInfo?.isAuth != 1) {
            showAuthDialog()
            return
        }

        val options = mVquCurrentOptions
        if (options == null) {
            toast(R.string.mine_vqu_earning_withdrawal_tips)
            return
        }

        //实际兑换价格信息
        mViewModel.vquGetWithdrawPrice(options.money)

    }

    /**
     * 显示实名认证弹窗
     */
    private fun showAuthDialog() {
        val dialog = MessageDialog()
        dialog.setContent(R.string.vqu_bill_withdrawal_auth_tips)
        dialog.setRightText(R.string.common_vqu_go_to_auth)
        dialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {
                ARouter.getInstance().build(RouteUrl.Auth.AuthVquRealAuthActivity).navigation()
                return false
            }
        })
        activity?.supportFragmentManager?.let { dialog.show(it, "") }
    }

    private fun jumpBindAccountPage() {
        if(mAlipayType == null){
            ToastUtils.showShort("不能提现")
            return
        }
        if (UserManager.userInfo?.isAuth != 1) {
            showAuthDialog()
        } else {
            if (mAlipayAccount.isEmpty()) {
                ARouter.getInstance().build(RouteUrl.Bill.BillTantaNewWithdrawalAccountActivity)
                    .withInt(RouteKey.TYPE, 0)
                    .withInt(RouteKey.IS_MASTER, 1)
                    .navigation()
            } else {
                ARouter.getInstance().build(RouteUrl.Bill.BillTantaAccountListActivity)
                    .withInt(RouteKey.TYPE, 0)
                    .navigation()
            }
        }
    }

    private fun observeWithdrawPriceData(it: BillVquWithdrawPriceBean) {
        showWithdrawDialog(it)
    }

    /**
     * 实际到账
     */
    private fun showWithdrawDialog(bean: BillVquWithdrawPriceBean) {
        val messageDialog = MessageDialog()
        messageDialog.setIsHtml(true)
        messageDialog.setTitle(R.string.vqu_bill_apply_withdrawal)

        val withdrawText = getString(
            R.string.vqu_bill_withdrawal_text,
            bean.withdrawMoney,
            bean.receiveMoney,
            bean.account,
            bean.accountName
        )
        messageDialog.setContent(com.mshy.VInterestSpeed.common.utils.TextUtils.getSpannedHtml(withdrawText))
        messageDialog.setContentGravity(Gravity.CENTER)

        messageDialog.setRightButtonColor(
            ResUtils.getColor(R.color.color_6BBFFD),
            ResUtils.getColor(R.color.color_4CB6FF)
        )

        messageDialog.setRightText(R.string.common_vqu_submit)

        messageDialog.setOnButtonClickListener(object : MessageDialog.OnButtonClickListener {
            override fun onLeftClick(): Boolean {
                return false
            }

            override fun onRightClick(): Boolean {
                val options = mVquCurrentOptions
                if (options == null) {
                    toast(R.string.mine_vqu_earning_withdrawal_tips)
                    return false
                }

                mViewModel.vquUserWithDraw(options.money)
                RiskControlUtil.getToken(activity,2)
                return false
            }

        })
        activity?.supportFragmentManager?.let { messageDialog.show(it, "") }
    }

    /**
     * 提现到达上限
     */
    private fun showMothLimitDialog() {
        val dialog = TopDownButtonMessageDialog()
        dialog.setTopText("使用其他支付宝")
        dialog.setTitle("提现到达上限")
        dialog.setContent("支付宝账号：${mAlipayAccount}。到达每月提现上限，请使用其他支付宝。\n（下个月1号可以继续使用）")
        dialog.setOnButtonClickListener(object : TopDownButtonMessageDialog.OnButtonClickListener {
            override fun onTopClick(): Boolean {
                ARouter.getInstance().build(RouteUrl.Bill.BillTantaAccountListActivity)
                    .withInt(RouteKey.TYPE, 1)
                    .navigation()
                return false
            }

            override fun onDownClick(): Boolean {
                return false
            }
        })
        activity?.supportFragmentManager?.let { dialog.show(it, "") }
//        dialog.show(supportFragmentManager, "")
    }
}