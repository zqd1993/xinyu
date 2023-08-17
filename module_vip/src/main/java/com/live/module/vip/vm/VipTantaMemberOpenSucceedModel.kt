package com.live.module.vip.vm

import com.live.module.vip.repository.VipTantaMemberOpenSucceedRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
@HiltViewModel
class VipTantaMemberOpenSucceedModel @Inject constructor(private val repo: VipTantaMemberOpenSucceedRepository) :
    BaseViewModel() {
}