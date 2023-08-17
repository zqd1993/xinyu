package com.mshy.VInterestSpeed.common.utils

import android.text.TextUtils
import com.shuyu.gsyvideoplayer.GSYVideoBaseManager
import com.shuyu.gsyvideoplayer.player.IPlayerManager
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager

/**
 * author: Tany
 * date: 2022/5/7
 * description:
 */
class CustomVideoManager : GSYVideoBaseManager() {
   init {
       init()
   }

    var TAG = "GSYVideoManager"

    private val sMap: MutableMap<String, CustomVideoManager?> =
        HashMap<String, CustomVideoManager?>()



    protected override fun getPlayManager(): IPlayerManager? {
        return IjkPlayerManager()
    }


    /**
     * 页面销毁了记得调用是否所有的video
     */
    fun releaseAllVideos(key: String) {
        if (getCustomManager(key)?.listener() != null) {
            getCustomManager(key)?.listener()?.onCompletion()
        }
        getCustomManager(key)?.releaseMediaPlayer()
    }


    /**
     * 暂停播放
     */
    fun onPause(key: String) {
        if (getCustomManager(key)?.listener() != null) {
            getCustomManager(key)?.listener()?.onVideoPause()
        }
    }

    /**
     * 恢复播放
     */
    fun onResume(key: String) {
        if (getCustomManager(key)?.listener() != null) {
            getCustomManager(key)?.listener()?.onVideoResume()
        }
    }


    /**
     * 恢复暂停状态
     *
     * @param seek 是否产生seek动作,直播设置为false
     */
    fun onResume(key: String, seek: Boolean) {
        if (getCustomManager(key)?.listener() != null) {
            getCustomManager(key)?.listener()?.onVideoResume(seek)
        }
    }


    /**
     * 单例管理器
     */
    @Synchronized
    fun instance(): Map<String, CustomVideoManager?>? {
        return sMap
    }

    /**
     * 单例管理器
     */
    @Synchronized
    fun getCustomManager(key: String): CustomVideoManager? {
        check(!TextUtils.isEmpty(key)) { "key not be empty" }
        var customManager: CustomVideoManager? = sMap[key]
        if (customManager == null) {
            customManager = CustomVideoManager()
            sMap[key] = customManager
        }
        return customManager
    }

    fun onPauseAll() {
        if (sMap.size > 0) {
            for ((key, value) in sMap) {
                value?.onPause(key)
            }
        }
    }

    fun onResumeAll() {
        if (sMap.size > 0) {
            for ((key, value) in sMap) {
                value?.onResume(key)
            }
        }
    }

    /**
     * 恢复暂停状态
     *
     * @param seek 是否产生seek动作
     */
    fun onResumeAll(seek: Boolean) {
        if (sMap.size > 0) {
            for ((key, value) in sMap) {
                value?.onResume(key, seek)
            }
        }
    }

    fun clearAllVideo() {
        if (sMap.size > 0) {
            for ((key) in sMap) {
                releaseAllVideos(key)
            }
        }
        sMap.clear()
    }

    fun removeManager(key: String) {
        sMap.remove(key)
    }

}