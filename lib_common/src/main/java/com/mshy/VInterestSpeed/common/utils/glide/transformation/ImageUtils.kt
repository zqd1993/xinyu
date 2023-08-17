package com.ellison.glide.translibrary

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.ellison.glide.translibrary.base.Loader
import com.ellison.glide.translibrary.base.LoaderBuilder
import com.ellison.glide.translibrary.listener.IResultListener


/**
 * 说明：
 * 作者：逍遥子
 * 时间：2016/3/21 17:09
 */
class ImageUtils {
    companion object {
        private var instances: ImageUtils? = null
            get() {
                if (field == null) {
                    field = ImageUtils()
                }
                return field
            }

        // 判断是否初始化
        private lateinit var loader: Loader
        private fun isInitLoader(): Boolean {
            return this::loader.isInitialized
        }

        lateinit var applicationContext: Application
        private fun isApplicationContextInit(): Boolean {
            return this::applicationContext.isInitialized
        }

        @JvmStatic
        fun getLoader(): Loader {
            if (!isInitLoader()) {
                throw RuntimeException("Must call ImageLoader.init() first!")
            }
            return loader
        }

        @JvmStatic
        fun init(applicationContext: Context, loader: Loader) {
            Companion.loader = loader
        }

        @JvmStatic
        fun getInstance(): ImageUtils {
            return instances!!
        }

    }


    fun load(url: String, width: Int = 0, height: Int = 0, iResult: IResultListener<Bitmap>?) {
        val builder = LoaderBuilder()
            .load(url)
            .width(width)
            .height(height)
            .listener(iResult)
        getLoader().load(builder)
    }

    /**
     * 对外设置
     * @see ImageView  使用ImageView加载时，可以动态配置
     */
    fun bind(view: ImageView?, loaderBuilder: LoaderBuilder) {
        loaderBuilder.into(view)
    }


    private var density = -1f

    fun dip2px(dpValue: Float): Int {
        return ((dpValue * getDensity() + 0.5f).toInt())
    }

    fun getDensity(): Float {
        if (density <= 0f) {
            density =
                applicationContext.resources.displayMetrics.density
        }
        return density
    }


}