package com.mshy.VInterestSpeed.common.ui.view.imagepreview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mshy.VInterestSpeed.common.R
import org.greenrobot.eventbus.EventBus

/**
 * author: Tany
 * date: 2022/8/16
 * description:
 */
class GPreviewVideoImgActivity : FragmentActivity() {

    /***
     * 图片的地址
     */
    private var imgUrls: List<com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.IThumbViewInfo>? = null

    /***
     * 当前图片的位置
     */
    private var currentIndex = 0

    /***
     * 显示图片数
     */
    private var ltAddDot: TextView? = null

    /***
     * 指示器控件
     */
    private var bezierBannerView: com.mshy.VInterestSpeed.common.ui.view.imagepreview.wight.BezierBannerView? = null

    /***
     * 指示器类型枚举
     */
    private var type: com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewVideoImgBuilder.IndicatorType? = null

    /***
     * 默认显示
     */
    private var isShow = true

    /***
     * 展示图片的viewPager
     */
    private var viewPager: com.mshy.VInterestSpeed.common.ui.view.imagepreview.wight.PhotoViewPager? = null

    /***
     * 图片的展示的Fragment
     */
    var fragments: ArrayList<Fragment> = ArrayList()

    var hasVideo:Boolean=false

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview_photo)
        initData()
        initView()
    }

    /**
     * 初始化控件
     */
    @SuppressLint("StringFormatMatches")
    private fun initView() {
        viewPager = findViewById<View>(R.id.viewPager) as com.mshy.VInterestSpeed.common.ui.view.imagepreview.wight.PhotoViewPager
        //viewPager的适配器
        val adapter = PhotoPagerAdapter(
            supportFragmentManager)
        viewPager?.setAdapter(adapter)
        viewPager?.setCurrentItem(currentIndex)
        viewPager?.setOffscreenPageLimit(fragments.size)
        bezierBannerView = findViewById<View>(R.id.bezierBannerView) as com.mshy.VInterestSpeed.common.ui.view.imagepreview.wight.BezierBannerView
        ltAddDot = findViewById<View>(R.id.ltAddDot) as TextView
        if (type == com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewVideoImgBuilder.IndicatorType.Dot) {
            bezierBannerView?.setVisibility(View.VISIBLE)
            bezierBannerView?.attachToViewpager(viewPager)
        } else {
            ltAddDot?.setVisibility(View.VISIBLE)
            ltAddDot?.setText(getString(R.string.string_count, currentIndex + 1, imgUrls!!.size))
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                }

                override fun onPageSelected(position: Int) {
                    //当被选中的时候设置小圆点和当前位置
                    if (ltAddDot != null) {
                        ltAddDot?.setText(getString(R.string.string_count,
                            position + 1,
                            imgUrls!!.size))
                    }
                    currentIndex = position
                    viewPager?.setCurrentItem(currentIndex, true)
                    if (position == 0) {
                        if (hasVideo) {
                            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.VideoPlayEvent(
                                true))
                        } else {
                            EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.VideoPlayEvent(
                                false))
                        }
                    } else {
                        EventBus.getDefault().post(com.mshy.VInterestSpeed.common.event.VideoPlayEvent(
                            false))
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
        if (fragments.size == 1) {
            if (!isShow) {
                bezierBannerView?.setVisibility(View.GONE)
                ltAddDot?.setVisibility(View.GONE)
            }
        }
        viewPager?.getViewTreeObserver()
            ?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    viewPager?.getViewTreeObserver()?.removeGlobalOnLayoutListener(this)
                }
            })
    }

    /**
     * 关闭页面
     */
    private fun exit() {
        finish()
        overridePendingTransition(0, 0)
    }

    private var isTransformOut = false

    /***
     * 退出预览的动画
     */
    fun transformOut() {
        exit()
    }

    /**
     * 初始化
     *
     * @param imgUrls      集合
     * @param currentIndex 选中索引
     * @param className    显示Fragment
     */
    protected fun iniFragment(
        imgUrls: List<com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.IThumbViewInfo?>?,
        currentIndex: Int,
    ) {
        if (imgUrls != null) {
            val size = imgUrls.size
            for (i in 0 until size) {
                if (!imgUrls[i]?.videoUrl.isNullOrEmpty()) {
                    hasVideo=true
                    fragments.add(InfoVquPreviewVideoFragment.newInstance(imgUrls[0]?.videoUrl!!,imgUrls[0]!!))
                } else {
                    fragments.add(com.mshy.VInterestSpeed.common.ui.view.imagepreview.view.BasePhotoFragment.getInstance(
                        com.mshy.VInterestSpeed.common.ui.view.imagepreview.view.BasePhotoFragment::class.java,
                        imgUrls[i],
                        currentIndex == i,
                        intent.getBooleanExtra("isSingleFling", false),
                        intent.getBooleanExtra("isDrag", false),
                        intent.getFloatExtra("sensitivity", 0.01f))
                    )
                }

            }
        } else {
            finish()
        }
    }

    /**
     * 初始化数据
     */
    private fun initData() {
        imgUrls = intent.getParcelableArrayListExtra<com.mshy.VInterestSpeed.common.ui.view.imagepreview.entity.IThumbViewInfo>("imagePaths")
        if (imgUrls == null) {
            return
        }
        if (imgUrls?.size == 0) {
            return
        }
        currentIndex = intent.getIntExtra("position", -1)
        type = intent.getSerializableExtra("type") as com.mshy.VInterestSpeed.common.ui.view.imagepreview.GPreviewVideoImgBuilder.IndicatorType?
        isShow = intent.getBooleanExtra("isShow", true)
        val duration = intent.getIntExtra("duration", 300)
        iniFragment(imgUrls, currentIndex)
    }

    /**
     * pager的适配器
     */
    inner class PhotoPagerAdapter internal constructor(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }

        override fun getCount(): Int {
            return if (fragments == null) 0 else fragments.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            super.destroyItem(container, position, `object`)
        }
    }
}