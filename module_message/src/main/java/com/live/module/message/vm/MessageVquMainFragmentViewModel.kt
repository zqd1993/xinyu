package com.live.module.message.vm

import com.live.module.message.repo.MessageVquMainFragmentRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * FileName: com.live.module.message.vm
 * Date: 2022/4/14 15:07
 * Description:
 * History:
 */
@HiltViewModel
class MessageVquMainFragmentViewModel @Inject constructor() :BaseViewModel() {
    @Inject
    lateinit var vquRepository: MessageVquMainFragmentRepository
}