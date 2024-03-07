package com.live.module.mine.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.live.module.mine.repo.MineRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.bean.*
import com.mshy.VInterestSpeed.common.utils.UserManager
import com.mshy.VInterestSpeed.common.utils.UserSpUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/1
 * description:
 */
@HiltViewModel
class MineViewModel @Inject constructor(private val repository: MineRepository) : BaseViewModel() {

    val userInfoData = MutableLiveData<VquUserHomeBean>()
    val walletData = MutableLiveData<TantaWalletBean>()
    val bannerListData = MutableLiveData<MutableList<com.mshy.VInterestSpeed.common.bean.CommonVquBannerBean>?>()


    fun vquGetUserInfo() {
        launchIO {
            Log.d("MineFragment", "initRequestData: ")
            repository.vquGetUserInfo().catch {

            }.collect {

                var bean = UserManager.userInfo
                if (bean == null) {
                    bean = UserSpUtils.getUserBean()
                }

                UserManager.webUrl = it.data.webUrl

                if (bean != null) {
                    bean.avatar = it.data.userinfo.avatar
                    bean.isAnchor = it.data.userinfo.isAnchor
                    bean.vip = it.data.userinfo.vip
                    bean.vipDes = it.data.userinfo.vipDes
                    bean.vipIcon = it.data.userinfo.vipIcon
                    bean.avatarFrame = it.data.userinfo.avatarFrame
                    bean.isAuth = it.data.userinfo.isAuth
                    bean.isRpAuth = it.data.userinfo.isRpAuth
                    bean.isStarScout = it.data.userinfo.isStarScout
                    bean.scoutModel = it.data.userinfo.scoutModel
                    bean.guardianNum = it.data.userinfo.guardianNum
                    UserManager.userInfo = bean
                    UserSpUtils.putUserBean(bean)
                }

                userInfoData.postValue(it.data!!)
            }
        }
    }

    fun vquWalletIndex() {
        launchIO {
            repository.vquWalletIndex().catch { }
                .collect {
                    walletData.postValue(it.data!!)
                }
        }
    }

    fun vquIndexBanner() {
        launchIO {
            repository.vquIndexBanner()
                .catch { }
                .collect {
                    bannerListData.postValue(it.data.banner)
                }
        }
    }

}