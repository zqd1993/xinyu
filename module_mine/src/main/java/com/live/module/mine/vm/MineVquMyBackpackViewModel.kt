package com.live.module.mine.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.mine.bean.MineVquCouponBean
import com.live.module.mine.repo.MineVquMyBackpackRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/21
 * description:
 */
@HiltViewModel
class MineVquMyBackpackViewModel @Inject constructor(private val repo: MineVquMyBackpackRepository) :
    BaseViewModel() {

    val listData = MutableLiveData<MutableList<MineVquCouponBean>>()

    val useResult = MutableLiveData<Boolean>()

    fun getPackage(type: String) {
        launchIO {
            repo.vquGetPackage(type)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect {
                    listData.postValue(it.data!!)
                }
        }
    }

    fun vquUseNobleCard(id: String) {
        launchIO {
            repo.vquUseNobleCard(id)
                .catch { e ->
                    e.printStackTrace()
                    toast("使用失败")
                }
                .collect {
                    toast("使用成功")
                    useResult.postValue(true)
                }
        }
    }
}