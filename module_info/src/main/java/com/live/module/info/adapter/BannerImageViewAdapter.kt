package com.live.module.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.live.module.info.R
import com.live.module.info.bean.Album
import com.live.module.info.viewholder.BannerImageVideoHolder
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.view.video.NoClickControlVideo
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.youth.banner.adapter.BannerAdapter

/**
 * author: Tany
 * date: 2022/8/15
 * description:
 */

 class BannerImageViewAdapter(mData: List<Album>?) :
    BannerAdapter<Album?, BannerImageVideoHolder?>(mData) {
    var myVideoView:NoClickControlVideo?=null
    var screenshotUrl:String="?x-oss-process=video/snapshot,t_0,f_jpg,w_0,h_0,m_fast,ar_auto"
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerImageVideoHolder {
        return BannerImageVideoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.info_tanta_banner_info,
                parent,
                false
            )
        )
    }

    override fun onBindView(holder: BannerImageVideoHolder?, data: Album?, position: Int, size: Int) {
        var ivImg = holder?.ivImg
        var video =holder?.videoView
        var ivPlay = holder?.ivPlay
        if(data?.isVideo == 1){
            ivImg?.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + data?.url+screenshotUrl)
            myVideoView=holder?.videoView
        }else{
            ivImg?.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + data?.url)
        }

        if (data?.isVideo == 1) {
            ivPlay?.visible()
            video?.thumbImageView = ivImg
            video?.isLooping = true
            ivPlay?.gone()
            video?.visible()
            video?.setUp(NetBaseUrlConstant.IMAGE_URL+data?.url, true, "")
            video?.startPlayLogic()
            video?.setVideoAllCallBack(object : VideoAllCallBack {
                override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
                }

                override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                }

                override fun onClickResume(url: String?, vararg objects: Any?) {
                    ivPlay?.visible()

                }

                override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
                }

                override fun onStartPrepared(url: String?, vararg objects: Any?) {

                }

                override fun onClickStartIcon(url: String?, vararg objects: Any?) {
                }

                override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                }

                override fun onClickStartThumb(url: String?, vararg objects: Any?) {
                    ivPlay?.gone()
                }

                override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
                }

                override fun onClickStartError(url: String?, vararg objects: Any?) {
                }

                override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
                }

                override fun onPrepared(url: String?, vararg objects: Any?) {
                    GSYVideoManager.instance().isNeedMute = true
                }

                override fun onAutoComplete(url: String?, vararg objects: Any?) {
                }

                override fun onComplete(url: String?, vararg objects: Any?) {
                }

                override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
                }

                override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
                }

                override fun onClickBlank(url: String?, vararg objects: Any?) {
                }

                override fun onClickStop(url: String?, vararg objects: Any?) {
                }

                override fun onClickSeekbar(url: String?, vararg objects: Any?) {
                }

                override fun onPlayError(url: String?, vararg objects: Any?) {
                }

                override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
                }

                override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
                }


            })
            video?.currentPlayer?.setGSYStateUiListener {
                when (it) {
                    GSYVideoView.CURRENT_STATE_PLAYING -> {
                        ivPlay?.gone()
                    }
                    GSYVideoView.CURRENT_STATE_PAUSE -> {
                        ivPlay?.visible()
                    }
                }
            }

        } else {
            ivImg?.visible()
            ivPlay?.gone()
            video?.gone()
        }
    }
    fun getPlayer():NoClickControlVideo?{
        if(getData(0)?.isVideo==1){
            return myVideoView
        }
        return null
    }
}