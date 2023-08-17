package com.mshy.VInterestSpeed.common.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.BaseResponse;
import com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean;
import com.mshy.VInterestSpeed.common.bean.gift.GiftNumBean;
import com.mshy.VInterestSpeed.common.bean.gift.GiftTypeBean;
import com.mshy.VInterestSpeed.common.bean.gift.VoiceRoomRefreshCoinEvent;
import com.mshy.VInterestSpeed.common.ui.dialog.gift.LiveGiftPagesManager;
import com.mshy.VInterestSpeed.common.ui.dialog.gift.LiveGiftRecycleAdapter;
import com.mshy.VInterestSpeed.common.ui.dialog.gift.ScaleCircleNavigator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.MagicIndicator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.ViewPagerHelper;
import com.mshy.VInterestSpeed.common.net.GiftApiService;
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage;
import com.mshy.VInterestSpeed.common.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GiftFragment extends Fragment {

    public static GiftFragment newInstance(int type, int position) {

        GiftFragment fragment = new GiftFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("gift_type", type);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_gift, container, false);
        initView(view);
        return view;
    }

    ViewPager child_gift_pager;
    MagicIndicator magicIndicator;
    int type = -1;
    int mPosition = -1;
    LinearLayout gift_empty_layout;
    int giftId = -1;//存一个礼物id，判断包裹里面是否还有这个礼物
    GiftApiService giftApiService;

    private void initView(View view) {
        giftApiService = GlobalServiceManage.INSTANCE.getRetrofit().create(GiftApiService.class);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        type = getArguments().getInt("gift_type", -1);
        mPosition = getArguments().getInt("position", -1);
        child_gift_pager = view.findViewById(R.id.child_gift_pager);


        magicIndicator = view.findViewById(R.id.magic_indicator3);
        gift_empty_layout = view.findViewById(R.id.ll_gift_empty_layout);
        initData();
    }

    @Subscribe
    public void onEventMainThread(VoiceRoomRefreshCoinEvent event) {
        if (type == 0) { //如果是包裹里面的礼物
            giftApiService.vquGetGift(type + "").enqueue(new Callback<BaseResponse<GiftNumBean>>() {
                @Override
                public void onResponse(Call<BaseResponse<GiftNumBean>> call, Response<BaseResponse<GiftNumBean>> response) {
                    getActivity().runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            GiftNumBean result = response.body().getData();
                            if (mGiftManager != null) {
                                if (getTotalPage(result.getGift_list()) < mGiftManager.getTotalPage()) {
                                    for (int p = 0; p < mGiftManager.getTotalPage(); p++) {
                                        LiveGiftRecycleAdapter giftRecycleAdapter = mGiftManager.getmAdapters().get(p);
                                        giftRecycleAdapter.setSelect(-1);
                                    }
                                    for (int i = 0; i < mGiftManager.getTotalPage(); i++) {
                                        if (i > (getTotalPage(result.getGift_list()) - 1)) {
                                            mGiftManager.getmGiftView().remove(i);
                                            child_gift_pager.getAdapter().notifyDataSetChanged();
                                        }
                                    }
                                }

                                mGiftManager.getmTotalGiftBean().clear();
                                mGiftManager.addGiftBean(result.getGift_list());

                                for (int p = 0; p < mGiftManager.getTotalPage(); p++) {
                                    LiveGiftRecycleAdapter giftRecycleAdapter = mGiftManager.getmAdapters().get(p);
                                    List<DialogGiftBean> list = mGiftManager.makeList(p);
                                    if (list != null && list.size() > 0) {

                                        giftRecycleAdapter.setGiftList(list);
                                        giftRecycleAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                            if (giftId != -1) {
                                boolean ishave = false;
                                for (int i = 0; i < result.getGift_list().size(); i++) {
                                    if (giftId == result.getGift_list().get(i).getId()) {
                                        ishave = true;
                                        break;
                                    }
                                }
                                if (!ishave) {
                                    LiveGiftRecycleAdapter giftRecycleAdapter = mGiftManager.getmAdapters().get(child_gift_pager.getCurrentItem());
                                    giftRecycleAdapter.setSelect(-1);
                                    giftRecycleAdapter.notifyDataSetChanged();
                                    EventBus.getDefault().post(new GiftTypeBean(-2, null));
                                }
                            }
                        } else {

                        }
                    });
                }

                @Override
                public void onFailure(Call<BaseResponse<GiftNumBean>> call, Throwable t) {

                }
            });

        }

    }

    //单页最大view数 2的倍数
    private final int SIMPLE_PAGE_MAX_COUNT = 8;

    public int getTotalPage(List<DialogGiftBean> mTotalGiftBean) {
        if (0 == mTotalGiftBean.size()) {
            return 0;
        }
        return mTotalGiftBean.size() % SIMPLE_PAGE_MAX_COUNT == 0
                ? mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT
                : mTotalGiftBean.size() / SIMPLE_PAGE_MAX_COUNT + 1;
    }

    LiveGiftPagesManager mGiftManager;

    public void initData() {
//        String json = GsonUtil.GsonString(result);
        getGiftFormService();
//        try {
//            String json = SpUtils.getString(SpKey.GIFT_DIALOG_BEAN_JSON + "_" + type, "");
//            if (TextUtils.isEmpty(json)) {
//                getGiftFormService();
//            } else {
//                GiftNumBean bean = GsonUtil.GsonToBean(json, GiftNumBean.class);
//                if (bean == null) {
//                    getGiftFormService();
//                } else {
//                    initGift(bean);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            getGiftFormService();
//        }

    }

    private void getGiftFormService() {
        giftApiService.vquGetGift(type + "").enqueue(new Callback<BaseResponse<GiftNumBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<GiftNumBean>> call, Response<BaseResponse<GiftNumBean>> response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (response == null) {
                            return;
                        }
                        if (response.isSuccessful()) {
                            if (response.body() == null) {
                                return;
                            }
                            GiftNumBean result = response.body().getData();

//                            String json = GsonUtil.GsonString(result);
//
//                            SpUtils.putString(SpKey.GIFT_DIALOG_BEAN_JSON + "_" + type, json);

                            initGift(result);
                        } else {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<GiftNumBean>> call, Throwable t) {

            }
        });
    }

    private void initGift(GiftNumBean result) {
        mGiftManager = new LiveGiftPagesManager(child_gift_pager, getActivity());

//                                if (result.getGift_list() != null && result.getGift_list().size() > 0) {
//                                    result.getGift_list().get(0).setIsSelect(1);
//                                }


        mGiftManager.addGiftBean(result.getGift_list());
        mGiftManager.manage(type);
        mGiftManager.setOnGiftItemClickedListener(bean -> {
            if (type != -1) {
                giftId = bean.getId();
                EventBus.getDefault().post(new GiftTypeBean(type, bean));
            }
        });

        if (mPosition == 0 && result.getGift_list() != null && result.getGift_list().size() > 0) {
            giftId = result.getGift_list().get(0).getId();
            EventBus.getDefault().post(new GiftTypeBean(type, result.getGift_list().get(0)));
            if (mGiftManager.getmAdapters() != null && mGiftManager.getmAdapters().size() > 0) {
                LiveGiftRecycleAdapter liveGiftRecycleAdapter = mGiftManager.getmAdapters().get(0);
                if (liveGiftRecycleAdapter != null) {
                    liveGiftRecycleAdapter.setSelect(0);
                    liveGiftRecycleAdapter.notifyDataSetChanged();
                }
            }
        }

        if (mGiftManager.getmGiftView().size() > 0) {

            ScaleCircleNavigator scaleCircleNavigator = new ScaleCircleNavigator(getActivity());
            scaleCircleNavigator.setNormalCircleColor(Color.parseColor("#FFD7EC"));
            scaleCircleNavigator.setSelectedCircleColor(Color.parseColor("#FF7AC2"));
            scaleCircleNavigator.setMaxRadius(UiUtils.dip2px(BaseApplication.application, 3f));
            scaleCircleNavigator.setMinRadius(UiUtils.dip2px(BaseApplication.application, 2f));
            scaleCircleNavigator.setCircleCount(mGiftManager.getmGiftView().size());
//            scaleCircleNavigator.setCount(mGiftManager.getmGiftView().size());
//                                    scaleCircleNavigator.setNormalCircleColor(Color.DKGRAY);
//                                    scaleCircleNavigator.setMaxRadius(10);
//                                    scaleCircleNavigator.setMinRadius(3);
//                                    scaleCircleNavigator.setSelectedCircleColor(Color.LTGRAY);
//                                    scaleCircleNavigator.setCircleClickListener(new ScaleLineNavigator.OnCircleClickListener() {
//                                        @Override
//                                        public void onClick(int index) {
//                                            child_gift_pager.setCurrentItem(index);
//                                        }
//                                    });
            magicIndicator.setNavigator(scaleCircleNavigator);
            ViewPagerHelper.bind(magicIndicator, child_gift_pager);
            magicIndicator.setVisibility(View.VISIBLE);
            gift_empty_layout.setVisibility(View.GONE);
        } else {
            magicIndicator.setVisibility(View.INVISIBLE);
            gift_empty_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
