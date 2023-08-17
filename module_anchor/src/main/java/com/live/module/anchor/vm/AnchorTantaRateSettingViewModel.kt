package com.live.module.anchor.vm

import androidx.lifecycle.MutableLiveData
import com.live.module.anchor.bean.AnchorTantaPriceBean
import com.live.module.anchor.bean.AnchorTantaSettingBean
import com.live.module.anchor.net.AnchorVquApiService
import com.live.module.anchor.repo.AnchorTantaRateSettingRepository
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.live.vquonline.base.utils.toast
import com.mshy.VInterestSpeed.common.utils.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/24
 * description:
 */
@HiltViewModel
class AnchorTantaRateSettingViewModel @Inject constructor(private val repo: AnchorTantaRateSettingRepository) :
    BaseViewModel() {

    val skillIdData = MutableLiveData<String>()

    val settingSelectionData = MutableLiveData<AnchorTantaSettingBean>()

    val priceData = MutableLiveData<AnchorTantaPriceBean>()

    fun vquSkillSetting() {
        launchIO {
            repo.vquSkillSetting()
                .catch {

                }
                .collect {
                    if (!it.data.isNullOrEmpty()) {
                        skillIdData.postValue(it.data[0].skillId)
                    }
                }
        }
    }

    /**
     * 获取主播收费设置参数
     *
     * type 主播收费设置列表（为空获取全部）：video，chat，voice
     * @see [AnchorVquApiService.vquGetAnchorSetting]
     */
    fun tantaGetAnchorSetting(type: String?) {
        val params = mutableMapOf<String, String>()

        if (!type.isNullOrEmpty()) {
            params["type"] = type
        }

        launchIO {
            if (UserManager.userInfo?.gender == 1) {
                repo.tantaGetAnchorSetting(params)
                    .catch { e -> e.printStackTrace() }
                    .collect {
                        settingSelectionData.postValue(it.data!!)
                    }
            } else {
                repo.tantaManGetAnchorSetting(params)
                    .catch { e -> e.printStackTrace() }
                    .collect {
                        settingSelectionData.postValue(it.data!!)
                    }
            }
        }
    }

    fun tantaGetPrice() {
        launchIO {
            val params = mutableMapOf<String, String>()
            params["anchor_id"] = UserManager.userInfo?.userId ?: ""
            if (UserManager.userInfo?.gender == 1) {
                repo.tantaGetPrice(params)
                    .catch { }
                    .collect {
                        priceData.postValue(it.data!!)
                    }
            } else {
                repo.tantaManGetPrice(params)
                    .catch { }
                    .collect {
                        priceData.postValue(it.data!!)
                    }
            }
        }
    }

    fun parsingAndSetPrice(
        skillID: String,
        rateList: List<AnchorTantaSettingBean.Selection>?,
        options: Int,
        type: String
    ) {
        if (options < 0 || rateList == null || options >= rateList.size) {
            return
        }
        val rateBean: AnchorTantaSettingBean.Selection = rateList[options]
        if (rateBean.status == 2) {
            toast("您的等级不够，无法设置该选项")
            return
        }
        setPrice(skillID, rateBean, type,rateBean.coins)
    }

    private fun setPrice(
        skillID: String,
        rateBean: AnchorTantaSettingBean.Selection,
        type: String,
        price: String
    ) {

        launchIO {
            if (UserManager.userInfo?.gender == 1) {
                val params = mutableMapOf<String, Any>()
                params["skill_id"] = skillID
                params["type"] = type
                params["price_id"] = rateBean.id
                repo.vquSetPrice(params)
                    .catch {
                        it.printStackTrace()
                        toast("设置价格失败")
                    }
                    .collect {
                        toast(it.message)
                        tantaGetPrice()
                    }
            } else {
                val params = mutableMapOf<String, Any>()
                params["skill_id"] = skillID
                params["type"] = type
                params["price_id"] = rateBean.id
                params["price"] = price
                repo.vquManSetPrice(params)
                    .catch {
                        it.printStackTrace()
                        toast("设置价格失败")
                    }
                    .collect {
                        toast(it.message)
                        tantaGetPrice()
                    }
            }
        }
    }

    fun vquSetVideoStatus(status: Int) {
        launchIO {
            repo.vquSetVideoStatus(status)
                .catch {
                    toast("设置失败")
                }
                .collect {
                    toast("设置成功")
                }
        }
    }
}