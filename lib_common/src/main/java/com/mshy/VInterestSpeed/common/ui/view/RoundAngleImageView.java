package com.mshy.VInterestSpeed.common.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.mshy.VInterestSpeed.common.R;


public class RoundAngleImageView extends AppCompatImageView {

    private Paint paint;
    private int round = 0;

    private int roundLeftTop = 0;
    private int roundRightTop = 0;
    private int roundLeftBottom = 0;
    private int roundRightBottom = 0;

    private float mRatio = 0;


    private Paint paint2;
    private boolean hasAllAngle;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundAngleImageView);

        hasAllAngle = typedArray.hasValue(R.styleable.RoundAngleImageView_raiv_angle);
        if (hasAllAngle) {
            int angle = typedArray.getDimensionPixelSize(R.styleable.RoundAngleImageView_raiv_angle, 0);
            round = angle;
        } else {
            int angleLeftTop = typedArray.getDimensionPixelSize(R.styleable.RoundAngleImageView_raiv_angleLeftTop, 0);
            int angleRightTop = typedArray.getDimensionPixelSize(R.styleable.RoundAngleImageView_raiv_angleRightTop, 0);
            int angleLeftBottom = typedArray.getDimensionPixelSize(R.styleable.RoundAngleImageView_raiv_angleLeftBottom, 0);
            int angleRightBottom = typedArray.getDimensionPixelSize(R.styleable.RoundAngleImageView_raiv_angleRightBottom, 0);

            roundLeftTop = angleLeftTop;
            roundRightTop = angleRightTop;
            roundLeftBottom = angleLeftBottom;
            roundRightBottom = angleRightBottom;
        }

        mRatio = typedArray.getFloat(R.styleable.RoundAngleImageView_raiv_ratio, 0f);


//        roundHeight = dimensionPixelSize;

        typedArray.recycle();


        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        super.draw(canvas2);
        drawLiftUp(canvas2);
        drawRightUp(canvas2);
        drawLiftDown(canvas2);
        drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();


        int angle = hasAllAngle ? round : roundLeftTop;

        path.moveTo(0, angle);
        path.lineTo(0, 0);
        path.lineTo(angle, 0);
        path.arcTo(new RectF(
                        0,
                        0,
                        angle * 2,
                        angle * 2),
                -90,
                -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();

        int angle = hasAllAngle ? round : roundLeftBottom;

        path.moveTo(0, getHeight() - angle);
        path.lineTo(0, getHeight());
        path.lineTo(angle, getHeight());
        path.arcTo(new RectF(
                        0,
                        getHeight() - angle * 2,
                        angle * 2,
                        getHeight()),
                90,
                90);

        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();

        int angle = hasAllAngle ? round : roundRightBottom;

        path.moveTo(getWidth() - angle, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - angle);
        path.arcTo(new RectF(
                getWidth() - angle * 2,
                getHeight() - angle * 2,
                getWidth(),
                getHeight()), 0, 90);

        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();

        int angle = hasAllAngle ? round : roundRightTop;

        path.moveTo(getWidth(), angle);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - angle, 0);
        path.arcTo(new RectF(
                        getWidth() - angle * 2,
                        0,
                        getWidth(),
                        angle * 2),
                -90,
                90);

        path.close();
        canvas.drawPath(path, paint);
    }

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
