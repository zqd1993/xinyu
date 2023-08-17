package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

/**
 * author: Lau
 * date: 2022/4/14
 * description:
 */
class WaveView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : View(
    context,
    attrs,
    defStyleAttr
) {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    private var mInitialRadius // 初始波纹半径
            = 0f
    private var mMaxRadiusRate = 0.85f // 如果没有设置mMaxRadius，可mMaxRadius = 最小长度 * mMaxRadiusRate;

    private var mMaxRadius = 0f // 最大波纹半径

    private var mDuration: Long = 2000 // 一个波纹从创建到消失的持续时间

    private var mSpeed = 500 // 波纹的创建速度，每500ms创建一个

    private var mInterpolator: Interpolator? = LinearInterpolator()

    private val mCircleList = mutableListOf<Circle>()

    fun ismIsRunning(): Boolean {
        return mIsRunning
    }

    private var mIsRunning = false

    private var mMaxRadiusSet = false

    private lateinit var mPaint: Paint
    private var mLastCreateTime: Long = 0

    private val mCreateCircle: Runnable = object : Runnable {
        override fun run() {
            if (mIsRunning) {
                newCircle()
                postDelayed(this, mSpeed.toLong())
            }
        }
    }

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG);
        setStyle(Paint.Style.FILL);
    }

    fun setStyle(style: Paint.Style?) {
        mPaint.style = style
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (!mMaxRadiusSet) {
            mMaxRadius = w.coerceAtMost(h) * mMaxRadiusRate / 2.0f;
        }
    }

    fun setMaxRadiusRate(maxRadiusRate: Float) {
        mMaxRadiusRate = maxRadiusRate
    }

    fun setColor(color: Int) {
        mPaint.color = color
    }

    /**
     * 开始
     */
    fun start() {
        if (!mIsRunning) {
            mIsRunning = true
            mCreateCircle.run()
        }
    }

    /**
     * 停止
     */
    fun stop() {
        mIsRunning = false
    }

    override fun onDraw(canvas: Canvas?) {
        val iterator: MutableIterator<Circle> = mCircleList.iterator()
        while (iterator.hasNext()) {
            val circle: Circle = iterator.next() as Circle
            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.alpha = circle.alpha
                canvas!!.drawCircle(
                    (width / 2).toFloat(),
                    (height / 2).toFloat(), circle.currentRadius, mPaint
                )
            } else {
                iterator.remove()
            }
        }
        if (mCircleList.isNotEmpty()) {
            postInvalidateDelayed(10)
        }
    }

    fun setInitialRadius(radius: Float) {
        mInitialRadius = radius
    }

    fun setDuration(duration: Long) {
        mDuration = duration
    }

    fun setMaxRadius(maxRadius: Float) {
        mMaxRadius = maxRadius
        mMaxRadiusSet = true
    }


    fun setSpeed(speed: Int) {
        mSpeed = speed
    }

    private fun newCircle() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastCreateTime < mSpeed) {
            return
        }
        val circle = Circle()
        mCircleList.add(circle)
        invalidate()
        mLastCreateTime = currentTime
    }

    private inner class Circle {
        val mCreateTime: Long = System.currentTimeMillis()
        val alpha: Int
            get() {
                val percent: Float = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration
                return ((1.0f - (mInterpolator?.getInterpolation(percent) ?: 0f)) * 255).toInt()
            }
        val currentRadius: Float
            get() {
                val percent: Float = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration
                return mInitialRadius + (mInterpolator?.getInterpolation(percent)
                    ?: 0f) * (mMaxRadius - mInitialRadius)
            }

    }

    fun setInterpolator(interpolator: Interpolator?) {
        mInterpolator = interpolator
        if (mInterpolator == null) {
            mInterpolator = LinearInterpolator()
        }
    }
}