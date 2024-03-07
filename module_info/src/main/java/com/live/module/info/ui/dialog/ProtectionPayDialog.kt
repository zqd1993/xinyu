package com.live.module.info.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.launcher.ARouter
import com.live.module.info.adapter.ProtectionOptionAdapter
import com.live.module.info.databinding.DialogProtectionPayBinding
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.ProtectionOptionBean
import com.mshy.VInterestSpeed.common.bean.ProtectionStatusBean
import com.mshy.VInterestSpeed.common.bean.TantaPayBean
import com.mshy.VInterestSpeed.common.bean.pay.BillPaymentData
import com.mshy.VInterestSpeed.common.bean.pay.PayOrderInfoBean
import com.mshy.VInterestSpeed.common.bean.pay.RechargeRoute
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.vquLoadCircleImage
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.ui.adapter.PaymentAdapter
import com.mshy.VInterestSpeed.common.ui.dialog.PayDialog
import com.mshy.VInterestSpeed.common.utils.PayUtils
import com.mshy.VInterestSpeed.common.utils.UmUtils
import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProtectionPayDialog : BaseDialogFragment<DialogProtectionPayBinding>(),
    View.OnClickListener {

    var myActivity: AppCompatActivity? = null
    var globalApiService =
        GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
    var id: Int? = 0

    private val mAdapter = ProtectionOptionAdapter()

    private var mSelectedPosition = 0

    private var mPayType = ""

    private var billPaymentDataList: MutableList<BillPaymentData> = mutableListOf()

    private var wechatRechargeRoute: MutableList<RechargeRoute> = mutableListOf()

    private var aliRechargeRoute: MutableList<RechargeRoute> = mutableListOf()

    private val mPaymentAdapter = PaymentAdapter()

    private val mAliPaymentAdapter = PaymentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myActivity = activity as AppCompatActivity?
    }

    override fun DialogProtectionPayBinding.initView() {
        mBinding.protectionBg.setOnClickListener { LogUtil.i("e", "") }
        mBinding.protectionTopBg.setOnClickListener { LogUtil.i("e", "") }
        initDialogData()
        mBinding.protectionPayStv.setViewClickListener {
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
            LogUtil.i("payconfig", "paytype$mPayType")
            when (mPayType) {
                "alipay" -> {
                    recharge(mPayType, item.id.toString(), id!!)
                }

                "sand_alipay" -> {
                    sandRecharge(
                        "sand_alipay",
                        item!!.id,
                        -1,
                        getScheme(), id!!
                    )
                }

                "wechat" -> {
                    recharge(mPayType, item.id.toString(), id!!)
                }

                "sand_wechat" -> {
                    sandRecharge(
                        "sand_wechat",
                        item!!.id,
                        -1,
                        getScheme(), id!!
                    )
                }

                "LeshuaPay" -> {
                    recharge(mPayType, item.id.toString(), id!!)
                }

                "wechat_h5_pay", "TaishanPay" -> {
                    recharge(mPayType, item.id.toString(), id!!)
                }
            }
        }
        (mBinding.protectionOptionList.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        mBinding.protectionOptionList.adapter = mAdapter
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
        }

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
        paymentAdapter(mBinding.rvWechatPay, mPaymentAdapter)
        paymentAdapter(mBinding.rvAliPay, mAliPaymentAdapter)

        mBinding.whatProtectionTv.setOnClickListener {
            ARouter.getInstance()
                .build(RouteUrl.Info.ProtectionDetailsActivity)
                .withInt(
                    RouteKey.USERID,
                    id!!
                )
                .navigation()
        }
    }

    override fun onClick(v: View?) {

    }

    private fun initDialogData() {
        id = arguments?.getInt("userId")
        globalApiService.vquGuardianStatus(id!!)
            .enqueue(object : Callback<BaseResponse<ProtectionStatusBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<ProtectionStatusBean>>,
                    response: Response<BaseResponse<ProtectionStatusBean>>,
                ) {
                    if (response.body()?.code == 0) {
                        if (response.body()?.data != null) {
                            response.body()?.data?.let {
                                mBinding.ivLeft.vquLoadCircleImage(it.manUrl)
                                mBinding.ivRight.vquLoadCircleImage(it.womanUrl)
                                mBinding.tvProtectionDay.text =
                                    "当前守护天数为${it.accumulateDay}天，守护后，自动累计天数"
                                if (it.guardianOptionsSnapshotList != null) {
                                    mBinding.protectionLevelIm.vquLoadImage(it.guardianOptionsSnapshotList.icon)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ProtectionStatusBean>>,
                    t: Throwable,
                ) {
                }
            })
        globalApiService.vquGetGuardianOptions(3)
            .enqueue(object : Callback<BaseResponse<MutableList<ProtectionOptionBean>>> {
                override fun onResponse(
                    call: Call<BaseResponse<MutableList<ProtectionOptionBean>>>,
                    response: Response<BaseResponse<MutableList<ProtectionOptionBean>>>,
                ) {
                    if (response.body()?.code == 0) {
                        if (response.body()?.data != null) {
                            if(response.body()?.data!!.size > 0){
                                response.body()?.data!![0].isSelected = true
                            }
                            mAdapter.setNewInstance(response.body()?.data)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<MutableList<ProtectionOptionBean>>>,
                    t: Throwable,
                ) {
                }
            })
        globalApiService.vquGetProtectionPayConfig()
            .enqueue(object : Callback<BaseResponse<MutableList<BillPaymentData>>> {
                override fun onResponse(
                    call: Call<BaseResponse<MutableList<BillPaymentData>>>,
                    response: Response<BaseResponse<MutableList<BillPaymentData>>>,
                ) {
                    if (response.body()?.code == 0) {
                        if (response.body()?.data != null) {
                            response.body()?.data?.let {
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
                                                    mPaymentAdapter.setNewInstance(
                                                        wechatRechargeRoute
                                                    )
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
                                                    mAliPaymentAdapter.setNewInstance(
                                                        aliRechargeRoute
                                                    )
                                                    mBinding.cbTantaAlipay.visibility = View.GONE
                                                } else {
                                                    mBinding.cbTantaWechat.visibility = View.VISIBLE
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<MutableList<BillPaymentData>>>,
                    t: Throwable,
                ) {
                }
            })
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

    private fun recharge(payType: String, goodsId: String, toUserId: Int) {
        globalApiService.vquProtectionRecharge(payType, goodsId, -1, toUserId, 2)
            .enqueue(object : Callback<BaseResponse<TantaPayBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<TantaPayBean>>,
                    response: Response<BaseResponse<TantaPayBean>>,
                ) {
                    if (response.body()?.code == 0) {
                        if (response.body()?.data != null) {
                            response.body()?.data?.let {
                                if (mPayType == PayDialog.WECHAT) {
                                    PayUtils.wechatPay(requireActivity(), it)
                                } else if (mPayType == PayDialog.LE_SHUA_PAY || mPayType == PayDialog.WECHAT_H5_PAY) {
                                    if (it.payUrl.isNotEmpty()) {
                                        try {
                                            val intent = Intent()
                                            intent.action = Intent.ACTION_VIEW
                                            val targetUrl = Uri.parse(it.payUrl)
                                            intent.data = targetUrl
                                            startActivity(intent)
                                        } catch (e: Exception) {

                                        }
                                    }
                                } else if (mPayType == PayDialog.ALIPAY) {
                                    PayUtils.aliPay(requireActivity(), it.payinfo)
                                }
                            }
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<TantaPayBean>>,
                    t: Throwable,
                ) {
                }
            })
    }

    private fun sandRecharge(
        channel: String,
        goodsId: Int,
        polling: Int,
        scheme: String,
        toUserId: Int
    ) {
        globalApiService.vquSandProtectionRecharge(channel, goodsId, polling, scheme, toUserId, 2)
            .enqueue(object : Callback<BaseResponse<PayOrderInfoBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<PayOrderInfoBean>>,
                    response: Response<BaseResponse<PayOrderInfoBean>>,
                ) {
                    if (response.body()?.code == 0) {
                        if (response.body()?.data != null) {
                            when (mPayType) {
                                "sand_wechat" -> {
                                    PayUtils.sendWechat(requireActivity(), response.body()?.data!!)
                                }

                                "sand_alipay" -> {
                                    PayUtils.sendWechat(requireActivity(), response.body()?.data!!)
                                }
                            }
                            dismiss()
                        }
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<PayOrderInfoBean>>,
                    t: Throwable,
                ) {
                }
            })
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
}