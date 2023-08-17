package com.live.module.info.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * 作者: Tany
 * 图片预览viewpager
 */

public class PicturePaperAdapter extends PagerAdapter {
    private List<View> views;

    public PicturePaperAdapter(List<View> views) {
        super();
        this.views = views;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        // super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
