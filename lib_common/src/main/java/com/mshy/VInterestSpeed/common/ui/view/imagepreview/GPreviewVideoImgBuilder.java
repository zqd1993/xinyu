package com.mshy.VInterestSpeed.common.ui.view.imagepreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.IThumbViewInfo;
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.loader.VideoClickListener;
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.view.BasePhotoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzq on 2018/10/16.
 * eprecated:
 */

public class GPreviewVideoImgBuilder {
    private Activity mContext;
    private Intent intent;
    private Class className;
    private VideoClickListener videoClickListener;

    private GPreviewVideoImgBuilder(@NonNull Activity activity) {
        mContext = activity;
        intent = new Intent();
    }

    /***
     * 设置开始启动预览
     *
     * @param activity 启动
     * @return GPreviewBuilder
     **/
    public static GPreviewVideoImgBuilder from(@NonNull Activity activity) {
        return new GPreviewVideoImgBuilder(activity);
    }

    /***
     * 设置开始启动预览
     *
     * @param fragment 启动
     * @return GPreviewBuilder
     **/
    public static GPreviewVideoImgBuilder from(@NonNull Fragment fragment) {
        return new GPreviewVideoImgBuilder(fragment.getActivity());
    }

    /****
     * 自定义预览activity 类名
     *
     * @param className 继承GPreviewActivity
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder to(@NonNull Class className) {
        this.className = className;
        intent.setClass(mContext, className);
        return this;
    }

    /***
     * 设置单个数据源
     *
     * @param imgUrl 数据
     * @param <E>    你的实体类类型
     * @return GPreviewBuilder
     **/
    public <E extends IThumbViewInfo> GPreviewVideoImgBuilder setSingleData(@NonNull E imgUrl) {
        ArrayList arrayList = new ArrayList<Parcelable>();
        arrayList.add(imgUrl);
        intent.putParcelableArrayListExtra("imagePaths", arrayList);
        return this;
    }

    /***
     * 设置数据源
     *
     * @param imgUrls 数据
     * @param <T>     你的实体类类型
     * @return GPreviewBuilder
     **/
    public <T extends IThumbViewInfo> GPreviewVideoImgBuilder setData(@NonNull List<T> imgUrls) {
        intent.putParcelableArrayListExtra("imagePaths", new ArrayList<Parcelable>(imgUrls));
        return this;
    }

    /***
     * 设置数据源
     *
     * @param className 你的Fragment类
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setUserFragment(@NonNull Class<? extends BasePhotoFragment> className) {
        intent.putExtra("className", className);
        return this;
    }

    /***
     * 设置默认索引
     *
     * @param currentIndex 数据
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setCurrentIndex(int currentIndex) {
        intent.putExtra("position", currentIndex);
        return this;
    }

    /***
     * 设置指示器类型
     *
     * @param indicatorType 枚举
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setType(@NonNull IndicatorType indicatorType) {
        intent.putExtra("type", indicatorType);
        return this;
    }

    /***
     * 设置图片禁用拖拽返回
     *
     * @param isDrag true  可以 false 默认 true
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setDrag(boolean isDrag) {
        intent.putExtra("isDrag", isDrag);
        return this;
    }

    /***
     * 设置图片禁用拖拽返回
     *
     * @param isDrag      true  可以 false 默认 true
     * @param sensitivity sensitivity MAX_TRANS_SCALE 的值来控制灵敏度。
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setDrag(boolean isDrag, float sensitivity) {
        intent.putExtra("isDrag", isDrag);
        intent.putExtra("sensitivity", sensitivity);
        return this;
    }

    /***
     * 是否设置为一张图片时 显示指示器  默认显示
     *
     * @param isShow true  显示 false 不显示
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setSingleShowType(boolean isShow) {
        intent.putExtra("isShow", isShow);
        return this;
    }

    /***
     * 设置超出内容点击退出（黑色区域）
     *
     * @param isSingleFling true  可以 false
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setSingleFling(boolean isSingleFling) {
        intent.putExtra("isSingleFling", isSingleFling);
        return this;
    }

    /***
     * 设置动画的时长
     *
     * @param setDuration 单位毫秒
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setDuration(int setDuration) {
        intent.putExtra("duration", setDuration);
        return this;
    }

    /***
     * 设置是怕你点击播放回调
     *
     * @return GPreviewBuilder
     **/
    public GPreviewVideoImgBuilder setOnVideoPlayerListener(VideoClickListener listener) {
        this.videoClickListener = listener;
        return this;
    }

    /***
     * 启动
     **/
    public void start() {
        if (className == null) {
            intent.setClass(mContext, GPreviewVideoImgActivity.class);
        } else {
            intent.setClass(mContext, className);
        }
//        BasePhotoFragment.listener = videoClickListener;
        mContext.startActivity(intent);
        mContext.overridePendingTransition(0, 0);
        intent = null;
        mContext = null;
    }

    /***
     * 指示器类型
     ***/
    public enum IndicatorType {
        Dot, Number
    }
}
