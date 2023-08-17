package com.mshy.VInterestSpeed.common.ui.dialog.gift;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.NavigatorHelper;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.abs.IPagerNavigator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.ArgbEvaluatorHolder;
import com.mshy.VInterestSpeed.common.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 类似CircleIndicator的效果
 * Created by hackware on 2016/9/3.
 */

public class ScaleLineNavigator extends View implements IPagerNavigator, NavigatorHelper.OnNavigatorScrollListener {

    private int mCount = 0;

    private boolean mFollowTouch = true;    // 是否跟随手指滑动
    private NavigatorHelper mNavigatorHelper = new NavigatorHelper();
    private Interpolator mStartInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private List<RectF> mRectFPoints = new ArrayList<>();

    //    private SparseArray<Float> mLeftDiffArray = new SparseArray<>();
//    private SparseArray<Float> mRightDiffArray = new SparseArray<>();
    private SparseArray<Float> mFractionArray = new SparseArray<>();

    private int mMinWidth;
    private int mMaxWidth;
    private int mCircleSpacing;

    private int mRadius;

    public ScaleLineNavigator(Context context) {
        super(context);
        init(context);

    }


    private void init(Context context) {


        mMinWidth = UiUtils.dip2px(context, 3f);
        mMaxWidth = UiUtils.dip2px(context, 10f);
        mCircleSpacing = UiUtils.dip2px(context, 3f);
        mRadius = UiUtils.dip2px(context, 3f);

        mNavigatorHelper.setNavigatorScrollListener(this);
        mNavigatorHelper.setSkimOver(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                if (mCount <= 0) {
                    result = getPaddingLeft() + getPaddingRight();
                } else {
                    result = (mCount - 1) * (mMinWidth + mCircleSpacing) + (mMaxWidth + mCircleSpacing) + getPaddingLeft() + getPaddingRight();
                }
                break;
            default:
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mMinWidth + getPaddingTop() + getPaddingBottom();
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0, j = mRectFPoints.size(); i < j; i++) {
            RectF rectF = mRectFPoints.get(i);

            float fraction = mFractionArray.get(i, 0f);
            mPaint.setColor(ArgbEvaluatorHolder.eval(fraction, getResources().getColor(R.color.color_CC7C69FE), getResources().getColor(R.color.color_667C69FE)));

//            mPaint.setAlpha();

            canvas.drawRoundRect(rectF.left, rectF.top, rectF.right, rectF.bottom, mRadius, mRadius, mPaint);
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mFollowTouch) {
            mFractionArray.put(index, 1f - enterPercent);

            if (index < 0 || mRectFPoints.size() <= index) {
                return;
            }


            RectF rectF = mRectFPoints.get(index);

            float v = (mMaxWidth - mMinWidth) * enterPercent;
            if (leftToRight) {
                rectF.left = rectF.right - mMinWidth - v;
            } else {
                rectF.right = rectF.left + mMinWidth + v;

            }

            invalidate();
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mFollowTouch) {
            mFractionArray.put(index, leavePercent);
            if (index < 0 || mRectFPoints.size() <= index) {
                return;
            }

            RectF rectF = mRectFPoints.get(index);

            float v = (mMaxWidth - mMinWidth) * leavePercent;
            if (leftToRight) {
                rectF.right = rectF.left + mMaxWidth - v;
            } else {
                rectF.left = rectF.right - mMaxWidth + v;

            }
            invalidate();
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {

    }

    @Override
    public void onDeselected(int index, int totalCount) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mNavigatorHelper.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mNavigatorHelper.onPageScrollStateChanged(state);
    }

    @Override
    public void onAttachToMagicIndicator() {

    }

    @Override
    public void onDetachFromMagicIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {

    }


    public void setCount(int count) {
        mCount = count;
        mNavigatorHelper.setTotalCount(count);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        preparePoints();
    }


    private void preparePoints() {
        mRectFPoints.clear();
        if (mCount > 0) {
            int startX = getPaddingLeft();
            for (int i = 0; i < mCount; i++) {
                RectF rectF = new RectF();
                rectF.left = startX;
                if (i == 0) {
                    rectF.right = startX + mMaxWidth;
                } else {
                    rectF.right = startX + mMinWidth;
                }
                rectF.top = getHeight() - mMinWidth;
                rectF.bottom = getHeight();
                mRectFPoints.add(rectF);
                startX = (int) (rectF.right + mCircleSpacing);
            }
        }
    }
}
