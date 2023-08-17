package com.mshy.VInterestSpeed.common.ui.view.imagepreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.mshy.VInterestSpeed.common.ui.view.imagepreview.loader.IZoomMediaLoader;


/**
 * Created by liuzq on 2018/10/16.
 */

public class TestImageLoader implements IZoomMediaLoader {


    @Override
    public void displayImage(@NonNull Fragment context, @NonNull String path, @NonNull final com.mshy.VInterestSpeed.common.ui.view.imagepreview.loader.SimpleTarget<Bitmap> simpleTarget) {
        Glide.with(context)
                .asBitmap()
                .load(path)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        simpleTarget.onResourceReady(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        simpleTarget.onLoadFailed(errorDrawable);
                    }

                });
    }

    @Override
    public void displayGifImage(@NonNull Fragment context, @NonNull String path, ImageView imageView, @NonNull final com.mshy.VInterestSpeed.common.ui.view.imagepreview.loader.SimpleTarget simpleTarget) {

        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate();
        Glide.with(context).load(path)
                .listener(new RequestListener<Drawable>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        simpleTarget.onResourceReady();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        simpleTarget.onLoadFailed(null);
                        return false;
                    }
                })
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void onStop(@NonNull Fragment context) {
        Glide.with(context).onStop();
    }

    @Override
    public void clearMemory(@NonNull Context c) {
        Glide.get(c).clearMemory();
    }
}
