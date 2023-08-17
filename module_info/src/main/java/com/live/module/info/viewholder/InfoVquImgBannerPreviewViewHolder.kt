package com.live.module.info.viewholder

import android.view.View
import android.widget.ImageView
import com.live.module.info.R
import com.live.module.info.bean.Album
import com.live.vquonline.base.ktx.gone
import com.live.vquonline.base.ktx.visible
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant
import com.mshy.VInterestSpeed.common.ext.vquLoadImage
import com.mshy.VInterestSpeed.common.ui.view.video.NoClickControlVideo
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.zhpan.bannerview.BaseViewHolder

/**
 * Created by Tany on 2021/11/1.
 * Desc:
 */


class InfoVquImgBannerPreviewViewHolder(view: View) : BaseViewHolder<Album>(view) {
    override fun bindData(data: Album?, position: Int, pageSize: Int) {
        var ivImg = findView<ImageView>(R.id.iv_img)
        var video = findView<NoClickControlVideo>(R.id.video)
        var ivPlay = findView<ImageView>(R.id.iv_play)
        ivImg.vquLoadImage(NetBaseUrlConstant.IMAGE_URL + data?.url)
        if (data?.isVideo == 1) {
            ivPlay.visible()
            video.visible()
            video?.isLooping = true
            ivImg.gone()
            ivPlay.gone()
            video?.setUp(NetBaseUrlConstant.IMAGE_URL+data?.url, true, "")
            video?.startPlayLogic()
            GSYVideoManager.instance().isNeedMute = true
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
            ivImg.visible()
            ivPlay.gone()
            video.gone()
        }



    }
}