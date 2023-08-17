package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.databinding.CommonTantaDialogPaySelectionBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseVMDialogFragment
import com.mshy.VInterestSpeed.common.ui.adapter.CommonTantaRechargeAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog.Companion.ALIPAY
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog.Companion.WECHAT
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.PayUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author: Lau
 * date: 2022/4/28
 * description:充值弹框
 */
@EventBusRegister
class CommonRechargeDialog :
    BaseVMDialogFragment<CommonTantaDialogPaySelectionBinding, CommonPayViewModel>() {
    override val mViewModel: CommonPayViewModel by viewModels()

    private val mAdapter = CommonTantaRechargeAdapter()

    private var mPayType = WECHAT

    private var mSelectedPosition = -1

    private val loadingDialog by lazy {
        com.mshy.VInterestSpeed.common.ui.dialog.LoadingDialog(requireActivity(), "支付请求中...")
    }

    private val firstRechargeDialog by lazy {
        CommonFirstRechargeDialog().apply {
            onVquRechargeClickListener {
                dismiss()
                return@onVquRechargeClickListener true
            }
        }
    }
    override fun CommonTantaDialogPaySelectionBinding.initView() {

        com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(activity, com.mshy.VInterestSpeed.common.utils.UmUtils.RECHARGEPOPUP)

        (mBinding.rvSelectionList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        mBinding.rvSelectionList.adapter = mAdapter

        mBinding.ivVquPayFinish.clickDelay { dismiss() }

        mBinding.llTantaWechatPay.setViewClickListener(0) {
            mPayType = WECHAT
            mBinding.cbTantaWechat.isChecked = true
            mBinding.cbTantaAlipay.isChecked = false
        }

        mBinding.vFirstRechargeBanner.setViewClickListener(0) {

            firstRechargeDialog.show(childFragmentManager, "")
        }

        mBinding.llTantaAlipayPay.setViewClickListener(0) {
            mPayType = ALIPAY
            mBinding.cbTantaWechat.isChecked = false
            mBinding.cbTantaAlipay.isChecked = true
        }

        mAdapter.setOnItemClickListener { _, _, position ->

            val item = mAdapter.getItemOrNull(position) ?: return@setOnItemClickListener
            if (position != mSelectedPosition) {
                item.isSelected = true
                mAdapter.notifyItemChanged(position)

                val lastItem = mAdapter.getItemOrNull(mSelectedPosition)
                if (lastItem != null) {
                    lastItem.isSelected = false
                    mAdapter.notifyItemChanged(mSelectedPosition)
                }
            }

            mSelectedPosition = position

//            val lastItem = mAdapter.getItemOrNull(mSelectedPosition)
//            if (lastItem != null) {
//                lastItem.isSelected = false
//            }
//
//            if (mSelectedPosition == position) {
//                mSelectedPosition = -1
//            } else {
//                val item = mAdapter.getItemOrNull(position)
//                item?.isSelected = true
//                mSelectedPosition = position
//            }
//            mAdapter.notifyDataSetChanged()
        }

        mBinding.tvPay.setViewClickListener {
            if (mSelectedPosition < 0) {
                toast(R.string.common_vqu_select_recharge_tips)
                return@setViewClickListener
            }

            val item = mAdapter.getItemOrNull(mSelectedPosition)
            if (item == null) {
                toast(R.string.common_vqu_select_recharge_data_error_tips)
                return@setViewClickListener
            }

            if (mPayType.isEmpty()) {
                toast(R.string.common_vqu_select_pay_way_tips)
                return@setViewClickListener
            }

            com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
                requireActivity(),
                com.mshy.VInterestSpeed.common.utils.UmUtils.RECHARGECLICK
            )

            mViewModel.recharge(mPayType, item.id.toString())

//            val vquPayDialog = PayDialog()
//            vquPayDialog.setPrice(item.price)
//            vquPayDialog.setRechargeID(item.id.toString())
//            vquPayDialog.show(childFragmentManager, "")
        }
    }

    override fun initObserve() {
        mViewModel.rechargeData.observe(this) {
            mAdapter.setNewInstance(it.list)
        }

        mViewModel.defaultPositionData.observe(this) {
            mSelectedPosition = it
        }

        mViewModel.walletData.observe(this) {
            mBinding.tvCommonVquPaySelectionDiamonds.text = "我的金币：${it.account.coin}"
        }

        mViewModel.payData.observe(this) {
            if (mPayType == PayDialog.WECHAT) {
                PayUtils.wechatPay(requireActivity(), it)
            } else if (mPayType == PayDialog.ALIPAY) {
                PayUtils.aliPay(requireActivity(), it.payinfo)
            }
        }

        mViewModel.stateViewLD.observe(this) {
            when (it) {
                StateLayoutEnum.LOADING -> loadingDialog.show()
                else -> loadingDialog.dismiss()
            }
        }
        //首冲弹框
        mViewModel.firstRechargeResultData.observe(this) {
            var mVquIsActivity = it.isActivity ?: false
            //没有首冲
            if (!mVquIsActivity) {
                mBinding.vFirstRechargeBanner.visibility = View.VISIBLE
//                val lastTime = SpUtils.getLong(SpKey.SHOWED_FIRST_RECHARGE_DIALOG_TIME) ?: 0L
//
//                val time = System.currentTimeMillis()
//
//                val lastTimeDate = DateUtils.getDateFormatString(lastTime, "yyyy-MM-dd")
//
//                val timeDate = DateUtils.getDateFormatString(time, "yyyy-MM-dd")
//
//                if (lastTimeDate == timeDate) {
//                    return@observe
//                }
//                SpUtils.putLong(SpKey.SHOWED_FIRST_RECHARGE_DIALOG_TIME, time)
                firstRechargeDialog.setList(it.list)

            }else{
                mBinding.vFirstRechargeBanner.visibility = View.GONE
            }
        }

    }

    override fun initRequestData() {

        mViewModel.vquGoodsList(2)
        mViewModel.getFirstRechargeInfo()
        mViewModel.vquWalletIndex()
    }

    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDestroy() {
        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.ExitMatchEvent())
        super.onDestroy()

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRechargeDialog(payResult: PayResultEvent) {
        if (payResult.isSuccess) {
            dismiss()
//            mViewModel.vquWalletIndex()
        }
    }
}