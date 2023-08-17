package com.mshy.VInterestSpeed.common.utils

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import com.mshy.VInterestSpeed.common.bean.BaseResponse
import com.mshy.VInterestSpeed.common.bean.SettingVquVersionBean
import com.mshy.VInterestSpeed.common.ext.toast
import com.mshy.VInterestSpeed.common.net.GlobalApiService
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage
import com.mshy.VInterestSpeed.common.ui.dialog.CommonUpdateDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author: Tany
 * date: 2022/6/6
 * description:
 */
class AppUpdateUtil {
    companion object {
        fun checkUpdate(isSilent: Boolean, activity: AppCompatActivity) {
            val globalApiService =
                GlobalServiceManage.getRetrofit().create(GlobalApiService::class.java)
            globalApiService.vquUpdateVersion("update")
                .enqueue(object : Callback<BaseResponse<SettingVquVersionBean>> {
                    override fun onResponse(
                        call: Call<BaseResponse<SettingVquVersionBean>>,
                        response: Response<BaseResponse<SettingVquVersionBean>>,
                    ) {
                        if (response.body() == null) {
                            if (!isSilent) {
                                "已是最新版本".toast()
                            }
                            return
                        }
                        if (response.body()?.data == null) {
                            if (!isSilent) {
                                "已是最新版本".toast()
                            }
                            return
                        }
                        if (isSilent && response.body()?.data?.isTips == 0) {
                            return
                        }


                        if (response.body()?.code == 0) {
                            val pm: PackageManager = activity.packageManager
                            val packageInfo: PackageInfo =
                                pm.getPackageInfo(activity.packageName, 0)
                            val versionCode = packageInfo.versionCode
                            if (versionCode >= response.body()?.data!!.versioncode) {
                                if (!isSilent) {
                                    "已是最新版本".toast()
                                }
                                return
                            }
                            if (response.body()?.data?.downloadurl.isNullOrEmpty()) {
                                if (!isSilent) {
                                    "已是最新版本".toast()
                                }
                            } else {
                                val updateDialog = CommonUpdateDialog()
                                updateDialog.setData(response.body()!!.data)
                                updateDialog.show(activity.supportFragmentManager, "update")
                            }

                        } else {
                            if (!isSilent) {
                                "已是最新版本".toast()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<BaseResponse<SettingVquVersionBean>>,
                        t: Throwable,
                    ) {
                        if (!isSilent) {
                            "已是最新版本".toast()
                        }
                    }
                })
        }
    }
}