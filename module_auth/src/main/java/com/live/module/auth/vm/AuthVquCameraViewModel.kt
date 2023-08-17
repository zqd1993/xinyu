package com.live.module.auth.vm

import com.live.module.auth.repo.AuthVquCameraRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/20
 * description:
 */
@HiltViewModel
class AuthVquCameraViewModel @Inject constructor(private val repo: AuthVquCameraRepository) :
    BaseViewModel() {

}