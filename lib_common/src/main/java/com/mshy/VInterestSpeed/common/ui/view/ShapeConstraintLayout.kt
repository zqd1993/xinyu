package com.mshy.VInterestSpeed.common.ui.view

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.mshy.VInterestSpeed.common.R

/**
 * author: Lau
 * date: 2022/4/2
 * description:
 */
class ShapeConstraintLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    //自定背景边框Drawable
    private var gradientDrawable: GradientDrawable? = null

    //填充色
    private var solidColorThis = 0

    //渐变色
    private var startColor = 0
    private var endColor = 0

    //边框色
    private var strokeColor = 0

    //边框宽度
    private var strokeWidth = 0

    //渐变方向 0 是从左到右 1是从上到下
    private var orientation = 0

    //四个角的弧度
    private var radius = 0f
    private var topLeftRadius = 0f
    private var topRightRadius = 0f
    private var bottomLeftRadius = 0f
    private var bottomRightRadius = 0f

    private var mRatio = 0f

    //边框虚线的宽度
    private var dashWidth = 0f

    //边框虚线的间隙
    private var dashGap = 0f

    init {
        init(context,attrs)

        gradientDrawable = if (startColor != 0 && endColor != 0) {
            //渐变背景
         getLineDrawable(
                floatArrayOf(
                    topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                    bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
                ),
                startColor, endColor, strokeWidth, strokeColor, dashWidth, dashGap, orientation
            )
        } else {
            //默认背景
            getNeedDrawable(
                floatArrayOf(
                    topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                    bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
                ),
                solidColorThis, strokeWidth, strokeColor, dashWidth, dashGap
            )
        }

        background = gradientDrawable

    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.ShapeConstraintLayout, 0, 0)
        //填充颜色
        solidColorThis = ta.getInteger(R.styleable.ShapeConstraintLayout_scl_solidColor, 0x00000000)
        //边框颜色
        strokeColor = ta.getInteger(R.styleable.ShapeConstraintLayout_scl_strokeColor, 0x00000000)
        startColor = ta.getInteger(R.styleable.ShapeConstraintLayout_scl_startColor, 0x00000000)
        endColor = ta.getInteger(R.styleable.ShapeConstraintLayout_scl_endColor, 0x00000000)
        //边框宽度
        strokeWidth = ta.getDimension(R.styleable.ShapeConstraintLayout_scl_strokeWidth, 0f).toInt()
        //四个角单独设置会覆盖radius设置
        radius = ta.getDimension(R.styleable.ShapeConstraintLayout_scl_layoutRadius, 0f)
        topLeftRadius = ta.getDimension(R.styleable.ShapeConstraintLayout_scl_topLeftRadius, radius)
        topRightRadius = ta.getDimension(R.styleable.ShapeConstraintLayout_scl_topRightRadius, radius)
        bottomLeftRadius =
            ta.getDimension(R.styleable.ShapeConstraintLayout_scl_bottomLeftRadius, radius)
        bottomRightRadius =
            ta.getDimension(R.styleable.ShapeConstraintLayout_scl_bottomRightRadius, radius)

        dashGap = ta.getDimension(R.styleable.ShapeConstraintLayout_scl_dashGap, 0f)
        dashWidth = ta.getDimension(R.styleable.ShapeConstraintLayout_scl_dashWidth, 0f)

        orientation = ta.getInt(R.styleable.ShapeConstraintLayout_scl_orientation, 0)

        mRatio = ta.getFloat(R.styleable.ShapeConstraintLayout_scl_ratio, mRatio)

        ta.recycle()
    }

    /**
     * @param radius      四个角的半径
     * @param bgColor     背景颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @param dashWidth   虚线边框宽度
     * @param dashGap     虚线边框间隙
     * @return
     */
    private fun getNeedDrawable(radius: FloatArray?, bgColor: Int, strokeWidth: Int, strokeColor: Int, dashWidth: Float, dashGap: Float
    ): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadii = radius
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
        drawable.setColor(bgColor)
        return drawable
    }

    private fun getLineDrawable(radius: FloatArray?, startColor: Int, endColor: Int, strokeWidth: Int, strokeColor: Int, dashWidth: Float, dashGap: Float, orientation: Int
    ): GradientDrawable {
        val drawable = GradientDrawable()
        if (orientation == 0) {
            drawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
        } else if (orientation == 1) {
            drawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        }
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadii = radius
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap)
        drawable.colors = intArrayOf(startColor, endColor)
        return drawable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (mRatio <= 0) {
            return
        }

        val height = measuredWidth * mRatio

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        )
    }
}