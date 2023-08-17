package com.mshy.VInterestSpeed.common.ui.vm

import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.ui.repo.CommonPayResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/18
 * description:
 */
@HiltViewModel
class CommonPayResultViewModel @Inject constructor(val repo: CommonPayResultRepository):BaseViewModel() {
}