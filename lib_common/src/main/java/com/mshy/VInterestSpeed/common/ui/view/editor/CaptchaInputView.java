package com.mshy.VInterestSpeed.common.ui.view.editor;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luchao
 */
public class CaptchaInputView extends androidx.appcompat.widget.AppCompatEditText {
    private int borderColor;
    private int borderWidth;
    private int borderHeight;
    private int borderRadius;

    private int passwordLength = 4;
    private int passwordColor;
    private float passwordWidth;
    private float passwordRadius;
    private final Paint txtPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Paint passwordPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(ANTI_ALIAS_FLAG);
    private final Paint linePaint = new Paint(ANTI_ALIAS_FLAG);


    private TextChangeListener mTextChangeListener;
    private final float mDefaultInputViewPadding;
    private final float mDefaultInputTextSize;
    private final float mCursorWidth;
    private final int mCursorHeight;
    private final float mDefalutMargin = 0;
    private boolean mPwdVisiable = true;
    private String mInputText;
    private final List<RectF> rectList;
    private final Context mContext;
    private int mSelectIndex = 0;
    private final Handler mCursorHandler;
    private final CursorRunnable mCursorRunnable;
    static final int CURSOR_DELAY_TIME = 500;

    public CaptchaInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        final Resources res = getResources();
        final int defaultBorderColor = res.getColor(R.color.common_main_layout_color);
        final int defaultBorderWidth = res.getDimensionPixelSize(R.dimen.commont_dp_1);
        final int defaultBorderRadius = res.getDimensionPixelSize(R.dimen.commont_dp_3);
        final int defaultPasswordLength = 4;
        final int defaultPasswordColor = res.getColor(R.color.common_layout_color);
        final float defaultPasswordWidth = res.getDimension(R.dimen.commont_dp_10);
        final float defaultPasswordRadius = res.getDimension(R.dimen.commont_dp_3);
        final float defaultInputViewPadding = res.getDimension(R.dimen.commont_dp_12);
        final int defaultInputTextSize = res.getDimensionPixelSize(R.dimen.commont_dp_22);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CaptchaInputView, 0, 0);
        try {
            borderColor = a.getColor(R.styleable.CaptchaInputView_civ_captchaBorderColor, defaultBorderColor);
            borderWidth = a.getDimensionPixelSize(R.styleable.CaptchaInputView_civ_captchaBorderWidth, 0);
            borderHeight = a.getDimensionPixelSize(R.styleable.CaptchaInputView_civ_captchaBorderHeight, 0);
            borderRadius = a.getDimensionPixelSize(R.styleable.CaptchaInputView_civ_captchaBorderRadius, defaultBorderRadius);
            passwordLength = a.getInt(R.styleable.CaptchaInputView_civ_captchaLength, defaultPasswordLength);
            passwordColor = a.getColor(R.styleable.CaptchaInputView_civ_captchaColor, defaultPasswordColor);
            passwordWidth = a.getDimension(R.styleable.CaptchaInputView_civ_captchaWidth, defaultPasswordWidth);
            passwordRadius = a.getDimension(R.styleable.CaptchaInputView_civ_captchaRadius, defaultPasswordRadius);
            mDefaultInputViewPadding = a.getDimension(R.styleable.CaptchaInputView_civ_captchaViewSize, defaultInputViewPadding);
            mDefaultInputTextSize = a.getDimensionPixelSize(R.styleable.CaptchaInputView_civ_captchaTextSize, defaultInputTextSize);
        } finally {
            a.recycle();
        }

        mCursorWidth = mContext.getResources().getDimension(R.dimen.commont_dp_1);
        mCursorHeight = (int) mContext.getResources().getDimension(R.dimen.commont_dp_30);


        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);

        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setAntiAlias(true);


        //绘制直线
        linePaint.setColor(getResources().getColor(R.color.common_select_border_color));
        linePaint.setStrokeWidth(mCursorWidth);

        passwordPaint.setColor(ResUtils.INSTANCE.getColor(R.color.color_273145));
        passwordPaint.setTextSize(passwordWidth);
        txtPaint.setColor(getResources().getColor(R.color.color_273145));
        txtPaint.setTextSize(mDefaultInputTextSize);
        txtPaint.isFakeBoldText();
        rectList = new ArrayList<>();

        mCursorHandler = new Handler();
        mCursorRunnable = new CursorRunnable();
        mCursorHandler.post(mCursorRunnable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = passwordLength * borderWidth + (int) mDefaultInputViewPadding * 3 + (int) mDefalutMargin * 2;
        int height = borderHeight;
        setMeasuredDimension(getWidth(),height);
    }

    private class CursorRunnable implements Runnable {
        private boolean mCancelled = false;
        private boolean mCursorVisible = false;

        @Override
        public void run() {
            if (mCancelled) {
                return;
            }
            postInvalidate();
            postDelayed(this, CURSOR_DELAY_TIME);
        }

        void cancel() {
            if (!mCancelled) {
                mCursorHandler.removeCallbacks(this);
                mCancelled = true;
            }
        }

        public boolean getCursorVisiable() {
            return mCursorVisible = !mCursorVisible;
        }
    }


    public void stopCursor() {
        if (mCursorRunnable != null && mCursorHandler != null) {
            mCursorRunnable.cancel();
        }
    }

//	点击事件的处理
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		float x = event.getX();
//		float y = event.getY();
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//			for(int i = 0; i< rectList.size(); i++) {
//				RectF rectF = rectList.get(i);
//				if(rectF.contains(x, y)) {
//					mSelectIndex = i;
//					postInvalidate();
//
//					Log.d("draw", "index="+i);
//					break;
//				}
//			}
//			break;
//		}
//		return super.onTouchEvent(event);
//	}

    @Override
    protected void onDraw(Canvas canvas) {
        if (borderWidth == 0) {
            borderWidth = (int) (getWidth() / 4 - mDefaultInputViewPadding * 3 / 4);
            borderPaint.setStrokeWidth(borderWidth);

        }


        rectList.clear();
        //边框
        int left = (int) mDefalutMargin;
        int top = (int) mDefalutMargin;
        for (int i = 0; i < passwordLength; i++) {

            borderPaint.setColor(borderColor);

            RectF rectF = new RectF(left, top, left + borderWidth, borderHeight + top);
            rectList.add(rectF);
            canvas.drawRoundRect(rectF, borderRadius, borderRadius, borderPaint);
            left += mDefaultInputViewPadding + borderWidth;
        }

        //内容,密码可见
        int textLeft = (int) mDefalutMargin + borderWidth / 2;
        if (mPwdVisiable) {
            for (int i = 0; i < mInputText.length(); i++) {
                String text = mInputText.substring(i, i + 1);
                int textWidth = !TextUtils.isEmpty(text) ? getTextWidth(txtPaint, text) / 2 : 0;
                canvas.drawText(text, textLeft - textWidth, borderHeight / 2f + mDefaultInputTextSize / 2, txtPaint);
                textLeft += mDefaultInputViewPadding + borderWidth;
            }
        } else {
            for (int i = 0; i < mInputText.length(); i++) {
                String text = mInputText.substring(i, i + 1);
                int textWidth = !TextUtils.isEmpty(text) ? getTextWidth(passwordPaint, "*") / 2 : 0;
                canvas.drawText("*", textLeft - textWidth, borderWidth / 2f + mDefaultInputTextSize / 2 + 5, passwordPaint);
                textLeft += mDefaultInputViewPadding + borderWidth;
            }
        }

        //光标
        if (mSelectIndex < passwordLength && mCursorRunnable.getCursorVisiable()) {
            int cursorLeft = (int) mDefalutMargin;
            int cursorTop = (int) mDefalutMargin + ((int) borderHeight - mCursorHeight) / 2;
            int startX = cursorLeft + borderWidth / 2 + mSelectIndex * borderWidth + mSelectIndex * (int) mDefaultInputViewPadding;
            int stopY = cursorTop + mCursorHeight;
            canvas.drawLine(startX, cursorTop, startX, stopY, linePaint);
        }

    }

    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mTextChangeListener != null) {
            mTextChangeListener.onTextChanged(text, start, lengthBefore, lengthAfter);
        }
        mInputText = text.toString();
        if (mInputText.length() > 0) {
            mSelectIndex = mInputText.length();
        } else {
            mSelectIndex = 0;
        }
        postInvalidate();
    }

    public void setPwdVisiable(boolean pwdVisiable) {
        this.mPwdVisiable = pwdVisiable;
    }

    public void setTextLength(int length) {
        this.passwordLength = length;
        postInvalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        borderPaint.setStrokeWidth(borderWidth);
        invalidate();
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        invalidate();
    }

    public int getPasswordLength() {
        return passwordLength;
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        invalidate();
    }

    public int getPasswordColor() {
        return passwordColor;
    }

    public void setPasswordColor(int passwordColor) {
        this.passwordColor = passwordColor;
        passwordPaint.setColor(passwordColor);
        invalidate();
    }

    public float getPasswordWidth() {
        return passwordWidth;
    }

    public void setPasswordWidth(float passwordWidth) {
        this.passwordWidth = passwordWidth;
        passwordPaint.setStrokeWidth(passwordWidth);
        invalidate();
    }

    public float getPasswordRadius() {
        return passwordRadius;
    }

    public void setPasswordRadius(float passwordRadius) {
        this.passwordRadius = passwordRadius;
        invalidate();
    }

    public void setTextChangeListener(TextChangeListener mTextChangeListener) {
        this.mTextChangeListener = mTextChangeListener;
    }

    public interface TextChangeListener {
        void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter);
    }
}
