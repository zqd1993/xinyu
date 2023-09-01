package com.live.module.message.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.message.repo.MessageVquRecentFragmentRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.CommonVquMainBannerBean
import com.mshy.VInterestSpeed.common.bean.MessageVisitorBean
import com.mshy.VInterestSpeed.uikit.business.recent.RecentContactsFragment
import com.mshy.VInterestSpeed.uikit.common.CommonUtil
import com.mshy.VInterestSpeed.uikit.util.IntimateUtils
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.RecentContact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * FileName: com.live.module.message.vm
 * Date: 2022/4/14 15:07
 * Description:
 * History:x
 */
@HiltViewModel
class MessageVquRecentFragmentViewModel @Inject constructor(private val mRepo: MessageVquRecentFragmentRepository) :
    BaseViewModel() {

    val banners = MutableLiveData<BaseResponse<CommonVquMainBannerBean>>()
    val visitorsData = MutableLiveData<BaseResponse<MessageVisitorBean>>()

    val isGetIntimate = MutableLiveData<Boolean>()

    val vquSaveRemarkNameBean = MutableLiveData<BaseResponse<Any>>()

    val isClearMsg = MutableLiveData<Boolean>()

    fun getBannerData() {
        launchIO {
            mRepo.vquGetBanner()
                .catch { toast(it.message ?: "") }
                .collect {
                    banners.postValue(it)
                }
        }
    }

    fun vquDelBeckons(uids: String) {
        launchIO {
            mRepo.vquDelBeckons(uids)
                .catch { toast(it.message ?: "") }
                .collect {
                }
        }
    }

    fun getVisitorsData() {
        launchIO {
            mRepo.getWhoLookMeInfo()
                .catch {
                    toast(it.message ?: "")
                }
                .collect {
                    visitorsData.postValue(it)
                }
        }
    }

    fun vquQueryIntimateList() {
        launchIO {
            mRepo.vquQueryIntimateList()
                .catch { isGetIntimate.postValue(false) }
                .collect { bean ->
                    bean.data.list.forEach {
                        IntimateUtils.getInstance().putData(it)
                    }
                    isGetIntimate.postValue(true)
                }
        }

    }

    /**
     * 置顶聊天
     */
    fun topChat(recent: RecentContact) {
        if (CommonUtil.isTagSet(recent, RecentContactsFragment.RECENT_TAG_STICKY)) {
            //取消置顶
            CommonUtil.removeTag(recent, RecentContactsFragment.RECENT_TAG_STICKY)
        } else {
            //置顶
            CommonUtil.addTag(recent, RecentContactsFragment.RECENT_TAG_STICKY)
        }
        NIMClient.getService(MsgService::class.java).updateRecentAndNotify(recent)
    }

    fun vquVisitorList() {
        launchIO {
            mRepo.vquVisitorList(1)
                .catch { }
                .collect {
                    isClearMsg.postValue(true)
                }
        }

    }

    fun resetClearStatue() {
        isClearMsg.postValue(false)
    }

}