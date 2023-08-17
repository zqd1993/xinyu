package com.mshy.VInterestSpeed.common.livedata

import androidx.lifecycle.ViewModel
import com.netease.nimlib.sdk.msg.model.RecentContact

/**
 * author: Tany
 * date: 2022/7/2
 * description:
 */
class AppViewModel : ViewModel() {
    var unreadConversationList = UnPeekLiveData<MutableList<RecentContact>>()
}