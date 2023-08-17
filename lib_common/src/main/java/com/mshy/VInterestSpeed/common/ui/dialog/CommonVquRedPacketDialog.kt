package com.mshy.VInterestSpeed.common.ui.dialog

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquRedPacketBean
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogRedPacketBinding
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author: Tany
 * date: 2022/5/2
 * description:红包弹框
 */
class CommonVquRedPacketDialog : BaseDialogFragment<CommonVquDialogRedPacketBinding>(),
    View.OnClickListener {
    var myActivity: AppCompatActivity?=null
    var globalApiService: GlobalApiService? = null
    private var mData: CommonVquRedPacketBean? = null
    override fun CommonVquDialogRedPacketBinding.initView() {
        tvTitle.text = mData?.title
        ivClose.setOnClickListener(this@CommonVquRedPacketDialog)
        ivOpen.setOnClickListener(this@CommonVquRedPacketDialog)
        myActivity= activity as AppCompatActivity?

        com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("CommonVquDialogRedPacketBinding")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }
            R.id.iv_open -> {//调用开红包接口
                globalApiService =
                    GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
                if (mData?.type == 1) {
                    globalApiService?.vquReceiveTaskReward(mData?.task_id!!)
                        ?.enqueue(object : Callback<BaseResponse<CommonVquRedPacketBean>> {
                            override fun onResponse(
                                call: Call<BaseResponse<CommonVquRedPacketBean>>,
                                response: Response<BaseResponse<CommonVquRedPacketBean>>,
                            ) {
                                if(response==null){
                                    return
                                }
                                val dialog = CommonVquRedPacketOpenDialog()
                                dialog.setData(response.body()!!.data)
                                dialog.show(myActivity!!.supportFragmentManager, "red")
                                dismiss()
                            }

                            override fun onFailure(
                                call: Call<BaseResponse<CommonVquRedPacketBean>>,
                                t: Throwable,
                            ) {
                            }
                        })
                } else if (mData?.type == 2) {
                    globalApiService?.vquReceiveTaskRewardDay(mData?.task_id!!)
                        ?.enqueue(object : Callback<BaseResponse<CommonVquRedPacketBean>> {
                            override fun onResponse(
                                call: Call<BaseResponse<CommonVquRedPacketBean>>,
                                response: Response<BaseResponse<CommonVquRedPacketBean>>,
                            ) {
                                if (response.body() == null) {
                                    "领取失败".toast()
                                    return
                                }
                                if (response.body() != null && response.body()!!.code == 0) {
                                    var dialog = CommonVquRedPacketOpenDialog()
                                    dialog.setData(response.body()!!.data)
                                    dialog.show(myActivity!!.supportFragmentManager, "red")
                                    dismiss()
                                } else {
                                    response.body()!!.message.toast()
                                }
                            }

                            override fun onFailure(
                                call: Call<BaseResponse<CommonVquRedPacketBean>>,
                                t: Throwable,
                            ) {
                            }
                        })
                }


            }
        }
    }

    fun setData(data: CommonVquRedPacketBean) {
        mData = data
    }
}