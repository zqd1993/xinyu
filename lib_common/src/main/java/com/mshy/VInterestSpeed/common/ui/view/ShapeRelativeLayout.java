package com.mshy.VInterestSpeed.common.ui.view;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/1/5 17:45
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.mshy.VInterestSpeed.common.R;

/**
 * @description 实现自定义圆角相对布局
 * 支持
 * 1.四边圆角
 * 2.指定边圆角
 * 3.支持填充色以及边框色,边框虚线
 */

public class ShapeRelativeLayout extends RelativeLayout {

    //自定背景边框Drawable
    private GradientDrawable gradientDrawable;
    //填充色
    private int solidColor = 0;
    //渐变色
    private int startColor = 0;
    private int endColor = 0;
    //边框色
    private int strokeColor = 0;
    //边框宽度
    private int strokeWidth = 0;
    //渐变方向 0 是从左到右 1是从上到下
    private int orientation = 0;
    //四个角的弧度
    private float radius;
    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;
    //边框虚线的宽度
    float dashWidth = 0;
    //边框虚线的间隙
    float dashGap = 0;

    public ShapeRelativeLayout(Context context) {
        this(context,null);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        if (startColor != 0 && endColor != 0) {
            //渐变背景
            gradientDrawable = getLineDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    startColor, endColor, strokeWidth, strokeColor, dashWidth, dashGap, orientation);
        } else {
            //默认背景
            gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidColor, strokeWidth, strokeColor, dashWidth, dashGap);
        }


        setBackground(gradientDrawable);
    }


    /**
     * 初始化参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeRelativeLayout, 0, 0);
        //填充颜色
        solidColor = ta.getInteger(R.styleable.ShapeRelativeLayout_srl_solidColor, 0x00000000);
        //边框颜色
        strokeColor = ta.getInteger(R.styleable.ShapeRelativeLayout_srl_strokeColor, 0x00000000);
        startColor = ta.getInteger(R.styleable.ShapeRelativeLayout_srl_startColor, 0x00000000);
        endColor = ta.getInteger(R.styleable.ShapeRelativeLayout_srl_endColor, 0x00000000);
        //边框宽度
        strokeWidth = (int) ta.getDimension(R.styleable.ShapeRelativeLayout_srl_strokeWidth, 0);
        //四个角单独设置会覆盖radius设置
        radius = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_layoutRadius, 0);
        topLeftRadius = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_topLeftRadius, radius);
        topRightRadius = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_topRightRadius, radius);
        bottomLeftRadius = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_bottomLeftRadius, radius);
        bottomRightRadius = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_bottomRightRadius, radius);

        dashGap = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_dashGap, 0);
        dashWidth = ta.getDimension(R.styleable.ShapeRelativeLayout_srl_dashWidth, 0);

        orientation = ta.getInt(R.styleable.ShapeRelativeLayout_srl_orientation, 0);

        mRatio = ta.getFloat(R.styleable.ShapeLinearLayout_sll_ratio, mRatio);
        ta.recycle();
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
    public static GradientDrawable getNeedDrawable(float[] radius, int bgColor, int strokeWidth, int strokeColor, float dashWidth, float dashGap) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
        drawable.setColor(bgColor);
        return drawable;
    }


    public static GradientDrawable getLineDrawable(float[] radius, int startColor, int endColor, int strokeWidth, int strokeColor, float dashWidth, float dashGap, int orientation) {
        GradientDrawable drawable = new GradientDrawable();
        if (orientation == 0) {
            drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        } else if (orientation == 1) {
            drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        }
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor, dashWidth, dashGap);
        drawable.setColors(new int[]{startColor, endColor});

        return drawable;
    }

    private float mRatio = 0f;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRatio <= 0) {
            return;
        }

        float height = getMeasuredWidth() * mRatio;

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY)
        );
    }

}
