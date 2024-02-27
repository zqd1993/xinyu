package com.quyunshuo.module.home.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkfuns.logutils.LogUtils
import com.live.module.home.bean.HomeAccostBean
import com.live.module.home.bean.HomeNewDataBean
import com.live.module.home.bean.HomeVquChannelAnchorBean
import com.live.module.home.bean.HomeVquMatchBean
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.status.ViewStatusHelper
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import com.mshy.VInterestSpeed.common.bean.CommonVquAdBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

/**
 * @author DBoy 2021/7/6 <p>
 * - 文件描述 : ViewModel再ViewPager2的Fragment中会随着Fragment执行[Fragment.onDestory]一同销毁。
 * 所以一些需要长期保存的变量数据，不适合保存再ViewModel，考虑使用[ViewStatusHelper]保存页面上部分数据，
 * 页面恢复的时候再交给ViewModel处理,例如[recreatedCont]
 */
@HiltViewModel
class HomeVquFragmentViewModel @Inject constructor() :
    BaseViewModel() {

    @Inject
    lateinit var vquRepository: HomeVquFragmentRepository

    val vquOnlineData = MutableLiveData<HomeNewDataBean>()
    /**
     * 首个数据
     */
    val vquAdBean = MutableLiveData<CommonVquAdBean>()
    val vquRemoveItem = MutableLiveData<Int>()
    /**
     * 首个数据
     */
    val vquAdBean6 = MutableLiveData<CommonVquAdBean>()

    val vquGetRecommendAnchors = MutableLiveData<HomeVquChannelAnchorBean>()

    val vquGetOnLiveAnchors = MutableLiveData<HomeVquChannelAnchorBean>()

    val vquBeckonInt = MutableLiveData<Int>()

    val vquNoDiamond = MutableLiveData<Boolean>()
    val vquNewRecommendData = MutableLiveData<HomeNewDataBean>()
    val vquAccostStrBean = MutableLiveData<HomeAccostBean>()
    /**
     * 获取数据
     */
    fun vquGetAdvert(vquType: String = "2") {
        viewModelScope.launch(Dispatchers.IO) {
            vquRepository.vquGetAdvert(vquType)
                .catch {

                }
                .onStart { changeStateView(loading = true) }
                .collect {
                    if (vquType.equals("6")) {
                        vquAdBean6.postValue(it.data)
                    } else {
                        vquAdBean.postValue(it.data)
                    }
                }
        }
    }


    var vquMatch = MutableLiveData<HomeVquMatchBean>()

    /**
     *  加入速配
     */
    fun vquAddMatching(vquType: String = "video") {
        UserManager.isMatch = true
        viewModelScope.launch(Dispatchers.IO) {
            vquRepository.vquAddMatching(vquType)
                .catch {
                    toast(it.message + "")
                }
                .collect {
                    vquMatch.postValue(HomeVquMatchBean(vquType, it.code, it.message))
                }
        }

    }

    fun vquSendBeckon(vquUserId: String, vquPosition: Int, isContinue: Int, isTip: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            vquRepository.vquSendBeckon(vquUserId,isContinue,isTip)
                .catch {
                }
                .collect {
                    LogUtils.e("vquSendBeckon${it.toString()}")
                    if (it.code == 1001) {//违规账号退出到登录页
                        EventBus.getDefault().post("logout")
                    } else if (it.code == 1002) {
                        vquNoDiamond.postValue(true)
                        vquBeckonInt.postValue(vquPosition)
                    } else if (it.code == 0 || it.code == 7480) {
                        if (it.code == 7480) {
                            it.message.toast()
                        }
                        if (it.data == null) {
                            return@collect
                        }
                    } else if(it.code == 7482){
                        vquRemoveItem.postValue(vquPosition)
                        toast(it.message ?: "")
                    }else if (it.code == 1333) {
                        vquAccostStrBean.postValue(HomeAccostBean(vquUserId,
                            vquPosition,
                            it.message))
                    } else {
                        vquBeckonInt.postValue(vquPosition)
                        toast(it.message ?: "")
                    }
                }
        }
    }

    fun vquGetNewRecommendAnchors(vquPage: String) {

        viewModelScope.launch(Dispatchers.IO) {
            vquRepository.vquGetNewRecommendAnchors(vquPage)
                .catch {

                }
                .collect {
                    if (it.data != null) {
                        vquNewRecommendData.postValue(it.data!!)
                    }
                }
        }
    }


    fun vquGetOnLineAnchors(vquPage: String) {

        viewModelScope.launch(Dispatchers.IO) {
            vquRepository.vquGetOnLineAnchors(vquPage)
                .catch {

                }
                .collect {
                    if (it.data != null) {
                        vquOnlineData.postValue(it.data!!)
                    }
                }
        }
    }

    /**
     * 获取推荐
     */
    fun vquGetRecommendAnchors(vquType: Int, vquPage: String) {

        if (vquType == 0) {
            viewModelScope.launch(Dispatchers.IO) {
                vquRepository.vquGetRecommendAnchors(vquPage)
                    .catch {

                    }
                    .collect {
                        if (it.data != null) {
                            vquGetRecommendAnchors.postValue(it.data!!)
                        }
                    }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                vquRepository.vquGetOnlineAnchors(vquPage)
                    .catch {

                    }
                    .onStart {
                        changeStateView(loading = true)
                    }
                    .onCompletion {
                        changeStateView(hide = true)
                    }
                    .collect {
                        if (it.data != null) {
                            vquGetOnLiveAnchors.postValue(it.data!!)
                        }
                    }
            }
        }

    }

    fun vquGetUserInfo() {
        launchIO {
            val params = hashMapOf<String, Any>()
            var userId = UserManager.userInfo?.userId
            params["user_id"] = userId ?: ""
            vquRepository.vquGetUserInfo(params)
                .catch { toast(it.message ?: "") }
                .collect {
                    UserManager.userInfo?.isAuth = it?.data?.userinfo?.isAuth
                    UserManager.userInfo?.isRpAuth = it?.data?.userinfo?.isRpAuth
                    UserManager.webUrl = it?.data?.webUrl
                    UserSpUtils.putUserBean(it?.data?.userinfo)
                }
        }
    }

    fun vquCloseInfoFinishTip() {
        launchIO {
            vquRepository.vquCloseInfoFinishTip()
                .catch { toast(it.message ?: "") }
                .collect {

                }
        }
    }

}
