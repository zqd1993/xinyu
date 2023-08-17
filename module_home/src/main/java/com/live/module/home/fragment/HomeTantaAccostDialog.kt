package com.live.module.home.fragment

import android.view.View
import androidx.fragment.app.DialogFragment
import com.live.module.home.databinding.HomeTantaAccostDialogBinding
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment

/**
 * author: Tany
 * date: 2022/7/4
 * description:挽留弹框
 */
class HomeTantaAccostDialog : BaseDialogFragment<HomeTantaAccostDialogBinding>(),
    View.OnClickListener {
    private var mClickListener: OnClickListener? = null
    var myContent:String=""

    override fun HomeTantaAccostDialogBinding.initView() {
        mBinding.tvHomeContent.text=myContent
        mBinding.tvLeft.setOnClickListener {
            mClickListener?.onLeft(this@HomeTantaAccostDialog, mBinding.tvSelect.isSelected)
            dismiss()
        }
        mBinding.tvRight.setOnClickListener {
            mClickListener?.onRight(this@HomeTantaAccostDialog, mBinding.tvSelect.isSelected)
            dismiss()
        }
        mBinding.tvSelect.setOnClickListener {
            mBinding.tvSelect.isSelected = !mBinding.tvSelect.isSelected
        }
    }
    fun setContent(str:String ){
        myContent=str
    }
    fun setOnClickListener(clickListener: OnClickListener) {
        this.mClickListener = clickListener
    }

    override fun onClick(v: View?) {
    }

    interface OnClickListener {
        fun onLeft(dialogFragment: DialogFragment?, isCheck: Boolean)
        fun onRight(dialogFragment: DialogFragment?, isCheck: Boolean)
    }

}