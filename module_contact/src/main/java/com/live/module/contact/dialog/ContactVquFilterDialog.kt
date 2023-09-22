package com.live.module.contact.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.live.module.contact.R
import com.live.module.contact.bean.ContactVquSortData
import com.live.module.contact.databinding.ContactVquDialogFilterBinding
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment


/**
 * author: Lau
 * date: 2022/6/24
 * description:
 */
class ContactVquFilterDialog : BaseDialogFragment<ContactVquDialogFilterBinding>(),
    View.OnClickListener {
    private var sortType: ContactVquSortData = ContactVquSortData(1, 1)
    private var mVquListener: ((type: ContactVquSortData) -> Unit)? = null

    override fun ContactVquDialogFilterBinding.initView() {
        mBinding.tvSexAll.setOnClickListener(this@ContactVquFilterDialog)
        mBinding.tvSexMan.setOnClickListener(this@ContactVquFilterDialog)
        mBinding.tvSexWoman.setOnClickListener(this@ContactVquFilterDialog)
        mBinding.tvSortValue.setOnClickListener(this@ContactVquFilterDialog)
        mBinding.tvSortRegister.setOnClickListener(this@ContactVquFilterDialog)
        mBinding.stvConfirm.setOnClickListener(this@ContactVquFilterDialog)
        mBinding.ivClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }


    override fun initWindow() {
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_sex_all -> {
                sexSelect(1)
            }
            R.id.tv_sex_man -> {
                sexSelect(2)
            }
            R.id.tv_sex_woman -> {
                sexSelect(3)
            }
            R.id.tv_sort_value -> {
                valueSelect(1)
            }
            R.id.tv_sort_register -> {
                valueSelect(2)
            }
            R.id.stv_confirm -> {
                com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil.e("ContactVquSortData11=${sortType.sort}...${sortType.sex}")
                mVquListener?.invoke(sortType)
                dismiss()
            }
        }
    }

    private fun sexSelect(sex: Int) {
        sortType.sex = sex
        setTextConfig(mBinding.tvSexAll, sortType.sex == 1)
        setTextConfig(mBinding.tvSexMan, sortType.sex == 2)
        setTextConfig(mBinding.tvSexWoman, sortType.sex == 3)
    }

    private fun valueSelect(sort: Int) {
        sortType.sort = sort
        setTextConfig(mBinding.tvSortValue, sortType.sort == 1)
        setTextConfig(mBinding.tvSortRegister, sortType.sort == 2)
    }

    private fun setTextConfig(textView: TextView, isSelect: Boolean) {
        textView.apply {
            if (isSelect) {
                context?.resources?.getColor(R.color.color_5FBCFE)?.let { setTextColor(it) }
                background = context?.getDrawable(R.drawable.bg_stroke_ff7ac2)
            } else {
                context?.resources?.getColor(R.color.color_black_273145)?.let { setTextColor(it) }
                background = context?.getDrawable(R.drawable.bg_gray_stroke_d3d1d7)
            }
        }
    }

    fun vquSetOnSortTypeClickListener(listener: (type: ContactVquSortData) -> Unit) {
        mVquListener = listener
    }
}