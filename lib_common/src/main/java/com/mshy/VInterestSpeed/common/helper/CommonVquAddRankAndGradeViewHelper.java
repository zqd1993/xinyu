package com.mshy.VInterestSpeed.common.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.CommonVquGradeConfigBean;
import com.mshy.VInterestSpeed.common.bean.CommonVquNormalBadgeConfigBean;
import com.mshy.VInterestSpeed.common.bean.CommonVquRankConfigBean;
import com.mshy.VInterestSpeed.common.ui.view.ShapeLinearLayout;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;


public class CommonVquAddRankAndGradeViewHelper {
    /**
     * 添加等级
     */
    @SuppressLint("SetTextI18n")
    public static void addGradeView(Context mContext, ViewGroup viewGroup, CommonVquGradeConfigBean bean) {
        ImageView ivGradeBg = viewGroup.findViewById(R.id.iv_grade_bg);
        ImageView ivGradeIcon = viewGroup.findViewById(R.id.iv_grade_icon);
        TextView tvGrade = viewGroup.findViewById(R.id.tv_grade);
        TextView tvGradeNum = viewGroup.findViewById(R.id.tv_grade_num);
        FrameLayout flGrade = viewGroup.findViewById(R.id.fl_grade);
        if (bean != null) {
            flGrade.setVisibility(View.VISIBLE);
            //添加等级icon
            FrameLayout.LayoutParams ivGradeIconLayoutParams = (FrameLayout.LayoutParams) ivGradeIcon.getLayoutParams();
            ivGradeIconLayoutParams.width = UIUtil.dip2px(mContext, Float.parseFloat(bean.getIcon_width()));
            ivGradeIcon.setLayoutParams(ivGradeIconLayoutParams);
            Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL()+ bean.getImg1())
                    .into(ivGradeIcon);
            //添加等级背景
            FrameLayout.LayoutParams ivGradeBgLayoutParams = (FrameLayout.LayoutParams) ivGradeBg.getLayoutParams();
            ivGradeBgLayoutParams.leftMargin = UIUtil.dip2px(mContext, Float.parseFloat(bean.getIcon_width()) / 2);
            ivGradeBgLayoutParams.width = UIUtil.dip2px(mContext, Float.parseFloat(bean.getIcon1_width()));
            ivGradeBg.setLayoutParams(ivGradeBgLayoutParams);
            Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + bean.getImg2())
                    .into(ivGradeBg);
            //添加等级文字
            FrameLayout.LayoutParams tvGradeParams = (FrameLayout.LayoutParams) tvGrade.getLayoutParams();
            tvGradeParams.leftMargin =UIUtil.dip2px(mContext, (Float.parseFloat(bean.getIcon_width())));
            tvGrade.setLayoutParams(tvGradeParams);
            tvGrade.setText(bean.getGrade_name());

            FrameLayout.LayoutParams tvGradeNumParams = (FrameLayout.LayoutParams) tvGradeNum.getLayoutParams();
            tvGradeNumParams.leftMargin = UIUtil.dip2px(mContext, (Float.parseFloat(bean.getIcon_width()) / 4 * 3));
            tvGradeNum.setLayoutParams(tvGradeNumParams);
            AssetManager assetsManager = mContext.getAssets();//得到AssetManager
            Typeface tf = Typeface.createFromAsset(assetsManager, "fonts/mini.ttf");//根据路径得到Typeface
            tvGradeNum.setTypeface(tf);
            tvGradeNum.setText(String.valueOf(bean.getGrade()));
        } else {
            flGrade.setVisibility(View.GONE);
        }
    }

    /**
     * 添加排名
     */
    @SuppressLint("SetTextI18n")
    public static void addRankView(Context mContext, ViewGroup viewGroup, CommonVquRankConfigBean bean) {
        FrameLayout flRank = viewGroup.findViewById(R.id.fl_rank);
        if (bean != null && !TextUtils.isEmpty(bean.getImg())) {
            ImageView ivRank = viewGroup.findViewById(R.id.iv_rank);
            Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + bean.getImg())
                    .into(ivRank);
            flRank.setVisibility(View.VISIBLE);
        } else {
            flRank.setVisibility(View.GONE);
        }
    }

    /**
     * 添加贵族标识
     *
     * @param mContext
     * @param viewGroup
     * @param vip_icon
     */
    public static void addNobleView(Context mContext, ViewGroup viewGroup, String vip_icon) {
        FrameLayout flNobleBadge = viewGroup.findViewById(R.id.fl_noble_badge);
        if (!TextUtils.isEmpty(vip_icon)) {
            ImageView ivNobleBadge = viewGroup.findViewById(R.id.iv_noble_badge);
            Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + vip_icon)
                    .into(ivNobleBadge);
            flNobleBadge.setVisibility(View.VISIBLE);
        } else {
            flNobleBadge.setVisibility(View.GONE);
        }
    }


    /**
     * 添加性别
     *
     * @param mContext
     * @param viewGroup
     * @param gender    性别
     * @param age       年龄
     */
    public static void addGender(Context mContext, ViewGroup viewGroup, int gender, String age) {
        ShapeLinearLayout sll_gender = viewGroup.findViewById(R.id.sll_gender);
        ImageView ivGenderIncon = viewGroup.findViewById(R.id.iv_gender);
        TextView tv_age = viewGroup.findViewById(R.id.tv_age);
        if (gender == 2) {
            tv_age.setText(String.valueOf(age));
            tv_age.setTextColor(Color.parseColor("#FFFFFF"));
            sll_gender.setSolidColor(Color.parseColor("#4E9EFE"));
            ivGenderIncon.setImageResource(R.mipmap.resources_vqu_newboy);
            sll_gender.setVisibility(View.VISIBLE);
        } else if (gender == 1) {
            tv_age.setText(String.valueOf(age));
            sll_gender.setSolidColor(Color.parseColor("#FF73A1"));
            tv_age.setTextColor(Color.parseColor("#FFFFFF"));
            ivGenderIncon.setImageResource(R.mipmap.resources_vqu_newgirl);
            sll_gender.setVisibility(View.VISIBLE);
        } else {
            sll_gender.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(age)){
            tv_age.setVisibility(View.VISIBLE);
        }else{
            tv_age.setVisibility(View.GONE);
        }
    }


    /**
     * 添加普通勋章
     *
     * @param mContext
     * @param viewGroup
     * @param bean
     */
    public static void addNormalBadgeView(Context mContext, ViewGroup viewGroup, CommonVquNormalBadgeConfigBean bean) {
        if (bean != null && !TextUtils.isEmpty(bean.getFile())) {
            ImageView ivNormalBadge = viewGroup.findViewById(R.id.iv_normal_badge);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivNormalBadge.getLayoutParams();
            params.width = UIUtil.dip2px(mContext, Float.parseFloat(bean.getWidth()));
            ivNormalBadge.setLayoutParams(params);
            Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + bean.getFile()).into(ivNormalBadge);
            viewGroup.findViewById(R.id.fl_normal_badge).setVisibility(View.VISIBLE);
        } else {
            viewGroup.findViewById(R.id.fl_normal_badge).setVisibility(View.GONE);
        }
    }

    /**
     * 添加普通勋章
     *
     * @param mContext
     * @param viewGroup
     * @param bean
     */
    public static void addBadgeView(Context mContext, ViewGroup viewGroup, CommonVquNormalBadgeConfigBean bean) {
        if (bean != null && !TextUtils.isEmpty(bean.getFile())) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.common_vqu_include_normal_badge, null);
            ImageView ivNormalBadge = view.findViewById(R.id.iv_normal_badge);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivNormalBadge.getLayoutParams();
            params.width = UIUtil.dip2px(mContext, Float.parseFloat(bean.getWidth()));
            params.rightMargin = UIUtil.dip2px(mContext, 5);
            ivNormalBadge.setLayoutParams(params);
            Glide.with(mContext).load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + bean.getFile()).into(ivNormalBadge);
            view.findViewById(R.id.fl_normal_badge).setVisibility(View.VISIBLE);
            viewGroup.addView(view);
        } else {
            viewGroup.findViewById(R.id.fl_normal_badge).setVisibility(View.GONE);
        }
    }
}
