package com.mshy.VInterestSpeed.common.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.live.vquonline.base.ktx.SizeUnitKtxKt;
import com.live.vquonline.base.utils.DateUtils;
import com.live.vquonline.base.utils.SpUtils;
import com.live.vquonline.base.utils.ToastUtils;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.BaseResponse;
import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean;
import com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean;
import com.mshy.VInterestSpeed.common.bean.gift.GiftCateListBean;
import com.mshy.VInterestSpeed.common.bean.gift.GiftListBean;
import com.mshy.VInterestSpeed.common.bean.gift.GiftTypeBean;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;
import com.mshy.VInterestSpeed.common.net.GiftApiService;
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage;
import com.mshy.VInterestSpeed.common.ui.adapter.CommonVquMainPageAdapter;
import com.mshy.VInterestSpeed.common.ui.fragment.GiftFragment;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.MagicIndicator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.badge.BadgeAnchor;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.commonnavigator.titles.badge.BadgeRule;
import com.mshy.VInterestSpeed.common.utils.UiUtils;
import com.mshy.VInterestSpeed.common.utils.UmUtils;
import com.xujiaji.happybubble.BubbleDialog;
import com.xujiaji.happybubble.BubbleLayout;
import com.xujiaji.happybubble.Util;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 送礼物弹窗
 */
public class BottomGiftFragmentDialog extends BaseDialogFragment {

    private View view;
    MagicIndicator gift_magic_indicator;
    private ViewPager mViewPage;
    private final List<Fragment> mFragments = new ArrayList<>();
    TextView mCaiBiText;
    private View mCaiBiLayout;
    TextView mTvGiveGift;

    int PageSelected = 0;
    private boolean isAutoTouchClose = false;
    private boolean isSendGift = false;
    Map map;
    int sendGiftType = 0;  // 0 默认送礼  1悄悄送礼
    CheckBox sendGiftGone;
    LinearLayout ll_gift_invisiable;

    //默认赠送礼物数
    private int mGiftCount = 1;
    private TextView mEtCount;
    private ImageView mIvAdd;
    private ImageView mIvMin;
    LinearLayout show_select_number_gift;
    private int send_gift_hide = 0;//是否悄悄送礼 0不显示 1是 2否
    GiftApiService giftApiService;

    @Override
    protected void initView(View view) {
        this.view = view;
        giftApiService = GlobalServiceManage.INSTANCE.getRetrofit().create(GiftApiService.class);
        mDataList = new ArrayList<>();
        map = new HashMap<Integer, GiftTypeBean>();
        mEtCount = view.findViewById(R.id.et_count);
        mIvAdd = view.findViewById(R.id.iv_add);
        show_select_number_gift = view.findViewById(R.id.show_select_number_gift);
        mIvMin = view.findViewById(R.id.iv_min);
        sendGiftGone = view.findViewById(R.id.cb_send_gift_invisible);
        ll_gift_invisiable = view.findViewById(R.id.ll_gift_invisiable);
        gift_magic_indicator = view.findViewById(R.id.gift_magic_indicator);
        mViewPage = view.findViewById(R.id.gift_pager);
        mCaiBiText = view.findViewById(R.id.gift_shell_number);
        mCaiBiLayout = view.findViewById(R.id.gift_shell_layout);
        mTvGiveGift = view.findViewById(R.id.tv_give_gift);
        mCaiBiLayout.setOnClickListener(v -> {
            if (null != mGiftListener) {
                dismiss();
                mGiftListener.onWalletClicked();
            }
        });
        initAdvert(view);

        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().startsWith("0")) {
                    s.delete(0, 1);
                    mEtCount.setText(s.toString().trim());
                }
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    int count = Integer.parseInt(s.toString().trim());
                    if (count > 999) {
                        mGiftCount = 999;
                        mEtCount.setText(String.valueOf(mGiftCount));
                    } else {
                        mGiftCount = count;
                    }
                }
            }
        });
        mIvMin.setOnClickListener(v -> {
            if (mGiftCount > 1) {
                mGiftCount--;
                mEtCount.setText(String.valueOf(mGiftCount));
            }
        });
        mEtCount.setOnClickListener(v -> {
//                if (mGiftCount < 999) {
//                    mGiftCount++;
//                    mEtCount.setText(String.valueOf(mGiftCount));
//                }

            List<String> list = new ArrayList<>();
            list.add("520");
            list.add("99");
            list.add("66");
            list.add("10");
            list.add("5");
            list.add("1");
            showSelectGiftNum(list);
        });


        mIvAdd.setOnClickListener(v -> {
//                if (mGiftCount < 999) {
//                    mGiftCount++;
//                    mEtCount.setText(String.valueOf(mGiftCount));
//                }

            List<String> list = new ArrayList<>();
            list.add("520");
            list.add("99");
            list.add("66");
            list.add("10");
            list.add("5");
            list.add("1");
            showSelectGiftNum(list);
        });

        mTvGiveGift.setOnClickListener(view1 -> {
            if (null != mGiftListener) {
                UmUtils.setUmEvent(getActivity(), UmUtils.GIVEAWAYGIFT);
                if (map.get(PageSelected) != null) {
                    if (map.get(PageSelected) != null) {
                        if (PageSelected < now_result.size()) {
                            ((DialogGiftBean) map.get(PageSelected)).setGift_type_id(now_result.get(PageSelected).getId() + "");
                        }
                    }
                    mGiftListener.onGiftClicked(((DialogGiftBean) map.get(PageSelected)), mGiftCount);
                } else {
                    ToastUtils.showToast("请选择礼物", 1500);
                    return;
                }
                isSendGift = true;

                if (isAutoTouchClose) {
                    startAutoDown();
                } else {
                    dismiss();
                }
            }
        });
        view.findViewById(R.id.ll_gift_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isSendGift) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startAutoDown();
                    }
                }
                return false;
            }

        });

        EventBus.getDefault().register(this);
        //礼物分类
        getGiftCate();
        sendGiftType = getArguments().getInt("sendGiftType", 0);

    }


    private Banner mAdBanner;
    private ImageView mIvAdClose;

    /**
     * 初始化广告控件
     */
    private void initAdvert(View view) {
        mAdBanner = view.findViewById(R.id.vqu_banner_ad);
        mIvAdClose = view.findViewById(R.id.vqu_iv_close);
        mIvAdClose.setVisibility(View.GONE);
        mIvAdClose.setOnClickListener(v -> {
            mAdBanner.setVisibility(View.GONE);
            mIvAdClose.setVisibility(View.GONE);
        });
        getAdvert();
    }

    private void notifyAdBanner(final CommonVquAdBean result) {
        if (result != null && result.getIs_open() == 1) {
            mAdBanner.setVisibility(View.VISIBLE);
        } else {
            mAdBanner.setVisibility(View.GONE);
            return;
        }

        if (!result.getList().isEmpty()) {
            List<String> images = new ArrayList<>();
            for (int i = 0; i < result.getList().size(); i++) {
                images.add(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + result.getList().get(i).getImage());
            }
            mAdBanner.addBannerLifecycleObserver(this)
                    .setOrientation(Banner.HORIZONTAL)
                    //.setIndicator(null,false)
                    .setLoopTime(3000)
                    .setAdapter(new BannerImageAdapter(images) {

                        @Override
                        public void onBindView(Object holder, Object data, int position, int size) {
                            if (holder instanceof BannerImageHolder) {
                                ImageExtKt.vquLoadRoundImage(((BannerImageHolder) holder).imageView, data, SizeUnitKtxKt.dp2px(getContext(), 6f));
                            }
                        }
                    });
        } else {
            mAdBanner.setVisibility(View.GONE);
            mIvAdClose.setVisibility(View.GONE);
        }

    }

    /**
     * 获取广告信息
     */
    private void getAdvert() {
        giftApiService.vquJavaGetAdvert("5").enqueue(new Callback<BaseResponse<CommonVquAdBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<CommonVquAdBean>> call, Response<BaseResponse<CommonVquAdBean>> response) {
                FragmentActivity activity = getActivity();

                if (activity == null || activity.isDestroyed()) {
                    return;
                }
                activity.runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        BaseResponse<CommonVquAdBean> bsae = response.body();
                        notifyAdBanner(bsae.getData());
                    } else {
                        notifyAdBanner(null);
                    }
                });
            }

            @Override
            public void onFailure(Call<BaseResponse<CommonVquAdBean>> call, Throwable t) {
                FragmentActivity activity = getActivity();

                if (activity == null || activity.isDestroyed()) {
                    return;
                }
//                if (getActivity().isDestroyed()) {
//                    return;
//                }
                activity.runOnUiThread(() -> notifyAdBanner(null));
            }
        });
    }


    /**
     * 是否开启自动关闭礼物弹窗
     * 触发条件 赠送完礼物 三秒内没操作自动关闭
     *
     * @param autoTouchClose
     */
    public void setAutoTouchClose(boolean autoTouchClose) {
        isAutoTouchClose = autoTouchClose;
    }

    @Subscribe
    public void onEventMainThread(GiftTypeBean event) {
        map.put(PageSelected, event.getDialogGiftBean());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dialog_gift;
    }

    @Override
    public void onResume() {
        super.onResume();
        getGiftBeans();
    }

    List<GiftCateListBean> now_result;

    public void getGiftCate() {
        now_result = new ArrayList<>();
        getGiftCateFormService();
        /*
        try {
            String giftJson = SpUtils.getString(SpKey.GIFT_DIALOG_TAB_JSON, "");
            if (TextUtils.isEmpty(giftJson)) {
                getGiftCateFormService();
            } else {
                List<GiftCateListBean> beans = GsonUtil.jsonToList(giftJson, GiftCateListBean.class);
                if (!beans.isEmpty()) {
                    initGiftTab(beans);
                } else {
                    getGiftCateFormService();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            getGiftCateFormService();
        }

         */
    }

    private void getGiftCateFormService() {
        giftApiService.vquGetGiftCate().enqueue(new Callback<BaseResponse<List<GiftCateListBean>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<GiftCateListBean>>> call, Response<BaseResponse<List<GiftCateListBean>>> response) {
                FragmentActivity activity = getActivity();

                if (activity == null || activity.isDestroyed()) {
                    return;
                }

                activity.runOnUiThread(() -> {
                    if (response.isSuccessful()) {

                        List<GiftCateListBean> result = response.body().getData();

//                        String s = GsonUtil.GsonString(result);
//                        SpUtils.putString(SpKey.GIFT_DIALOG_TAB_JSON, s);

                        initGiftTab(result);
                    }
                });
            }


            @Override
            public void onFailure(Call<BaseResponse<List<GiftCateListBean>>> call, Throwable t) {

            }
        });
    }

    private void initGiftTab(List<GiftCateListBean> result) {

        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                now_result.add(result.get(i));
                mFragments.add(GiftFragment.newInstance(result.get(i).getId(), i));
                mDataList.add(result.get(i).getName());
                send_gift_hide = result.get(i).getSend_gift_hide();
            }
            setSendGiftVisiable();
            initMagicIndicator();
            CommonVquMainPageAdapter adapter = new CommonVquMainPageAdapter(getChildFragmentManager(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            adapter.setData(mFragments);
            mViewPage.setAdapter(adapter);
//                                mViewPage.setCurrentItem(1);
        }
    }

    List<String> mDataList;

    private void initMagicIndicator() {

        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setMiddle(true);
        commonNavigator.setSpacePx(UiUtils.dip2px(getActivity(), 20));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.showRedPoint(true);
                simplePagerTitleView.setNormalColor(Color.parseColor("#999999"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#B4A3FD"));
                simplePagerTitleView.setOnClickListener(v -> mViewPage.setCurrentItem(index));

                BadgePagerTitleView badgePagerTitleView = new BadgePagerTitleView(context);
                badgePagerTitleView.setInnerPagerTitleView(simplePagerTitleView);

                Long lastTime = SpUtils.INSTANCE.getLong("gift_show_red_point_id_" + now_result.get(index).getId(), 0L);

                long nowTime = System.currentTimeMillis();

                if (lastTime == null) {
                    lastTime = 0L;
                }

                String lastDD = DateUtils.INSTANCE.getDateFormatString(lastTime, "dd");
                String nowDD = DateUtils.INSTANCE.getDateFormatString(nowTime, "dd");

                if (now_result.get(index).getDot() == 1 && !nowDD.equals(lastDD)) {
                    SpUtils.INSTANCE.putLong("gift_show_red_point_id_" + now_result.get(index).getId(), nowTime);
                    ImageView badgeImageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.simple_red_dot_badge_layout, null);
                    badgePagerTitleView.setBadgeView(badgeImageView);
                    badgePagerTitleView.setXBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_RIGHT, -UiUtils.dip2px(context, 3f)));
                    badgePagerTitleView.setYBadgeRule(new BadgeRule(BadgeAnchor.CONTENT_TOP, UIUtil.dip2px(context, 2)));
                }

                return badgePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 4));
                indicator.setLineWidth(UIUtil.dip2px(context, 8));
                indicator.setRoundRadius(UiUtils.dip2px(context, 2f));
                indicator.setColors(Color.parseColor("#B4A3FD"));
                indicator.setYOffset(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        6, getResources().getDisplayMetrics()));
                return indicator;
            }
        });

        gift_magic_indicator.setNavigator(commonNavigator);
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                gift_magic_indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                gift_magic_indicator.onPageSelected(position);
                PageSelected = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                gift_magic_indicator.onPageScrollStateChanged(state);
            }
        });

    }

    /**
     * 刷新金币
     */
    public void getGiftBeans() {

        giftApiService.vquGetGiftBeans().enqueue(new Callback<BaseResponse<GiftListBean>>() {
            @Override
            public void onResponse(Call<BaseResponse<GiftListBean>> call, Response<BaseResponse<GiftListBean>> response) {
                FragmentActivity activity = getActivity();

                if (activity == null || activity.isDestroyed()) {
                    return;
                }

                activity.runOnUiThread(() -> {
                    if (response == null) {
                        return;
                    }
                    if (response.isSuccessful()) {
                        GiftListBean result = response.body().getData();
                        mCaiBiText.setText(String.valueOf(result.getCoin()));
                    }
                });
            }


            @Override
            public void onFailure(Call<BaseResponse<GiftListBean>> call, Throwable t) {

            }
        });


    }

    BubbleDialog bubbleDialog;

    private void showSelectGiftNum(List<String> numList) {
        LinearLayout pop_view = (LinearLayout) this.getLayoutInflater().inflate(R.layout.dialog_easy_gift_tip_pop, null);

        for (int i = 0; i < numList.size(); i++) {
            TextView textView = new TextView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getActivity().getResources().getDimension(R.dimen.dp_size_90), (int) getActivity().getResources().getDimension(R.dimen.dp_size_30));
            layoutParams.gravity = Gravity.CENTER;
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(Color.parseColor("#222222"));
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            textView.setText(numList.get(i));
            textView.setOnClickListener(v -> {

                if (bubbleDialog != null) {
                    ((TextView) v).setTextColor(Color.parseColor("#FF0084"));
                    mEtCount.setText(((TextView) v).getText().toString().trim());
                    mIvAdd.setImageResource(R.mipmap.bottom_dialog);
                    bubbleDialog.dismiss();
                }


            });


            pop_view.addView(textView);
        }

        mIvAdd.setImageResource(R.mipmap.top_dialog);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            BubbleLayout bl = new BubbleLayout(activity);
            bl.setBubbleColor(Color.parseColor("#F5F5F5"));
//            bl.setBubbleBorderColor(ContextCompat.getColor(getActivity(), R.color.color_7C69FE));
//            bl.setBubbleBorderSize(3);
            bl.setLookLength(Util.dpToPx(getActivity(), 9));
            bl.setLookWidth(Util.dpToPx(getActivity(), 12));
            bl.setBubbleRadius(Util.dpToPx(getActivity(), 8));
            bubbleDialog = new BubbleDialog(getActivity());
            bubbleDialog.setBubbleContentView(pop_view);
            bubbleDialog.setClickedView(mIvAdd);
            bubbleDialog.setBubbleLayout(bl);
            bubbleDialog.show();
        }
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }


    public interface OnGiftItemClickedListener {
        void onGiftClicked(DialogGiftBean bean, int giftCount);

        void onWalletClicked();
    }

    public void setOnGiftItemClickedListener(OnGiftItemClickedListener listener) {
        mGiftListener = listener;
    }

    private OnGiftItemClickedListener mGiftListener;


    @Override
    protected int getWidth() {
        int width = 0;
        try {
            width = UIUtil.getScreenWidth(requireContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return width;
    }


    private Disposable mdDisposable;

    private void startAutoDown() {
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
        mdDisposable = Flowable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> Log.e("accept", aLong + "开始了吗"))
                .doOnComplete(() -> {
                    //倒计时完毕置为可点击状态
                    if (mdDisposable != null) {
                        mdDisposable.dispose();
                        mdDisposable = null;
                    }
                    Dialog dialog = getDialog();
                    if (dialog != null && getDialog().isShowing()) {
                        dismiss();
                    }
                })
                .subscribe();
    }

    public void setSendGiftVisiable() {
        if (sendGiftType == 1) {
            if (send_gift_hide == 0) { // 是否悄悄送礼 0不显示 1是 2否
                ll_gift_invisiable.setVisibility(View.GONE);
            } else if (send_gift_hide == 1) { // 已经打开悄悄送礼
                ll_gift_invisiable.setVisibility(View.VISIBLE);
                sendGiftGone.setChecked(true);
            } else { // 已经关闭悄悄
                sendGiftGone.setChecked(false);
                ll_gift_invisiable.setVisibility(View.VISIBLE);
            }
        } else {
            ll_gift_invisiable.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mdDisposable != null) {
            mdDisposable.dispose();
            mdDisposable = null;
        }
    }
}