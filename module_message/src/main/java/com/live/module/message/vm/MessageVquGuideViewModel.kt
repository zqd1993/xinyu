package com.live.module.message.vm

import com.live.module.message.repo.MessageVquGuideRepository
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * FileName: com.live.module.message.vm
 * Date: 2022/5/7 15:24
 * Description:
 * History:
 */
@HiltViewModel
class MessageVquGuideViewModel @Inject constructor(private val mRepo: MessageVquGuideRepository) :
    BaseViewModel() {

}