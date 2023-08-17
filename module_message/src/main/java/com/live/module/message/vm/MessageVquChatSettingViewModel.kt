package com.live.module.message.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.message.bean.ChatSettingBean
import com.live.module.message.repo.MessageVquChatSettingRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * FileName: com.live.module.message.vm
 * Date: 2022/4/14 15:07
 * Description:
 * History:
 */
@HiltViewModel
class MessageVquChatSettingViewModel @Inject constructor(private val mRepo: MessageVquChatSettingRepository) :
    BaseViewModel() {

    val vquChatSettingBean = MutableLiveData<BaseResponse<ChatSettingBean>>()

    val vquSaveRemarkNameBean = MutableLiveData<BaseResponse<Any>>()

    val vquDoBlackSuccess = MutableLiveData<Boolean>()

    fun vquGetChatSetting(userId: String) {
        launchIO {
            mRepo.vquGetChatSetting(userId)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquChatSettingBean.postValue(it)
                }
        }
    }

    fun vquSaveRemarkName(userId: String, remarkName: String) {
        launchIO {
            mRepo.vquSaveRemarkName(userId, remarkName)
                .catch { toast(it.message ?: "") }
                .collect {
                    vquSaveRemarkNameBean.postValue(it)
                }
        }
    }

    fun vquDoBlack(uid: String) {
        launchIO {
            val params = hashMapOf<String, Any>()
            params["black_uid"] = uid
            mRepo.vquDoBlack(params)
                .catch {
                    toast(it.message ?: "")
                    vquDoBlackSuccess.postValue(false)
                }
                .collect {
                    vquDoBlackSuccess.postValue(true)
                }
        }
    }

}