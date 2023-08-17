package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.alibaba.android.arouter.launcher.ARouter
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonCallPriceBean
import com.mshy.VInterestSpeed.common.constant.RouteKey
import com.mshy.VInterestSpeed.common.constant.RouteUrl
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogCallBinding
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.utils.ResUtils
import com.mshy.VInterestSpeed.common.utils.UserManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment

class CommonVquCallDialog : BaseDialogFragment<CommonVquDialogCallBinding>(),
    View.OnClickListener {


    var myActivity: AppCompatActivity? = null
    var globalApiService =
        GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
    var id: String? = ""

    var mListener: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myActivity = activity as AppCompatActivity?
    }

    private fun initDialogData() {
        id = arguments?.getString("anchor_id", "")
        globalApiService.vquGetPrice(id!!, 1)
            .enqueue(object : Callback<BaseResponse<CommonCallPriceBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<CommonCallPriceBean>>,
                    response: Response<BaseResponse<CommonCallPriceBean>>,
                ) {
                    if (response.body()?.code == 0) {
                        if (response.body()?.data?.isLock == 1) {
                            mBinding.llVideoPrice.gone()
                            mBinding.llVoicePrice.gone()
                            mBinding.ivVideo.setImageResource(R.mipmap.ic_tanta_common_call_lock)
                            mBinding.ivVoice.setImageResource(R.mipmap.ic_tanta_common_call_lock)
                        } else {
                            if (response.body()?.data?.videoPrice.isNullOrEmpty()) {
                                mBinding.tvVideoPrice.gone()
                                mBinding.llVideoPrice.gone()
                            } else {
                                mBinding.tvVideoPrice.text = response.body()?.data?.videoPrice
                                mBinding.tvVideoPrice.visible()
                                mBinding.llVideoPrice.visible()
                            }
                            if (response.body()?.data?.voicePrice.isNullOrEmpty()) {
                                mBinding.tvVoicePrice.gone()
                                mBinding.llVoicePrice.gone()
                            } else {
                                mBinding.tvVoicePrice.text = response.body()?.data?.voicePrice
                                mBinding.tvVoicePrice.visible()
                                mBinding.llVoicePrice.visible()
                            }
                            //免费一分钟
                            if (response.body()?.data?.videoFree.isNullOrEmpty()) {
                                mBinding.tvFreeVideo.gone()
                            } else {
                                mBinding.tvFreeVideo.text = response.body()?.data?.videoFree
                                mBinding.tvFreeVideo.visible()
                                mBinding.llVideoPrice.visible()
                            }
                            if (response.body()?.data?.voiceFree.isNullOrEmpty()) {
                                mBinding.tvFreeVoice.gone()
                            } else {
                                mBinding.tvFreeVoice.text = response.body()?.data?.voiceFree
                                mBinding.tvFreeVoice.visible()
                                mBinding.llVoicePrice.visible()
                            }
                            if (UserManager.userInfo?.gender == 1) {
                                mBinding.tvVideoVip.gone()
                                mBinding.tvVideoVip.gone()
                            } else {
                                //vip折扣
                                if (response.body()?.data?.videoVip.isNullOrEmpty()) {
                                    mBinding.tvVideoVip.gone()
                                } else {
                                    mBinding.tvVideoVip.text = response.body()?.data?.videoVip
                                    mBinding.tvVideoVip.visible()
                                    if (response.body()?.data?.vip == 0) {
                                        mBinding.tvVideoVip.setStartColor(
                                            ResUtils.getColor(R.color.color_FF7AC2),
                                            ResUtils.getColor(R.color.color_FF7AC2)
                                        )
                                        mBinding.tvVideoVip.setTextColor(ResUtils.getColor(R.color.white))
                                    } else {
                                        mBinding.tvVideoVip.setStartColor(
                                            ResUtils.getColor(R.color.color_FADCC0),
                                            ResUtils.getColor(R.color.color_EEB090)
                                        )
                                        mBinding.tvVideoVip.setTextColor(ResUtils.getColor(R.color.color_652906))
                                    }


                                }
                                if (response.body()?.data?.voiceVip.isNullOrEmpty()) {
                                    mBinding.tvVideoVip.gone()
                                } else {
                                    mBinding.tvVoiceVip.text = response.body()?.data?.voiceVip
                                    mBinding.tvVoiceVip.visible()
                                    if (response.body()?.data?.vip == 0) {
                                        mBinding.tvVoiceVip.setStartColor(
                                            ResUtils.getColor(R.color.color_FF7AC2),
                                            ResUtils.getColor(R.color.color_FF7AC2)
                                        )
                                        mBinding.tvVoiceVip.setTextColor(ResUtils.getColor(R.color.white))
                                    } else {
                                        mBinding.tvVoiceVip.setStartColor(
                                            ResUtils.getColor(R.color.color_FADCC0),
                                            ResUtils.getColor(R.color.color_EEB090)
                                        )
                                        mBinding.tvVoiceVip.setTextColor(ResUtils.getColor(R.color.color_652906))
                                    }

                                }
                            }

                        }

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<CommonCallPriceBean>>,
                    t: Throwable,
                ) {
                }
            })

        mBinding.llVideo.setViewClickListener {
            vquCall(0)
            com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(myActivity, com.mshy.VInterestSpeed.common.utils.UmUtils.STARTVIDEO)
            this@CommonVquCallDialog.dismiss()
        }
        mBinding.llVoice.setViewClickListener {
            vquCall(1)
            com.mshy.VInterestSpeed.common.utils.UmUtils.setUmEvent(myActivity, com.mshy.VInterestSpeed.common.utils.UmUtils.STARTPHONE)
            this@CommonVquCallDialog.dismiss()
        }
    }

    private fun vquCall(type: Int) {
        globalApiService.vquGetCallInfo(id!!, type)
            .enqueue(object : Callback<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>> {
                override fun onResponse(
                    call: Call<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>>,
                    response: Response<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>>,
                ) {
                    when (response.body()?.code) {
                        0, -9999001 -> {//成功
                            if (type == 0) {
                                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                    .withString(
                                        RouteKey.SOCKET_URL,
                                        response.body()?.data?.socket_url ?: ""
                                    )
                                    .withBoolean(RouteKey.IS_CALLER, true)
                                    .withInt(RouteKey.CODE, response.body()?.code ?: 0)
                                    .withParcelable(RouteKey.CALL_BEAN, response.body()?.data)
                                    .navigation()
                            } else {
                                ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                                    .withString(
                                        RouteKey.SOCKET_URL,
                                        response.body()?.data?.socket_url ?: ""
                                    )
                                    .withInt(RouteKey.CODE, response.body()?.code ?: 0)
                                    .withBoolean(RouteKey.IS_CALLER, true)
                                    .withParcelable(RouteKey.CALL_BEAN, response.body()?.data)
                                    .navigation()
                            }

                        }
                        1003, 1002 -> {
                            "余额不足，请先充值".toast()
                            mListener?.invoke()
                            //                            mPayViewModel.showRechargeDialog(myActivity!!.supportFragmentManager)
                            //                            CommonRechargeDialog().show(myActivity!!.supportFragmentManager, "充值")

                            //                            CommonHintDialog()
                            //                                .setTitle("提示")
                            //                                .setContent("您的金币数量不足，请先充值")
                            //                                .setLeftText("舍不得")
                            //                                .setRightText("马上充值")
                            //                                .setContentSize(15)
                            //                                .setContentGravity(Gravity.CENTER)
                            //                                .setOnClickListener(object : CommonHintDialog.OnClickListener {
                            //                                    override fun onLeft(dialogFragment: DialogFragment) {}
                            //                                    override fun onRight(dialogFragment: DialogFragment) {
                            //                                        ARouter.getInstance().build(RouteUrl.Bill.BillVquRechargeActivity)
                            //                                            .navigation();
                            //                                    }
                            //                                })
                            //                                .show(childFragmentManager)
                        }
                        1004 -> {
                            com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog()
                                .setContentSize(15)
                                .setContentGravity(Gravity.CENTER)
                                .setTitle("温馨提示")
                                .setContent(response.body()?.message)
                                .setLeftText("取消")
                                .setRightText("去聊天")
                                .setOnClickListener(object : com.mshy.VInterestSpeed.common.ui.dialog.CommonHintDialog.OnClickListener {
                                    override fun onLeft(dialogFragment: DialogFragment) {
                                        dialogFragment.dismissAllowingStateLoss()
                                    }

                                    override fun onRight(dialogFragment: DialogFragment) {
                                        if ("class com.live.module.message.ui.activity.MessageVquP2PActivity" == myActivity?.javaClass.toString()) {
                                            dialogFragment.dismissAllowingStateLoss()
                                        } else {
                                            com.mshy.VInterestSpeed.uikit.api.NimUIKit.startP2PSession(myActivity, id)
                                        }

                                    }
                                })
                                .show(myActivity!!.supportFragmentManager)
                        }
                        else -> {
                            response.body()?.message?.toast()
                        }

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean>>,
                    t: Throwable,
                ) {

                }
            })
    }

    override fun onClick(v: View?) {
    }


    override fun CommonVquDialogCallBinding.initView() {
        mBinding.tvCancel.setOnClickListener {
            dismiss()
        }
        initDialogData()
    }

    public fun setNoMoneyListener(listener: (() -> Unit)?) {
        mListener = listener
    }
}