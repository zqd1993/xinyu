package com.mshy.VInterestSpeed.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mshy.VInterestSpeed.common.R;


/**
 * FileName: com.live.vquonline.view.main.dialog
 * Author: Reisen
 * Date: 2020/11/12 15:51
 * Description: 进度条弹窗
 * History:
 */
public class LoadingDialog extends Dialog {

    private TextView mTvProgress;
    private String  mTitle="加载中...";

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.SelectiveDialog);
    }
    public LoadingDialog(@NonNull Context context, String title) {
        super(context, R.style.SelectiveDialog);
        mTitle=title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_down_progress);
        initDialogAttrs();
        initView();
    }

    private void initView() {
        mTvProgress = findViewById(R.id.tv_progress);
        mTvProgress.setText(mTitle);
        mTvProgress.setVisibility(isVisiable);
    }

    int isVisiable;
    public void setVisiableProgress(int isVisiable){
        this.isVisiable = isVisiable;
    }


    /**
     * 初始化弹窗属性
     */
    private void initDialogAttrs() {
        if (getWindow() != null) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(layoutParams);
        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
