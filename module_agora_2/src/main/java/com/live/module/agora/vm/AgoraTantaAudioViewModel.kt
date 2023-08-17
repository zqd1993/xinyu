package com.live.module.agora.vm

import androidx.lifecycle.viewModelScope
import com.live.module.agora.repo.AgoraVquAudioRepository
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
 * date: 2022/7/25
 * description:
 */
@HiltViewModel
class AgoraTantaAudioViewModel @Inject constructor(private val repo:AgoraVquAudioRepository):BaseViewModel() {

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
}