package com.live.module.anchor.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.anchor.R
import com.live.module.anchor.bean.AnchorVquHelloListBean
import com.live.module.anchor.bean.AnchorVquHelloTemplateBean
import com.live.module.anchor.repo.AnchorVquHelloSettingRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/25
 * description:
 */
@HiltViewModel
class AnchorVquHelloSettingViewModel @Inject constructor(private val repo: AnchorVquHelloSettingRepository) :
    BaseViewModel() {


    val helloListData = MutableLiveData<AnchorVquHelloListBean>()

    val refreshPosition = MutableLiveData<Int>()
    val deletePosition = MutableLiveData<Int>()

    fun vquListNew() {
        launchIO {
            repo.vquListNew()
                .catch { }
                .collect {
                    helloListData.postValue(it.data!!)
                }
        }
    }

    fun vquSetName(item: AnchorVquHelloTemplateBean, content: String, position: Int) {
        val params = mutableMapOf<String, Any>()
        params["id"] = item.id
        params["name"] = content
        launchIO {
            repo.vquSetName(params)
                .catch { e ->
                    e.printStackTrace()
//                    toast(R.string.anchor_vqu_modify_template_mark_faild)
                }
                .collect {
                    item.name = content
                    refreshPosition.postValue(position)
                    toast(R.string.anchor_vqu_modify_template_mark_success)
                }
        }
    }

    fun vquDeleteNew(item: AnchorVquHelloTemplateBean, position: Int) {
        launchIO {
            repo.vquDeleteNew(item.id)
                .catch { }
                .collect {
                    toast(R.string.anchor_vqu_delete_success)
                    deletePosition.postValue(position)
                }
        }
    }

    fun vquSetDefault(item: AnchorVquHelloTemplateBean, position: Int) {
        launchIO {
            repo.vquSetDefault(item.id)
                .catch { }
                .collect {
                    toast(R.string.anchor_vqu_set_success)
                    vquListNew()
//                    refreshPosition.postValue(position)
                }
        }
    }

}