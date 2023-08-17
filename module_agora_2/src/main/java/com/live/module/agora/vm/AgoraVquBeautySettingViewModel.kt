package com.live.module.agora.vm

import com.live.module.agora.repo.AgoraVquBeautySettingRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/28
 * description:
 */
@HiltViewModel
class AgoraVquBeautySettingViewModel @Inject constructor(val repo: AgoraVquBeautySettingRepository) :
    BaseViewModel() {
}