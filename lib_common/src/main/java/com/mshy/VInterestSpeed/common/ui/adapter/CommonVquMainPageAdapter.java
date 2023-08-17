package com.mshy.VInterestSpeed.common.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/4/7 9:45
 */
public class CommonVquMainPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments ;
    public CommonVquMainPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setData(List<Fragment> data){
        if (fragments == null){
            fragments = new ArrayList<>();
        }
        fragments.addAll(data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (fragments != null && fragments.size() > 0){
            return fragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fragments != null && fragments.size() > 0){
            return fragments.size();
        }
        return 0;
    }
}

