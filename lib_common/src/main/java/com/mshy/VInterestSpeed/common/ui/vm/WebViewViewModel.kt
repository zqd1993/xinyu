package com.mshy.VInterestSpeed.common.ui.vm


import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.ui.repo.WebViewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(mRepository: WebViewRepository): BaseViewModel() {
}