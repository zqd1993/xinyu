package com.mshy.VInterestSpeed.common.ui.dialog;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.fragment.app.DialogFragment;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView;
import com.tencent.mmkv.MMKV;


/**
 * FileName: com.live.vquonline.view.main.dialog
 * Author: Reisen
 * Date: 2020/12/11 10:25
 * Description:
 * History:
 */
public class CommonHintDialog extends BaseDialogFragment {

    private TextView mTvContent;
    private TextView mTvTitle;
    private ShapeTextView mTvLeft;
    private ShapeTextView mTvRight;
    private ShapeTextView mTvSingleButton;
    private TextView mTvFutureNoTip;
    private String mTitle = "";
    private String mContent;
    private SpannableString mSpannableContent;
    private SpannableStringBuilder spannableStringBuilder;
    private int mContentSize = 20;
    private int mContentGravity = Gravity.START;
    private String mLeftText = "取消";
    private String mRightText = "确定";
    private String mSingleText = "我知道了";

    private int mLeftStartColor;
    private int mLeftEndColor;
    private int mSingleButtonStartColor;
    private int mSingleButtonEndColor;
    private int mRightStartColor;
    private int mRightEndColor;

    private OnClickListener mClickListener;

    public OnSingleClickListener getOnSingleClickListener() {
        return onSingleClickListener;
    }

    public CommonHintDialog setOnSingleClickListener(OnSingleClickListener onSingleClickListener) {
        this.onSingleClickListener = onSingleClickListener;
        return this;
    }

    private OnSingleClickListener onSingleClickListener;
    private boolean isClickCancelable;
    private boolean isShowFutureNoTip;

    private boolean isBackCanceledOnTouchOutside;
    private GradientDrawable.Orientation mLeftOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
    private GradientDrawable.Orientation mRightOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
    private int mLeftTextColor = -1;
    private int mRightTextColor = -1;
    private boolean singleButton = false;
    private boolean titleVisible = false;

    @Override
    protected void initView(View view) {
        mTvFutureNoTip = view.findViewById(R.id.tv_future_no_tip);
        mTvContent = view.findViewById(R.id.tv_content);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvLeft = view.findViewById(R.id.tv_left);
        mTvRight = view.findViewById(R.id.tv_right);
        mTvSingleButton = view.findViewById(R.id.tv_single_button);
        mTvTitle.setText(mTitle);
        if (mTitle.isEmpty()) {
            mTvTitle.setVisibility(View.GONE);
        }

        if (mSpannableContent != null) {
            mTvContent.setText(mSpannableContent);
        } else if(mContent != null)  {
            mTvContent.setText(mContent);
        }else if(spannableStringBuilder != null){
            mTvContent.setText(spannableStringBuilder);
            mTvContent.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if(singleButton){
            mTvLeft.setVisibility(View.GONE);
            mTvRight.setVisibility(View.GONE);
            mTvSingleButton.setVisibility(View.VISIBLE);
            mTvSingleButton.setText(mSingleText);
            mTvSingleButton.setStartColor(mSingleButtonStartColor, mSingleButtonEndColor, mRightOrientation);
            mTvSingleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSingleClickListener != null) {
                        onSingleClickListener.onClick(CommonHintDialog.this);
                    }
                    dismiss();
                }
            });
        }else {
            mTvLeft.setVisibility(View.VISIBLE);
            mTvRight.setVisibility(View.VISIBLE);
            mTvSingleButton.setVisibility(View.GONE);
        }
        mTvLeft.setText(mLeftText);
        mTvRight.setText(mRightText);
        mTvLeft.setStartColor(mLeftStartColor, mLeftEndColor, mLeftOrientation);
        mTvRight.setStartColor(mRightStartColor, mRightEndColor, mRightOrientation);

        if (mLeftTextColor!=-1){
            mTvLeft.setTextColor(mLeftTextColor);
        }

        if (mRightTextColor!=-1){
            mTvRight.setTextColor(mRightTextColor);
        }

        mTvContent.setTextSize(mContentSize);

        mTvContent.setGravity(mContentGravity);


        mTvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onLeft(CommonHintDialog.this);
                }
                dismiss();
            }
        });
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onRight(CommonHintDialog.this);
                }
                dismiss();
            }
        });
        if(isShowFutureNoTip){
            mTvFutureNoTip.setVisibility(View.VISIBLE);
            mTvFutureNoTip.setTag(1); //不是选中
            mTvFutureNoTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Integer.parseInt(mTvFutureNoTip.getTag().toString()) == 1){
                        mTvFutureNoTip.setTag(2);
                        Drawable drawable = getResources().getDrawable(
                                R.mipmap.agora_future_no_tip_select);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        mTvFutureNoTip.setCompoundDrawables(drawable, null, null, null);
                        MMKV.defaultMMKV().putInt("tvFutureNoTip",2);
                    }else {
                        MMKV.defaultMMKV().putInt("tvFutureNoTip",1);
                        mTvFutureNoTip.setTag(1);
                        Drawable drawable = getResources().getDrawable(
                                R.mipmap.agora_future_no_tip_unselect);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        mTvFutureNoTip.setCompoundDrawables(drawable, null, null, null);
                    }


                }
            });
        }else{
            mTvFutureNoTip.setVisibility(View.GONE);
        }
    }

    public CommonHintDialog setShowFutureNoTip(boolean isShow) {
        isShowFutureNoTip = isShow;
        return this;
    }

    public CommonHintDialog setClickCancelable(boolean clickCancelable) {
        isClickCancelable = clickCancelable;
        return this;
    }

    public CommonHintDialog setBackCanceledOnTouchOutside(boolean backCanceledOnTouchOutside) {
        isBackCanceledOnTouchOutside = backCanceledOnTouchOutside;
        return this;
    }
    public CommonHintDialog setTitleVisible(boolean isShow) {
        titleVisible = isShow;
        return this;
    }
    public CommonHintDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public CommonHintDialog setContent(String content) {
        mContent = content;
        return this;
    }

    public CommonHintDialog setContent(SpannableString content) {
        mSpannableContent = content;
        return this;
    }

    public CommonHintDialog setContent(SpannableStringBuilder spannableStringBuilder) {
        this.spannableStringBuilder = spannableStringBuilder;
        return this;
    }


    public CommonHintDialog setContentSize(int size) {
        mContentSize = size;
        return this;
    }

    public CommonHintDialog setContentGravity(int gravity) {
        mContentGravity = gravity;
        return this;
    }

    public CommonHintDialog setLeftStartColor(int leftStartColor) {
        this.mLeftStartColor = leftStartColor;
        return this;
    }

    public CommonHintDialog setLeftEndColor(int leftEndColor) {
        this.mLeftEndColor = leftEndColor;
        return this;
    }

    public CommonHintDialog setRightStartColor(int rightStartColor) {
        this.mRightStartColor = rightStartColor;
        return this;
    }

    public CommonHintDialog setLeftOrientation(GradientDrawable.Orientation orientation) {
        mLeftOrientation = orientation;
        return this;
    }

    public CommonHintDialog setRightOrientation(GradientDrawable.Orientation orientation) {
        mRightOrientation = orientation;
        return this;
    }

    public CommonHintDialog setRightEndColor(int rightEndColor) {
        this.mRightEndColor = rightEndColor;
        return this;
    }

    public CommonHintDialog setLeftText(String content) {
        mLeftText = content;
        return this;
    }

    public CommonHintDialog setRightText(String content) {
        mRightText = content;
        return this;
    }

    public CommonHintDialog setSingleButton(boolean isShow) {
        singleButton = isShow;
        return this;
    }

    public CommonHintDialog setSingleButtonText(String vquSingleText) {
        mSingleText = vquSingleText;
        return this;
    }

    public CommonHintDialog setSingleButtonStartColor(int startColor) {
        this.mSingleButtonStartColor = startColor;
        return this;
    }

    public CommonHintDialog setSingleButtonEndColor(int endColor) {
        this.mSingleButtonEndColor = endColor;
        return this;
    }

    public CommonHintDialog setLeftTextColor(@ColorInt int color) {
        mLeftTextColor = color;
        return this;
    }

    public CommonHintDialog setRightTextColor(@ColorInt int color) {
        mRightTextColor = color;
        return this;
    }

    public CommonHintDialog setOnClickListener(OnClickListener clickListener) {
        this.mClickListener = clickListener;
        return this;
    }

    @Override
    protected boolean isClickCancelable() {
        return isClickCancelable;
    }

    @Override
    protected boolean isCanceledOnTouchOutside() {
        return isClickCancelable;
    }

    @Override
    protected boolean isBackCanceledOnTouchOutside() {
        return isBackCanceledOnTouchOutside;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_vqu_dialog_common_hint;
    }

    public interface OnClickListener {
        void onLeft(DialogFragment dialogFragment);

        void onRight(DialogFragment dialogFragment);
    }

    public interface OnSingleClickListener {
        void onClick(DialogFragment dialogFragment);
    }
}
