package com.mshy.VInterestSpeed.common.ui.view.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.utils.ResUtils;

/**
 * Created by ChenQihong on 2016/12/14.
 */

public class CaiEditText extends LinearLayout implements View.OnClickListener {
    public static final int EDIT_TYPE_NONE = 0x00000000;
    public static final int EDIT_TYPE_TEXT = 0x00000001;
    public static final int EDIT_TYPE_PHONE_NUM = 0x00000003;
    public static final int EDIT_TYPE_NUM_PASSWORD = 0x00000012;
    private AutoDivisionEditText mEditText;
    private final ImageView mFunctionalImage;
    private final ImageView mInputLogo;
    private int mInputType = EDIT_TYPE_NONE;
    private OnRightFunctionButtonClickListener mRightClick;
    private final TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            if (!mEditText.isEnabled()) {
                mFunctionalImage.setVisibility(View.INVISIBLE);
                return;
            }
            if (editable.length() > 0 && mEditText.isFocused()) {
                mFunctionalImage.setVisibility(View.VISIBLE);
            } else {
                mFunctionalImage.setVisibility(View.INVISIBLE);
            }
        }
    };

    public interface OnRightFunctionButtonClickListener {
        void onClick(View v);
    }

    public CaiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.common_phone_edit_text, this, true);
        mEditText = (AutoDivisionEditText) findViewById(R.id.cai_edit_text);
        mFunctionalImage = (ImageView) findViewById(R.id.cai_function_image);
        mInputLogo = (ImageView) findViewById(R.id.cai_input_logo);
        mFunctionalImage.setOnClickListener(this);

        mEditText.setDivisionEnable(false);
        mEditText.addTextChangedListener(mWatcher);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CaiEditText);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CaiEditText_common_edit_type) {
                mInputType = a.getInt(attr, EditorInfo.TYPE_NULL);
            } else if (attr == R.styleable.CaiEditText_common_hint) {
                String hint = a.getString(attr);
                mEditText.setHint(hint);
            } else if (attr == R.styleable.CaiEditText_common_left_img) {
                int resId = a.getResourceId(attr, 0);
                if (0 != resId) {
                    mInputLogo.setImageResource(resId);
                }
            } else if (attr == R.styleable.CaiEditText_common_right_img) {
                int resId = a.getResourceId(attr, 0);
                if (0 != resId) {
                    mFunctionalImage.setImageResource(resId);
                }
            } else if (attr == R.styleable.CaiEditText_common_left_img_enable) {
                boolean enable = a.getBoolean(attr, true);
                if (enable) {
                    mInputLogo.setVisibility(VISIBLE);
                } else {
                    mInputLogo.setVisibility(GONE);
                }
            } else if (attr == R.styleable.CaiEditText_common_right_img_enable) {
                boolean enable = a.getBoolean(attr, true);
                if (enable) {
                    mFunctionalImage.setVisibility(VISIBLE);
                } else {
                    mFunctionalImage.setVisibility(GONE);
                }
            } else if (attr == R.styleable.CaiEditText_common_hint_color) {
                int colorId = a.getResourceId(attr, 0);
                if (0 != colorId) {
                    mEditText.setHintTextColor(colorId);
                }
            } else if (attr == R.styleable.CaiEditText_common_gravity_center) {
                boolean enable = a.getBoolean(attr, false);
                if (enable) {
                    mEditText.setGravity(Gravity.CENTER);
                }
            } else if (attr == R.styleable.CaiEditText_common_text_color) {
                int color = a.getColor(attr, ResUtils.INSTANCE.getColor(R.color.color_273145));
                mEditText.setTextColor(color);
            }
        }
        if (mInputType == EDIT_TYPE_PHONE_NUM) {
            mEditText.setDivisionEnable(true);
            int[] newFormatType = {3, 4, 4};
            mEditText.setFormatPattern(11, newFormatType);
            //mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        } else {

            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        }
        mEditText.setInputType(mInputType);
    }

    public int getInputType() {
        return mInputType;
    }

    public void setInputType(int inputType) {
        mEditText.setInputType(inputType);
        this.mInputType = inputType;
    }

    public void setDivisionEnable(boolean enable) {
        mEditText.setDivisionEnable(enable);
    }

    public void setRightButtonFunction(OnRightFunctionButtonClickListener rightClick) {
        mRightClick = rightClick;
    }

    public void setRightButtonImageById(int resid) {
        mFunctionalImage.setImageResource(resid);
    }

    public AutoDivisionEditText getEditText() {
        return mEditText;
    }

    public void setEditText(AutoDivisionEditText editText) {
        this.mEditText = editText;
    }

    @Override
    public void onClick(View view) {
        if (view == mFunctionalImage && null != mRightClick) {
            mRightClick.onClick(view);
        }
    }

    public String getText() {
        return mEditText.getRealInputText();
    }

    public void setText(String message) {
        mEditText.setText(message);
    }

    public void enable(boolean isEnable) {
        mEditText.setEnabled(isEnable);
    }
}
