package com.mshy.VInterestSpeed.common.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.live.vquonline.base.ktx.SizeUnitKtxKt;


/**
 * FileName: com.live.tianyanonline.view.main.dialog
 * Author: Reisen
 * Date: 2020/12/10 19:03
 * Description: 基类DialogFragment
 * History:
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected String tag = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), isBindContainer() ? container : null, isAttachToRoot());
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(isClickCancelable());
        getDialog().setCanceledOnTouchOutside(isCanceledOnTouchOutside());
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (!isBackCanceledOnTouchOutside()) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
            return false;
        });
        return view;
    }

    /**
     * 是否点击外部可以取消弹窗
     *
     * @return
     */
    protected boolean isClickCancelable() {
        return true;
    }

    /**
     * 是否点击外部可以取消弹窗
     *
     * @return
     */
    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    /**
     * 是否点击返回可以取消弹窗
     *
     * @return
     */
    protected boolean isBackCanceledOnTouchOutside() {
        return true;
    }

    /**
     * 是否添加蒙版
     *
     * @return
     */
    protected boolean isAddMask() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isAddMask()) {
            getDialog().getWindow().setDimAmount(0f);
        }
        initView(view);
    }


    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        if (getWidth() != 0) {
            layoutParams.width = getWidth();
        }
        if (getHeight() != 0) {
            layoutParams.height = getHeight();
        }
        layoutParams.gravity = getGravity();
        getDialog().getWindow().setAttributes(layoutParams);
    }

    protected abstract void initView(View view);

    protected int getWidth() {
        return (int) (SizeUnitKtxKt.getScreenWidth(requireContext()) * 0.8);
    }

    protected int getHeight() {
        return 0;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    /**
     * 根布局id
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutId();

    /**
     * 是否绑定ViewGroup
     *
     * @return
     */
    protected boolean isBindContainer() {
        return true;
    }

    protected boolean isAttachToRoot() {
        return false;
    }

    public void show(FragmentManager manager) {
        if (manager.isDestroyed())
            return;
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commitNowAllowingStateLoss();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常，这里捕获一下
            e.printStackTrace();
        }
//        show(manager, tag);
    }
}
