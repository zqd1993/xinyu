package com.mshy.VInterestSpeed.common.ui.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.utils.*
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.PayResultEvent
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.RechargeRoute
import com.mshy.VInterestSpeed.common.databinding.CommonTantaDialogPaySelectionBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseVMDialogFragment
import com.mshy.VInterestSpeed.common.ui.adapter.CommonTantaRechargeAdapter
import com.mshy.VInterestSpeed.common.ui.adapter.PaymentAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog.Companion.ALIPAY
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog.Companion.WECHAT
import com.mshy.VInterestSpeed.common.ui.vm.CommonPayViewModel
import com.mshy.VInterestSpeed.common.utils.PayUtils
import com.mshy.VInterestSpeed.common.utils.UmUtils
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

    private var mPayType = ""

    private var mSelectedPosition = -1

    private var billPaymentDataList: MutableList<BillPaymentData> = mutableListOf()

    private var wechatRechargeRoute: MutableList<RechargeRoute> = mutableListOf()

    private var aliRechargeRoute: MutableList<RechargeRoute> = mutableListOf()

    private val mPaymentAdapter = PaymentAdapter()

    private val mAliPaymentAdapter = PaymentAdapter()

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

        com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(
            activity,
            com.mshy.VInterestSpeed.common.utils.UmUtils.RECHARGEPOPUP
        )

        (mBinding.rvSelectionList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        mBinding.rvSelectionList.adapter = mAdapter

        mBinding.ivVquPayFinish.clickDelay { dismiss() }

        mBinding.llTantaWechatPay.setViewClickListener(0) {
            if (wechatRechargeRoute.size == 1) {
                mPayType = wechatRechargeRoute[0].payCode
                mBinding.cbTantaWechat.isChecked = true
                mBinding.cbTantaAlipay.isChecked = false
                for (paymentData in billPaymentDataList) {
                    for (rechargeRoute in paymentData.rechargeRoute) {
                        rechargeRoute.checked = false
                    }
                }
                if (mPaymentAdapter.data.size > 0) {
                    mPaymentAdapter.notifyDataSetChanged()
                }
                if (mAliPaymentAdapter.data.size > 0) {
                    mAliPaymentAdapter.notifyDataSetChanged()
                }
            }
        }

        mBinding.vFirstRechargeBanner.setViewClickListener(0) {

            firstRechargeDialog.show(childFragmentManager, "")
        }

        mBinding.llTantaAlipayPay.setViewClickListener(0) {
            if (aliRechargeRoute.size == 1) {
                mPayType = aliRechargeRoute[0].payCode
                mBinding.cbTantaWechat.isChecked = false
                mBinding.cbTantaAlipay.isChecked = true
                for (paymentData in billPaymentDataList) {
                    for (rechargeRoute in paymentData.rechargeRoute) {
                        rechargeRoute.checked = false
                    }
                }
                if (mPaymentAdapter.data.size > 0) {
                    mPaymentAdapter.notifyDataSetChanged()
                }
                if (mAliPaymentAdapter.data.size > 0) {
                    mAliPaymentAdapter.notifyDataSetChanged()
                }
            }
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

            UmUtils.setUmEvent(
                requireActivity(),
                UmUtils.RECHARGECLICK
            )

            when (mPayType) {
                "alipay" -> {
                    mViewModel.recharge(mPayType, item.id.toString())
                }

                "sand_alipay" -> {
                    mViewModel.recharge(
                        "sand_alipay",
                        item!!.id,
                        -1,
                        getScheme()
                    )
                }

                "wechat" -> {
                    mViewModel.recharge(mPayType, item.id.toString())
                }

                "sand_wechat" -> {
                    mViewModel.recharge(
                        "sand_wechat",
                        item!!.id,
                        -1,
                        getScheme()
                    )
                }
            }

//            val vquPayDialog = PayDialog()
//            vquPayDialog.setPrice(item.price)
//            vquPayDialog.setRechargeID(item.id.toString())
//            vquPayDialog.show(childFragmentManager, "")
        }

        paymentAdapter(mBinding.rvWechatPay, mPaymentAdapter)
        paymentAdapter(mBinding.rvAliPay, mAliPaymentAdapter)
    }

    private fun getScheme(): String {
        var scheme = ""
        val activity = requireActivity()
        if (activity.localClassName.contains("MessageVquP2PActivity")) {
            scheme = "xinyu://xinyu.p2p.recharge"
        } else if (activity.localClassName.contains("AgoraTantaMatchActivity")) {
            scheme = "xinyu://xinyu.match.recharge"
        } else if (activity.localClassName.contains("CommonVquMainActivity")) {
            scheme = "xinyu://xinyu.main.recharge"
        } else if (activity.localClassName.contains("InfoVquPersonalInfoActivity")) {
            scheme = "xinyu://xinyu.personal.info.recharge"
        } else if (activity.localClassName.contains("HomeSearchActivity")) {
            scheme = "xinyu://xinyu.home.search.recharge"
        } else if (activity.localClassName.contains("AgoraTantaVideo2Activity")) {
            scheme = "xinyu://xinyu.match.video.recharge"
        } else if (activity.localClassName.contains("AgoraTantaAudio2Activity")) {
            scheme = "xinyu://xinyu.match.audio.recharge"
        } else if (activity.localClassName.contains("BillTantaRechargeActivity")) {
            scheme = "xinyu://xinyu.recharge"
        } else if (activity.localClassName.contains("DynamicTantaDynamicListActivity")) {
            scheme = "xinyu://xinyu.dynamic.list.recharge"
        } else if (activity.localClassName.contains("DynamicTantaDynamicDetailActivity")) {
            scheme = "xinyu://xinyu.dynamic.detail.recharge"
        } else if (activity.localClassName.contains("RelationVquActivity")) {
            scheme = "xinyu://xinyu.relation.recharge"
        } else if (activity.localClassName.contains("VipTantaCenterActivity")) {
            scheme = "xinyu://xinyu.vip"
        } else {
            scheme = ""
        }
        return scheme
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

            } else {
                mBinding.vFirstRechargeBanner.visibility = View.GONE
            }
        }
        mViewModel.payConfigData.observe(this) {
            billPaymentDataList.clear()
            billPaymentDataList.addAll(it)
            var isChecked = false
            if (it.isNotEmpty()) {
                for (paymentData in it) {
                    if (mPayType.isNotEmpty()) {
                        for (rechargeRoute in paymentData.rechargeRoute) {
                            if (mPayType == rechargeRoute.payCode) {
                                rechargeRoute.checked = true
                            }
                        }
                    } else {
                        if (!isChecked && paymentData.rechargeRoute.size > 0) {
                            paymentData.rechargeRoute[0].checked = true
                            mPayType = paymentData.rechargeRoute[0].payCode
                            isChecked = true
                        }
                    }
                    if (paymentData.payType == "wechat") {
                        wechatRechargeRoute = paymentData.rechargeRoute
                        if (wechatRechargeRoute.size > 0) {
                            mBinding.llTantaWechatPay.visibility = View.VISIBLE
                            if (wechatRechargeRoute.size > 1) {
                                mPaymentAdapter.setNewInstance(wechatRechargeRoute)
                                mBinding.cbTantaWechat.visibility = View.GONE
                            } else {
                                mBinding.cbTantaWechat.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        aliRechargeRoute = paymentData.rechargeRoute
                        if (aliRechargeRoute.size > 0) {
                            mBinding.llTantaAlipayPay.visibility = View.VISIBLE
                            if (aliRechargeRoute.size > 1) {
                                mAliPaymentAdapter.setNewInstance(aliRechargeRoute)
                                mBinding.cbTantaAlipay.visibility = View.GONE
                            } else {
                                mBinding.cbTantaWechat.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
        mViewModel.orderJson.observe(this) {
            if (it != null) {
                when (mPayType) {
                    "sand_wechat" -> {
                        PayUtils.sendWechat(requireActivity(), it)
                    }

                    "sand_alipay" -> {
                        PayUtils.sendWechat(requireActivity(), it)
                    }
                }
            }
            dismiss()
        }
    }

    private fun paymentAdapter(
        recyclerView: RecyclerView,
        adapter: PaymentAdapter,
    ) {
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, _, position ->

            val item = adapter.getItemOrNull(position) ?: return@setOnItemClickListener
            mPayType = item.payCode
            for (paymentData in billPaymentDataList) {
                for (rechargeRoute in paymentData.rechargeRoute) {
                    rechargeRoute.checked = rechargeRoute.payCode == item.payCode
                }
            }
            if (mPayType.contains("wechat")) {
                mBinding.cbTantaAlipay.isChecked = false
            } else {
                mBinding.cbTantaWechat.isChecked = false
            }
            mPaymentAdapter.notifyDataSetChanged()
            mAliPaymentAdapter.notifyDataSetChanged()
        }
    }

    override fun initRequestData() {

        mViewModel.vquGoodsList(2)
        mViewModel.getFirstRechargeInfo()
        mViewModel.vquWalletIndex()
        mViewModel.payConfig()
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