package com.mshy.VInterestSpeed.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;


public class BottomSelectiveDialog extends Dialog {

    private final Context mContext;
    private LinearLayout mSelectiveLayout;
    private final List<TextView> mButtonList = new ArrayList<>();
    private boolean isAddCancelButton = true;
    private View mTopView;
    //是否添加虚线
    private boolean isAddLine = true;

    public interface OnButtonSelectListener {
        void onClicked(View view, int index);
    }

    public BottomSelectiveDialog addSelectButton(String buttonName, final OnButtonSelectListener listener) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                UiUtils.dip2px(mContext, 48));
        textView.setLayoutParams(params);
        textView.setText(buttonName);
        textView.setTextSize(15);
        textView.setTextColor(Color.parseColor("#FF273145"));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.onClicked(view, mButtonList.size());
                }
                dismiss();
            }
        });
        mButtonList.add(textView);
        return this;
    }

    public BottomSelectiveDialog addTopView(View view) {
        mTopView = view;
        return this;
    }

    public BottomSelectiveDialog cleanButtons() {
        mButtonList.clear();
        return this;
    }

    public BottomSelectiveDialog isAddCancelButton(boolean isAddCancelButton) {
        this.isAddCancelButton = isAddCancelButton;
        return this;
    }

    public BottomSelectiveDialog isAddLine(boolean isAddLine) {
        this.isAddLine = isAddLine;
        return this;
    }

    public BottomSelectiveDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BottomSelectiveDialog(@NonNull Context context, int themeResId) {
        super(context, R.style.SelectiveDialog);
        mContext = context;
    }

    protected BottomSelectiveDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, R.style.SelectiveDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selector);
//        initView();
        mSelectiveLayout = findViewById(R.id.selector_layout);
        if (mTopView != null) {
            mSelectiveLayout.addView(mTopView);
            View line = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.dip2px(mContext, 1));
            line.setBackgroundColor(Color.parseColor("#FFFAFAFC"));
            line.setLayoutParams(params);
            mSelectiveLayout.addView(line);
        }
        if (mButtonList.size() > 0) {
            LinearLayout btnLl = new LinearLayout(mContext);
            LinearLayout.LayoutParams btnLlParams = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnLl.setLayoutParams(btnLlParams);
            btnLl.setOrientation(LinearLayout.VERTICAL);
            btnLl.setBackgroundResource(R.drawable.vqu_shape_white_12_bg);
            for (int i = 0; i < mButtonList.size(); i++) {
                TextView textView = mButtonList.get(i);
                btnLl.addView(textView);
                if (!(i == mButtonList.size() - 1) && isAddLine) {
                    View line = new View(mContext);
                    LinearLayout.LayoutParams params = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtils.dip2px(mContext, 1));
                    line.setBackgroundColor(Color.parseColor("#FFFAFAFC"));
                    line.setLayoutParams(params);
                    btnLl.addView(line);
                }
            }
            mSelectiveLayout.addView(btnLl);
        }
        addCancelBtn();
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void initView() {
        mSelectiveLayout = findViewById(R.id.selector_layout);
        if (isAddCancelButton) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    UiUtils.dip2px(mContext, 48));
            params.topMargin = UiUtils.dip2px(mContext, 9);
            textView.setLayoutParams(params);
            textView.setText("取消");
            textView.setTextSize(15);
            textView.setTextColor(Color.parseColor("#FFA3AABE"));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(R.drawable.vqu_shape_white_12_bg);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            mButtonList.add(textView);
        }
    }

    private void addCancelBtn(){
        if (isAddCancelButton) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    UiUtils.dip2px(mContext, 48));
            params.topMargin = UiUtils.dip2px(mContext, 9);
            params.bottomMargin = UiUtils.dip2px(mContext, 33);
            textView.setLayoutParams(params);
            textView.setText("取消");
            textView.setTextSize(15);
            textView.setTextColor(Color.parseColor("#FFA3AABE"));
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.vqu_shape_white_12_bg);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            mSelectiveLayout.addView(textView);
        }
    }
}
