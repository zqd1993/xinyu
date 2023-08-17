package com.mshy.VInterestSpeed.common.utils

import android.os.CountDownTimer


/**
 * author: Lau
 * date: 2022/4/26
 * description:
 */
class CommonVquCountDownTimerHelper {

    var mTotalTime: Long = 60 * 1000L
    var mTimeInterval: Long = 1 * 1000L
    private var mTimer: CountDownTimer? = null

    private var mListener: CountDownTimerListener? = null

    fun build() {
        mTimer = object : CountDownTimer(mTotalTime, mTimeInterval) {
            override fun onTick(millisUntilFinished: Long) {
                mListener?.onTickEvent(millisUntilFinished)
            }

            override fun onFinish() {
                mListener?.onFinishEvent()
            }
        }

    }

    fun setTime(totalTime: Long, timeInterval: Long): CommonVquCountDownTimerHelper {
        mTotalTime = totalTime
        mTimeInterval = timeInterval
        return this
    }

    fun setCountDownTimerListener(listener: CountDownTimerListener?): CommonVquCountDownTimerHelper {
        mListener = listener
        return this
    }

    interface CountDownTimerListener {
        fun onTickEvent(millisUntilFinished: Long)
        fun onFinishEvent()
    }

    fun start() {
        mTimer?.start()
    }

    fun cancel() {
        mTimer?.cancel()
    }
}