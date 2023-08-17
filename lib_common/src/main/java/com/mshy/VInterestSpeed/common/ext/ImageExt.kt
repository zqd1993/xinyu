package com.mshy.VInterestSpeed.common.ext

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.live.vquonline.base.BaseApplication

/**
 *
 * @Description: glide 扩展
 * @Author: theobald wen
 * @CreateDate: 2022/4/7 17:09
 *
 */

fun ImageView.vquLoadView(vquContext: Context, vquUrl: String) {
    Glide.with(vquContext).load(vquUrl).into(this)
}

/**
 * 加载圆形网络图片
 */
fun ImageView.vquLoadCircleImage(url: Any?) {
    if (url == null) {
        return
    }
    Glide.with(BaseApplication.context)
        .load(url)
        .circleCrop()
        .into(this)
}
fun ImageView.vquLoadCircleImage(url: Any?,defaultId: Int) {
    if (url == null) {
        return
    }
    Glide.with(BaseApplication.context)
        .load(url)
        .circleCrop()
        .placeholder(defaultId)
        .into(this)
}


/**
 * 加载圆角图片
 * radius 圆角
 */
fun ImageView.vquLoadRoundImage(url: Any?, radius: Int) {
    if (url == null) {
        return
    }
    val roundOptions = RequestOptions()
        .transform(CenterCrop(), RoundedCorners(radius))
    Glide.with(BaseApplication.context)
        .load(url)
        .apply(roundOptions)
        .into(this)
}
fun ImageView.vquLoadRoundImage(url: Any?, radius: Int,defaultId: Int) {
    if (url == null) {
        return
    }
    val roundOptions = RequestOptions()
        .transform(CenterCrop(), RoundedCorners(radius))
    Glide.with(BaseApplication.context)
        .load(url)
        .apply(roundOptions)
        .placeholder(defaultId)
        .into(this)
}

/**
 * 加载圆角图片
 * radius 圆角
 */
fun ImageView.vquLoadRoundImage(url: Any?, defaultId: Int, thumbSize: Int, radius: Int) {
    if (url == null) {
        return
    }
    val roundOptions = RequestOptions()
        .override(thumbSize, thumbSize)
        .error(defaultId)
        .transform(CenterCrop(), RoundedCorners(radius))
    Glide.with(BaseApplication.context)
        .load(url)
        .apply(roundOptions)
        .into(this)
}


/**
 * 加载圆角图片 边框
 * radius 圆角
 */
fun ImageView.vquLoadRoundFrameImage(
    vquUrl: Any?,
    vquRadius: Int,
    vquBorderColor: String,
    vquBorderWidth: Int,
) {
    if (vquUrl == null) {
        return
    }
//    val requestOptions  = RequestOptions()
//        .transform(CircleBorderTransformation (context,vquRadius,vquBorderColor,vquBorderWidth))
//        .dontAnimate()
//    Glide.with(context)
//        .load(vquUrl)
//        .apply(requestOptions)
//        .into(this)
}

/**
 * 加载直角图片
 */
fun ImageView.vquLoadImage(url: Any?) {
    if (url == null) {
        return
    }
    val roundOptions = RequestOptions()
        .transform(CenterCrop())
    Glide.with(BaseApplication.context)
        .load(url)

        .into(this)
}

fun ImageView.vquLoadImage(url: Any?, defaultId: Int) {
    if (url == null) {
        return
    }
    Glide.with(BaseApplication.context)
        .load(url)
        .placeholder(defaultId)
        .into(this)
}


fun ImageView.vquLoadVideoFirstFrameRound(url: Any?, radius: Int) {
    if (url == null) {
        return
    }

    val options = RequestOptions.frameOf(0)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(MultiTransformation(CenterCrop(), RoundedCorners(radius)))

    Glide.with(BaseApplication.context)
        .load(url)
        .apply(options)
        .into(this)
}

fun ImageView.vquLoadVideoFirstFrame(url: Any?) {
    if (url == null) {
        return
    }

    val options = RequestOptions.frameOf(0)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(MultiTransformation(CenterCrop()))

    Glide.with(BaseApplication.context)
        .load(url)
        .apply(options)
        .into(this)
}
