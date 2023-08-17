package com.live.module.message.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.message.repo.MessageVquCommonWordRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.uikit.bean.NIMCommonWordBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * FileName: com.live.module.message.vm
 * Date: 2022/5/5 16:36
 * Description:
 * History:
 */
@HiltViewModel
class MessageVquCommonWordViewModel @Inject constructor(private val mRepo: MessageVquCommonWordRepository) :
    BaseViewModel() {

    val commonWordList = MutableLiveData<BaseResponse<List<NIMCommonWordBean>>>()


    fun vquGetCommonWordData() {
        launchIO {
            mRepo.vquGetCommonWord()
                .catch { toast(it.message ?: "") }
                .collect {
                    commonWordList.postValue(it)
                }
        }
    }

     fun vquDelCommonWord(id: Int) {
        launchIO {
            mRepo.vquDelCommonWord(id)
                .catch { }
                .collect {
                    vquGetCommonWordData()
                }
        }
    }

    fun vquAddCommonWord(word: String) {
        launchIO {
            mRepo.vquAddCommonWord(word)
                .catch { }
                .collect {
                    vquGetCommonWordData()
                }
        }
    }

}