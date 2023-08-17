package com.mshy.VInterestSpeed.common.ui.vm

import androidx.lifecycle.MutableLiveData
import com.live.vquonline.base.ktx.launchIO
import com.live.vquonline.base.mvvm.vm.BaseViewModel
import com.mshy.VInterestSpeed.common.bean.City
import com.mshy.VInterestSpeed.common.bean.TantaLocationBean
import com.mshy.VInterestSpeed.common.db.CityManager
import com.mshy.VInterestSpeed.common.ui.repo.CommonVquCitySelectorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * author: Lau
 * date: 2022/4/11
 * description:
 */
@HiltViewModel
class CommonVquCitySelectorViewModel @Inject constructor(val repo: CommonVquCitySelectorRepository) :
    BaseViewModel() {

    val cities = MutableLiveData<MutableList<City>>()

    val searchCities = MutableLiveData<MutableList<City>>()


    val location = MutableLiveData<TantaLocationBean>()

    private val manager by lazy {
        CityManager()
    }

    fun getCityData() {
        launchIO {
            val allCities = manager.getAllCities()
            cities.postValue(allCities)
        }
    }

    fun locate() {
//        VquLocationUtils.location(listener = object : VquLocationListener {
//            override fun vquSuccess(
//                mapLocationClient: AMapLocationClient?,
//                locationBean: VquLocationBean?
//            ) {
//                if (locationBean != null) {
//                    location.postValue(locationBean!!)
//                }
//                mapLocationClient?.stopLocation()
//            }
//
//            override fun vquError(
//                mapLocationClient: AMapLocationClient?,
//                errorCode: Int,
//                errorInfo: String?
//            ) {
//                val locationBean = VquLocationBean()
//                locationBean.city = ResUtils.getString(R.string.common_vqu_locat_error)
//                location.postValue(locationBean)
//                mapLocationClient?.stopLocation()
//            }
//        })
    }

    fun search(keyword: String) {
        launchIO {
            val searchCity = manager.searchCity(keyword)
            searchCities.postValue(searchCity)
        }
    }
}