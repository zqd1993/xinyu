package com.mshy.VInterestSpeed.common.ui.view.floating;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.HashMap;

@SuppressLint("ViewConstructor")
public class DkFloatingView extends FloatingMagnetView implements MagnetViewListener {

    private final View mInflate;

    public DkFloatingView(@NonNull Context context, @LayoutRes int resource, ViewGroup.LayoutParams layoutParam) {
        super(context, null);
        mInflate = inflate(context, resource, this);
        setLayoutParams(layoutParam == null ? getParams() : layoutParam);
    }

    private FrameLayout.LayoutParams getParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.setMargins(0, params.topMargin + 100, params.rightMargin, 0);
        return params;
    }

    private final HashMap<Integer, Region> regionHashMap = new HashMap<>();

    private void clickView(View view) {
        if (view == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            view.setClickable(false);
            int childCount = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = ((ViewGroup) view).getChildAt(i);
                clickView(childAt);
            }
        } else {
            view.setClickable(false);
            Rect rect = new Rect();
            view.getHitRect(rect);
            Region region = new Region(rect);
            regionHashMap.put(view.getId(), region);
        }
    }

    public View getView() {
        return mInflate;
    }

    private ViewClickListener mListener;

    public void setOnClickListener(ViewClickListener listener) {
        this.mListener = listener;
        mInflate.post(() -> {
            regionHashMap.clear();
            clickView(mInflate);
            setMagnetViewListener(DkFloatingView.this);
        });
    }

    @Override
    public void onRemove(FloatingMagnetView magnetView) {
        regionHashMap.clear();
    }

    @Override
    public void onClick(MotionEvent event) {

        if (mListener != null) {
            mListener.onClick(0);
        }

//        Set<Map.Entry<Integer, Region>> entries = regionHashMap.entrySet();
//
//        for (Map.Entry<Integer, Region> entry : entries) {
//
//            if (entry.getValue().contains((int) event.getX(), (int) event.getY())) {
//
//                break;
//            }
//        }
    }

    public interface ViewClickListener {
        void onClick(int viewId);
    }
}
