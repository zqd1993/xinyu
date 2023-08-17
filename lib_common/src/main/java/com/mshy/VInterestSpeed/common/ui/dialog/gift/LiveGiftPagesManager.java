package com.mshy.VInterestSpeed.common.ui.dialog.gift;

import android.content.Context;
import android.os.Parcelable;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LiveGiftPagesManager {
    private final ViewPager mAdvPager;

    public List<View> getmGiftView() {
        return mGiftView;
    }

    private final List<View> mGiftView = new ArrayList<>();
    private final Context mContext;
    private final List<DialogGiftBean> mTotalGiftBean = new ArrayList<>();
    private List<DialogGiftBean>[] mPageGiftBean = null;
    private OnGiftItemClickedListener mGiftListener;
    private List<LiveGiftRecycleAdapter> mAdapters;

    public List<LiveGiftRecycleAdapter> getmAdapters() {
        return mAdapters;
    }

    //单页最大view数 2的倍数
    private final int SIMPLE_PAGE_MAX_COUNT = 8;

    public interface OnGiftItemClickedListener {
        void onGiftClicked(DialogGiftBean bean);
    }

    public void setOnGiftItemClickedListener(OnGiftItemClickedListener listener) {
        mGiftListener = listener;
    }

    public LiveGiftPagesManager(ViewPager advPager, Context context) {
        this.mAdvPager = advPager;
        this.mContext = context;
    }

    public void addGiftBean(List<DialogGiftBean> giftBeans) {
        mTotalGiftBean.addAll(giftBeans);
    }

    public List<DialogGiftBean> getmTotalGiftBean() {
        return mTotalGiftBean;
    }

    private void makeGiftView() {
        if (0 == mTotalGiftBean.size()) {
            return;
        }

        Iterator<DialogGiftBean> iterator = mTotalGiftBean.iterator();
        int count = 0;
        int groupId = 0;
        mPageGiftBean = new List[mTotalGiftBean.size() % SIMPLE_PAGE_MAX_COUNT == 0
                ? mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT
                : mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT + 1];
        mAdapters = new ArrayList<>();
        LiveGiftRecycleAdapter adapter = new LiveGiftRecycleAdapter(mContext);

        mAdapters.add(adapter);
        initView(adapter, groupId);
        while (iterator.hasNext()) {
            DialogGiftBean bean = iterator.next();
            if (count >= SIMPLE_PAGE_MAX_COUNT) {
                count = 0;
                groupId++;
                adapter = new LiveGiftRecycleAdapter(mContext);
                mAdapters.add(adapter);
                initView(adapter, groupId);
            }

            mPageGiftBean[groupId].add(bean);
            adapter.notifyDataSetChanged();
            count++;
        }
    }


    public List<DialogGiftBean> makeList(int page) {
        if (0 == mTotalGiftBean.size()) {
            return null;
        }
        Iterator<DialogGiftBean> iterator = mTotalGiftBean.iterator();
        int count = 0;
        int groupId = 0;
        if (mPageGiftBean.length > 0) {
            mPageGiftBean = null;
        }
        mPageGiftBean = new List[mTotalGiftBean.size() % SIMPLE_PAGE_MAX_COUNT == 0
                ? mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT
                : mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT + 1];
        mPageGiftBean[groupId] = new ArrayList<>();
        while (iterator.hasNext()) {
            DialogGiftBean bean = iterator.next();
            if (count >= SIMPLE_PAGE_MAX_COUNT) {
                count = 0;
                groupId++;
                mPageGiftBean[groupId] = new ArrayList<>();
            }
            mPageGiftBean[groupId].add(bean);
            count++;
        }
        return mPageGiftBean[page];
    }


    public int getTotalPage() {
        if (0 == mTotalGiftBean.size()) {
            return 0;
        }
        return mTotalGiftBean.size() % SIMPLE_PAGE_MAX_COUNT == 0
                ? mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT
                : mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT + 1;
    }

    int oldPosition = -1;
    int currViewPager = 0;

    private void initView(final LiveGiftRecycleAdapter adapter, final int groupId) {
        View giftView = View.inflate(mContext, R.layout.item_gift_board, null);
        mGiftView.add(giftView);
        RecyclerView board = giftView.findViewById(R.id.gift_recycler_view);
        mPageGiftBean[groupId] = new ArrayList<>();
        adapter.setGiftList(mPageGiftBean[groupId]);

        adapter.setOnGiftClickListener(position -> {
            if (null != mGiftListener) {
                mGiftListener.onGiftClicked(mPageGiftBean[groupId].get(position));
                int size = mTotalGiftBean.size() % SIMPLE_PAGE_MAX_COUNT == 0
                        ? mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT
                        : mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT + 1;
                for (int i = 0; i < size; i++) {
                    mAdapters.get(i).setSelect(-1);
                    mAdapters.get(i).notifyDataSetChanged();
                }

                adapter.setSelect(position);
                adapter.notifyDataSetChanged();

            }
        });

        board.setAdapter(adapter);
        board.setLayoutManager(new GridLayoutManager(mContext, SIMPLE_PAGE_MAX_COUNT / 2));
    }

    public void manage(int type) {
        makeGiftView();
        mAdvPager.setAdapter(new AdvAdapter(mGiftView));
        mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
    }


    private static final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {

        }

    }

    public static final class AdvAdapter extends PagerAdapter {
        private List<View> views = null;

        public AdvAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            ((ViewPager) arg0).addView(views.get(arg1), 0);
//            return views.get(arg1);
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
            return POSITION_NONE;
        }


    }
}
