package com.mshy.VInterestSpeed.common.ui.view.photoview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Tany on 2021/12/18.
 * Desc:
 */


public class ImgViewPager extends ViewPager {


        public ImgViewPager(Context context) {
            super(context);
        }

        public ImgViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            try {
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
