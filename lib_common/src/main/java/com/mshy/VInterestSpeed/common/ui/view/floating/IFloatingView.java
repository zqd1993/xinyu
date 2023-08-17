package com.mshy.VInterestSpeed.common.ui.view.floating;

import android.view.View;

public interface IFloatingView {

    void show();
    void hide();

    View getView();

    void setOnClickListener(DkFloatingView.ViewClickListener listener);

}
