package com.live.module.home.activity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * 首页的VM层
 *
 * @property mRepository HomeRepository 仓库层 通过Hilt注入
 *
 * ""
 * @since 5/25/21 5:41 PM
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val mRepository: HomeRepository) : BaseViewModel() {

    val data = MutableLiveData<String>()

    /**
     * 模拟获取数据
     */
    fun getData() {
        launchIO {
            mRepository.getData()
                .catch { Log.d("qqq", "getData: $it") }
                .collect { data.postValue(it) }
        }
    }
}