package com.live.module.agora.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.live.module.agora.bean.AgoraVquMatchRecordBean
import com.live.module.agora.repo.AgoraVquMatchActivityRepo
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2021/12/11 20:30
 *
 */
@HiltViewModel
class AgoraTantaMatchingViewModel @Inject constructor(private val mRepo: AgoraVquMatchActivityRepo) : BaseViewModel() {

    var tantaMatchCode =  MutableLiveData<Int>()

    var tantaMatchRecord =  MutableLiveData<AgoraVquMatchRecordBean>()

    var tantaVideoTantaCallBean =  MutableLiveData<VideoVquCallBean>()

    /**
     *  加入速配
     */
    fun tantaAddMatching(vquType : String = "video") {

            viewModelScope.launch(Dispatchers.IO) {
                mRepo.vquAddMatching(vquType)
                    .catch {
                        toast(it.message+"")
                    }
                    .collect {
                        if(it.code == 0){
                            tantaMatchRecord.postValue(it?.data ?:null)
                        }else{
                            toast(it.message+"")
                            tantaMatchCode.postValue(it.code)
                        }
                    }
            }

    }


    /**
     *  退出速配
     */
    var tantaExitMatchCode =  MutableLiveData<Int>()
    fun tantaExitMatch(type: String) {
            viewModelScope.launch(Dispatchers.IO) {
                mRepo.vquCancelMatching(type)
                    .catch {
                        toast(it.message+"")
                    }
                    .collect {
                        tantaExitMatchCode.postValue(it.code)
                    }
            }

    }




    /**
     *  速配接听挂断
     */
    fun tantaReceiveCall(type :String, match_id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            mRepo.vquReceiveCall(type,match_id)
                .catch {
                    toast(it.message+"")
                }
                .collect {
                    tantaVideoTantaCallBean.postValue(it?.data?:null)
                }
        }
    }
}