package com.live.module.anchor.vm

import com.live.module.anchor.repo.AnchorVquSettingsMenuRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
@HiltViewModel
class AnchorVquSettingsMenuViewModel @Inject constructor(private val repo:AnchorVquSettingsMenuRepository):BaseViewModel() {
}