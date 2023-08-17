package com.live.module.message.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.message.bean.CallRecordData
import com.live.module.message.repo.MessageVquCallRecordRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * FileName: com.live.module.message.vm
 * Date: 2022/5/20 16:31
 * Description:
 * History:
 */
@HiltViewModel
class MessageVquCallRecordViewModel @Inject constructor(private val mRepo: MessageVquCallRecordRepository) :
    BaseViewModel() {

    private var page = 1

    val mCallRecords = MutableLiveData<List<CallRecordData.ListBean>>()

    val mIsDeletePosition = MutableLiveData<Int>()

    var mIsLoadMore = false

    /**
     * @param isLoadMore 是否是加载更多
     */
    fun getCallRecords(isLoadMore: Boolean) {
        page = if (isLoadMore) page else 1
        launchIO {
            mIsLoadMore = isLoadMore
            mRepo.vquGetCallRecords(page)
                .catch { toast(it.message ?: "") }
                .collect {
                    if (it.data.list.isNotEmpty()) {
                        page++
                    }
                    mCallRecords.postValue(it.data.list)
                }
        }
    }

    fun deleteRecord(id: Int, position: Int) {
        launchIO {
            mRepo.vquDeleteCallRecords(id)
                .catch {
                    toast(it.message ?: "")
                    mIsDeletePosition.postValue(-1)
                }
                .collect {
                    toast("删除成功")
                    mIsDeletePosition.postValue(position)
                }
        }
    }
}