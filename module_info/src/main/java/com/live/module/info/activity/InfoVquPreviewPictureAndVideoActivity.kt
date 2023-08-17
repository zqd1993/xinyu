package com.live.module.info.activity

import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.live.module.info.adapter.InfoVquImgPreviewBannerAdapter
import com.live.module.info.bean.Album
import com.live.module.info.databinding.InfoTantaActivityPreviewPictureVideoBinding
import com.live.module.info.viewholder.InfoVquImgBannerViewHolder
import com.live.module.info.vm.InfoVquViewModel
import com.mshy.VInterestSpeed.common.ui.BaseActivity
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.zhpan.bannerview.BannerViewPager

/**
 * author: Tany
 * date: 2022/5/5
 * description:
 */
class InfoVquPreviewPictureAndVideoActivity :
    BaseActivity<InfoTantaActivityPreviewPictureVideoBinding, InfoVquViewModel>() {
    override val mViewModel: InfoVquViewModel by viewModels()
    var myAdapter:InfoVquImgPreviewBannerAdapter?=null
    override fun initObserve() {
    }

    override fun initRequestData() {
    }

    override fun InfoTantaActivityPreviewPictureVideoBinding.initView() {
        GSYVideoManager.instance().isNeedMute=false
        ImmersionBar.with(this@InfoVquPreviewPictureAndVideoActivity).transparentStatusBar()
            .fitsSystemWindows(false).init()
        var obj = intent.getSerializableExtra("list")
        if (obj != null) {
            var imgList = obj as ArrayList<Album>
            (mBinding.banner as BannerViewPager<Album, InfoVquImgBannerViewHolder>).apply {
                myAdapter = InfoVquImgPreviewBannerAdapter()
                adapter = myAdapter
                setLifecycleRegistry(lifecycle)
                registerOnPageChangeCallback(
                    object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            if (position == 0) {//是视频
                                if (imgList[0].isVideo == 1) {
                                    myAdapter?.getVideoPlayer()?.onVideoResume()
                                }
                            } else {
                                myAdapter?.getVideoPlayer()?.onVideoPause()
                            }
                        }
                    }
                )
                create(imgList)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        myAdapter?.getVideoPlayer()?.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        myAdapter?.getVideoPlayer()?.onVideoResume()
    }

    override fun onStop() {
        super.onStop()
        GSYVideoManager.instance().isNeedMute=true
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}