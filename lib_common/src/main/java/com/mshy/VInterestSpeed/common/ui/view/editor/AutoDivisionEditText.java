package com.mshy.VInterestSpeed.common.ui.view.editor;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenQihong on 2016/12/15.
 */

public class AutoDivisionEditText extends androidx.appcompat.widget.AppCompatEditText {

    private char divideCharacter = ' ';
    private int[] formatPattern = {4};
    private int maxLength = 24;
    private int[] divisionPattern;
    private boolean isMoneyType = false;

    private OnFocusChangeListener extFocusListener = null;

    private boolean divisionEnable = true;

    public AutoDivisionEditText(Context context) {
        super(context);
        init();
    }

    public AutoDivisionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoDivisionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        characters.add("0");
        characters.add("1");
        characters.add("2");
        characters.add("3");
        characters.add("4");
        characters.add("5");
        characters.add("6");
        characters.add("7");
        characters.add("8");
        characters.add("9");
        // addTextChangedListener(mInputWatcher);
        // setInputType(InputType.TYPE_CLASS_PHONE);
        Divider u = new Divider();
        addTextChangedListener(u);
        setFilters(new InputFilter[]{u});
        divisionPattern = getDivisionPatter();
        final OnFocusChangeListener listener = getOnFocusChangeListener();
        super.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (listener != null) {
                    listener.onFocusChange(v, hasFocus);
                }
                if (extFocusListener != null) {
                    extFocusListener.onFocusChange(v, hasFocus);
                }
            }
        });
    }

    public OnFocusChangeListener getExtFocusListener() {
        return extFocusListener;
    }

//
    //去掉拦截，放开某些自定义系统的调用
//    @Override
//    public void setOnFocusChangeListener(OnFocusChangeListener extFocusListener) {
//        throw new RuntimeException("Don't call setOnFocusChangeListener on ZlEditText. "
//                + "You probably want setExtFocusListener instead");
//    }

    /**
     * 设置监听函数
     *
     * @param extFocusListener 监听函数
     */
    public void setExtFocusListener(OnFocusChangeListener extFocusListener) {
        this.extFocusListener = extFocusListener;
    }

    public boolean isDivisionEnable() {
        return divisionEnable;
    }

    /**
     * 设置是否允许分割
     *
     * @param divisionEnable true 自动分隔
     */
    public void setDivisionEnable(boolean divisionEnable) {
        this.divisionEnable = divisionEnable;
    }

    public boolean isMoneyType() {
        return isMoneyType;
    }

    /**
     * 设置输入类型为金额
     *
     * @param isMoneyType true 金额类型
     */
    public void setMoneyType(boolean isMoneyType) {
        this.isMoneyType = isMoneyType;
    }

    /**
     * 字符格式化模式，例如设置为{3,4,4}，字符将要显示成138 3800 3800的形式
     *
     * @param formatPattern 分隔模式
     */
    public void setFormatPattern(int maxLength, int[] formatPattern) {
        divisionEnable = true;
        this.maxLength = maxLength;
        this.formatPattern = formatPattern;
        divisionPattern = getDivisionPatter();
    }

    /**
     * 获取分隔字符
     */
    public char getDividerCharacter() {
        return divideCharacter;
    }

    /**
     * 获取允许输入的最大长度
     */
    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * 设置分隔字符
     *
     * @param dividerCharacter 分隔字符
     */
    public void setDividerCharacter(char dividerCharacter) {
        this.divideCharacter = dividerCharacter;
    }

    private CharSequence deleteDivideChar(CharSequence string) {
        StringBuilder builder = new StringBuilder();
        int length = string.length();
        for (int i = 0; i < length; i++) {
            if (string.charAt(i) != divideCharacter) {
                builder.append(string.charAt(i));
            }
        }
        return builder;
    }

    /**
     * 获取去掉分隔符的内容
     *
     * @return 去掉了分隔符的内容
     */
    public String getRealInputText() {
        if (divisionEnable) {
            return deleteDivideChar(getText()).toString();
        } else {
            return getText().toString();
        }
    }

    private int[] getDivisionPatter() {
        int len = 0;
        int i = 0;
        while (len < maxLength) {
            if (i < formatPattern.length) {
                len += formatPattern[i];
            } else {
                len += formatPattern[formatPattern.length - 1];
            }
            i++;
        }
        int[] arrayInt = new int[--i];
        arrayInt[0] = formatPattern[0];
        for (int j = 1; j < i; j++) {
            arrayInt[j] = arrayInt[j - 1];
            if (j < formatPattern.length) {
                arrayInt[j] += formatPattern[j] + 1;
            } else {
                arrayInt[j] += formatPattern[formatPattern.length - 1] + 1;
            }
        }
        return arrayInt;
    }


    private final List<CharSequence> characters = new ArrayList<>();


    final class Divider implements TextWatcher, InputFilter {
        private int deleteDivider = 0;

        public final void afterTextChanged(Editable source) {
            if (isMoneyType) {
                do {
                    if (source.length() == 1 && source.charAt(0) == '.') {
                        source.clear();
                        break;
                    }

                    if (source.length() == 2 && source.charAt(0) == '0' && !source.toString().equals("0.")) {
                        source.delete(0, 1);
                    }

                    int dotPos = -1;
                    int len = source.length();
                    for (int i = 0; i < len; i++) {
                        if (source.charAt(i) == '.') {
                            dotPos = i;
                            break;
                        }
                    }
                    //金额类型最多输入两位小数
                    if (dotPos != -1 && (dotPos + 3) < len) {
                        source.delete(dotPos + 3, source.length());
                    }
                } while (false);
            }

            if (!divisionEnable) {
                return;
            }


            if (this.deleteDivider > 1) {
                int tmp = this.deleteDivider;
                this.deleteDivider = 0;
                source.delete(tmp - 1, tmp);
            }
            int j = 0;
            for (int i = 0; i < source.length(); i++) {
                char character = source.charAt(i);
                if (j > divisionPattern.length - 1) {
                    j = divisionPattern.length - 1;
                }
                if (i == divisionPattern[j]) {
                    if (j < divisionPattern.length - 1) {
                        j++;
                    }
                    if (character != divideCharacter) {
                        source.insert(i, String.valueOf(divideCharacter));
                    }
                } else if (character == divideCharacter) {
                    source.delete(i, i + 1);
                    i--;
                }
            }
        }

        public final void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }

        public final void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }

        @Override
        public final CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Log.d("abcd", "filter source: " + source);
            Log.d("abcd", "filter start: " + start);
            Log.d("abcd", "filter end: " + end);
            Log.d("abcd", "filter dest: " + dest);
            Log.d("abcd", "filter dstart: " + dstart);
            Log.d("abcd", "filter dend: " + dend);
            for (int i = 0; i < divisionPattern.length; i++) {
                Log.d("abcd", "filter divisionPattern " + i + ": " + divisionPattern[i]);
            }


            if (!divisionEnable) {
                return source;
            }


            if (!TextUtils.isEmpty(source)) {
                if (source.length() == 1) {
                    if (!characters.contains(source)) {
                        return "";
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < source.length(); i++) {
                        String c = String.valueOf(source.charAt(i));
                        if (!characters.contains(c)) {
                            c = "";
                        }
                        sb.append(c);
                    }
                    source = sb.toString();
                    end = source.length();
                }
            }


            CharSequence charSequence = deleteDivideChar(
                    new SpannableStringBuilder(dest).replace(dstart, dend, source, start, source.length()).toString());
            Log.d("abcd", "filter charSequence: " + charSequence);

            if (charSequence.length() > maxLength) {
                return "";
            }
            SpannableStringBuilder result = new SpannableStringBuilder(source);
            int[] arrayOfInt = divisionPattern;
            int dLength = dend - dstart;
            // if(dest.length()<=dstart)
            // return result;

            for (int i = 0; i < arrayOfInt.length; i++) {
                if ((source.length() == 0) && (dstart == arrayOfInt[i]) && (dest.charAt(dstart) == divideCharacter)) {
                    this.deleteDivider = arrayOfInt[i];
                }
                int j;

                if ((dstart - dLength <= arrayOfInt[i])
                        && (dstart + end - dLength >= arrayOfInt[i])
                        && (((j = arrayOfInt[i] - dstart) == end) || ((j >= 0) && (j < end) && (result.charAt(j) != divideCharacter)))) {
                    result.insert(j, String.valueOf(divideCharacter));
                    end++;
                }
            }
            return result;
        }
    }
}
