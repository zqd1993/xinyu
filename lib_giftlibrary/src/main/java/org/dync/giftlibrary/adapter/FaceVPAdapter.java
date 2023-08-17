package org.dync.giftlibrary.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class FaceVPAdapter extends PagerAdapter {

    // 界面列表
    private final List<View> views;

    public FaceVPAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views != null ? views.size() : 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}
}