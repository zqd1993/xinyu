package com.live.module.mine.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.view.Gravity
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.live.module.mine.R
import com.live.module.mine.adapter.MineVquDialogSignDayAdapter
import com.live.module.mine.databinding.MineDialogSignDayBinding
import com.live.module.mine.vm.MineSignDayViewModel
import com.mshy.VInterestSpeed.common.ext.setViewClickListener
import com.mshy.VInterestSpeed.common.ui.BaseVMDialogFragment
import com.mshy.VInterestSpeed.common.utils.ResUtils
import dagger.hilt.android.AndroidEntryPoint

/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
@AndroidEntryPoint
class SignDayDialog : BaseVMDialogFragment<MineDialogSignDayBinding, MineSignDayViewModel>() {

    override val mViewModel: MineSignDayViewModel by viewModels()

    private val mAdapter = MineVquDialogSignDayAdapter()

    override fun MineDialogSignDayBinding.initView() {

        val gridLayoutManager = GridLayoutManager(context, 24)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position <= 3) {
                    6
                } else {
                    8
                }
            }
        }

        mBinding.rvMineDialogSignDayList.layoutManager = gridLayoutManager
        mBinding.rvMineDialogSignDayList.adapter = mAdapter


        mBinding.ivMineDialogSignDayClose.setViewClickListener { dismiss() }

        mBinding.stvMineDialogSignDaySubmit.setViewClickListener {
            mViewModel.vquTodayActivity()
        }
    }

    override fun initObserve() {
        mViewModel.todayLiveData.observe(this) {
            mAdapter.todayCount = it.todayCount
            mAdapter.setNewInstance(it.today)

//            mBinding.stvMineDialogSignDaySubmit.isSelected = it.todayStatus

            if (it.todayStatus) {
                mBinding.stvMineDialogSignDaySubmit.text = "已签到"
                mBinding.stvMineDialogSignDaySubmit.setStartColor(
                    ResUtils.getColor(R.color.color_CCCCCC),
                    ResUtils.getColor(R.color.color_CCCCCC)
                )
            } else {
                mBinding.stvMineDialogSignDaySubmit.text = "签到"
                mBinding.stvMineDialogSignDaySubmit.setStartColor(
                    ResUtils.getColor(R.color.color_FFBF44),
                    ResUtils.getColor(R.color.color_F6AC1C)
                )
            }

            val count = "已连续签到 <font color=\'#FFEE31\'>${it.todayCount}</font> 天"

            val fromHtml = Html.fromHtml(count)

            mBinding.stvMineDialogSignDaySignCount.text = fromHtml
        }

        mViewModel.successData.observe(this) {
            dismiss()
        }
    }

    override fun initRequestData() {
        mViewModel.vquTodayActivityIndex()
    }

    override fun initWindow() {
//        super.initWindow()

        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.setCanceledOnTouchOutside(mCancelAble)
        dialog?.setCancelable(mCancelAble)
    }
}