package com.live.module.mine.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.mine.repo.MineSignDayRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.bean.SignDaySuccessData
import com.mshy.VInterestSpeed.common.bean.TodayBean
import com.mshy.VInterestSpeed.common.ext.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/6/23
 * description:
 */
@HiltViewModel
class MineSignDayViewModel @Inject constructor(private val repo: MineSignDayRepository) :
    BaseViewModel() {

    val todayLiveData = MutableLiveData<TodayBean>()

    val successData = MutableLiveData<SignDaySuccessData>()

    fun vquTodayActivityIndex() {
        launchIO {
            repo.vquTodayActivityIndex()
                .catch { }
                .collect {
                    todayLiveData.postValue(it.data!!)
                }
        }
    }

    fun vquTodayActivity() {
        launchIO {
            repo.vquTodayActivity()
                .catch {
                    it.message?.toast()
                }
                .collect {
//                    it.message.toast()

//                    vquTodayActivityIndex()

//                    EventBus.getDefault().post(SignDaySuccessData())


//                    if (it.data)

                    EventBus.getDefault().post(it.data)

                    successData.postValue(it.data!!)
                }
        }
    }
}