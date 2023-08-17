package com.faceunity.nama.repo;


import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.faceunity.core.controller.facebeauty.FaceBeautyParam;
import com.faceunity.core.entity.FUBundleData;

import com.faceunity.core.model.facebeauty.FaceBeauty;
import com.faceunity.core.model.facebeauty.FaceBeautyFilterEnum;
import com.faceunity.nama.DemoConfig;
import com.faceunity.nama.entity.FaceBeautyBean;
import com.faceunity.nama.entity.FaceBeautyFilterBean;
import com.faceunity.nama.entity.FaceBeautySaveBean;
import com.faceunity.nama.entity.ModelAttributeData;
import com.faceunity.nama.utils.FuDeviceUtils;
import com.live.faceunity.R;
import com.live.vquonline.base.utils.GsonUtil;
import com.live.vquonline.base.utils.SpUtils;
import com.live.vquonline.base.utils.ToastUtils;
import com.mshy.VInterestSpeed.common.constant.SpKey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DESC：美颜数据构造
 * Created on 2021/3/27
 */
public class FaceBeautySource {

    /**
     * 获取默认推荐美颜模型
     *
     * @return
     */
    public static FaceBeauty getDefaultFaceBeauty() {
        FaceBeauty recommendFaceBeauty = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
        recommendFaceBeauty.setFilterName(FaceBeautyFilterEnum.ZIRAN_2);
        recommendFaceBeauty.setFilterIntensity(0.4);
        /*美肤*/
        recommendFaceBeauty.setSharpenIntensity(0.2);
        recommendFaceBeauty.setColorIntensity(0.3);
        recommendFaceBeauty.setRedIntensity(0.3);
        recommendFaceBeauty.setBlurIntensity(4.2);
        /*美型*/
        recommendFaceBeauty.setFaceShapeIntensity(1.0);
        recommendFaceBeauty.setEyeEnlargingIntensityV2(0.4);
        recommendFaceBeauty.setCheekVIntensity(0.5);
        recommendFaceBeauty.setNoseIntensityV2(0.5);
        recommendFaceBeauty.setForHeadIntensityV2(0.3);
        recommendFaceBeauty.setMouthIntensityV2(0.4);
        recommendFaceBeauty.setChinIntensity(0.3);
        //性能最优策略
        if (DemoConfig.DEVICE_LEVEL > FuDeviceUtils.DEVICE_LEVEL_MID) {
//            setFaceBeautyPropertyMode(recommendFaceBeauty);
        }
        return recommendFaceBeauty;
    }


    /**
     * 获取默认推荐美颜模型
     *
     * @return
     */
    public static FaceBeauty getFaceBeauty() {


        FaceBeauty recommendFaceBeauty = getLocalFaceBeauty();

        if (recommendFaceBeauty == null) {
            recommendFaceBeauty = getDefaultFaceBeauty();
        }

        return recommendFaceBeauty;
    }

    public static FaceBeauty getLocalFaceBeauty() {

        String values = SpUtils.getString(SpKey.FACE_BEAUTY_VALUE, "");

        FaceBeauty recommendFaceBeauty = null;

        if (!TextUtils.isEmpty(values)) {
            FaceBeautySaveBean bean = GsonUtil.GsonToBean(values, FaceBeautySaveBean.class);
            if (bean != null) {
                recommendFaceBeauty = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
                Log.d("aaaa", "getLocalFaceBeauty: "+bean.getCheekSmallIntensityV2());

                /*滤镜*/
                recommendFaceBeauty.setFilterName(bean.getFilterName());
                recommendFaceBeauty.setFilterIntensity(bean.getFilterIntensity());
                /*美肤*/
                recommendFaceBeauty.setBlurIntensity(bean.getBlurIntensity());
                recommendFaceBeauty.setEnableHeavyBlur(bean.getEnableHeavyBlur());
                recommendFaceBeauty.setEnableSkinDetect(bean.getEnableSkinDetect());
                recommendFaceBeauty.setNonSkinBlurIntensity(bean.getNonSkinBlurIntensity());
                recommendFaceBeauty.setBlurType(bean.getBlurType());
                recommendFaceBeauty.setEnableBlurUseMask(bean.getEnableBlurUseMask());
                recommendFaceBeauty.setColorIntensity(bean.getColorIntensity());
                recommendFaceBeauty.setRedIntensity(bean.getRedIntensity());
                recommendFaceBeauty.setSharpenIntensity(bean.getSharpenIntensity());
                recommendFaceBeauty.setEyeBrightIntensity(bean.getEyeBrightIntensity());
                recommendFaceBeauty.setToothIntensity(bean.getToothIntensity());
                recommendFaceBeauty.setRemovePouchIntensity(bean.getRemovePouchIntensity());
                recommendFaceBeauty.setRemoveLawPatternIntensity(bean.getRemoveLawPatternIntensity());
                /*美型*/
                recommendFaceBeauty.setFaceShape(bean.getFaceShape());
                recommendFaceBeauty.setFaceShapeIntensity(bean.getFaceShapeIntensity());
                recommendFaceBeauty.setCheekThinningIntensity(bean.getCheekThinningIntensity());
                recommendFaceBeauty.setCheekVIntensity(bean.getCheekVIntensity());
                recommendFaceBeauty.setCheekLongIntensity(bean.getCheekLongIntensity());
                recommendFaceBeauty.setCheekCircleIntensity(bean.getCheekCircleIntensity());
                recommendFaceBeauty.setCheekNarrowIntensityV2(bean.getCheekNarrowIntensityV2());
                recommendFaceBeauty.setCheekShortIntensity(bean.getCheekShortIntensity());
                recommendFaceBeauty.setCheekSmallIntensityV2(bean.getCheekSmallIntensityV2());
                recommendFaceBeauty.setCheekBonesIntensity(bean.getCheekBonesIntensity());
                recommendFaceBeauty.setLowerJawIntensity(bean.getLowerJawIntensity());
                recommendFaceBeauty.setEyeEnlargingIntensityV2(bean.getEyeEnlargingIntensityV2());
                recommendFaceBeauty.setChinIntensity(bean.getChinIntensity());
                recommendFaceBeauty.setForHeadIntensityV2(bean.getForHeadIntensityV2());
                recommendFaceBeauty.setNoseIntensityV2(bean.getNoseIntensityV2());
                recommendFaceBeauty.setMouthIntensityV2(bean.getMouthIntensityV2());
                recommendFaceBeauty.setCanthusIntensity(bean.getCanthusIntensity());
                recommendFaceBeauty.setEyeSpaceIntensity(bean.getEyeSpaceIntensity());
                recommendFaceBeauty.setEyeRotateIntensity(bean.getEyeRotateIntensity());
                recommendFaceBeauty.setLongNoseIntensity(bean.getLongNoseIntensity());
                recommendFaceBeauty.setPhiltrumIntensity(bean.getPhiltrumIntensity());
                recommendFaceBeauty.setSmileIntensity(bean.getSmileIntensity());
                recommendFaceBeauty.setEyeCircleIntensity(bean.getEyeCircleIntensity());
                recommendFaceBeauty.setChangeFramesIntensity(bean.getChangeFramesIntensity());
            }
        }
        return recommendFaceBeauty;
    }


    /**
     * 初始化美肤参数
     *
     * @return ArrayList<FaceBeautyBean>
     */
    public static ArrayList<FaceBeautyBean> buildSkinParams() {
        ArrayList<FaceBeautyBean> params = new ArrayList<>();
        params.add(new FaceBeautyBean(
                        FaceBeautyParam.BLUR_INTENSITY, R.string.beauty_box_heavy_blur_fine,
                        R.drawable.icon_beauty_skin_buffing_close_selector, R.drawable.icon_beauty_skin_buffing_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.COLOR_INTENSITY, R.string.beauty_box_color_level,
                        R.drawable.icon_beauty_skin_color_close_selector, R.drawable.icon_beauty_skin_color_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.RED_INTENSITY, R.string.beauty_box_red_level,
                        R.drawable.icon_beauty_skin_red_close_selector, R.drawable.icon_beauty_skin_red_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.SHARPEN_INTENSITY, R.string.beauty_box_sharpen,
                        R.drawable.icon_beauty_skin_sharpen_close_selector, R.drawable.icon_beauty_skin_sharpen_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.EYE_BRIGHT_INTENSITY, R.string.beauty_box_eye_bright,
                        R.drawable.icon_beauty_skin_eyes_bright_close_selector, R.drawable.icon_beauty_skin_eyes_bright_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.TOOTH_WHITEN_INTENSITY, R.string.beauty_box_tooth_whiten,
                        R.drawable.icon_beauty_skin_teeth_close_selector, R.drawable.icon_beauty_skin_teeth_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.REMOVE_POUCH_INTENSITY, R.string.beauty_micro_pouch,
                        R.drawable.icon_beauty_skin_dark_circles_close_selector, R.drawable.icon_beauty_skin_dark_circles_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.REMOVE_NASOLABIAL_FOLDS_INTENSITY, R.string.beauty_micro_nasolabial,
                        R.drawable.icon_beauty_skin_wrinkle_close_selector, R.drawable.icon_beauty_skin_wrinkle_open_selector
                )
        );
        return params;
    }

    /**
     * 初始化美型参数
     *
     * @return ArrayList<FaceBeautyBean>
     */
    public static ArrayList<FaceBeautyBean> buildShapeParams() {
        ArrayList<FaceBeautyBean> params = new ArrayList<>();

        //瘦脸
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CHEEK_THINNING_INTENSITY, R.string.beauty_box_cheek_thinning,
                        R.drawable.icon_beauty_shape_face_cheekthin_close_selector, R.drawable.icon_beauty_shape_face_cheekthin_open_selector
                )
        );

        //V脸
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CHEEK_V_INTENSITY, R.string.beauty_box_cheek_v,
                        R.drawable.icon_beauty_shape_face_v_close_selector, R.drawable.icon_beauty_shape_face_v_open_selector
                )
        );

        //窄脸
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CHEEK_NARROW_INTENSITY_V2, R.string.beauty_box_cheek_narrow,
                        R.drawable.icon_beauty_shape_face_narrow_close_selector, R.drawable.icon_beauty_shape_face_narrow_open_selector
                )
        );

        //小脸 -> 短脸  --使用的参数是以前小脸的
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CHEEK_SHORT_INTENSITY, R.string.beauty_box_cheek_short,
                        R.drawable.icon_beauty_shape_face_short_close_selector, R.drawable.icon_beauty_shape_face_short_open_selector
                )
        );

        //小脸 -> 新增
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CHEEK_SMALL_INTENSITY_V2, R.string.beauty_box_cheek_small,
                        R.drawable.icon_beauty_shape_face_little_close_selector, R.drawable.icon_beauty_shape_face_little_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.INTENSITY_CHEEKBONES_INTENSITY, R.string.beauty_box_cheekbones,
                        R.drawable.icon_beauty_shape_cheek_bones_close_selector, R.drawable.icon_beauty_shape_cheek_bones_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.INTENSITY_LOW_JAW_INTENSITY, R.string.beauty_box_lower_jaw,
                        R.drawable.icon_beauty_shape_lower_jaw_close_selector, R.drawable.icon_beauty_shape_lower_jaw_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.EYE_ENLARGING_INTENSITY_V2, R.string.beauty_box_eye_enlarge,
                        R.drawable.icon_beauty_shape_enlarge_eye_close_selector, R.drawable.icon_beauty_shape_enlarge_eye_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.EYE_CIRCLE_INTENSITY, R.string.beauty_box_eye_circle,
                        R.drawable.icon_beauty_shape_round_eye_close_selector, R.drawable.icon_beauty_shape_round_eye_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CHIN_INTENSITY, R.string.beauty_box_intensity_chin,
                        R.drawable.icon_beauty_shape_chin_close_selector, R.drawable.icon_beauty_shape_chin_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.FOREHEAD_INTENSITY_V2, R.string.beauty_box_intensity_forehead,
                        R.drawable.icon_beauty_shape_forehead_close_selector, R.drawable.icon_beauty_shape_forehead_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.NOSE_INTENSITY_V2, R.string.beauty_box_intensity_nose,
                        R.drawable.icon_beauty_shape_thin_nose_close_selector, R.drawable.icon_beauty_shape_thin_nose_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.MOUTH_INTENSITY_V2, R.string.beauty_box_intensity_mouth,
                        R.drawable.icon_beauty_shape_mouth_close_selector, R.drawable.icon_beauty_shape_mouth_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.CANTHUS_INTENSITY, R.string.beauty_micro_canthus,
                        R.drawable.icon_beauty_shape_open_eyes_close_selector, R.drawable.icon_beauty_shape_open_eyes_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.EYE_SPACE_INTENSITY, R.string.beauty_micro_eye_space,
                        R.drawable.icon_beauty_shape_distance_close_selector, R.drawable.icon_beauty_shape_distance_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.EYE_ROTATE_INTENSITY, R.string.beauty_micro_eye_rotate,
                        R.drawable.icon_beauty_shape_angle_close_selector, R.drawable.icon_beauty_shape_angle_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.LONG_NOSE_INTENSITY, R.string.beauty_micro_long_nose,
                        R.drawable.icon_beauty_shape_proboscis_close_selector, R.drawable.icon_beauty_shape_proboscis_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.PHILTRUM_INTENSITY, R.string.beauty_micro_philtrum,
                        R.drawable.icon_beauty_shape_shrinking_close_selector, R.drawable.icon_beauty_shape_shrinking_open_selector
                )
        );
        params.add(
                new FaceBeautyBean(
                        FaceBeautyParam.SMILE_INTENSITY, R.string.beauty_micro_smile,
                        R.drawable.icon_beauty_shape_smile_close_selector, R.drawable.icon_beauty_shape_smile_open_selector
                )
        );
        return params;
    }

    /**
     * 加载脸型子项
     *
     * @return
     */
    public static ArrayList<FaceBeautyBean> buildFaceShapeSubItemParams() {
        return buildSubItemParams(FaceBeautyParam.FACE_SHAPE);
    }

    public static ArrayList<FaceBeautyBean> buildSubItemParams(String key) {
        ArrayList<FaceBeautyBean> params = new ArrayList<>();
//        if (key != null && !key.isEmpty()) {
//            if (key.equals(FaceBeautyParam.FACE_SHAPE)) {
//                //返回
//                params.add(
//                        new FaceBeautyBean(
//                                "", R.string.back,
//                                R.mipmap.icon_beauty_back, R.mipmap.icon_beauty_back, FaceBeautyBean.ButtonType.BACK_BUTTON
//                        )
//                );
//
//                //自然 V脸 -> 自然脸
//                params.add(
//                        new FaceBeautyBean(
//                                FaceBeautyParam.CHEEK_V_INTENSITY, R.string.beauty_box_cheek_natural,
//                                R.drawable.icon_beauty_shape_face_natural_close_selector, R.drawable.icon_beauty_shape_face_natural_open_selector
//                        )
//                );
//
//                //女神 瘦脸 -> 女神脸
//                params.add(
//                        new FaceBeautyBean(
//                                FaceBeautyParam.CHEEK_THINNING_INTENSITY, R.string.beauty_box_cheek_goddess,
//                                R.drawable.icon_beauty_shape_face_goddess_close_selector, R.drawable.icon_beauty_shape_face_goddess_open_selector
//                        )
//                );
//
//                //长脸
//                params.add(
//                        new FaceBeautyBean(
//                                FaceBeautyParam.CHEEK_LONG_INTENSITY, R.string.beauty_box_cheek_long_face,
//                                R.drawable.icon_beauty_shape_face_long_close_selector, R.drawable.icon_beauty_shape_face_long_open_selector
//                        )
//                );
//
//                //圆脸
//                params.add(
//                        new FaceBeautyBean(
//                                FaceBeautyParam.CHEEK_CIRCLE_INTENSITY, R.string.beauty_box_cheek_round_face,
//                                R.drawable.icon_beauty_shape_face_round_close_selector, R.drawable.icon_beauty_shape_face_round_open_selector
//                        )
//                );
//            }
//        }

        return params;
    }

    /**
     * 初始化参数扩展列表
     *
     * @return HashMap<String, ModelAttributeData>
     */
    public static HashMap<String, ModelAttributeData> buildModelAttributeRange() {
        HashMap<String, ModelAttributeData> params = new HashMap<>();
        /*美肤*/
        params.put(FaceBeautyParam.COLOR_INTENSITY, new ModelAttributeData(0.3, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.BLUR_INTENSITY, new ModelAttributeData(4.2, 0.0, 0.0, 6.0));
        params.put(FaceBeautyParam.RED_INTENSITY, new ModelAttributeData(0.3, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.SHARPEN_INTENSITY, new ModelAttributeData(0.2, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.EYE_BRIGHT_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.TOOTH_WHITEN_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.REMOVE_POUCH_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.REMOVE_NASOLABIAL_FOLDS_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        /*美型*/
        params.put(FaceBeautyParam.FACE_SHAPE_INTENSITY, new ModelAttributeData(1.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.CHEEK_THINNING_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.CHEEK_LONG_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.CHEEK_CIRCLE_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.CHEEK_V_INTENSITY, new ModelAttributeData(0.5, 0.0, 0.0, 1.0));
        //1
        params.put(FaceBeautyParam.CHEEK_NARROW_INTENSITY_V2, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.CHEEK_SHORT_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        //2
        params.put(FaceBeautyParam.CHEEK_SMALL_INTENSITY_V2, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.INTENSITY_CHEEKBONES_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.INTENSITY_LOW_JAW_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        //3
        params.put(FaceBeautyParam.EYE_ENLARGING_INTENSITY_V2, new ModelAttributeData(0.4, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.EYE_CIRCLE_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.CHIN_INTENSITY, new ModelAttributeData(0.3, 0.5, 0.0, 1.0));
        //4
        params.put(FaceBeautyParam.FOREHEAD_INTENSITY_V2, new ModelAttributeData(0.3, 0.5, 0.0, 1.0));
        //5
        params.put(FaceBeautyParam.NOSE_INTENSITY_V2, new ModelAttributeData(0.5, 0.0, 0.0, 1.0));
        //6
        params.put(FaceBeautyParam.MOUTH_INTENSITY_V2, new ModelAttributeData(0.4, 0.5, 0.0, 1.0));
        params.put(FaceBeautyParam.CANTHUS_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        params.put(FaceBeautyParam.EYE_SPACE_INTENSITY, new ModelAttributeData(0.5, 0.5, 0.0, 1.0));
        params.put(FaceBeautyParam.EYE_ROTATE_INTENSITY, new ModelAttributeData(0.5, 0.5, 0.0, 1.0));
        params.put(FaceBeautyParam.LONG_NOSE_INTENSITY, new ModelAttributeData(0.5, 0.5, 0.0, 1.0));
        params.put(FaceBeautyParam.PHILTRUM_INTENSITY, new ModelAttributeData(0.5, 0.5, 0.0, 1.0));
        params.put(FaceBeautyParam.SMILE_INTENSITY, new ModelAttributeData(0.0, 0.0, 0.0, 1.0));
        return params;
    }


    /**
     * 初始化滤镜参数
     *
     * @return ArrayList<FaceBeautyFilterBean>
     */
    public static ArrayList<FaceBeautyFilterBean> buildFilters() {
        ArrayList<FaceBeautyFilterBean> filters = new ArrayList<>();
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ORIGIN, R.mipmap.icon_beauty_filter_cancel, R.string.origin, 0.0));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_1, R.mipmap.icon_beauty_filter_natural_1, R.string.ziran_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_2, R.mipmap.icon_beauty_filter_natural_2, R.string.ziran_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_3, R.mipmap.icon_beauty_filter_natural_3, R.string.ziran_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_4, R.mipmap.icon_beauty_filter_natural_4, R.string.ziran_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_5, R.mipmap.icon_beauty_filter_natural_5, R.string.ziran_5));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_6, R.mipmap.icon_beauty_filter_natural_6, R.string.ziran_6));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_7, R.mipmap.icon_beauty_filter_natural_7, R.string.ziran_7));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZIRAN_8, R.mipmap.icon_beauty_filter_natural_8, R.string.ziran_8));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_1, R.mipmap.icon_beauty_filter_texture_gray_1, R.string.zhiganhui_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_2, R.mipmap.icon_beauty_filter_texture_gray_2, R.string.zhiganhui_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_3, R.mipmap.icon_beauty_filter_texture_gray_3, R.string.zhiganhui_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_4, R.mipmap.icon_beauty_filter_texture_gray_4, R.string.zhiganhui_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_5, R.mipmap.icon_beauty_filter_texture_gray_5, R.string.zhiganhui_5));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_6, R.mipmap.icon_beauty_filter_texture_gray_6, R.string.zhiganhui_6));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_7, R.mipmap.icon_beauty_filter_texture_gray_7, R.string.zhiganhui_7));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.ZHIGANHUI_8, R.mipmap.icon_beauty_filter_texture_gray_8, R.string.zhiganhui_8));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_1, R.mipmap.icon_beauty_filter_peach_1, R.string.mitao_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_2, R.mipmap.icon_beauty_filter_peach_2, R.string.mitao_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_3, R.mipmap.icon_beauty_filter_peach_3, R.string.mitao_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_4, R.mipmap.icon_beauty_filter_peach_4, R.string.mitao_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_5, R.mipmap.icon_beauty_filter_peach_5, R.string.mitao_5));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_6, R.mipmap.icon_beauty_filter_peach_6, R.string.mitao_6));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_7, R.mipmap.icon_beauty_filter_peach_7, R.string.mitao_7));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.MITAO_8, R.mipmap.icon_beauty_filter_peach_8, R.string.mitao_8));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_1, R.mipmap.icon_beauty_filter_bailiang_1, R.string.bailiang_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_2, R.mipmap.icon_beauty_filter_bailiang_2, R.string.bailiang_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_3, R.mipmap.icon_beauty_filter_bailiang_3, R.string.bailiang_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_4, R.mipmap.icon_beauty_filter_bailiang_4, R.string.bailiang_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_5, R.mipmap.icon_beauty_filter_bailiang_5, R.string.bailiang_5));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_6, R.mipmap.icon_beauty_filter_bailiang_6, R.string.bailiang_6));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.BAILIANG_7, R.mipmap.icon_beauty_filter_bailiang_7, R.string.bailiang_7));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_1, R.mipmap.icon_beauty_filter_fennen_1, R.string.fennen_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_2, R.mipmap.icon_beauty_filter_fennen_2, R.string.fennen_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_3, R.mipmap.icon_beauty_filter_fennen_3, R.string.fennen_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_5, R.mipmap.icon_beauty_filter_fennen_5, R.string.fennen_5));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_6, R.mipmap.icon_beauty_filter_fennen_6, R.string.fennen_6));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_7, R.mipmap.icon_beauty_filter_fennen_7, R.string.fennen_7));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.FENNEN_8, R.mipmap.icon_beauty_filter_fennen_8, R.string.fennen_8));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_1, R.mipmap.icon_beauty_filter_lengsediao_1, R.string.lengsediao_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_2, R.mipmap.icon_beauty_filter_lengsediao_2, R.string.lengsediao_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_3, R.mipmap.icon_beauty_filter_lengsediao_3, R.string.lengsediao_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_4, R.mipmap.icon_beauty_filter_lengsediao_4, R.string.lengsediao_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_7, R.mipmap.icon_beauty_filter_lengsediao_7, R.string.lengsediao_7));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_8, R.mipmap.icon_beauty_filter_lengsediao_8, R.string.lengsediao_8));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.LENGSEDIAO_11, R.mipmap.icon_beauty_filter_lengsediao_11, R.string.lengsediao_11));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.NUANSEDIAO_1, R.mipmap.icon_beauty_filter_nuansediao_1, R.string.nuansediao_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.NUANSEDIAO_2, R.mipmap.icon_beauty_filter_nuansediao_2, R.string.nuansediao_2));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_1, R.mipmap.icon_beauty_filter_gexing_1, R.string.gexing_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_2, R.mipmap.icon_beauty_filter_gexing_2, R.string.gexing_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_3, R.mipmap.icon_beauty_filter_gexing_3, R.string.gexing_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_4, R.mipmap.icon_beauty_filter_gexing_4, R.string.gexing_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_5, R.mipmap.icon_beauty_filter_gexing_5, R.string.gexing_5));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_7, R.mipmap.icon_beauty_filter_gexing_7, R.string.gexing_7));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_10, R.mipmap.icon_beauty_filter_gexing_10, R.string.gexing_10));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.GEXING_11, R.mipmap.icon_beauty_filter_gexing_11, R.string.gexing_11));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.XIAOQINGXIN_1, R.mipmap.icon_beauty_filter_xiaoqingxin_1, R.string.xiaoqingxin_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.XIAOQINGXIN_3, R.mipmap.icon_beauty_filter_xiaoqingxin_3, R.string.xiaoqingxin_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.XIAOQINGXIN_4, R.mipmap.icon_beauty_filter_xiaoqingxin_4, R.string.xiaoqingxin_4));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.XIAOQINGXIN_6, R.mipmap.icon_beauty_filter_xiaoqingxin_6, R.string.xiaoqingxin_6));

        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.HEIBAI_1, R.mipmap.icon_beauty_filter_heibai_1, R.string.heibai_1));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.HEIBAI_2, R.mipmap.icon_beauty_filter_heibai_2, R.string.heibai_2));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.HEIBAI_3, R.mipmap.icon_beauty_filter_heibai_3, R.string.heibai_3));
        filters.add(new FaceBeautyFilterBean(FaceBeautyFilterEnum.HEIBAI_4, R.mipmap.icon_beauty_filter_heibai_4, R.string.heibai_4));

        return filters;
    }

//    private static final String CONFIG_BIAOZHUN = "biaozhun";
//    private static final String CONFIG_HUAJIAO = "huajiao";
//    private static final String CONFIG_KUAISHOU = "kuaishou";
//    private static final String CONFIG_QINGYAN = "qingyan";
//    private static final String CONFIG_SHANGTANG = "shangtang";
//    private static final String CONFIG_YINGKE = "yingke";
//    private static final String CONFIG_ZIJIETIAODONG = "zijietiaodong";


//    /**
//     * 初始化风格推荐
//     *
//     * @return ArrayList<FaceBeautyBean>
//     */
//    public static ArrayList<FaceBeautyStyleBean> buildStylesParams() {
//        ArrayList<FaceBeautyStyleBean> params = new ArrayList<>();
//        params.add(new FaceBeautyStyleBean(CONFIG_KUAISHOU, R.drawable.icon_beauty_style_1_selector, R.string.beauty_face_style_1));
//        params.add(new FaceBeautyStyleBean(CONFIG_QINGYAN, R.drawable.icon_beauty_style_2_selector, R.string.beauty_face_style_2));
//        params.add(new FaceBeautyStyleBean(CONFIG_ZIJIETIAODONG, R.drawable.icon_beauty_style_3_selector, R.string.beauty_face_style_3));
//        params.add(new FaceBeautyStyleBean(CONFIG_HUAJIAO, R.drawable.icon_beauty_style_4_selector, R.string.beauty_face_style_4));
//        params.add(new FaceBeautyStyleBean(CONFIG_YINGKE, R.drawable.icon_beauty_style_5_selector, R.string.beauty_face_style_5));
//        params.add(new FaceBeautyStyleBean(CONFIG_SHANGTANG, R.drawable.icon_beauty_style_6_selector, R.string.beauty_face_style_6));
//        params.add(new FaceBeautyStyleBean(CONFIG_BIAOZHUN, R.drawable.icon_beauty_style_7_selector, R.string.beauty_face_style_7));
//        return params;
//    }

    /**
     * 风格对应参数配置
     */
//    public static HashMap<String, Runnable> styleParams = new HashMap<String, Runnable>() {
//        {
//            put(CONFIG_KUAISHOU, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setColorIntensity(0.5);
//                model.setBlurIntensity(3.6);
//                model.setEyeBrightIntensity(0.35);
//                model.setToothIntensity(0.25);
//                model.setCheekThinningIntensity(0.45);
//                model.setCheekVIntensity(0.08);
//                model.setCheekSmallIntensity(0.05);
//                model.setEyeEnlargingIntensity(0.3);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//
//            });
//            put(CONFIG_QINGYAN, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setFilterName(FaceBeautyFilterEnum.ZIRAN_3);
//                model.setFilterIntensity(0.3);
//                model.setColorIntensity(0.4);
//                model.setRedIntensity(0.2);
//                model.setBlurIntensity(3.6);
//                model.setEyeBrightIntensity(0.5);
//                model.setToothIntensity(0.4);
//                model.setCheekThinningIntensity(0.3);
//                model.setNoseIntensityV2(0.5);
//                model.setEyeEnlargingIntensity(0.25);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//            });
//            put(CONFIG_ZIJIETIAODONG, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setColorIntensity(0.4);
//                model.setRedIntensity(0.3);
//                model.setBlurIntensity(2.4);
//                model.setCheekThinningIntensity(0.3);
//                model.setCheekSmallIntensity(0.15);
//                model.setEyeEnlargingIntensity(0.65);
//                model.setNoseIntensityV2(0.3);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//            });
//            put(CONFIG_HUAJIAO, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setColorIntensity(0.7);
//                model.setBlurIntensity(3.9);
//                model.setCheekThinningIntensity(0.3);
//                model.setCheekSmallIntensity(0.05);
//                model.setEyeEnlargingIntensity(0.65);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//            });
//            put(CONFIG_YINGKE, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setFilterName(FaceBeautyFilterEnum.FENNEN_2);
//                model.setFilterIntensity(0.5);
//                model.setColorIntensity(0.6);
//                model.setBlurIntensity(3.0);
//                model.setCheekThinningIntensity(0.5);
//                model.setEyeEnlargingIntensity(0.65);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//            });
//            put(CONFIG_SHANGTANG, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setFilterName(FaceBeautyFilterEnum.FENNEN_2);
//                model.setFilterIntensity(0.8);
//                model.setColorIntensity(0.7);
//                model.setBlurIntensity(4.2);
//                model.setEyeEnlargingIntensity(0.6);
//                model.setCheekThinningIntensity(0.3);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//            });
//            put(CONFIG_BIAOZHUN, () -> {
//                FaceBeauty model = new FaceBeauty(new FUBundleData(DemoConfig.BUNDLE_FACE_BEAUTIFICATION));
//                model.setFaceShapeIntensity(1.0);
//                model.setFilterName(FaceBeautyFilterEnum.ZIRAN_5);
//                model.setFilterIntensity(0.55);
//                model.setColorIntensity(0.2);
//                model.setRedIntensity(0.65);
//                model.setBlurIntensity(3.3);
//                model.setCheekSmallIntensity(0.05);
//                model.setCheekThinningIntensity(0.1);
//                FaceBeautyDataFactory.faceBeauty = model;
//                FURenderKit.getInstance().setFaceBeauty(FaceBeautyDataFactory.faceBeauty);
//            });
//        }
//
//    };

    /**
     * 克隆模型
     *
     * @param faceBeauty
     * @return
     */
    public static FaceBeauty clone(FaceBeauty faceBeauty) {
        FaceBeauty cloneFaceBeauty = new FaceBeauty(new FUBundleData(faceBeauty.getControlBundle().getPath()));
        /*滤镜*/
        cloneFaceBeauty.setFilterName(faceBeauty.getFilterName());
        cloneFaceBeauty.setFilterIntensity(faceBeauty.getFilterIntensity());
        /*美肤*/
        cloneFaceBeauty.setBlurIntensity(faceBeauty.getBlurIntensity());
        cloneFaceBeauty.setEnableHeavyBlur(faceBeauty.getEnableHeavyBlur());
        cloneFaceBeauty.setEnableSkinDetect(faceBeauty.getEnableSkinDetect());
        cloneFaceBeauty.setNonSkinBlurIntensity(faceBeauty.getNonSkinBlurIntensity());
        cloneFaceBeauty.setBlurType(faceBeauty.getBlurType());
        cloneFaceBeauty.setEnableBlurUseMask(faceBeauty.getEnableBlurUseMask());
        cloneFaceBeauty.setColorIntensity(faceBeauty.getColorIntensity());
        cloneFaceBeauty.setRedIntensity(faceBeauty.getRedIntensity());
        cloneFaceBeauty.setSharpenIntensity(faceBeauty.getSharpenIntensity());
        cloneFaceBeauty.setEyeBrightIntensity(faceBeauty.getEyeBrightIntensity());
        cloneFaceBeauty.setToothIntensity(faceBeauty.getToothIntensity());
        cloneFaceBeauty.setRemovePouchIntensity(faceBeauty.getRemovePouchIntensity());
        cloneFaceBeauty.setRemoveLawPatternIntensity(faceBeauty.getRemoveLawPatternIntensity());
        /*美型*/
        cloneFaceBeauty.setFaceShape(faceBeauty.getFaceShape());
        cloneFaceBeauty.setFaceShapeIntensity(faceBeauty.getFaceShapeIntensity());
        cloneFaceBeauty.setCheekThinningIntensity(faceBeauty.getCheekThinningIntensity());
        cloneFaceBeauty.setCheekVIntensity(faceBeauty.getCheekVIntensity());
        cloneFaceBeauty.setCheekLongIntensity(faceBeauty.getCheekLongIntensity());
        cloneFaceBeauty.setCheekCircleIntensity(faceBeauty.getCheekCircleIntensity());
        cloneFaceBeauty.setCheekNarrowIntensityV2(faceBeauty.getCheekNarrowIntensityV2());
        cloneFaceBeauty.setCheekShortIntensity(faceBeauty.getCheekShortIntensity());
        cloneFaceBeauty.setCheekSmallIntensityV2(faceBeauty.getCheekSmallIntensityV2());
        cloneFaceBeauty.setCheekBonesIntensity(faceBeauty.getCheekBonesIntensity());
        cloneFaceBeauty.setLowerJawIntensity(faceBeauty.getLowerJawIntensity());
        cloneFaceBeauty.setEyeEnlargingIntensityV2(faceBeauty.getEyeEnlargingIntensityV2());
        cloneFaceBeauty.setChinIntensity(faceBeauty.getChinIntensity());
        cloneFaceBeauty.setForHeadIntensityV2(faceBeauty.getForHeadIntensityV2());
        cloneFaceBeauty.setNoseIntensityV2(faceBeauty.getNoseIntensityV2());
        cloneFaceBeauty.setMouthIntensityV2(faceBeauty.getMouthIntensityV2());
        cloneFaceBeauty.setCanthusIntensity(faceBeauty.getCanthusIntensity());
        cloneFaceBeauty.setEyeSpaceIntensity(faceBeauty.getEyeSpaceIntensity());
        cloneFaceBeauty.setEyeRotateIntensity(faceBeauty.getEyeRotateIntensity());
        cloneFaceBeauty.setLongNoseIntensity(faceBeauty.getLongNoseIntensity());
        cloneFaceBeauty.setPhiltrumIntensity(faceBeauty.getPhiltrumIntensity());
        cloneFaceBeauty.setSmileIntensity(faceBeauty.getSmileIntensity());
        cloneFaceBeauty.setEyeCircleIntensity(faceBeauty.getEyeCircleIntensity());
        cloneFaceBeauty.setChangeFramesIntensity(faceBeauty.getChangeFramesIntensity());
        return cloneFaceBeauty;
    }

    public static void save(FaceBeauty faceBeauty) {

        FaceBeautySaveBean saveBeauty = new FaceBeautySaveBean();
        /*滤镜*/
        saveBeauty.setFilterName(faceBeauty.getFilterName());
        saveBeauty.setFilterIntensity(faceBeauty.getFilterIntensity());
        /*美肤*/
        saveBeauty.setBlurIntensity(faceBeauty.getBlurIntensity());
        saveBeauty.setEnableHeavyBlur(faceBeauty.getEnableHeavyBlur());
        saveBeauty.setEnableSkinDetect(faceBeauty.getEnableSkinDetect());
        saveBeauty.setNonSkinBlurIntensity(faceBeauty.getNonSkinBlurIntensity());
        saveBeauty.setBlurType(faceBeauty.getBlurType());
        saveBeauty.setEnableBlurUseMask(faceBeauty.getEnableBlurUseMask());
        saveBeauty.setColorIntensity(faceBeauty.getColorIntensity());
        saveBeauty.setRedIntensity(faceBeauty.getRedIntensity());
        saveBeauty.setSharpenIntensity(faceBeauty.getSharpenIntensity());
        saveBeauty.setEyeBrightIntensity(faceBeauty.getEyeBrightIntensity());
        saveBeauty.setToothIntensity(faceBeauty.getToothIntensity());
        saveBeauty.setRemovePouchIntensity(faceBeauty.getRemovePouchIntensity());
        saveBeauty.setRemoveLawPatternIntensity(faceBeauty.getRemoveLawPatternIntensity());
        /*美型*/
        saveBeauty.setFaceShape(faceBeauty.getFaceShape());
        saveBeauty.setFaceShapeIntensity(faceBeauty.getFaceShapeIntensity());
        saveBeauty.setCheekThinningIntensity(faceBeauty.getCheekThinningIntensity());
        saveBeauty.setCheekVIntensity(faceBeauty.getCheekVIntensity());
        saveBeauty.setCheekLongIntensity(faceBeauty.getCheekLongIntensity());
        saveBeauty.setCheekCircleIntensity(faceBeauty.getCheekCircleIntensity());
        saveBeauty.setCheekNarrowIntensityV2(faceBeauty.getCheekNarrowIntensityV2());
        saveBeauty.setCheekShortIntensity(faceBeauty.getCheekShortIntensity());
        Log.d("aaaa", "save: "+faceBeauty.getCheekSmallIntensityV2());
        saveBeauty.setCheekSmallIntensityV2(faceBeauty.getCheekSmallIntensityV2());
        saveBeauty.setCheekBonesIntensity(faceBeauty.getCheekBonesIntensity());
        saveBeauty.setLowerJawIntensity(faceBeauty.getLowerJawIntensity());
        saveBeauty.setEyeEnlargingIntensityV2(faceBeauty.getEyeEnlargingIntensityV2());
        saveBeauty.setChinIntensity(faceBeauty.getChinIntensity());
        saveBeauty.setForHeadIntensityV2(faceBeauty.getForHeadIntensityV2());
        saveBeauty.setNoseIntensityV2(faceBeauty.getNoseIntensityV2());
        saveBeauty.setMouthIntensityV2(faceBeauty.getMouthIntensityV2());
        saveBeauty.setCanthusIntensity(faceBeauty.getCanthusIntensity());
        saveBeauty.setEyeSpaceIntensity(faceBeauty.getEyeSpaceIntensity());
        saveBeauty.setEyeRotateIntensity(faceBeauty.getEyeRotateIntensity());
        saveBeauty.setLongNoseIntensity(faceBeauty.getLongNoseIntensity());
        saveBeauty.setPhiltrumIntensity(faceBeauty.getPhiltrumIntensity());
        saveBeauty.setSmileIntensity(faceBeauty.getSmileIntensity());
        saveBeauty.setEyeCircleIntensity(faceBeauty.getEyeCircleIntensity());
        saveBeauty.setChangeFramesIntensity(faceBeauty.getChangeFramesIntensity());

        try {
            String json = GsonUtil.GsonString(saveBeauty);
            SpUtils.put(SpKey.FACE_BEAUTY_VALUE, json);
            ToastUtils.showToast("保存成功", Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("保存失败", Toast.LENGTH_LONG);
        }

    }

    public static void reset(FaceBeauty cloneFaceBeauty) {
        FaceBeauty faceBeauty = getDefaultFaceBeauty();
        /*滤镜*/
        cloneFaceBeauty.setFilterName(faceBeauty.getFilterName());
        cloneFaceBeauty.setFilterIntensity(faceBeauty.getFilterIntensity());
        /*美肤*/
        cloneFaceBeauty.setBlurIntensity(faceBeauty.getBlurIntensity());
        cloneFaceBeauty.setEnableHeavyBlur(faceBeauty.getEnableHeavyBlur());
        cloneFaceBeauty.setEnableSkinDetect(faceBeauty.getEnableSkinDetect());
        cloneFaceBeauty.setNonSkinBlurIntensity(faceBeauty.getNonSkinBlurIntensity());
        cloneFaceBeauty.setBlurType(faceBeauty.getBlurType());
        cloneFaceBeauty.setEnableBlurUseMask(faceBeauty.getEnableBlurUseMask());
        cloneFaceBeauty.setColorIntensity(faceBeauty.getColorIntensity());
        cloneFaceBeauty.setRedIntensity(faceBeauty.getRedIntensity());
        cloneFaceBeauty.setSharpenIntensity(faceBeauty.getSharpenIntensity());
        cloneFaceBeauty.setEyeBrightIntensity(faceBeauty.getEyeBrightIntensity());
        cloneFaceBeauty.setToothIntensity(faceBeauty.getToothIntensity());
        cloneFaceBeauty.setRemovePouchIntensity(faceBeauty.getRemovePouchIntensity());
        cloneFaceBeauty.setRemoveLawPatternIntensity(faceBeauty.getRemoveLawPatternIntensity());
        /*美型*/
        cloneFaceBeauty.setFaceShape(faceBeauty.getFaceShape());
        cloneFaceBeauty.setFaceShapeIntensity(faceBeauty.getFaceShapeIntensity());
        cloneFaceBeauty.setCheekThinningIntensity(faceBeauty.getCheekThinningIntensity());
        cloneFaceBeauty.setCheekVIntensity(faceBeauty.getCheekVIntensity());
        cloneFaceBeauty.setCheekLongIntensity(faceBeauty.getCheekLongIntensity());
        cloneFaceBeauty.setCheekCircleIntensity(faceBeauty.getCheekCircleIntensity());
        cloneFaceBeauty.setCheekNarrowIntensityV2(faceBeauty.getCheekNarrowIntensityV2());
        cloneFaceBeauty.setCheekShortIntensity(faceBeauty.getCheekShortIntensity());
        cloneFaceBeauty.setCheekSmallIntensityV2(faceBeauty.getCheekSmallIntensityV2());
        cloneFaceBeauty.setCheekBonesIntensity(faceBeauty.getCheekBonesIntensity());
        cloneFaceBeauty.setLowerJawIntensity(faceBeauty.getLowerJawIntensity());
        cloneFaceBeauty.setEyeEnlargingIntensityV2(faceBeauty.getEyeEnlargingIntensityV2());
        cloneFaceBeauty.setChinIntensity(faceBeauty.getChinIntensity());
        cloneFaceBeauty.setForHeadIntensityV2(faceBeauty.getForHeadIntensityV2());
        cloneFaceBeauty.setNoseIntensityV2(faceBeauty.getNoseIntensityV2());
        cloneFaceBeauty.setMouthIntensityV2(faceBeauty.getMouthIntensityV2());
        cloneFaceBeauty.setCanthusIntensity(faceBeauty.getCanthusIntensity());
        cloneFaceBeauty.setEyeSpaceIntensity(faceBeauty.getEyeSpaceIntensity());
        cloneFaceBeauty.setEyeRotateIntensity(faceBeauty.getEyeRotateIntensity());
        cloneFaceBeauty.setLongNoseIntensity(faceBeauty.getLongNoseIntensity());
        cloneFaceBeauty.setPhiltrumIntensity(faceBeauty.getPhiltrumIntensity());
        cloneFaceBeauty.setSmileIntensity(faceBeauty.getSmileIntensity());
        cloneFaceBeauty.setEyeCircleIntensity(faceBeauty.getEyeCircleIntensity());
        cloneFaceBeauty.setChangeFramesIntensity(faceBeauty.getChangeFramesIntensity());
    }
}
