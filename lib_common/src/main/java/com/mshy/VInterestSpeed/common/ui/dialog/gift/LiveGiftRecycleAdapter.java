package com.mshy.VInterestSpeed.common.ui.dialog.gift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.gift.DialogGiftBean;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;

import java.util.ArrayList;
import java.util.List;

public class LiveGiftRecycleAdapter extends RecyclerView.Adapter {
    public final static String IMAGE_URL = "https://asset.whzhenban.top/";
    private List<DialogGiftBean> mGiftBeanList = new ArrayList<>();
    private final Context mContext;
    private OnGiftClickListener mListener;
    private int selectPosition = -1;

    public interface OnGiftClickListener {
        void onClicked(int position);
    }

    public void setOnGiftClickListener(OnGiftClickListener listener) {
        mListener = listener;
    }

    public LiveGiftRecycleAdapter(Context context) {
        mContext = context;
    }

    public void setGiftList(List<DialogGiftBean> giftBeans) {
        mGiftBeanList = giftBeans;
    }

    public void setSelect(int position) {
        this.selectPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_live_gift, null);
        return new Holder(mContext, view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        DialogGiftBean giftBean = mGiftBeanList.get(position);
        Holder dataHolder = (Holder) holder;
//        SharedPreferences sp = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
//        String imageUrl = sp.getString("baseImageUrl",IMAGE_URL);
        Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + giftBean.getImg()).into(dataHolder.mGiftLogo);
        // dataHolder.mGiftLogo.setImageURI();
        dataHolder.mName.setText(giftBean.getName());
        dataHolder.mCost.setText(String.valueOf(giftBean.getPrice()));
        if (giftBean.getNum() > 0) {
            dataHolder.stv_txt.setVisibility(View.VISIBLE);
            dataHolder.stv_txt.setText("x" + giftBean.getNum());
            dataHolder.stv_txt.setTextColor(Color.parseColor("#FF7AC2"));
            dataHolder.llItem.setBackgroundResource(0);
        } else {
            dataHolder.stv_txt.setVisibility(View.INVISIBLE);
        }

        if (giftBean.getSign() != null && !giftBean.getSign().equals("")) {
            dataHolder.stv_sign.setVisibility(View.VISIBLE);
            dataHolder.stv_sign.setText(giftBean.getSign() + "");
        } else {
            dataHolder.stv_sign.setVisibility(View.GONE);
        }


        if (selectPosition == position) {
            dataHolder.llItem.setBackgroundResource(R.drawable.shape_gift_orange_bg);
        } else {
            dataHolder.llItem.setBackgroundResource(0);
        }

        dataHolder.mThisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGiftBeanList.size();
    }

    public class Holder extends ViewHolder {
        ImageView mGiftLogo;
        TextView mName;
        TextView mCost;
        View mThisView;
        RelativeLayout llItem;
        TextView stv_txt;
        TextView stv_sign;


        public Holder(Context context, View itemView) {
            super(context, itemView);
            stv_txt = itemView.findViewById(R.id.stv_txt);
            stv_sign = itemView.findViewById(R.id.stv_sign);
            mGiftLogo = itemView.findViewById(R.id.gift_photo);
            mName = itemView.findViewById(R.id.gift_name);
            mCost = itemView.findViewById(R.id.gift_cost);
            llItem = itemView.findViewById(R.id.ll_item);
            mThisView = itemView;
        }
    }
}
