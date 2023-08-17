package com.mshy.VInterestSpeed.common.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.bean.CommonVquShareBean
import com.mshy.VInterestSpeed.common.databinding.CommonVquDialogShareBinding
import com.mshy.VInterestSpeed.common.ext.setNbOnItemClickListener
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquShareAdapter

class CommonVquShareDialog : BaseDialogFragment<CommonVquDialogShareBinding>(),
    View.OnClickListener {
    var myShareListener: OnShareListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun initDialogData() {
        var imgIds = arrayOf(
//            R.mipmap.ic_vqu_common_share_img,
            R.mipmap.ic_vqu_common_share_wechat,
            R.mipmap.ic_vqu_common_share_wx,
            R.mipmap.ic_vqu_common_share_qq,
            R.mipmap.ic_vqu_common_share_qqzone
//            R.mipmap.ic_vqu_common_share_copy
        )
        var strs = arrayOf(
//            R.string.common_vqu_share_img,
            R.string.common_vqu_share_wx,
            R.string.common_vqu_share_pyq,
            R.string.common_vqu_share_qq,
            R.string.common_vqu_share_qqzone
//            R.string.common_vqu_share_copy
        )

        var shareBeanList = mutableListOf<CommonVquShareBean>()
        for (index in imgIds.indices) {
            shareBeanList.add(CommonVquShareBean(imgIds[index], strs[index]))
        }
        var shareAdapter = CommonVquShareAdapter(shareBeanList)
        mBinding.rvShare.adapter = shareAdapter
        shareAdapter.setNbOnItemClickListener { adapter, view, position ->
            when (shareBeanList[position].titleId) {
                R.string.common_vqu_share_img -> {
                    myShareListener?.createImg()
                    dismiss()
                }
                R.string.common_vqu_share_wx -> {
                    myShareListener?.shareWx()
                    dismiss()
                }
                R.string.common_vqu_share_pyq -> {
                    myShareListener?.sharePyq()
                    dismiss()
                }
                R.string.common_vqu_share_qq -> {
                    myShareListener?.shareQQ()
                    dismiss()
                }
                R.string.common_vqu_share_qqzone -> {
                    myShareListener?.shareQQZone()
                    dismiss()
                }
                R.string.common_vqu_share_copy -> {
                    myShareListener?.copy()
                    dismiss()
                }
            }

        }
    }

    override fun onClick(v: View?) {
    }

    fun setOnShareListener(listener: OnShareListener) {
        myShareListener = listener
    }

    interface OnShareListener {
        fun createImg()
        fun shareWx()
        fun sharePyq()
        fun shareQQ()
        fun shareQQZone()
        fun copy()
    }

    override fun CommonVquDialogShareBinding.initView() {
        mBinding.tvCancel.setOnClickListener {
            dismiss()
        }
        initDialogData()
    }

}