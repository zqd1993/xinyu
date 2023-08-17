package com.live.module.home.adapter;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.ellison.glide.translibrary.ImageUtils;
import com.ellison.glide.translibrary.base.LoaderBuilder;
import com.live.module.home.R;
import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.constant.RouteKey;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.ext.ImageExtKt;
import com.mshy.VInterestSpeed.common.helper.CommonVquWebUrlHelper;
import com.mshy.VInterestSpeed.common.ui.view.ShapeTextView;
import com.mshy.VInterestSpeed.common.utils.UiUtils;
import com.live.vquonline.view.main.bean.HomeVquOnTvBean;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * 自定义布局，图片+标题+数字指示器
 */
public class TVNumAdapter extends BannerAdapter<HomeVquOnTvBean, TVNumAdapter.BannerViewHolder> {

    public TVNumAdapter(List<HomeVquOnTvBean> mDatas) {
        //设置数据，也可以调用banner提供的方法
        super(mDatas);
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        //注意布局文件，item布局文件要设置为match_parent，这个是viewpager2强制要求的
        //或者调用BannerUtils.getView(parent,R.layout.banner_image_title_num);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_tanta_view_banner_main_home_tv, parent, false);
        return new BannerViewHolder(view);
    }

    //绑定数据
    @Override
    public void onBindView(BannerViewHolder holder, HomeVquOnTvBean data, int position, int size) {
        ImageExtKt.vquLoadImage(holder.vqu_gift_img, NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + data.getGift_img());
        holder.vqu_gift_count.setText(data.getGift_count()+"个");
        holder.vqu_tv_from_name.setText(data.getFrom_nickname());
        holder.vqu_tv_to_name.setText(data.getTo_nickname());
        holder.vqu_gift_name.setText(data.getGift_name());

        LoaderBuilder builderConfig =new LoaderBuilder()
                .round(UiUtils.dip2px(BaseApplication.context, 20f))
                .circle(true)
                .width(UiUtils.dip2px(BaseApplication.context, 40))
                .height(UiUtils.dip2px(BaseApplication.context, 40))
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .round(new float[]{
                                UiUtils.dip2px(BaseApplication.context, 20f),
                                UiUtils.dip2px(BaseApplication.context, 20f),
                                UiUtils.dip2px(BaseApplication.context, 20f),
                                UiUtils.dip2px(BaseApplication.context, 20f)})
                .borderColor(ContextCompat.getColor(BaseApplication.context, R.color.color_FFFFFF))
                .borderWidth(UiUtils.dip2px(BaseApplication.context, 1f))
                .placeholder(R.mipmap.ic_common_head_circle_def)
                .load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + data.getFrom_avatar());
        ImageUtils.getInstance().bind(holder.vqu_from_iv, builderConfig);
        if(data.getLock_time()>0){
            LogUtils.d(data.getLock_time()+"vquStartCountDown");
            vquStartCountDown(data,position,holder);
        }else {
            holder.vqu_bt_know_url.setText("如何上电视");
            holder.vqu_bt_know_url.setTextColor(Color.parseColor("#222222"));
            holder.vqu_bt_know_url.setStrokeColor(Color.parseColor("#222222"));
            holder.vqu_bt_know_url.setClickable(true);
            holder.vqu_bt_know_url.setEnabled(true);
        }
        holder.vqu_bt_know_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity).withString(
                        RouteKey.URL,
                        CommonVquWebUrlHelper.getInstance().getWebUrl().getTv()
                ).navigation();
            }
        });
        holder.iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(RouteUrl.Common.WebViewActivity).withString(
                        RouteKey.URL,
                        CommonVquWebUrlHelper.getInstance().getWebUrl().getTv()
                ).navigation();
            }
        });
    }


    void vquStartCountDown(HomeVquOnTvBean data,int position,BannerViewHolder holder){
        CountDownTimer vquCountDownTimer = new CountDownTimer(data.getLock_time()*1000,1000){

            @Override
            public void onTick(long l) {
                String vquValue = (l / 1000)+"";
                holder.vqu_bt_know_url.setText("锁屏中" + vquValue + "秒");
                holder.vqu_bt_know_url.setTextColor(Color.parseColor("#A3AABE"));
                holder.vqu_bt_know_url.setStrokeColor(Color.parseColor("#A3AABE"));
                holder.vqu_bt_know_url.setClickable(false);
                holder.vqu_bt_know_url.setEnabled(false);
            }

            @Override
            public void onFinish() {
                data.setLock_time(0);
                holder.vqu_bt_know_url.setText("如何上电视");
                holder.vqu_bt_know_url.setTextColor(Color.parseColor("#222222"));
                holder.vqu_bt_know_url.setStrokeColor(Color.parseColor("#222222"));
                holder.vqu_bt_know_url.setClickable(true);
                holder.vqu_bt_know_url.setEnabled(true);
            }
        };
        vquCountDownTimer.start();
    }



    class BannerViewHolder extends RecyclerView.ViewHolder {

        ImageView vqu_gift_img;
        ImageView vqu_from_iv;
        TextView vqu_tv_from_name;
        TextView vqu_tv_to_name;
        TextView vqu_gift_count;
        TextView vqu_gift_name;
        ShapeTextView vqu_bt_know_url;
        ImageView iv_right;
        public BannerViewHolder(@NonNull View view) {
            super(view);
            vqu_gift_name = view.findViewById(R.id.vqu_gift_name);
            vqu_from_iv = view.findViewById(R.id.vqu_from_iv);
            vqu_bt_know_url = view.findViewById(R.id.vqu_bt_know_url);
            vqu_gift_img = view.findViewById(R.id.vqu_gift_img);
            vqu_tv_from_name = view.findViewById(R.id.vqu_tv_from_name);
            vqu_tv_to_name = view.findViewById(R.id.vqu_tv_to_name);
            vqu_gift_count = view.findViewById(R.id.vqu_gift_count);
            iv_right = view.findViewById(R.id.iv_right);
        }
    }

}
