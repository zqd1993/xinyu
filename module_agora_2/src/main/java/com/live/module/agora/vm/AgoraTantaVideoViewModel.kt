package com.live.module.agora.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.live.module.agora.bean.AgoraVquChatShootBean
import com.live.module.agora.repo.AgoraVquVideoRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/7/19
 * description:
 */
@HiltViewModel
class AgoraTantaVideoViewModel @Inject constructor(private val repo: AgoraVquVideoRepository) :
    BaseViewModel() {
    var vquChatShootBean = MutableLiveData<AgoraVquChatShootBean>()

    /**
     *  加入速配
     */
    fun vquAddMatching(vquType: String = "video") {

        viewModelScope.launch(Dispatchers.IO) {
            repo.vquAddMatching(vquType)
                .catch {
                    toast(it.message + "")
                }
                .collect {

                }
        }
    }

    /**
     *  截取主叫者的图片
     */
    fun vquPostChatShootFrom(vquRoom_id: String, from: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.vquPostChatShootFrom(vquRoom_id, from, uid)
                .catch {

                }
                .collect {
//                    if (it.data != null) {
                    it.data.let { bean ->
                        vquChatShootBean.postValue(bean)
                    }
//                    }
                }
        }
    }
}