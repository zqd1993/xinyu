package com.mshy.VInterestSpeed.common.ui.dialog

import android.view.View
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.CommonVquRedPacketBean
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogRedPacketOpenBinding
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.utils.TypefaceUtil

/**
 * author: Tany
 * date: 2022/5/2
 * description:红包弹框
 */
class CommonVquRedPacketOpenDialog : BaseDialogFragment<CommonVquDialogRedPacketOpenBinding>(),
    View.OnClickListener {
    var globalApiService: GlobalApiService? = null
    private var mData: CommonVquRedPacketBean? = null
    override fun CommonVquDialogRedPacketOpenBinding.initView() {
        TypefaceUtil.setFont1(tvPrice)
        tvPrice.text = mData?.price
        tvY.text = mData?.company
        ivClose.setOnClickListener(this@CommonVquRedPacketOpenDialog)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_close -> {
                dismiss()
            }
        }
    }

    fun setData(data: CommonVquRedPacketBean?) {
        mData = data
    }
}