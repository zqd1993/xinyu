package com.mshy.VInterestSpeed.common.ui.view;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/1/5 18:08
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.utils.ResUtils;


/**
 * @description 实现自定义圆角背景
 * 支持
 * 1.四边圆角
 * 2.指定边圆角
 * 3.支持填充色以及边框色,边框虚线
 * 4.支持按下效果
 */
@SuppressLint("AppCompatCustomView")
public class ShapeEditText extends EditText {
    private boolean openSelector;
    //自定背景边框Drawable
    private GradientDrawable gradientDrawable;
    //按下时的Drawable
    private GradientDrawable selectorDrawable;
    //填充色
    private int solidColor = 0;
    //边框色
    private int strokeColor = 0;
    //按下填充色
    private int solidTouchColor = 0;
    //按下边框色
    private int strokeTouchColor = 0;
    //按下字体色
    private final int textTouchColor = 0;
    //边框宽度
    private int strokeWidth = 0;
    //开始渐变色
    private int startColor = 0;
    //结尾渐变色
    private int endColor = 0;
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
    //字体色
    private int textColor = 0;
    private int orientation;

    public void setSolidColor(int solidColor) {
        this.solidColor = solidColor;
        gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                solidColor, strokeWidth, strokeColor, dashWidth, dashGap);
        setBackground(gradientDrawable);
        postInvalidate();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                solidColor, strokeWidth, strokeColor, dashWidth, dashGap);
        setBackground(gradientDrawable);
        postInvalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        postInvalidate();
    }

    public void setStartColor(int startColor, int endColor) {
        if (startColor != 0 && endColor != 0) {
            int[] colors = {startColor, endColor};
            gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidColor, strokeWidth, strokeColor, dashWidth, dashGap);
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            if (orientation == 0) {
                gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            } else {
                gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            }
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置线性渐变
            gradientDrawable.setColors(colors);
        }
        setBackground(gradientDrawable);
        postInvalidate();
    }

    public void setStartColor(int startColor, int endColor, GradientDrawable.Orientation orientation) {
        if (startColor != 0 && endColor != 0) {
            int[] colors = {startColor, endColor};
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置线性渐变
            gradientDrawable.setColors(colors);
            gradientDrawable.setOrientation(orientation);
        }
        setBackground(gradientDrawable);
        postInvalidate();
    }


    public void setRadius(float radius) {
        this.radius = radius;
        topLeftRadius = radius;
        topRightRadius = radius;
        bottomLeftRadius = radius;
        bottomRightRadius = radius;
        postInvalidate();
    }

    public ShapeEditText(Context context) {
        this(context, null);
    }

    public ShapeEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        setBackgroundColor(ResUtils.INSTANCE.getColor(android.R.color.transparent));
        //默认背景
        gradientDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                        bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                solidColor, strokeWidth, strokeColor, dashWidth, dashGap);
        if (startColor != 0 && endColor != 0) {
            int[] colors = {startColor, endColor};
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);
            if (orientation == 0) {
                gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            } else {
                gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            }

            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);//设置线性渐变
            gradientDrawable.setColors(colors);
        }
        //如果设置了选中时的背景
        if (openSelector) {
            selectorDrawable = getNeedDrawable(new float[]{topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius},
                    solidTouchColor, strokeWidth, strokeTouchColor, dashWidth, dashGap);

            //动态生成Selector
            StateListDrawable stateListDrawable = new StateListDrawable();
            //是否可用
            int enabled = android.R.attr.state_enabled;

            stateListDrawable.addState(new int[]{-enabled}, selectorDrawable);
            stateListDrawable.addState(new int[]{enabled}, gradientDrawable);

            setBackground(stateListDrawable);
        } else {
            setBackground(gradientDrawable);
        }
    }

    /**
     * 初始化参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShapeEditText, 0, 0);

        orientation = ta.getInteger(R.styleable.ShapeLinearLayout_sll_orientation, 0);
        openSelector = ta.getBoolean(R.styleable.ShapeEditText_set_openSelector, false);
        startColor = ta.getInteger(R.styleable.ShapeEditText_set_startColor, 0x00000000);
        endColor = ta.getInteger(R.styleable.ShapeEditText_set_endColor, 0x00000000);
        solidColor = ta.getInteger(R.styleable.ShapeEditText_set_solidColor, 0x00000000);
        strokeColor = ta.getInteger(R.styleable.ShapeEditText_set_strokeColor, 0x00000000);

        solidTouchColor = ta.getInteger(R.styleable.ShapeEditText_set_solidTouchColor, 0x00000000);
        strokeTouchColor = ta.getInteger(R.styleable.ShapeEditText_set_strokeTouchColor, 0x00000000);
        //textTouchColor = ta.getInteger(R.styleable.ShapeTextView_textTouchColor, 0x00000000);
        textColor = getCurrentTextColor();
        strokeWidth = (int) ta.getDimension(R.styleable.ShapeEditText_set_strokeWidth, 0);

        //四个角单独设置会覆盖radius设置
        radius = ta.getDimension(R.styleable.ShapeEditText_set_layoutRadius, 0);
        topLeftRadius = ta.getDimension(R.styleable.ShapeEditText_set_topLeftRadius, radius);
        topRightRadius = ta.getDimension(R.styleable.ShapeEditText_set_topRightRadius, radius);
        bottomLeftRadius = ta.getDimension(R.styleable.ShapeEditText_set_bottomLeftRadius, radius);
        bottomRightRadius = ta.getDimension(R.styleable.ShapeEditText_set_bottomRightRadius, radius);

        dashGap = ta.getDimension(R.styleable.ShapeEditText_set_dashGap, 0);
        dashWidth = ta.getDimension(R.styleable.ShapeEditText_set_dashWidth, 0);

        ta.recycle();
    }

    /**
     * @param radius      四个角的半径
     * @param colors      渐变的颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @return
     */
    public static GradientDrawable getNeedDrawable(float[] radius, int[] colors, int strokeWidth, int strokeColor) {
        //TODO:判断版本是否大于16  项目中默认的都是Linear散射 都是从左到右 都是只有开始颜色和结束颜色
        GradientDrawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            drawable = new GradientDrawable();
            drawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            drawable.setColors(colors);
        } else {
            drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        }

        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        return drawable;
    }

    /**
     * @param radius      四个角的半径
     * @param bgColor     背景颜色
     * @param strokeWidth 边框宽度
     * @param strokeColor 边框颜色
     * @return
     */
    public static GradientDrawable getNeedDrawable(float[] radius, int bgColor, int strokeWidth, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radius);
        drawable.setStroke(strokeWidth, strokeColor);
        drawable.setColor(bgColor);
        return drawable;
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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {

        } else {

        }
    }
}
