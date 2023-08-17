package com.live.module.contact.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.live.module.contact.R
import com.live.module.contact.databinding.ContactVquTimePickerviewDialogBinding
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.utils.DateUtils
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.ui.BaseDialogFragment
import java.util.*


class ContactVquTimeDialog : BaseDialogFragment<ContactVquTimePickerviewDialogBinding>(),
    View.OnClickListener {
    var startTime: Calendar? = null
    var endTime: Calendar? = null
    var pvTime: TimePickerView? = null
    var selectType: Int = 0//0选中的是开始时间 1选中的是结束时间
    var mySelectListener: OnSelectListener? = null

    private var mRange = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.BOTTOM
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = attributes
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBinding.llStart.setOnClickListener(this)
        mBinding.llEnd.setOnClickListener(this)
        mBinding.tvConfirm.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_start -> {
                selectType = 0
                setSelect()
                pvTime?.setDate(startTime)
            }
            R.id.ll_end -> {
                selectType = 1
                setSelect()
                pvTime?.setDate(endTime)
            }
            R.id.tv_confirm -> {
                val startYear = startTime?.get(Calendar.YEAR) ?: 0
                val startMonth: Int? = startTime?.get(Calendar.MONTH)?.plus(1)
                val startDay: Int? = startTime?.get(Calendar.DAY_OF_MONTH)
                startTime?.set(Calendar.HOUR_OF_DAY,0)
                startTime?.set(Calendar.MINUTE,0)
                startTime?.set(Calendar.SECOND,0)

                val endYear = endTime?.get(Calendar.YEAR) ?: 0
                val endMonth: Int? = endTime?.get(Calendar.MONTH)?.plus(1)
                val endDay: Int? = endTime?.get(Calendar.DAY_OF_MONTH)
                endTime?.set(Calendar.HOUR_OF_DAY,23)
                endTime?.set(Calendar.MINUTE,59)
                endTime?.set(Calendar.SECOND,59)

                val startTimeMillis = startTime?.timeInMillis ?: 0L
                val endTimeMillis = endTime?.timeInMillis ?: 0L
                if (startTimeMillis > endTimeMillis) {
                    toast("开始时间不能大于结束时间")
                    return
                }

                if (mRange > 0) {
                    val diffMillis = endTimeMillis - startTimeMillis
                    if (diffMillis / (1000 * 60 * 60 * 24) > mRange) {
                        toast("日期范围不能超过31天")
                        return
                    }
                }

//                if (startYear == endYear) {
//                    if (endMonth == null || startMonth == null || startDay == null || endDay == null) {
//                        toast("日期错误")
//                        return
//                    }
//
//                    if (startMonth == endMonth) {
//                        if (startDay < endDay) {
//                            toast("结束时间不能大于开始时间")
//                            return
//                        }
//                    } else if (startMonth < endMonth) {
//                        toast("结束时间不能大于开始时间")
//                        return
//                    }
//                } else if (startYear < endYear) {
//                    toast("结束时间不能大于开始时间")
//                    return
//                }


                mySelectListener?.setSelect(
                    startTime?.get(Calendar.YEAR)
                        .toString() + "-" + if (startMonth!! < 10) {
                        "0$startMonth"
                    } else {
                        startMonth
                    } + "-" +
                            if (startDay!! < 10) {
                                "0$startDay"
                            } else {
                                startDay
                            } + "",
                    endTime?.get(Calendar.YEAR).toString() + "-" + if (endMonth!! < 10) {
                        "0$endMonth"
                    } else {
                        endMonth
                    } + "-" + if (endDay!! < 10) {
                        "0$endDay"
                    } else {
                        endDay
                    } + ""
                )
                dismiss()
            }
        }
    }

    fun setSelect() {
        if (selectType == 0) {
            mBinding.tvStart.isSelected = true
            mBinding.tvStartTime.isSelected = true
            mBinding.tvEnd.isSelected = false
            mBinding.tvEndTime.isSelected = false
        } else {
            mBinding.tvStart.isSelected = false
            mBinding.tvStartTime.isSelected = false
            mBinding.tvEnd.isSelected = true
            mBinding.tvEndTime.isSelected = true
        }
    }

    override fun ContactVquTimePickerviewDialogBinding.initView() {

        selectType = 0
//        setSelect()
        setSelect()
        initTimePicker()
    }

    private fun initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        val selectedDate: Calendar = Calendar.getInstance()
        val endDate: Calendar = Calendar.getInstance()

        val startDate: Calendar = Calendar.getInstance()
        startDate.set(2020, 5, 20)

        selectedDate.time = Date()
        selectedDate.set(Calendar.DAY_OF_MONTH, 1)

        if (startTime == null) {
            startTime = selectedDate
        }

        if (endTime == null) {
            endTime = endDate
        }

        notifyTime()
        //时间选择器
        pvTime = TimePickerBuilder(
            activity
        ) { date, v ->
            //选中事件回调
            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            /*btn_Time.setText(getTime(date));*/
            // getTime(date);
        }
            .setLayoutRes(
                R.layout.contact_pickerview_select_time
            ) { }
            .setTimeSelectChangeListener { date ->
                if (selectType == 0) {
                    startTime = Calendar.getInstance()
                    startTime?.time = date
                } else {
                    endTime = Calendar.getInstance()
                    endTime?.time = date
                }
                notifyTime()
            }
            .setType(booleanArrayOf(true, true, true, false, false, false))
            .setLabel("年", "月", "日", "", "", "") //设置空字符串以隐藏单位提示   hide label
            .setDividerColor(Color.WHITE)
            .setContentTextSize(20)
            .setDate(startTime)
            .setDividerColor(ContextCompat.getColor(BaseApplication.context, R.color.main_bg_color))
            .setTextColorCenter(resources.getColor(R.color.color_273145))
            .setBgColor(android.R.color.transparent)
            .setTitleBgColor(android.R.color.transparent)
            .setRangDate(startDate, endDate)
            .setDecorView(mBinding.timeLayout) //非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
            .setOutSideColor(0x00000000)
            .setOutSideCancelable(false)
            .build()
        pvTime?.show(mBinding.timeLayout, false) //弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
    }

    private fun notifyTime() {
        mBinding.tvStartTime.text =
            startTime?.get(Calendar.YEAR).toString() + "年" + (startTime?.get(Calendar.MONTH)
                ?.plus(1)) + "月" + startTime?.get(Calendar.DAY_OF_MONTH) + "日"
        mBinding.tvEndTime.text =
            endTime?.get(Calendar.YEAR).toString() + "年" + (endTime?.get(Calendar.MONTH)
                ?.plus(1)) + "月" + endTime?.get(Calendar.DAY_OF_MONTH) + "日"
    }

    interface OnSelectListener {
        fun setSelect(startTime: String, endTime: String)
    }

    fun setOnSelectListener(listener: OnSelectListener) {
        mySelectListener = listener
    }

    fun setRange(range: Int) {
        mRange = range
    }

    fun setStartDate(startDate: String) {

        if (startDate.isEmpty()) {
            startTime = null
            return
        }


        val date = Date()
        date.time = DateUtils.getDateStringToDate(startDate, "yyyy-MM-dd") ?: 0
        val calendar = Calendar.getInstance()
        calendar.time = date

        startTime = calendar
    }

    fun setEndDate(endDate: String) {

        if (endDate.isEmpty()) {
            endTime = null
            return
        }

        val date = Date()
        date.time = DateUtils.getDateStringToDate(endDate, "yyyy-MM-dd") ?: 0
        val calendar = Calendar.getInstance()
        calendar.time = date

        endTime = calendar
    }
}